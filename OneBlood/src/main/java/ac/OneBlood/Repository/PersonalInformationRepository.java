package ac.OneBlood.Repository;

import ac.OneBlood.Model.PersonalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface PersonalInformationRepository extends JpaRepository<PersonalInformation, BigInteger> {
}