package ac.OneBlood.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "credentialsrole")
public class CredentialsRole {
    public CredentialsRole() {
    }

    @Id
    private Integer fk_account_id;
    private Integer fk_id_role;

}
