package ac.OneBlood.Service;

import ac.OneBlood.Model.CredentialsRole;
import ac.OneBlood.Model.Role;
import ac.OneBlood.Repository.CredentialsRepository;
import ac.OneBlood.Repository.CredentialsRoleRepository;
import ac.OneBlood.Repository.RoleRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleResult;
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

    public String getRoleByUserId(Integer userId){
       Integer roleId = credentialsRoleRepository.findById(userId).orElseThrow().getFk_account_id();
       return  roleRepository.findById(roleId).orElseThrow().getRole_name();
    }
}
