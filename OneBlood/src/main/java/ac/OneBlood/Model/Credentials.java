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
@Table(name = "credentials")
public class Credentials {
    public Credentials() {
    }
    public Credentials(String email, String password) {
        this.email = email;
        this.password=password;
    }
    @Id
    private Integer account_id;
    private String email;
    private String password;
}
