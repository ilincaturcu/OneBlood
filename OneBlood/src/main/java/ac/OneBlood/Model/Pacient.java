package ac.OneBlood.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "pacient")
public class Pacient {
    public Pacient() {
    }

    @Id
    private Integer CNP;
    private Integer fk_donor_code;
    private Integer fk_account_id;
    private Integer self_exclusion_form_id;
    private Timestamp created_at;
}
