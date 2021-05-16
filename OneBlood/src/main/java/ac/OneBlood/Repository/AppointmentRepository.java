package ac.OneBlood.Repository;

import ac.OneBlood.Model.Appointment;
import ac.OneBlood.Model.DonationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query(value = "SELECT * FROM appointment WHERE fk_donor_code = :donor_code", nativeQuery = true)
    List<Appointment> findByDonorCode(String donor_code);
    @Query(value = "SELECT * FROM appointment WHERE fk_doctor_code = :doctor_code", nativeQuery = true)
    List<Appointment> findByDoctorCode(Integer doctor_code);
}