package ac.OneBlood.Repository;

import ac.OneBlood.Model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Integer> {
    Credentials findByEmail(String email);
}