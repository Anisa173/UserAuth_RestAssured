package restAssured.userAuth.jwt.userLogin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(ControllerTestContainerRestAssured.class)
@SpringBootTest
class UserLoginApplicationTests {

	@Test
	void contextLoads() {
	}

}
