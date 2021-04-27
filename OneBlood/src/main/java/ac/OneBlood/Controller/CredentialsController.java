package ac.OneBlood.Controller;

import ac.OneBlood.Model.AuthRequest;
import ac.OneBlood.Model.Credentials;
import ac.OneBlood.Repository.CredentialsRepository;
import ac.OneBlood.Service.CredentialsService;
import ac.OneBlood.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class CredentialsController {

    @Autowired
    CredentialsService credentialsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String welcome(){
        return "Welcome";
    }

    @PostMapping("/api/cont")
    public ResponseEntity<?> addCont(@RequestBody Credentials credentials){
        //pentru inlocuirea completa (update) 204 no content
        //pentru crearea unei resurse noi 201 created
        credentialsService.save(Credentials.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .build());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping(value="/authenticate", produces={"application/json","application/xml"}, consumes="application/json")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            System.out.println(authRequest.getUserName());
            System.out.println(authRequest.getPassword());
            throw new Exception(ex.getMessage());
        }
        String jwt = jwtUtil.generateToken(authRequest.getUserName());
        System.out.println(jwt);
        return new ResponseEntity<String>( jwt, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/cont/{id}", method =  RequestMethod.GET)
    public ResponseEntity<?> listContById(@PathVariable Integer id){
        Credentials credentials = credentialsService.get(id);
        System.out.println(credentials.toString());
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}
