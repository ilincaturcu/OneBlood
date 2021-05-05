package ac.OneBlood.Repository;

import ac.OneBlood.Model.DonationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationFormRepository extends JpaRepository<DonationForm, Integer> {
    @Query(value = "SELECT * FROM donation_form WHERE fk_donor_code = :donor_code", nativeQuery = true)
    DonationForm findByDonorCode(String donor_code);
}