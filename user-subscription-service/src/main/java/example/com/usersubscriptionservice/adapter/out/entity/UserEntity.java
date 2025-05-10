package example.com.usersubscriptionservice.adapter.out.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private  String email;

    @Column(nullable = false)
    private  String password;

    @Column(nullable = false)
    private  String firstName;

    @Column(nullable = false)
    private  String lastName;

    @Column(nullable = false)
    private  String phone;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<SubscriptionEntity> subscriptions = new ArrayList<>();
}