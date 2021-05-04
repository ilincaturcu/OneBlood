package ac.OneBlood.Service;

import ac.OneBlood.Model.CredentialsRole;
import ac.OneBlood.Repository.CredentialsRepository;
import ac.OneBlood.Repository.CredentialsRoleRepository;
import ac.OneBlood.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CredentialsRoleService {

    @Autowired
    CredentialsRoleRepository credentialsRoleRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CredentialsRepository credentialsRepository;

    public List<CredentialsRole> listAllCredentialsRoleData() {
        return credentialsRoleRepository.findAll();
    }

    public CredentialsRole getCredentialsRoleByUserId(Integer userId) {
        if (credentialsRoleRepository.findById(userId).isPresent())
            return credentialsRoleRepository.findById(userId).get();
        else
            throw new EmptyResultDataAccessException(userId);
    }

    public CredentialsRole getCredentialsRoleByRoleId(Integer roleId) {
        if (credentialsRoleRepository.findCredentialsRoleByRoleId(roleId) != null)
            return credentialsRoleRepository.findById(roleId).get();
        else
            throw new EmptyResultDataAccessException(roleId);
    }

    public String getRoleByUserId(Integer userId) {
        Integer roleId = credentialsRoleRepository.findById(userId).orElseThrow().getFk_account_id();
        return roleRepository.findById(roleId).orElseThrow().getRole_name();
    }

    public void save(CredentialsRole credentialsRole) {
        credentialsRoleRepository.save(credentialsRole);
    }
}
