package ac.OneBlood.Service;

import ac.OneBlood.Model.Doctor;
import ac.OneBlood.Model.Pacient;
import ac.OneBlood.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public Doctor getDoctorById(Integer id) {
        if(doctorRepository.findById(id).isPresent())
            return doctorRepository.findById(id).get();
        else
            throw new EmptyResultDataAccessException(id);
    }


    public void save(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    public void delete(Integer id) {
        doctorRepository.deleteById(id);
    }

}
