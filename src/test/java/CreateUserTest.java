import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.User;
import org.example.UserGenerator;
import org.example.UserPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    private UserPage userPage;
    private User user;
    private String token;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        user = UserGenerator.random();
        userPage = new UserPage();
    }
    @After
    public void deleteUser() {
        if (token != null) {
            userPage.deleteUser(token);
        }
    }
    @Test
    @DisplayName("Check create user with correct status response")
    @Description("Проверка создания пользователя с корректным ответом статуса")
    public void checkCreateUserWithCorrectStatusResponse() {
        ValidatableResponse response = userPage.sendPostRequestCreateUser(user);
        token = response.extract().path("accessToken").toString();
        userPage.checkCorrectStatusCode(response, 200, true);
    }
    @Test
    @DisplayName("Check create two identical users")
    @Description("Проверка создания двух идентичных пользователей")
    public void checkCreateTwoIdenticalUsers() {
        userPage.sendPostRequestCreateUser(user);
        ValidatableResponse response = userPage.createUser(user);
        userPage.checkCorrectStatusCode(response, 403, false);
    }
    @Test
    @DisplayName("Check create user without email")
    @Description("Проверка создания пользователя без email")
    public void checkCreateCourierWithoutLogin() {
        user.setEmail("");
        ValidatableResponse response = userPage.sendPostRequestCreateUser(user);
        userPage.checkCorrectStatusCode(response, 403, false);
    }
    @Test
    @DisplayName("Check create user without password")
    @Description("Проверка создания пользователя без пароля")
    public void checkCreateCourierWithoutPass() {
        user.setPassword("");
        ValidatableResponse response = userPage.sendPostRequestCreateUser(user);
        userPage.checkCorrectStatusCode(response, 403, false);
    }
    @Test
    @DisplayName("Check create user without name")
    @Description("Проверка создания пользователя без имени")
    public void checkCreateCourierWithoutName() {
        user.setName("");
        ValidatableResponse response = userPage.sendPostRequestCreateUser(user);
        userPage.checkCorrectStatusCode(response, 403, false);
    }
}
