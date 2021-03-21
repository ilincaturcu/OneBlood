package ac.OneBlood.Service;

import ac.OneBlood.Model.PersonalInformation;
import ac.OneBlood.Repository.PersonalInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PersonalInformationService {

    @Autowired
    PersonalInformationRepository personalInformationRepository;

    public List<PersonalInformation> listAllPersonalInformation() {
        return personalInformationRepository.findAll();
    }

}