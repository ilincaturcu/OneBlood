package ac.OneBlood.Controller;

import ac.OneBlood.Model.AuthRequest;
import ac.OneBlood.Model.Credentials;
import ac.OneBlood.Service.CredentialsService;
import ac.OneBlood.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CredentialsController {

    @Autowired
    CredentialsService credentialsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CredentialsRoleController credentialsRoleController;
    @Autowired
    private RoleController roleController;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String welcome() {
        return "Welcome";
    }

    @PostMapping("/api/cont")
    public ResponseEntity<?> addCont(@RequestBody Credentials credentials) {
        //pentru inlocuirea completa (update) 204 no content
        //pentru crearea unei resurse noi 201 created
        credentialsService.save(Credentials.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .build());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/authenticate")
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
        return new ResponseEntity<String>(jwt, HttpStatus.OK);
    }

    @PostMapping(value = "/authorization")
    public ResponseEntity<?> getAuthorization(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            System.out.println(authRequest.getUserName());
            System.out.println(authRequest.getPassword());
            throw new Exception(ex.getMessage());
        }

        Integer id = credentialsService.getCredentialsByEmail(authRequest.getUserName()).getAccount_id();
        Integer idRole = (Integer) credentialsRoleController.listCredentialsRoleByUserId(id).getBody();
        String role = (String) roleController.listRoleById(idRole).getBody();
        return new ResponseEntity<String>(role, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/cont/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> listContById(@PathVariable Integer id) {
        Credentials credentials = credentialsService.get(id);
        System.out.println(credentials.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/api/cont/email/accountId/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getEmailByAccountId(@PathVariable Integer id) {
        Credentials credentials = credentialsService.get(id);

        return new ResponseEntity<>(credentials.getEmail(), HttpStatus.OK);
    }


    @RequestMapping(value = "/api/cont/email/{email}", method = RequestMethod.GET)
    public ResponseEntity<?> listContByEmail(@PathVariable String email) {
        Credentials credentials = credentialsService.getCredentialsByEmail(email);
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/mail/existingAccount/{email}", method = RequestMethod.GET)
    public ResponseEntity<?> validateIfTheEmailHasAnAccount(@PathVariable String email) {
        Credentials credentials = null;
        try {
            credentials = credentialsService.getCredentialsByEmail(email);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

        if (credentials != null)
            return new ResponseEntity<>(true, HttpStatus.OK);
        else return new ResponseEntity<>(false, HttpStatus.OK);

    }

    @RequestMapping(value = "/api/credentials", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<?> addNewCredentials(@RequestBody Credentials credentials) {
        Credentials newCredentials;
        try {
            credentialsService.get(credentials.getAccount_id());
        } catch (Exception e) {

            newCredentials = Credentials.builder()
                    .email(credentials.getEmail())
                    .password(passwordEncoder.encode(credentials.getPassword()))
                    .build();
            credentialsService.save(newCredentials);
            return new ResponseEntity<>(newCredentials.getAccount_id(), HttpStatus.CREATED);
        }
        newCredentials = Credentials.builder()
                .email(credentials.getEmail())
                .password(passwordEncoder.encode(credentials.getPassword()))
                .build();
        return new ResponseEntity<>(newCredentials.getAccount_id(), HttpStatus.OK);
    }


}
