package ac.OneBlood.Controller;

import ac.OneBlood.Model.DonationForm;
import ac.OneBlood.Model.Pacient;
import ac.OneBlood.Service.DonationFormService;
import ac.OneBlood.Service.PacientService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
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
        } catch (NotFoundException e) {
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
        } catch (NotFoundException e) {
            pacientService.save(Pacient.builder()
                    .CNP(pacient.getCNP())
                    .fk_account_id(pacient.getFk_account_id())
                    .fk_donor_code(pacient.getFk_donor_code())
                    .self_exclusion_form_id(pacient.getSelf_exclusion_form_id())
                    .created_at(pacient.getCreated_at())
                    .build());
            return new ResponseEntity<>(pacientService.getPacientByDonorCode(donor_code), HttpStatus.CREATED);
        }

        pacientService.save(Pacient.builder()
                .CNP(pacient.getCNP())
                .fk_account_id(pacient.getFk_account_id())
                .fk_donor_code(pacient.getFk_donor_code())
                .self_exclusion_form_id(pacient.getSelf_exclusion_form_id())
                .created_at(pacient.getCreated_at())
                .build());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
