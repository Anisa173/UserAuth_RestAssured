package restAssured.userAuth.jwt.userLogin.Entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class User extends BaseEntity<String>  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;

    @Column(nullable = false, name = "firstName", length = 50)
    private String firstName;
    @Column(nullable = false, name = "lastName", length = 20)
    private String lastName;
    @Column(nullable = false, name = "email", unique = true)
    private String email;
    @Column(nullable = false, name = "password", length = 16)
    private String encryptedPassword;

    @Override
    public String getId() {
        return userId;
    }


}
