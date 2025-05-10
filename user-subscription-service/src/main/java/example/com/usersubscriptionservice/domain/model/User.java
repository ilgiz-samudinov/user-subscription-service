package example.com.usersubscriptionservice.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private  String email;
    private  String password;
    private  String firstName;
    private  String lastName;
    private  String phone;
}
