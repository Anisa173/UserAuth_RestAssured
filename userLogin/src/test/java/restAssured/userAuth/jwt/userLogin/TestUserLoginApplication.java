package restAssured.userAuth.jwt.userLogin;

import org.springframework.boot.SpringApplication;

public class TestUserLoginApplication {

	public static void main(String[] args) {
		SpringApplication.from(UserLoginApplication::main).with(ControllerTestContainerRestAssured.class).run(args);
	}

}
