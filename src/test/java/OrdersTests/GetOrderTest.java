package OrdersTests;

import Base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.Constants.PathApi;
import org.example.Resources.Ingredients;
import org.example.Resources.LoginUser;
import org.junit.Test;

import java.util.ArrayList;

public class GetOrderTest extends BaseTest {
    private LoginUser loginUser;

    @Test
    @DisplayName("Get an order from an authorized user")
    @Description("Получение заказов авторизированого пользователя")
    public void getOrderFromAuthorizedUser() {
        userPage.createUser(user);
        loginUser = new LoginUser(user.getEmail(), user.getPassword());
        ValidatableResponse response = userPage.loginUser(loginUser);
        token = response.extract().path("accessToken").toString();
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(PathApi.FIRST_BUN);
        ingredients.add(PathApi.SAUCE);
        ingredients.add(PathApi.FIRST_MAIN);
        ingredients.add(PathApi.SECOND_MAIN);
        ingredients.add(PathApi.SECOND_BUN);
        Ingredients newIngredients = new Ingredients(ingredients);
        ValidatableResponse validatableResponse = userPage.createOrderOne(token, newIngredients)
                .assertThat().statusCode(HttpStatus.SC_OK);
        userPage.getOrderWithLogIn(token)
                .assertThat().statusCode(HttpStatus.SC_OK);
        userPage.checkCorrectStatusCodeAndBody(validatableResponse, true, null);
    }

    @Test
    @DisplayName("Get an order from an unauthorized user")
    @Description("Получение заказов неавторизированого пользователя")
    public void getOrderFromUnauthorizedUser() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(PathApi.FIRST_BUN);
        ingredients.add(PathApi.SAUCE);
        ingredients.add(PathApi.FIRST_MAIN);
        ingredients.add(PathApi.SECOND_MAIN);
        ingredients.add(PathApi.SECOND_BUN);
        Ingredients newIngredients = new Ingredients(ingredients);
        userPage.createOrderWithoutLogIn(newIngredients)
                .assertThat().statusCode(HttpStatus.SC_OK);
        ValidatableResponse validatableResponse = userPage.getOrderWithoutLogIn()
                .assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
        userPage.checkCorrectStatusCodeAndBody(validatableResponse, false, "You should be authorised");
    }
}
