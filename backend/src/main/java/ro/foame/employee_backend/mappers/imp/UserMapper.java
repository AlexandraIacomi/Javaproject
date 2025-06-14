package ro.foame.employee_backend.mappers.imp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ro.foame.employee_backend.dtos.UserDto;
import ro.foame.employee_backend.entities.User;
import ro.foame.employee_backend.mappers.Mapper;

@Component
public class UserMapper implements Mapper<User, UserDto> {
    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto mapTo(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }

    @Override
    public User mapFrom(UserDto userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}