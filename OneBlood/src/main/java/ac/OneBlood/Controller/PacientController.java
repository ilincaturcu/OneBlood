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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PacientController {

    @Autowired
    PacientService pacientService;

    @RequestMapping(value = "/api/pacients", method = RequestMethod.GET)
    public ResponseEntity<?> listPacients() {
        return new ResponseEntity<>(pacientService.listAllPacients(), HttpStatus.OK);
    }

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

    //crearea unei resurse noi sau inlocuirea completa
    @PutMapping("/api/pacient/{donor_code}")
    public ResponseEntity<?> addPacientByDonorCode(@RequestBody Pacient pacient, @PathVariable("donor_code") String donor_code) throws NotFoundException {
        //pentru inlocuirea completa (update) 204 no content
        //pentru crearea unei resurse noi 201 created
        try {
            pacientService.getPacientByDonorCode(donor_code);
        } catch (Exception e) {
            pacientService.save(Pacient.builder()
                    .CNP(pacient.getCNP())
                    .fk_account_id(pacient.getFk_account_id())
                    .donor_code(pacient.getDonor_code())
                    .self_exclusion_form_id(pacient.getSelf_exclusion_form_id())
                    .created_at(pacient.getCreated_at())
                    .build());
            return new ResponseEntity<>(pacientService.getPacientByDonorCode(donor_code), HttpStatus.CREATED);
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


    //crearea unei resurse noi sau inlocuirea completa
    @PutMapping("/api/pacient/cnp/{cnp}")
    public ResponseEntity<?> addPacientByCNP(@RequestBody Pacient pacient, @PathVariable("cnp") BigInteger cnp) throws NotFoundException {
        //pentru inlocuirea completa (update) 204 no content
        //pentru crearea unei resurse noi 201 created
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
    @RequestMapping(path="/api/pacient/{status}/{donor_code}", method = RequestMethod.PUT)
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
