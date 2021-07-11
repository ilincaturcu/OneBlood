package ac.OneBlood.Service;

import ac.OneBlood.Model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;


@Service
public class Aggregator {
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    PacientService pacientService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    PersonalInformationService personalInformationService;
    @Autowired
    RestTemplate restTemplate;

    //crearea unui cont de doctor (credentiale si informatii despre doctor)
    public void postAccountWithDoctorRole(RestTemplate restTemplate, Credentials credentials, Doctor doctor, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> entityCredentials = new HttpEntity<>(credentials, headers);

        try {
            ResponseEntity<Integer> accountId = restTemplate.postForEntity("http://localhost:9090/api/credentials", entityCredentials, Integer.class);
            doctor.setFk_account_id(accountId.getBody());
            HttpEntity<Object> entityDoctor = new HttpEntity<>(doctor, headers);
            restTemplate.put("http://localhost:9090/api/doctor/" + doctor.getDoctor_code(), entityDoctor);
            HttpEntity<Object> entityCredentialsRole = new HttpEntity<>(new CredentialsRole(accountId.getBody(), 2), headers);
            restTemplate.put("http://localhost:9090/api/credentialsrole", entityCredentialsRole);

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
        }
    }

    //crearea unui cont de pacient (credentiale, entitate de pacient si informatii personale)
    public void postAccountWithPacientRole(RestTemplate restTemplate, Credentials credentials, Pacient pacient, PersonalInformation personalInformation, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> entityCredentials = new HttpEntity<>(credentials, headers);

        try {
            ResponseEntity<Integer> accountId = restTemplate.postForEntity("http://localhost:9090/api/credentials", entityCredentials, Integer.class);
            pacient.setFk_account_id(accountId.getBody());
            HttpEntity<Object> entityPacient = new HttpEntity<>(pacient, headers);
            restTemplate.put("http://localhost:9090/api/pacient/" + pacient.getDonor_code(), entityPacient);
            HttpEntity<Object> entityCredentialsRole = new HttpEntity<>(new CredentialsRole(accountId.getBody(), 2), headers);
            restTemplate.put("http://localhost:9090/api/credentialsrole", entityCredentialsRole, String.class);
            personalInformation.setCNP(pacient.getCNP());
            HttpEntity<Object> entityPersonal = new HttpEntity<>(personalInformation, headers);
            restTemplate.put("http://localhost:9090/api/personalInformation/" + pacient.getCNP(), entityPersonal);

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            if (!HttpStatus.OK.equals(httpClientOrServerExc.getStatusCode())) {
                System.out.println(httpClientOrServerExc.getMessage());
            }

        }
    }

    public String getDonorCodeByCredentials(Credentials credentials, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        String donorCode = null;
        Credentials credentials1 = null;

        try {
            credentials1 = new TestRestTemplate().exchange(
                    "http://localhost:9090/api/cont/email/" + credentials.getEmail(), HttpMethod.GET, entity, Credentials.class).getBody();
            donorCode = new TestRestTemplate().exchange(
                    "http://localhost:9090/api/pacient/accountId/" + credentials1.getAccount_id().toString(), HttpMethod.GET, entity, String.class).getBody();

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            if (!HttpStatus.OK.equals(httpClientOrServerExc.getStatusCode())) {
            }

            return donorCode;
        }
        return donorCode;
    }


    public String getDoctorCodeByCredentials(Credentials credentials, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        String doctor_code = null;
        Credentials credentials1 = null;
        try {
            credentials1 = new TestRestTemplate().exchange(
                    "http://localhost:9090/api/cont/email/" + credentials.getEmail(), HttpMethod.GET, entity, Credentials.class).getBody();
            doctor_code = doctorService.getDoctorByAccountId(credentials1.getAccount_id()).getDoctor_code().toString();
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            if (!HttpStatus.OK.equals(httpClientOrServerExc.getStatusCode())) {
            }

            return doctor_code;
        }
        return doctor_code;
    }


    public String postPredonareData(RestTemplate restTemplate, JSONObject predonareData) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> predonareId = null;
        HttpEntity<Object> entity = new HttpEntity<>(predonareData, headers);
        try {
            predonareId = restTemplate.postForEntity("http://localhost:7070/api/predonare", entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            throw new Exception(httpClientOrServerExc.getMessage());
        }
        return predonareId.getBody();
    }


    public String postPostdonareData(RestTemplate restTemplate, JSONObject postdonareData) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> postdonareId = null;
        HttpEntity<Object> entity = new HttpEntity<>(postdonareData, headers);
        try {
            postdonareId = restTemplate.postForEntity("http://localhost:7070/api/postdonare", entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            throw new Exception((httpClientOrServerExc.getMessage()));
        }
        return postdonareId.getBody();
    }


    public ResponseEntity<String> getPostdonareDataByDateAndDonorCode(RestTemplate restTemplate, String date, String donor_code) {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> postdonareId = null;
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        try {
            postdonareId = restTemplate.getForEntity("http://localhost:7070/api/postdonare/date/" + date + "/donor_code/" + donor_code, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            System.out.println(httpClientOrServerExc.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postdonareId.getBody(), HttpStatus.OK);
    }


    public ResponseEntity<String> getPredonareDataByDateAndDonorCode(RestTemplate restTemplate, String date, String donor_code) {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> predonareId = ResponseEntity.ok("default state");
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        try {
            predonareId = restTemplate.getForEntity("http://localhost:7070/api/predonare/date/" + date + "/donor_code/" + donor_code, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            System.out.println(httpClientOrServerExc.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(predonareId.getBody(), HttpStatus.OK);
    }

    public List<Appointment> getAppointmentByDoctorCode(Integer doctor_code, Integer index, Integer size) {
        return appointmentService.getAppointmentByDoctorCode(doctor_code, index, size);
    }

    public List<Appointment> getAllAppointmentsByDoctorCode(Integer doctor_code) {
        return appointmentService.getAllAppointmentsByDoctorCode(doctor_code);
    }

    public Pacient getPacientByDonorCode(String donor_code) throws NotFoundException {
        return pacientService.getPacientByDonorCode(donor_code);
    }

    public PersonalInformation getPacientInfoByCNP(BigInteger cnp) throws Exception {
        return personalInformationService.getPersonalInformationByCNP(cnp);
    }

    public ArrayList getAppointmentAndPacientDetailsHistory(Integer doctor_code, Integer index, Integer size) {
        JSONObject pacientJson, appointmentJson, personalJson = null;
        JSONObject finalJson = new JSONObject();
        JSONParser parser = new JSONParser();
        List<Appointment> appointmentList = null;
        Pacient pacient = null;
        PersonalInformation personalInformation = null;
        ObjectMapper mapper = new ObjectMapper();
        ArrayList arrayList = new ArrayList();
        try {
            appointmentList = getAppointmentByDoctorCode(doctor_code, index, size);
            //for each appointment get fk_donor code and make call to pacient
            //from pacient get cnp and make call to personal info
            Date day = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getDefault());
            String formattedDate = sdf.format(day);
           // appointmentList.forEach(appointment -> System.out.println(appointment.toString()));
            for (Appointment appointment : appointmentList) {
                finalJson = new JSONObject();
                try {
                    pacient = getPacientByDonorCode(appointment.getFk_donor_code());
                    personalInformation = getPacientInfoByCNP(pacient.getCNP());

                    pacientJson = (JSONObject) parser.parse(mapper.writeValueAsString(pacient));
                    appointmentJson = (JSONObject) parser.parse(mapper.writeValueAsString(appointment));
                    personalJson = (JSONObject) parser.parse(mapper.writeValueAsString(personalInformation));

                    finalJson.put("pacient", pacientJson);
                    finalJson.put("personalInformation", personalJson);
                    finalJson.put("appointment", appointmentJson);

                    if (finalJson != null)
                        arrayList.add(finalJson);
                } catch (ParseException | NotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (NullPointerException e) {
            return null;
        }
        return arrayList;
    }


    public ArrayList getAllAppointmentAndPacientDetailsByDoctorCodeForToday(Integer doctor_code) {
        JSONObject pacientJson, appointmentJson, personalJson = null;
        JSONObject finalJson = new JSONObject();
        JSONParser parser = new JSONParser();
        List<Appointment> appointmentList = null;
        Pacient pacient = null;
        PersonalInformation personalInformation = null;
        ObjectMapper mapper = new ObjectMapper();
        ArrayList arrayList = new ArrayList();
        try {
            Date day = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getDefault());
            String formattedDate = sdf.format(day);
            appointmentList = getAllAppointmentsByDoctorCode(doctor_code).stream().filter(appointment -> sdf.format(appointment.getAppointment_date()).equals(formattedDate)).collect(Collectors.toList());

            for (Appointment appointment : appointmentList) {
                finalJson = new JSONObject();
                try {
                    pacient = getPacientByDonorCode(appointment.getFk_donor_code());
                    personalInformation = getPacientInfoByCNP(pacient.getCNP());

                    pacientJson = (JSONObject) parser.parse(mapper.writeValueAsString(pacient));
                    appointmentJson = (JSONObject) parser.parse(mapper.writeValueAsString(appointment));
                    personalJson = (JSONObject) parser.parse(mapper.writeValueAsString(personalInformation));

                    finalJson.put("pacient", pacientJson);
                    finalJson.put("personalInformation", personalJson);
                    finalJson.put("appointment", appointmentJson);

                    if (finalJson != null)
                        arrayList.add(finalJson);
                } catch (ParseException | NotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return arrayList;
    }


    public ArrayList getAppointmentAndPacientDetailsHistoryFiltered(Integer doctor_code, Integer index, Integer size, String filterDonorCode) {
        JSONObject pacientJson, appointmentJson, personalJson = null;
        JSONObject finalJson = new JSONObject();
        JSONParser parser = new JSONParser();
        List<Appointment> appointmentList = null;
        Pacient pacient = null;
        PersonalInformation personalInformation = null;
        ObjectMapper mapper = new ObjectMapper();
        ArrayList arrayList = new ArrayList();
        try {
            appointmentList = appointmentService.getAppointmentByDoctorCodeFilterByDonorCode(doctor_code, index, size, filterDonorCode);

            //appointmentList.forEach(appointment -> System.out.println(appointment));
            for (Appointment appointment : appointmentList) {
                finalJson = new JSONObject();
                try {
                    pacient = getPacientByDonorCode(appointment.getFk_donor_code());
                    personalInformation = getPacientInfoByCNP(pacient.getCNP());

                    pacientJson = (JSONObject) parser.parse(mapper.writeValueAsString(pacient));
                    appointmentJson = (JSONObject) parser.parse(mapper.writeValueAsString(appointment));
                    personalJson = (JSONObject) parser.parse(mapper.writeValueAsString(personalInformation));

                    finalJson.put("pacient", pacientJson);
                    finalJson.put("personalInformation", personalJson);
                    finalJson.put("appointment", appointmentJson);

                    if (finalJson != null)
                        arrayList.add(finalJson);
                } catch (ParseException | NotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (NullPointerException e) {
            return null;
        }
        return arrayList;
    }

}
