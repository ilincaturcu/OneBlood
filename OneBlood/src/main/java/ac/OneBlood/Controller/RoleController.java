package ac.OneBlood.Controller;

import ac.OneBlood.Model.Credentials;
import ac.OneBlood.Model.Role;
import ac.OneBlood.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/api/roles", method = RequestMethod.GET)
    public ResponseEntity<?> listRoles() {
        List<Role> roles = roleService.listAllRoles();
        return new ResponseEntity<>(roles.stream().map(Role::getRole_name).collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/role/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> listRoleById(@PathVariable Integer id) {
        String role = roleService.getRoleById(id).getRole_name();
        System.out.println(role.toString());
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

}
