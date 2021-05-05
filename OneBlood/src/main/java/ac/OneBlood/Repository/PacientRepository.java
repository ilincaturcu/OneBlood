package ac.OneBlood.Repository;

import ac.OneBlood.Model.Pacient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface PacientRepository extends JpaRepository<Pacient, Integer> {

    @Query(value = "SELECT * FROM pacient  WHERE donor_code = :donor_code", nativeQuery = true)
    Pacient findPacientByDonorCode(String donor_code);

    @Query(value = "SELECT * FROM pacient  WHERE cnp = :cnp", nativeQuery = true)
    Pacient findPacientByCnp(BigInteger cnp);
}