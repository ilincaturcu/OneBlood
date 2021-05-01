package ac.OneBlood.Service;

import ac.OneBlood.Model.Credentials;
import ac.OneBlood.Repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CredentialsRepository credentialsRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Credentials credentials = credentialsRepository.findByEmail(email);
       if(credentials==null){
           throw new UsernameNotFoundException("Could not find user");
       }
        return new User(credentials.getEmail(), credentials.getPassword(), new ArrayList<>());
    }
}
