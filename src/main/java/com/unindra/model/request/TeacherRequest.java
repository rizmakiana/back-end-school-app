package com.unindra.model.request;

import com.unindra.model.util.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherRequest {
 
    @NotBlank(message = "{name.not.blank}")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "{name.only.letters}")
    private String name;

    @NotNull(message = "{gender.not.blank}")
    private Gender gender;

    @NotBlank(message = "{birthplace.notblank}")
    private String birthPlaceRegency;

    @NotBlank(message = "{birthdate.notblank}")
    private String birthDate;

    @NotNull(message = "{birthmonth.notblank}")
    private Integer birthMonth;

    @NotBlank(message = "{birthyear.notblank}")
    private String birthYear;

    @NotBlank(message = "{district.address.notblank}")
    private String districtAddress;

    @NotBlank(message = "{address.notblank}")
    private String address;

    @NotBlank(message = "{username.notblank}")
    private String username;

    @NotBlank(message = "{password.notblank}")
    private String password;

    @NotBlank(message = "{confirm.password.notblank}")
    private String confirmPassword;

    @Email(message = "{email.notvalid}")
    @NotBlank(message = "{email.notblank}")
    private String email;

    @Size(min = 10, max = 15, message = "{phone.number.size}")
    @Pattern(regexp = "^[0-9+]+$", message = "{phone.number.only.digits}")
    @NotBlank(message = "{phone.number.notblank}")
    private String phoneNumber;
    
}
