package UserTests;

import Base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.example.Resources.User;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class ChangeUserDataTest extends BaseTest {
    private User userUpdate = new User();

    @Test
    @DisplayName("Check change Email with authorization")
    @Description("Проверка изменения электронной почты с авторизацией")
    public void checkChangeEmailWithAuthorization() {
        User userUpdate = user.clone();
        userUpdate.setEmail(RandomStringUtils.randomAlphabetic(8) + "@newpraktikum.ru");
        token = userPage.createUser(user).extract().header("Authorization");
        userPage.updateUser(token, userUpdate);
        ValidatableResponse response = userPage.getUserInfo(token)
                .assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("user.name", equalTo(user.getName()))
                .body("user.email", equalTo(userUpdate.getEmail().toLowerCase()));
        userPage.checkCorrectStatusCodeAndBody(response, true, null);
    }

    @Test
    @DisplayName("Check change Email without authorization")
    @Description("Проверка изменения электронной почты без авторизации")
    public void checkChangeEmailWithoutAuthorization() {
        userUpdate.setEmail(RandomStringUtils.randomAlphabetic(8) + "@newpraktikum.ru");
        ValidatableResponse response = userPage.updateUser("", userUpdate)
                .assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
        userPage.checkCorrectStatusCodeAndBody(response, false, "You should be authorised");
    }
}
