package ac.OneBlood.Controller;

import ac.OneBlood.Model.Role;
import ac.OneBlood.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/api/roles", method = RequestMethod.GET)
    public ResponseEntity<?> listRoles() {
        List<Role> roles = roleService.listAllRoles();
        return new ResponseEntity<>(roles.stream().map(Role::getRole_name).collect(Collectors.toList()), HttpStatus.OK);
    }

}
