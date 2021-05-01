package ac.OneBlood.Service;

import ac.OneBlood.Model.DonationForm;
import ac.OneBlood.Model.Pacient;
import ac.OneBlood.Repository.PacientRepository;
import javassist.NotFoundException;
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

    public Pacient getPacientByDonorCode(String id) throws NotFoundException {
        if(pacientRepository.findPacientByDonorCode(id) != null)
            return pacientRepository.findPacientByDonorCode(id);
        else
            throw new NotFoundException(id);
    }
}