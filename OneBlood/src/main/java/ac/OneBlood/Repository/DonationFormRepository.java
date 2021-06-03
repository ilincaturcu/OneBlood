package ac.OneBlood.Repository;

import ac.OneBlood.Model.DonationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationFormRepository extends JpaRepository<DonationForm, Integer> {
    @Query(value = "SELECT * FROM donation_form WHERE fk_donor_code = :donor_code", nativeQuery = true)
    List<DonationForm> findByDonorCode(String donor_code);
}