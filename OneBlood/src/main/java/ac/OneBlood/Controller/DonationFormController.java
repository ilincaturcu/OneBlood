package ac.OneBlood.Controller;

import ac.OneBlood.Model.Doctor;
import ac.OneBlood.Model.DonationForm;
import ac.OneBlood.Service.DonationFormService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class DonationFormController {

    @Autowired
    DonationFormService donationFormService;

    @RequestMapping(value = "/api/donationForm/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> listDonationFormById(@PathVariable Integer id) {
        DonationForm donationForm;
        try { donationForm = donationFormService.getDonationFormById(id); }
        catch(NotFoundException e){ return new ResponseEntity<> ( HttpStatus.BAD_REQUEST); }
        return new ResponseEntity<> (EntityModel.of(donationForm,
                linkTo(methodOn(DonationFormController.class).listDonationFormById(id)).withSelfRel(),
                linkTo(methodOn(PacientController.class).listPacientByDonorCode(donationForm.getDonor_code())).withRel("pacientByDonorCode")),  HttpStatus.OK);
    }
}
