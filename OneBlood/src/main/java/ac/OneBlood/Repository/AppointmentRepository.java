package ac.OneBlood.Repository;

import ac.OneBlood.Model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query(value = "SELECT * FROM appointment WHERE fk_donor_code = :donor_code ORDER BY appointment_date DESC ", nativeQuery = true)
    List<Appointment> findByDonorCode(String donor_code);

    @Query(value = "SELECT * FROM appointment WHERE fk_doctor_code = :doctor_code and appointment_status != \"deleted\" ORDER BY appointment_date DESC limit :take OFFSET :start", nativeQuery = true)
    List<Appointment> findByDoctorCode(Integer doctor_code, Integer start, Integer take);

    @Query(value = "SELECT * FROM appointment WHERE fk_doctor_code = :doctor_code and appointment_status != \"deleted\" ORDER BY appointment_date DESC", nativeQuery = true)
    List<Appointment> findAllByDoctorCode(Integer doctor_code);

    @Query(value = "SELECT * FROM appointment WHERE fk_doctor_code = :doctor_code and appointment_status != \"deleted\" AND fk_donor_code LIKE %:filter% ORDER BY appointment_date DESC limit :take OFFSET :start", nativeQuery = true)
    List<Appointment> findAllByDoctorCodeFiltered(Integer doctor_code, Integer start, Integer take, String filter);


}