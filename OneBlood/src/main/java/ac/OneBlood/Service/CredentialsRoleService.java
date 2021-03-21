package ac.OneBlood.Service;

import ac.OneBlood.Model.CredentialsRole;
import ac.OneBlood.Repository.CredentialsRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CredentialsRoleService {

    @Autowired
    CredentialsRoleRepository credentialsRoleRepository;

    public List<CredentialsRole> listAllCredentialsRoleData() {
        return credentialsRoleRepository.findAll();
    }

}
