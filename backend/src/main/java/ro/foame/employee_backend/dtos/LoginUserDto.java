package ro.foame.employee_backend.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {

    private String email;

    private String password;

    private String fullName;

}