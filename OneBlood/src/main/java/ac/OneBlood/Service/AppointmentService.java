package ac.OneBlood.Service;

import ac.OneBlood.Model.Appointment;
import ac.OneBlood.Repository.AppointmentRepository;
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

    public void save(Appointment appointment) {
        appointmentRepository.saveAndFlush(appointment);
    }
}
