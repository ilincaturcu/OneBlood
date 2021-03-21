package ac.OneBlood.Service;

import ac.OneBlood.Model.DonationForm;
import ac.OneBlood.Repository.DonationFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DonationFormService {

    @Autowired
    DonationFormRepository donationFormRepository;

    public List<DonationForm> listAllDonationForms() {
        return donationFormRepository.findAll();
    }

}