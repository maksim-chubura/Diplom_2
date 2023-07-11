package UserTests;

import Base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.Resources.LoginUser;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest extends BaseTest {
    private LoginUser loginUser;

    @Test
    @DisplayName("Check login with all required fields")
    @Description("Проверка логина пользователя со всеми необходимыми полями")
    public void checkLoginWithAllRequiredFields() {
        userPage.createUser(user);
        loginUser = new LoginUser(user.getEmail(), user.getPassword());
        ValidatableResponse response = userPage.loginUser(loginUser);
        token = response.extract().path("accessToken").toString();
        response.assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .assertThat()
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .and().body("user.name", equalTo(user.getName()));
        userPage.checkCorrectStatusCodeAndBody(response, true, null);
    }

    @Test
    @DisplayName("Check login user with wrong login")
    @Description("Проверка попытки залогиниться с неправильным логином")
    public void checkLoginWithWrongLogin() {
        loginUser = new LoginUser(user.getEmail(), user.getPassword());
        loginUser.setEmail("Mugivara");
        ValidatableResponse response = userPage.loginUser(loginUser)
                .assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
        userPage.checkCorrectStatusCodeAndBody(response, false, "email or password are incorrect");
    }

    @Test
    @DisplayName("Check login user with wrong password")
    @Description("Проверка попытки залогиниться с неправильным паролем")
    public void checkLoginWithWrongPass() {
        loginUser = new LoginUser(user.getEmail(), user.getPassword());
        loginUser.setPassword("12345");
        ValidatableResponse response = userPage.loginUser(loginUser)
                .assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
        userPage.checkCorrectStatusCodeAndBody(response, false, "email or password are incorrect");
    }

    @Test
    @DisplayName("Check login user with wrong login and password")
    @Description("Проверка попытки залогиниться с неправильным логином и паролем")
    public void checkLoginWithWrongLoginAndPassword() {
        loginUser = new LoginUser(user.getEmail(), user.getPassword());
        loginUser.setEmail("Mugivara");
        loginUser.setPassword("12345");
        ValidatableResponse response = userPage.loginUser(loginUser)
                .assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
        userPage.checkCorrectStatusCodeAndBody(response, false, "email or password are incorrect");
    }
}
