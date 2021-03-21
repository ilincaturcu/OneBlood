package ac.OneBlood.Service;

import ac.OneBlood.Model.Pacient;
import ac.OneBlood.Repository.PacientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PacientService {

    @Autowired
    PacientRepository pacientRepository;

    public List<Pacient> listAllPacients() {
        return pacientRepository.findAll();
    }

}