package restAssured.userAuth.jwt.userLogin.Controller.controller;


import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restAssured.userAuth.jwt.userLogin.Domain.UserResponseDto;
import restAssured.userAuth.jwt.userLogin.Domain.exception.GenericExceptionResponse;
import restAssured.userAuth.jwt.userLogin.Domain.exception.UserExceptionHandler;
import restAssured.userAuth.jwt.userLogin.Service.UserService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;
@RequireArgsConstructor
@Profile("rest")
@RestController
@RequestMapping("/api/user")
public class UserController {
   
    private UserService uService;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserResponseDto userDto) throws Exception {
        UserResponseDto returnUser;
        try {
            returnUser = uService.createUser(userDto);
        } catch (UserExceptionHandler excp) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericExceptionResponse(excp.getMessage(), "500"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(returnUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("userId") String Id) throws Exception {
        return ResponseEntity.ok(uService.getUserById(Id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getListOfUsers(@RequestParam(value = "page", defaultValue = "0")
                                                                    int page,
                                                                @RequestParam(value = "limit", defaultValue = "2") int limit)
            throws Exception {

        return ResponseEntity.ok(uService.getUsers(page, limit));
    }


}
