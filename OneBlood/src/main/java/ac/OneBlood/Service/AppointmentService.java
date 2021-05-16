package ac.OneBlood.Service;

import ac.OneBlood.Model.Appointment;
import ac.OneBlood.Repository.AppointmentRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    public List<Appointment> listAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Integer id) {
        if (appointmentRepository.findById(id).isPresent())
            return appointmentRepository.findById(id).get();
        else
            throw new EmptyResultDataAccessException(id);
    }

    public  List<Appointment> getAppointmentByDonorCode(String donor_code) throws NotFoundException {
        if (appointmentRepository.findByDonorCode(donor_code)!=null)
            return appointmentRepository.findByDonorCode(donor_code);
        else
            throw new NotFoundException(donor_code);
    }

    public List<Appointment> getAppointmentByDoctorCode(Integer doctor_code) {
        if (appointmentRepository.findByDoctorCode(doctor_code)!=null)
            return appointmentRepository.findByDoctorCode(doctor_code);
        else
            throw new EmptyResultDataAccessException(doctor_code);
    }

    public void save(Appointment appointment) {
        appointmentRepository.saveAndFlush(appointment);
    }
}
