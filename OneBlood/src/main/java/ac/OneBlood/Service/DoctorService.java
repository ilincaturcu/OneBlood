package ac.OneBlood.Service;

import ac.OneBlood.Model.Doctor;
import ac.OneBlood.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;

    public List<Doctor> listAllDoctors() {
        return doctorRepository.findAll();
    }

}
