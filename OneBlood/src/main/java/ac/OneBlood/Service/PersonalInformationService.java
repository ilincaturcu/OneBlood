package ac.OneBlood.Service;

import ac.OneBlood.Model.DonationForm;
import ac.OneBlood.Model.PersonalInformation;
import ac.OneBlood.Repository.PersonalInformationRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
public class PersonalInformationService {

    @Autowired
    PersonalInformationRepository personalInformationRepository;

    public List<PersonalInformation> listAllPersonalInformation() {
        return personalInformationRepository.findAll();
    }

    public PersonalInformation getPersonalInformationByCNP(BigInteger CNP) throws Exception {
        if(personalInformationRepository.findById(CNP).isPresent())
            return personalInformationRepository.findById(CNP).get();
        else
            throw new Exception("CNP not found");
    }
}