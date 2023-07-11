package UserTests;

import Base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest extends BaseTest {
    @Test
    @DisplayName("Check create user with correct status response")
    @Description("Проверка создания пользователя с корректным ответом статуса")
    public void checkCreateUserWithCorrectStatusResponse() {
        ValidatableResponse response = userPage.createUser(user);
        token = response.extract().path("accessToken").toString();
        response.assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .assertThat()
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .and().body("user.name", equalTo(user.getName()));
        userPage.checkCorrectStatusCodeAndBody(response, true, null);
    }

    @Test
    @DisplayName("Check create two identical users")
    @Description("Проверка создания двух идентичных пользователей")
    public void checkCreateTwoIdenticalUsers() {
        userPage.createUser(user);
        ValidatableResponse response = userPage.createUser(user);
        response.assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
        userPage.checkCorrectStatusCodeAndBody(response, false, "User already exists");
    }

    @Test
    @DisplayName("Check create user without email")
    @Description("Проверка создания пользователя без email")
    public void checkCreateCourierWithoutLogin() {
        user.setEmail("");
        ValidatableResponse response = userPage.createUser(user);
        response.assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
        userPage.checkCorrectStatusCodeAndBody(response, false, "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Check create user without password")
    @Description("Проверка создания пользователя без пароля")
    public void checkCreateCourierWithoutPass() {
        user.setPassword("");
        ValidatableResponse response = userPage.createUser(user);
        response.assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
        userPage.checkCorrectStatusCodeAndBody(response, false, "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Check create user without name")
    @Description("Проверка создания пользователя без имени")
    public void checkCreateCourierWithoutName() {
        user.setName("");
        ValidatableResponse response = userPage.createUser(user);
        response.assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
        userPage.checkCorrectStatusCodeAndBody(response, false, "Email, password and name are required fields");
    }
}
