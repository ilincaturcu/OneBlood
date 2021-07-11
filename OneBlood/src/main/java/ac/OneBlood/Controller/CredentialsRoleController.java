package ac.OneBlood.Controller;

import ac.OneBlood.Model.CredentialsRole;
import ac.OneBlood.Service.CredentialsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CredentialsRoleController {
    @Autowired
    CredentialsRoleService credentialsRoleService;

    @RequestMapping(value = "/api/credentialsrole/roleID/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> listCredentialsRoleByUserId(@PathVariable Integer userId) {
        CredentialsRole credentialsRole = credentialsRoleService.getCredentialsRoleByUserId(userId);
        return new ResponseEntity<>(credentialsRole.getFk_id_role(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/credentialsrole/userID/{roleId}", method = RequestMethod.GET)
    public ResponseEntity<?> listCredentialsRoleByRoleId(@PathVariable Integer roleId) {
        CredentialsRole credentialsRole = credentialsRoleService.getCredentialsRoleByRoleId(roleId);
        return new ResponseEntity<>(EntityModel.of(credentialsRole,
                linkTo(methodOn(CredentialsRoleController.class).listCredentialsRoleByRoleId(roleId)).withSelfRel()), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/credentialsrole", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<?> addCredentialsRoleByUserId(@RequestBody CredentialsRole credentialsRole) {
        //verifica daca exista, daca nu exista il creezi => 201 created
        //daca exista face update 200ok
        try {
            credentialsRoleService.getCredentialsRoleByUserId(credentialsRole.getFk_account_id());
        } catch (Exception e) {
            credentialsRoleService.save(credentialsRole);
            return new ResponseEntity<>(credentialsRole, HttpStatus.CREATED);
        }
        credentialsRoleService.save(credentialsRole);
        return new ResponseEntity<>(credentialsRole, HttpStatus.OK);
    }

}
