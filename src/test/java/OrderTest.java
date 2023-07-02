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

public class OrderTest {
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
    @DisplayName("Create an order with authorization and ingredients")
    @Description("Создание заказ с авторизацией и ингридиентами")
    public void createAnOrderWithAuthorizationAndIngredients() {
        userPage.createUser(user);
        ValidatableResponse response = userPage.loginUser(loginUser);
        token = response.extract().path("accessToken").toString();
        userPage.createOrder(token);
        userPage.checkCorrectStatusCode(response, 200, true);
    }
    @Test
    @DisplayName("Create an order with authorization and without ingredients")
    @Description("Создание заказ с авторизацией и без ингридиентов")
    public void createAnOrderWithAuthorizationAndWithoutIngredients() {
        userPage.createUser(user);
        ValidatableResponse response = userPage.loginUser(loginUser);
        token = response.extract().path("accessToken").toString();
        ValidatableResponse validatableResponse = userPage.createOrderWithoutIngredients(token);
        userPage.checkCorrectStatusCode(validatableResponse, 400, false);
    }
    @Test
    @DisplayName("Create an order without authorization and with ingredients")
    @Description("Создание заказ без авторизациии с ингридиентами")
    public void createAnOrderWithoutAuthorizationAndWithIngredients() {
        ValidatableResponse response = userPage.createOrderWithoutLogIn();
        userPage.checkCorrectStatusCode(response, 200, true);
    }
    @Test
    @DisplayName("Create an order with an invalid ingredient hash")
    @Description("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredientHash() {
        userPage.createUser(user);
        ValidatableResponse response = userPage.loginUser(loginUser);
        token = response.extract().path("accessToken").toString();
        ValidatableResponse validatableResponse = userPage.createOrderWithInvalidIngredientHash(token);
        userPage.checkCorrectStatusCode(validatableResponse, 400, false);
    }
}
