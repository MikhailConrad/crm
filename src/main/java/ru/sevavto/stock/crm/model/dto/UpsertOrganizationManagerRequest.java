package ru.sevavto.stock.crm.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertOrganizationManagerRequest {

    private long id;
    @NotBlank
    @Pattern(regexp = "^[А-Яа-я-]+$", message = "Использованы недопустимые символы")
    private String lastname;
    @NotBlank
    @Pattern(regexp = "^[А-Яа-я-]+$", message = "Использованы недопустимые символы")
    private String firstname;
    @NotBlank
    @Pattern(regexp = "^[А-Яа-я-]+$", message = "Использованы недопустимые символы")
    private String patronymicname; //фамилия
    @NotNull
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^(\\+|\\d)\\d{7,15}$", message = "В номере телефона допустимо использование только цифр и + в начале")
    private String phoneNumber;
    @NotBlank
    private String region;
    @NotNull
    @Pattern(regexp = "^[A-Za-z\\d]+$", message = "Недопустимые символы в пароле")
    @Size(min = 4, message = "Пароль должен содержать не менее 4 символов")
    private String password;
//    private String confirmPassword;
    @NotNull
    @Pattern(regexp = "^[А-Яа-я-\\d\\s]+$", message = "Использованы недопустимые символы")
    private String organizationName;
}
