package ac.OneBlood.Controller;

import ac.OneBlood.Model.DonationForm;
import ac.OneBlood.Service.DonationFormService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class DonationFormController {

    @Autowired
    DonationFormService donationFormService;

    //intoarce fisa de donare pe baza id-ului ei
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
                linkTo(methodOn(PacientController.class).listPacientByDonorCode(donationForm.getFk_donor_code())).withRel("pacientByDonorCode")), HttpStatus.OK);
    }

    //intoarce fise de donare pe baza codului dontorului si a datei
    @RequestMapping(value = "/api/donationForm/{donor_code}/{date}", method = RequestMethod.GET)
    public ResponseEntity<?> listDonationFormByDonorCodeAndDate(@PathVariable String donor_code, @PathVariable String date) throws ParseException {
        List<DonationForm> donationForms;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date appointmentDate = sdf.parse(date);
        try {
            donationForms = donationFormService
                    .getDonationFormByDonorCode(donor_code)
                    .stream()
                    .filter(form -> {
                        try {
                            return sdf.parse(form.getCreated_at().toString()).equals(appointmentDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(donationForms.stream().findFirst(), HttpStatus.OK);
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
                    .fk_donor_code(donor_code)
                    .id_analize_post_donare(donationForm.getId_analize_post_donare())
                    .id_analize_pre_donare(donationForm.getId_analize_pre_donare())
                    .created_at(donationForm.getCreated_at())
                    .build());
            return new ResponseEntity<>(donationFormService.getDonationFormByDonorCode(donor_code), HttpStatus.CREATED);
        }
        donationFormService.save(DonationForm.builder()
                .fk_donor_code(donor_code)
                .id_analize_post_donare(donationForm.getId_analize_post_donare())
                .id_analize_pre_donare(donationForm.getId_analize_pre_donare())
                .created_at(donationForm.getCreated_at())
                .build());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/api/donationForm/{donationFormId}/{id_predonare}/{id_postdonare}")
    public ResponseEntity<?> addTestsIdsAfterAnalize(@PathVariable Integer donationFormId, @PathVariable String id_predonare, @PathVariable String id_postdonare) throws NotFoundException {
        DonationForm donationForm = new DonationForm();
        try {
            donationForm = donationFormService.getDonationFormById(donationFormId);
            donationForm.setId_analize_pre_donare(id_predonare);
            donationForm.setId_analize_post_donare(id_postdonare);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        donationFormService.save(donationForm);
        return new ResponseEntity<>(donationForm, HttpStatus.NO_CONTENT);
    }

}
