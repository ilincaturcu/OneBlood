package ac.OneBlood.Controller;

import ac.OneBlood.Model.Pacient;
import ac.OneBlood.Service.PacientService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PacientController {

    @Autowired
    PacientService pacientService;

    //afiseaza toti pacientii
    @RequestMapping(value = "/api/pacients", method = RequestMethod.GET)
    public ResponseEntity<?> listPacients() {
        return new ResponseEntity<>(pacientService.listAllPacients(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/pacient/last/donor_code", method = RequestMethod.GET)
    public ResponseEntity<?> lastDonorCode() {
        List<Pacient> pacients = pacientService.listAllPacients();
        Pacient mostRecent = Collections.max(pacients, Comparator.comparing(Pacient::getDonor_code));
        return new ResponseEntity<>(mostRecent.getDonor_code(), HttpStatus.OK);
    }
//afiseaza pacientul pe baza codului sau de donator
    @RequestMapping(value = "/api/pacient/{donor_code}", method = RequestMethod.GET)
    public ResponseEntity<?> listPacientByDonorCode(@PathVariable String donor_code) {
        Pacient pacient;
        try {
            pacient = pacientService.getPacientByDonorCode(donor_code);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(EntityModel.of(pacient,
                linkTo(methodOn(PacientController.class).listPacientByDonorCode(donor_code)).withSelfRel(),
                linkTo(methodOn(PacientController.class).listPacients()).withRel("pacients")), HttpStatus.OK);
    }


    @RequestMapping(value = "/api/pacient/accountId/{accountId}", method = RequestMethod.GET)
    public ResponseEntity<?> getDonorCodeByEmail(@PathVariable Integer accountId) {
        Pacient pacient;
        try {
            pacient = pacientService.getPacientByAccountId(accountId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pacient.getDonor_code(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/pacient/accountId/donor_code/{donor_code}", method = RequestMethod.GET)
    public ResponseEntity<?> getaccountIdByDonorCode(@PathVariable String donor_code) {
        Pacient pacient;
        try {
            pacient = pacientService.getPacientByDonorCode(donor_code);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pacient.getFk_account_id(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/pacient/cnp/{donor_code}", method = RequestMethod.GET)
    public ResponseEntity<?> listPacientsCNPByDonorCode(@PathVariable String donor_code) {
        Pacient pacient;
        try {
            pacient = pacientService.getPacientByDonorCode(donor_code);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pacient.getCNP(), HttpStatus.OK);
    }


    @RequestMapping(value = "/api/pacient/status/{donor_code}", method = RequestMethod.GET)
    public ResponseEntity<?> getPacientStatusByDonorCode(@PathVariable String donor_code) {
        Pacient pacient;
        try {
            pacient = pacientService.getPacientByDonorCode(donor_code);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pacient.getStatus(), HttpStatus.OK);
    }

    @PutMapping("/api/pacient/{donor_code}")
    public ResponseEntity<?> addPacientByDonorCode(@RequestBody Pacient pacient, @PathVariable("donor_code") String donor_code) throws NotFoundException {
        try {
            pacientService.getPacientByDonorCode(donor_code);
        } catch (Exception e) {
            pacientService.save(Pacient.builder()
                    .CNP(pacient.getCNP())
                    .fk_account_id(pacient.getFk_account_id())
                    .donor_code(pacient.getDonor_code())
                    .self_exclusion_form_id(pacient.getSelf_exclusion_form_id())
                    .created_at(pacient.getCreated_at())
                    .status(pacient.getStatus())
                    .build());
            return new ResponseEntity<>(pacientService.getPacientByDonorCode(donor_code), HttpStatus.CREATED);
        }

        pacientService.save(Pacient.builder()
                .CNP(pacient.getCNP())
                .fk_account_id(pacient.getFk_account_id())
                .donor_code(pacient.getDonor_code())
                .self_exclusion_form_id(pacient.getSelf_exclusion_form_id())
                .created_at(pacient.getCreated_at())
                .status(pacient.getStatus())
                .build());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/api/pacient/quizId/{donor_code}")
    public ResponseEntity<?> addQuizIdByDonorCode(@RequestBody String quizId, @PathVariable("donor_code") String donor_code) throws NotFoundException {
        Pacient pacient = new Pacient();
        try {
            pacient = pacientService.getPacientByDonorCode(donor_code);
        } catch (Exception e) {
            pacient.setSelf_exclusion_form_id(quizId);
            pacientService.save(pacient);
            return new ResponseEntity<>(pacientService.getPacientByDonorCode(donor_code), HttpStatus.CREATED);
        }
        pacient.setSelf_exclusion_form_id(quizId);
        pacientService.save(pacient);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/api/pacient/cnp/{cnp}")
    public ResponseEntity<?> addPacientByCNP(@RequestBody Pacient pacient, @PathVariable("cnp") BigInteger cnp) throws NotFoundException {
        try {
            pacientService.getPacientByCnp(cnp);
        } catch (Exception e) {
            pacientService.save(Pacient.builder()
                    .CNP(pacient.getCNP())
                    .fk_account_id(pacient.getFk_account_id())
                    .donor_code(pacient.getDonor_code())
                    .self_exclusion_form_id(pacient.getSelf_exclusion_form_id())
                    .created_at(pacient.getCreated_at())
                    .build());
            return new ResponseEntity<>(pacientService.getPacientByCnp(cnp), HttpStatus.CREATED);
        }

        pacientService.save(Pacient.builder()
                .CNP(pacient.getCNP())
                .fk_account_id(pacient.getFk_account_id())
                .donor_code(pacient.getDonor_code())
                .self_exclusion_form_id(pacient.getSelf_exclusion_form_id())
                .created_at(pacient.getCreated_at())
                .build());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @CrossOrigin
    @RequestMapping(path = "/api/pacient/{status}/{donor_code}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePacient(@PathVariable("status") String status, @PathVariable("donor_code") String donor_code) throws NotFoundException {

        try {
            pacientService.getPacientByDonorCode(donor_code);

        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Pacient pacient = pacientService.getPacientByDonorCode(donor_code);
        pacient.setStatus(status);
        pacientService.save(pacient);
        System.out.println(status);
        return new ResponseEntity<>(pacient, HttpStatus.NO_CONTENT);
    }
}
