package ac.OneBlood.Repository;

import ac.OneBlood.Model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @Query(value = "SELECT * FROM doctor WHERE fk_account_id = :id", nativeQuery = true)
    Doctor findByAccountId(Integer id);

}