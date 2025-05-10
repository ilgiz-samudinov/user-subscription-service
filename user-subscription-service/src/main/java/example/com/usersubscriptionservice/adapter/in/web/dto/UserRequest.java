package example.com.usersubscriptionservice.adapter.in.web.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class UserRequest {

    @Email(message = "Неверный формат email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    @NotBlank(message = "Имя обязательно")
    @Size(max = 50, message = "Имя не должно превышать 50 символов")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Size(max = 50, message = "Фамилия не должна превышать 50 символов")
    private String lastName;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(
            regexp = "^\\+?[0-9. ()-]{7,25}$",
            message = "Неверный формат номера телефона"
    )
    private String phone;
}
