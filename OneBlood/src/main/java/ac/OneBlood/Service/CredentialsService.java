package ac.OneBlood.Service;

import ac.OneBlood.Model.Credentials;
import ac.OneBlood.Repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CredentialsService {

    @Autowired
    CredentialsRepository credentialsRepository;

    public List<Credentials> listAllCredentialsData() {
        return credentialsRepository.findAll();
    }

    public void save(Credentials credentials) {
        credentialsRepository.save(credentials);
    }

    public Credentials get(Integer id) {
        if (credentialsRepository.findById(id).isPresent())
            return credentialsRepository.findById(id).get();
        else
            throw new EmptyResultDataAccessException(id);
    }
}
