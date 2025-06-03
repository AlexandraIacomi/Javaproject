package ro.foame.employee_backend.dtos;

import java.util.List;

public record LoginResponseDto(String token, Long expiresIn, String fullName, List<String> roles) {

}
