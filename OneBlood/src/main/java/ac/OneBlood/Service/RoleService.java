package ac.OneBlood.Service;

import ac.OneBlood.Model.Doctor;
import ac.OneBlood.Model.Role;
import ac.OneBlood.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public List<Role> listAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Integer id) {
        if (roleRepository.findById(id).isPresent())
            return roleRepository.findById(id).get();
        else
            throw new EmptyResultDataAccessException(id);
    }


}