package ac.OneBlood.Controller;

import ac.OneBlood.Model.*;
import ac.OneBlood.Service.Aggregator;
import ac.OneBlood.Service.DonationFormService;
import javassist.NotFoundException;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
public class AggregatorController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    Aggregator aggregator;
    @Autowired
    DonationFormService donationFormService;


    //inregistrare ca doctor ar trebui sa poti avea avea acces doar daca esti logat ca doctor
    @PostMapping("/agreggator/cont/doctor")
    public ResponseEntity<?> addAccountWithDoctorRole(@Valid @RequestBody DoctorRegister doctorRegister, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization").substring(7);
        try {
            aggregator.postAccountWithDoctorRole(restTemplate, doctorRegister.getCredentials(), doctorRegister.getDoctor(), token);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() + e.getCause().getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(doctorRegister, HttpStatus.CREATED);
    }


    @CrossOrigin
    @PostMapping("/agreggator/cont/pacient")
    public ResponseEntity<?> addAccountWithPacientRole(@Valid @RequestBody PacientRegister pacientRegister, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entityJWT = new HttpEntity<>(new AuthRequest("ilinca.turcu555@gmail.com", "Ilinca-113473"), headers);
        String token = restTemplate.postForObject("http://localhost:9090/authenticate", entityJWT, String.class);
        try {
            aggregator.postAccountWithPacientRole(restTemplate, pacientRegister.getCredentials(), pacientRegister.getPacient(), pacientRegister.getPersonalInformation(), token);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() + e.getCause().getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pacientRegister, HttpStatus.CREATED);
    }

    @PostMapping("/agreggator/predonare")
    public ResponseEntity<?> addPredonareData(@Valid @RequestBody JSONObject jsonObject, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        String predonareId;
        try {
            predonareId = aggregator.postPredonareData(restTemplate, jsonObject);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() + e.getCause().getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(predonareId, HttpStatus.CREATED);
    }

    @PostMapping("/agreggator/postdonare")
    public ResponseEntity<?> addPostdonareData(@Valid @RequestBody JSONObject jsonObject, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        String postdonareId;
        try {
            postdonareId = aggregator.postPostdonareData(restTemplate, jsonObject);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() + e.getCause().getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postdonareId, HttpStatus.CREATED);
    }


    //toate toate analizele valabile pentru un pacient
    @RequestMapping(value = "/api/agreggator/donor/{donor_code}/date/{dateIn}", method = RequestMethod.GET)
    public ResponseEntity<?> getAnalize(@PathVariable String donor_code, @PathVariable String dateIn) {

        ResponseEntity<String> postdonare;
        String a, b;
        JSONObject pre, post, total = new JSONObject();
        ResponseEntity<String> predonare;

        System.out.println("linia 97 din aggregator controller " + dateIn);
        try {
            a = aggregator.getPostdonareDataByDateAndDonorCode(restTemplate, dateIn, donor_code).getBody();
            JSONParser parser = new JSONParser();
            post = (JSONObject) parser.parse(a);

            b = aggregator.getPredonareDataByDateAndDonorCode(restTemplate, dateIn, donor_code).getBody();
            pre = (JSONObject) parser.parse(b);

            post.putAll(pre);
            System.out.println(post);

        } catch (EmptyResultDataAccessException | ParseException | NullPointerException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }


    //un get analize by donation form id
    //primeste donation form id si face un req catre endpointul din aggregator cu donor code si createdAt ca sa intoarca toatele analizele
    @RequestMapping(value = "/api/donationForm/tests/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> listTestsResultsByDonationFormId(@PathVariable Integer id) {
        DonationForm donationForm = new DonationForm();
        Date date;
        String appointmentDate;
        try {
            donationForm = donationFormService.getDonationFormById(id);
            date = new Date(donationForm.getCreated_at().getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            appointmentDate = sdf.format(date);
            System.out.println("linia 97 din aggregator controller " + appointmentDate);
            // getAnalize(donationForm.getFk_donor_code(), appointmentDate);
        } catch (NotFoundException | NullPointerException e) {
            return new ResponseEntity<>("Analizele nu sunt introduse in sistem", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(getAnalize(donationForm.getFk_donor_code(), appointmentDate), HttpStatus.OK);
    }


    @RequestMapping(value = "/api/donationForm/tests/pre/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> listTestsResultsPreDonationByDonationFormId(@PathVariable Integer id) {
        DonationForm donationForm = new DonationForm();
        Date date;
        String appointmentDate, data;
        JSONObject preDonation = new JSONObject();
        JSONParser parser = new JSONParser();
        try {
            donationForm = donationFormService.getDonationFormById(id);
            date = new Date(donationForm.getCreated_at().getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            appointmentDate = sdf.format(date);
            data = aggregator.getPredonareDataByDateAndDonorCode(restTemplate, appointmentDate, donationForm.getFk_donor_code()).getBody();
            preDonation = (JSONObject) parser.parse(data);
        } catch (NotFoundException | NullPointerException | ParseException e) {
            return new ResponseEntity<>("Analizele nu sunt introduse in sistem", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(preDonation, HttpStatus.OK);
    }


    @RequestMapping(value = "/api/donationForm/tests/post/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> listTestsResultsPostDonationByDonationFormId(@PathVariable Integer id) {
        DonationForm donationForm = new DonationForm();
        Date date;
        String appointmentDate, data;
        JSONObject postDonation = new JSONObject();
        JSONParser parser = new JSONParser();
        try {
            donationForm = donationFormService.getDonationFormById(id);
            date = new Date(donationForm.getCreated_at().getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            appointmentDate = sdf.format(date);
            data = aggregator.getPostdonareDataByDateAndDonorCode(restTemplate, appointmentDate, donationForm.getFk_donor_code()).getBody();
            postDonation = (JSONObject) parser.parse(data);
        } catch (NotFoundException | NullPointerException | ParseException e) {
            return new ResponseEntity<>("Analizele nu sunt introduse in sistem", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postDonation, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/aggregator/pacient/donor_code", method = RequestMethod.POST)
    public ResponseEntity<?> getDonorCodeByCredentials(@RequestBody Credentials credentials, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String donor_code;
        try {
            donor_code = aggregator.getDonorCodeByCredentials(credentials, token);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(donor_code, HttpStatus.OK);
    }


    @RequestMapping(value = "/api/aggregator/doctor/doctor_code", method = RequestMethod.POST)
    public ResponseEntity<?> getDoctorCodeByCredentials(@RequestBody Credentials credentials, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String doctor_code;
        try {
            doctor_code = aggregator.getDoctorCodeByCredentials(credentials, token);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(doctor_code, HttpStatus.OK);
    }

    //toti pacientii de astazi pentru un anumit doctor, cu personal info
    @RequestMapping(value = "/api/aggregator/appointments/pacient/doctor/{doctor_code}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppointmentAndPacientDetailsForTodayByDoctorCode(@PathVariable Integer doctor_code) {
        ArrayList response = new ArrayList();
        try {
            response = aggregator.getAllAppointmentAndPacientDetailsByDoctorCodeForToday(doctor_code);
        } catch (Exception e) {
            System.out.println("CATCH");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @RequestMapping(value = "/api/aggregator/allAppointments/pacient/doctor/{doctor_code}", method = RequestMethod.GET)
//    public ResponseEntity<?> getAllAppointmentAndPacientDetailsByDoctorCode(@PathVariable Integer doctor_code) {
//        ArrayList response = new ArrayList();
//        try {
//            response = aggregator.getAllAppointmentAndPacientDetailsByDoctorCodeForToday(doctor_code);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }


    @RequestMapping(value = "/api/aggregator/allAppointments/pacient/doctor/{doctor_code}/{pageNo}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAppointmentAndPacientDetailsByDoctorCodePaginated(@PathVariable Integer doctor_code, @PathVariable Integer pageNo, @PathVariable Integer pageSize) {
        ArrayList response = new ArrayList();
        List responsePaginated = null;

        try {
            response = aggregator.getAppointmentAndPacientDetailsHistory(doctor_code, pageNo, pageSize);
            response.forEach(response1 -> System.out.println(response1));
            //responsePaginated = response.subList(pageNo * pageSize, (pageNo + 1) * pageSize);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/api/aggregator/allAppointments/pacient/doctor/{doctor_code}/{pageNo}/{pageSize}/{filter}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAppointmentAndPacientDetailsByDoctorCodePaginatedFiltered(@PathVariable Integer doctor_code, @PathVariable Integer pageNo, @PathVariable Integer pageSize, @PathVariable String filter) {
        ArrayList response = new ArrayList();
        List responsePaginated = null;

        try {
            response = aggregator.getAppointmentAndPacientDetailsHistoryFiltered(doctor_code, pageNo, pageSize, filter);
            response.forEach(response1 -> System.out.println(response1));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
