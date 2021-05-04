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
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class DonationFormController {

    @Autowired
    DonationFormService donationFormService;

    @RequestMapping(value = "/api/donationForm/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> listDonationFormById(@PathVariable Integer id) {
        DonationForm donationForm;
        try {
            donationForm = donationFormService.getDonationFormById(id);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(EntityModel.of(donationForm,
                linkTo(methodOn(DonationFormController.class).listDonationFormById(id)).withSelfRel(),
                linkTo(methodOn(PacientController.class).listPacientByDonorCode(donationForm.getDonor_code())).withRel("pacientByDonorCode")), HttpStatus.OK);
    }

    //crearea unei resurse noi sau inlocuirea completa
    @PutMapping("/api/donationForm/{donor_code}")
    public ResponseEntity<?> addDonationForm(@RequestBody DonationForm donationForm, @PathVariable("donor_code") String donor_code) throws NotFoundException {
        //pentru inlocuirea completa (update) 204 no content
        //pentru crearea unei resurse noi 201 created
        try {
            donationFormService.getDonationFormByDonorCode(donor_code);
        } catch (NotFoundException e) {
            donationFormService.save(DonationForm.builder()
                    .donor_code(donor_code)
                    .id_analize_post_donare(donationForm.getId_analize_post_donare())
                    .id_analize_pre_donare(donationForm.getId_analize_pre_donare())
                    .created_at(donationForm.getCreated_at())
                    .build());
            return new ResponseEntity<>(donationFormService.getDonationFormByDonorCode(donor_code), HttpStatus.CREATED);
        }
        donationFormService.save(DonationForm.builder()
                .donor_code(donor_code)
                .id_analize_post_donare(donationForm.getId_analize_post_donare())
                .id_analize_pre_donare(donationForm.getId_analize_pre_donare())
                .created_at(donationForm.getCreated_at())
                .build());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
