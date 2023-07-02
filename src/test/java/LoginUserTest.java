import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.LoginUser;
import org.example.User;
import org.example.UserGenerator;
import org.example.UserPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginUserTest {
    private UserPage userPage;
    private User user;
    private LoginUser loginUser;
    private String token;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        user = UserGenerator.random();
        userPage = new UserPage();
        loginUser = new LoginUser(user.getEmail(), user.getPassword());
    }
    @After
    public void deleteUser() {
        if (token != null) {
            userPage.deleteUser(token);
        }
    }
    @Test
    @DisplayName("Check login with all required fields")
    @Description("Проверка логина пользователя со всеми необходимыми полями")
    public void checkLoginWithAllRequiredFields() {
        userPage.createUser(user);
        ValidatableResponse response = userPage.loginUser(loginUser);
        token = response.extract().path("accessToken").toString();
        userPage.checkCorrectStatusCode(response, 200, true);
    }
    @Test
    @DisplayName("Check login user with wrong login")
    @Description("Проверка попытки залогиниться с неправильным логином")
    public void checkLoginWithWrongLogin() {
        loginUser.setEmail("Mugivara");
        ValidatableResponse response = userPage.loginUser(loginUser);
        userPage.checkCorrectStatusCode(response, 401, false);
    }
    @Test
    @DisplayName("Check login user with wrong password")
    @Description("Проверка попытки залогиниться с неправильным паролем")
    public void checkLoginWithWrongPass() {
        loginUser.setPassword("12345");
        ValidatableResponse response = userPage.loginUser(loginUser);
        userPage.checkCorrectStatusCode(response, 401, false);
    }
    @Test
    @DisplayName("Check login user with wrong login and password")
    @Description("Проверка попытки залогиниться с неправильным логином и паролем")
    public void checkLoginWithWrongLoginAndPassword() {
        loginUser.setEmail("Mugivara");
        loginUser.setPassword("12345");
        ValidatableResponse response = userPage.loginUser(loginUser);
        userPage.checkCorrectStatusCode(response, 401, false);
    }
}
