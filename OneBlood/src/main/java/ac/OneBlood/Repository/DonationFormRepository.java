package ac.OneBlood.Repository;

import ac.OneBlood.Model.DonationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationFormRepository extends JpaRepository<DonationForm, Integer> {
}