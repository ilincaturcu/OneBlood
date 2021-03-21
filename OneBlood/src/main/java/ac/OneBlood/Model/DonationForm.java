package ac.OneBlood.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "donation_form")
public class DonationForm {
    public DonationForm() {
    }

    @Id
    private Integer donor_code;
    private Integer id_analize_pre_donare;
    private Integer id_analize_post_donare;
    private Timestamp created_at;
}
