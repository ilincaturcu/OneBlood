package ac.OneBlood.Repository;

import ac.OneBlood.Model.CredentialsRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRoleRepository extends JpaRepository<CredentialsRole, Integer> {
}