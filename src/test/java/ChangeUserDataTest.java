import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.LoginUser;
import org.example.User;
import org.example.UserGenerator;
import org.example.UserPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class ChangeUserDataTest {
    private UserPage userPage;
    private User user;
    private User userUpdate;
    private LoginUser loginUser;
    private String token;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        user = UserGenerator.random();
        userUpdate = UserGenerator.random();
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
    @DisplayName("Check change Email with authorization")
    @Description("Проверка изменения электронной почты с авторизацией")
    public void checkChangeEmailWithAuthorization() {
        User userUpdate = user.clone();
        userUpdate.setEmail(RandomStringUtils.randomAlphabetic(8) + "@newpraktikum.ru");
        String token = userPage.createUser(user).extract().header("Authorization");
        userPage.updateUser(token, userUpdate);
        ValidatableResponse response = userPage.getUserInfo(token);
        response.body("user.name", equalTo(user.getName())).and().body("user.email", equalTo(userUpdate.getEmail().toLowerCase()));
    }
    @Test
    @DisplayName("Check change Email without authorization")
    @Description("Проверка изменения электронной почты без авторизации")
    public void checkChangeEmailWithoutAuthorization() {
        userUpdate.setEmail(RandomStringUtils.randomAlphabetic(8) + "@newpraktikum.ru");
        ValidatableResponse response = userPage.updateUser("", userUpdate);
        userPage.checkCorrectStatusCode(response, 401, false);
    }
}
