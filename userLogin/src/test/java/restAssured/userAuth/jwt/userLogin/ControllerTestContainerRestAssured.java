package restAssured.userAuth.jwt.userLogin;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import restAssured.userAuth.jwt.userLogin.Domain.UserResponseDto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfiguration(proxyBeanMethods = false)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTestContainerRestAssured {

    @Container
    @ServiceConnection
    static MySQLContainer mysqlContainer = new MySQLContainer<>("mysql:latest");

    @LocalServerPort
    private int port;

    private final String TEST_EMAIL = "celaanisa07@gmail.com";
    private final String TEST_PASSWORD = "vB123mN@@";
    private String userId;
    private String token;

    private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter respLoggingFilter = new ResponseLoggingFilter();

    @Order(1)
    @BeforeAll
    void setUpBeforeAll() {
        RestAssured.baseURI = "/api/users";
        RestAssured.port = port;
        RestAssured.filters(requestLoggingFilter, respLoggingFilter);

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectBody("id", notNullValue())
                .expectResponseTime(lessThanOrEqualTo(2000L))
                .build();
    }


    @Order(1)
    @DisplayName("Validate UserDetails in order to create User")
    @Test
    public void testCreatedUser_whenValidateUserDetailsProvided_thenReturnStatusCode() {
        //Arrange
        UserResponseDto newUser = new UserResponseDto(UUID.randomUUID().toString(), "Anisa", "Cela", TEST_EMAIL, TEST_PASSWORD);
        //Act
        given()
                .body(newUser)
                .when()
                .post("/api/users")
                .then()
                //Assert
                .statusCode(200)
                .body("Anisa", equalTo(newUser.getFirstName()))
                .body("Cela", equalTo(newUser.getLastName()))
                .body(TEST_EMAIL, equalTo(newUser.getEmail()))
                .body(TEST_PASSWORD, equalTo(newUser.getPassword()))
                .body("userId", notNullValue());
    }
    @DisplayName("Generate JWT after user is logged sucessfully!!")
    @Order(2)
    @Test
    public void givenValidatedUserDetails_whenLoginUser_thenReturnAuthenticatedUser() {
        //Arrange
        Map<String,String> userCredentials = new HashMap<>();
        userCredentials.put(TEST_EMAIL,"celaanisa07@gmail.com");
        userCredentials.put(TEST_PASSWORD,"vB123mN@@");
        //Act
            Response response = given()
                    .body(userCredentials)
                 .when()
                    .post("/api/users/login");

            this.userId = String.valueOf(response.headers()); //principali
            this.token = String.valueOf(response.headers());  //token
        //Assert
        assertEquals(HttpStatus.OK,response.getStatusCode());
      //  assertEquals(TEST_EMAIL,response.getHeaders().getValues(this.userId));
        assertNotNull(token);
        assertNotNull(userId);

    }
    @Order(3)
    @DisplayName("Return_User_Authenticated")
    @Test
    public void givenGetUser_whenUserIsAuth_thenReturnUser(){
        given()//Arrange
                .pathParam("userId",this.userId)
                .header("Authorization","Bearer",this.token)
                .when()
                //Act
                .post("/api/users/{userId}")
                .then()
                //Assert
                .statusCode(HttpStatus.OK.value())
                .body(TEST_EMAIL,equalTo(this.userId))
                .body(TEST_PASSWORD,equalTo(this.token))
                .body("$.firstName",notNullValue())
                .body("$.lastName",notNullValue()) ;

    }
    @DisplayName("Generate_Users_Authenticated")
    @Order(4)
    @Test
   public void givenListOfUsers_whenUsersAuthenticated_thenReturnsPageRequestUsers(){
        //Arrange
        given()
                .header("Authorization","Bearer",this.token)
                .queryParam("page",2)
                .queryParam("limit",5)

                .when()//Act
                .get("/api/users")
                .then()//Assert
                .statusCode(HttpStatus.OK.value())
                .body("size()",equalTo(1)) ;
   }



}


