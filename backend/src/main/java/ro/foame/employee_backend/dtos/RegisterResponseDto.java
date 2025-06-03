package ro.foame.employee_backend.dtos;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record RegisterResponseDto(Integer id, String fullName, String email, String username, Collection<? extends GrantedAuthority> authorities) {

}
