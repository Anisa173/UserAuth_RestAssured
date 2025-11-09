package restAssured.userAuth.jwt.userLogin.Service;

import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import restAssured.userAuth.jwt.userLogin.Domain.UserResponseDto;
import restAssured.userAuth.jwt.userLogin.Domain.exception.UserExceptionHandler;
import restAssured.userAuth.jwt.userLogin.Entity.User;
import restAssured.userAuth.jwt.userLogin.Repository.UserRepository;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserResponseDto createUser(UserResponseDto userDto) throws Exception {
        userDto.setUserId(UUID.randomUUID().toString());
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        var userResult = new UserResponseDto();
        try {
            var user1 = userRepository.save(user);
            BeanUtils.copyProperties(user1, userResult);
        } catch (Exception e) {
            throw new UserExceptionHandler(e.getMessage());
        }

        return userResult;
    }

    @Override
    public UserResponseDto getUserById(String userId) throws Exception {
        User user = Optional.ofNullable(userRepository.findById(String.valueOf(userId))).orElseThrow(()
                -> new UserExceptionHandler("UserId does not exist!!"));
        //  storedUserDto = new UserResponseDto();
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public List<UserResponseDto> getUsers(int page, int limit) throws Exception {
        List<UserResponseDto> usersss = new ArrayList<>();
        if (page > 0) {
            page = page - 1;
        }
        PageRequest pagebleRequest = PageRequest.of(page, limit);
        Page<User> usersPage = userRepository.findAll(pagebleRequest);
        List<User> userList = usersPage.getContent();
        Iterator<User> iter = userList.iterator();
        while (iter.hasNext()) {
            User user1 = iter.next();
            UserResponseDto userResponseDto = new ModelMapper().map(user1, UserResponseDto.class);
            usersss.add(userResponseDto);
        }
        return usersss;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User userByEmail = userRepository.findByEmail(email);
        if (userByEmail != null) {
            System.out.println("UserByMail is Found!!");
        } else {
            throw new UsernameNotFoundException(email);
        }

        return new org.springframework.security.core.userdetails.User(userByEmail.getUserId(),
                userByEmail.getEncryptedPassword(), true, true,
                true, true, new ArrayList<>());
    }


}
