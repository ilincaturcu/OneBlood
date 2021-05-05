package ac.OneBlood.Controller;

import ac.OneBlood.Model.DoctorRegister;
import ac.OneBlood.Model.PacientRegister;
import ac.OneBlood.Service.Aggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class AggregatorController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    Aggregator aggregator;

    @PostMapping("/agreggator/cont/doctor")
    public ResponseEntity<?> addAccountWithDoctorRole(@Valid @RequestBody DoctorRegister doctorRegister, BindingResult bindingResult, HttpServletRequest request) throws Exception {
//        ResponseEntity<String> token = restTemplate.getForEntity("http://localhost:9090/api/authenticate", String.class);
        String token = request.getHeader("Authorization").substring(7);
        try {
            aggregator.postAccountWithDoctorRole(restTemplate, doctorRegister.getCredentials(), doctorRegister.getDoctor(), token);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() + e.getCause().getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(doctorRegister, HttpStatus.CREATED);
    }


    @PostMapping("/agreggator/cont/pacient")
    public ResponseEntity<?> addAccountWithPacientRole(@Valid @RequestBody PacientRegister pacientRegister, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization").substring(7);
        try {
            aggregator.postAccountWithPacientRole(restTemplate, pacientRegister.getCredentials(), pacientRegister.getPacient(), pacientRegister.getPersonalInformation(), token);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() + e.getCause().getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pacientRegister, HttpStatus.CREATED);
    }
}
