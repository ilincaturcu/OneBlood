package ac.OneBlood.Repository;

import ac.OneBlood.Model.CredentialsRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRoleRepository extends JpaRepository<CredentialsRole, Integer> {
    @Query(value = "SELECT * FROM credentialsrole WHERE fk_id_role = :roleId", nativeQuery = true)
    CredentialsRole findCredentialsRoleByRoleId(Integer roleId);
}