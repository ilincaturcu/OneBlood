package ac.OneBlood.Service;

import ac.OneBlood.Model.Pacient;
import ac.OneBlood.Repository.PacientRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
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
        if (pacientRepository.findPacientByDonorCode(id) != null)
            return pacientRepository.findPacientByDonorCode(id);
        else
            throw new NotFoundException(id);
    }

    public Pacient getPacientByCnp(BigInteger cnp) throws NotFoundException {
        if (pacientRepository.findPacientByCnp(cnp) != null)
            return pacientRepository.findPacientByCnp(cnp);
        else
            throw new NotFoundException(String.valueOf(cnp));
    }

    public Pacient getPacientByAccountId(Integer accountId) throws NotFoundException {
        if (pacientRepository.findPacientByAccountId(accountId) != null)
            return pacientRepository.findPacientByAccountId(accountId);
        else
            throw new NotFoundException(String.valueOf(accountId));
    }

    public void save(Pacient pacient) {
        System.out.println(pacient.getCNP());
        pacientRepository.save(pacient);
    }
}