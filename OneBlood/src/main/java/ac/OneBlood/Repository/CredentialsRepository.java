package ac.OneBlood.Repository;

import ac.OneBlood.Model.Appointment;
import ac.OneBlood.Model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Integer> {

    @Query(value = "SELECT * FROM credentials WHERE email = :email", nativeQuery = true)
    Credentials findByEmail(String email);
}