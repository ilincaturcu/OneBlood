package ac.OneBlood.Service;

import ac.OneBlood.Model.Credentials;
import ac.OneBlood.Model.Doctor;
import ac.OneBlood.Model.DonationForm;
import ac.OneBlood.Repository.DoctorRepository;
import ac.OneBlood.Repository.DonationFormRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public DonationForm getDonationFormById(Integer id) throws NotFoundException {
        if(donationFormRepository.findById(id).isPresent())
            return donationFormRepository.findById(id).get();
        else
            throw new EmptyResultDataAccessException(id);
    }

    public DonationForm getDonationFormByDonorCode(String donor_code) throws NotFoundException {
        if(donationFormRepository.findByDonorCode(donor_code) != null)
            return donationFormRepository.findByDonorCode(donor_code);
        else
            throw new NotFoundException(donor_code);
    }

    public void save(DonationForm donationForm) {
        donationFormRepository.save(donationForm);
    }

    public void delete(Integer id) {
        donationFormRepository.deleteById(id);
    }

}