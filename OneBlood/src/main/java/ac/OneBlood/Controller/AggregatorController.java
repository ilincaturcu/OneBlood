package ac.OneBlood.Controller;

import ac.OneBlood.Model.AuthRequest;
import ac.OneBlood.Model.Credentials;
import ac.OneBlood.Model.DoctorRegister;
import ac.OneBlood.Model.PacientRegister;
import ac.OneBlood.Service.Aggregator;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AggregatorController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    Aggregator aggregator;

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
        String token = restTemplate.postForObject("http://localhost:9090/authenticate",entityJWT, String.class);
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
}
