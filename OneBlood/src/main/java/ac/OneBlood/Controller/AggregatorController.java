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
import org.springframework.hateoas.EntityModel;
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
import java.util.Date;
import java.util.TimeZone;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
//        ResponseEntity<String> token = restTemplate.getForEntity("http://localhost:9090/authenticate", String.class);
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
        HttpEntity<Object> entityJWT = new HttpEntity<>(new AuthRequest("ilincafturcu@gmail.com", "Ilinca-113473"), headers);
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
    public ResponseEntity<?> getAnalize(@PathVariable String donor_code, @PathVariable String dateIn){

        ResponseEntity<String> postdonare;
        String a, b;
        JSONObject pre, post, total = new JSONObject();
        ResponseEntity<String>  predonare;
        try {
            a = aggregator.getPostdonareDataByDateAndDonorCode(restTemplate, dateIn, donor_code).getBody();
            JSONParser parser = new JSONParser();
            post = (JSONObject) parser.parse(a);

            b = aggregator.getPredonareDataByDateAndDonorCode(restTemplate, dateIn, donor_code).getBody();
            pre = (JSONObject) parser.parse(b);

            post.putAll(pre);
            System.out.println(post);
//            total.put("pre",pre);
//            total.put("post", post);
           // System.out.println(total);

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
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            appointmentDate = sdf.format(date);
            System.out.println(appointmentDate);
           // getAnalize(donationForm.getFk_donor_code(), appointmentDate);
        } catch (NotFoundException | NullPointerException e ) {
            return new ResponseEntity<>("Analizele nu sunt introduse in sistem", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(getAnalize(donationForm.getFk_donor_code(), appointmentDate), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/aggregator/pacient/donor_code", method = RequestMethod.POST)
    public ResponseEntity<?> getDonorCodeByCredentials(@RequestBody Credentials credentials, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String donor_code;
        try {
            donor_code=aggregator.getDonorCodeByCredentials(credentials, token);
        } catch (NullPointerException e ) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(donor_code, HttpStatus.OK);
    }



}
