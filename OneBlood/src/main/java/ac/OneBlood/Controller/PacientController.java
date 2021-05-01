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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PacientController {

    @Autowired
    PacientService pacientService;

    @RequestMapping(value = "/api/pacients", method = RequestMethod.GET)
    public ResponseEntity<?> listPacients() { return new ResponseEntity<> ( pacientService.listAllPacients(), HttpStatus.OK); }

    @RequestMapping(value = "/api/pacient/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> listPacientByDonorCode(@PathVariable String id) {
        Pacient pacient;
        try { pacient = pacientService.getPacientByDonorCode(id); }
        catch(NotFoundException e){ return new ResponseEntity<> ( HttpStatus.BAD_REQUEST); }
        return new ResponseEntity<> (EntityModel.of(pacient,
                linkTo(methodOn(PacientController.class).listPacientByDonorCode(id)).withSelfRel(),
                linkTo(methodOn(PacientController.class).listPacients()).withRel("pacients")),  HttpStatus.OK);
    }
}
