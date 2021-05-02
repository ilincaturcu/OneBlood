package ac.OneBlood.Controller;

import ac.OneBlood.Model.PersonalInformation;
import ac.OneBlood.Service.PersonalInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        try { personalInformation = personalInformationService.getPersonalInformationByCNP(CNP);}
        catch(Exception e){ return new ResponseEntity<> ( HttpStatus.BAD_REQUEST); }
        return new ResponseEntity<> (EntityModel.of(personalInformation,
                linkTo(methodOn(PersonalInformationController.class).listPersonalInfoByCNP(CNP)).withSelfRel()), HttpStatus.OK);
    }
}
