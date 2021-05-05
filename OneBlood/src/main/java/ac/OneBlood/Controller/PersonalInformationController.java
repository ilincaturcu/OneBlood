package ac.OneBlood.Controller;

import ac.OneBlood.Model.PersonalInformation;
import ac.OneBlood.Service.PersonalInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PersonalInformationController {

    @Autowired
    PersonalInformationService personalInformationService;

    @RequestMapping(value = "/api/personalInformation/{CNP}", method = RequestMethod.GET)
    public ResponseEntity<?> listPersonalInfoByCNP(@PathVariable BigInteger CNP) throws Exception {
        PersonalInformation personalInformation;
        try {
            personalInformation = personalInformationService.getPersonalInformationByCNP(CNP);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(EntityModel.of(personalInformation,
                linkTo(methodOn(PersonalInformationController.class).listPersonalInfoByCNP(CNP)).withSelfRel()), HttpStatus.OK);
    }

    //crearea unei resurse noi sau inlocuirea completa
    @PutMapping("/api/personalInformation/{cnp}")
    public ResponseEntity<?> addPersonalInformationByCNP(@RequestBody PersonalInformation personalInformation, @PathVariable("cnp") BigInteger cnp) throws Exception {
        //pentru inlocuirea completa (update) 204 no content
        //pentru crearea unei resurse noi 201 created
        try {
            personalInformationService.getPersonalInformationByCNP(cnp);
        } catch (Exception e) {
            personalInformationService.save((PersonalInformation.builder())
                    .CNP(cnp)
                    .name(personalInformation.getName())
                    .surname(personalInformation.getSurname())
                    .birthdate(personalInformation.getBirthdate())
                    .mothers_name(personalInformation.getMothers_name())
                    .fathers_name(personalInformation.getFathers_name())
                    .identity_card_number(personalInformation.getIdentity_card_number())
                    .identity_card_series(personalInformation.getIdentity_card_series())
                    .phone_number(personalInformation.getPhone_number())
                    .sex(personalInformation.getSex())
                    .job(personalInformation.getJob())
                    .build());
            return new ResponseEntity<>(personalInformationService.getPersonalInformationByCNP(cnp), HttpStatus.CREATED);
        }
        personalInformationService.save((PersonalInformation.builder())
                .CNP(cnp)
                .name(personalInformation.getName())
                .surname(personalInformation.getSurname())
                .birthdate(personalInformation.getBirthdate())
                .mothers_name(personalInformation.getMothers_name())
                .fathers_name(personalInformation.getFathers_name())
                .identity_card_number(personalInformation.getIdentity_card_number())
                .identity_card_series(personalInformation.getIdentity_card_series())
                .phone_number(personalInformation.getPhone_number())
                .sex(personalInformation.getSex())
                .job(personalInformation.getJob())
                .build());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
