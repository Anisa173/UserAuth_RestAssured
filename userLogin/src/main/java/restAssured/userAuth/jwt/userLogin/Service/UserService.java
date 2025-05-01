package restAssured.userAuth.jwt.userLogin.Service;

import org.springframework.security.core.userdetails.UserDetailsService;
import restAssured.userAuth.jwt.userLogin.Domain.UserResponseDto;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserResponseDto createUser(UserResponseDto userDto) throws Exception;

    UserResponseDto getUserById(String userId) throws Exception;

    List<UserResponseDto> getUsers(int page, int limit) throws Exception;

}
