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

public class GetOrderTest {
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
    @DisplayName("Get an order from an authorized user")
    @Description("Получение заказов авторизированого пользователя")
    public void getOrderFromAuthorizedUser() {
        userPage.createUser(user);
        ValidatableResponse response = userPage.loginUser(loginUser);
        token = response.extract().path("accessToken").toString();
        userPage.createOrder(token);
        ValidatableResponse validatableResponse = userPage.getOrderWithLogIn(token);
        userPage.checkCorrectStatusCode(validatableResponse, 200, true);
    }
    @Test
    @DisplayName("Get an order from an unauthorized user")
    @Description("Получение заказов неавторизированого пользователя")
    public void getOrderFromUnauthorizedUser() {
        userPage.createOrderWithoutLogIn();
        ValidatableResponse response = userPage.getOrderWithoutLogIn();
        userPage.checkCorrectStatusCode(response, 401, false);
    }
}
