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

public class OrderTest extends BaseTest {
    private LoginUser loginUser;

    @Test
    @DisplayName("Create an order with authorization and ingredients")
    @Description("Создание заказ с авторизацией и ингридиентами")
    public void createAnOrderWithAuthorizationAndIngredients() {
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
        userPage.checkCorrectStatusCodeAndBody(validatableResponse, true, null);
    }

    @Test
    @DisplayName("Create an order with authorization and without ingredients")
    @Description("Создание заказ с авторизацией и без ингридиентов")
    public void createAnOrderWithAuthorizationAndWithoutIngredients() {
        userPage.createUser(user);
        loginUser = new LoginUser(user.getEmail(), user.getPassword());
        ValidatableResponse response = userPage.loginUser(loginUser);
        token = response.extract().path("accessToken").toString();
        Ingredients newIngredients = new Ingredients(null);
        ValidatableResponse validatableResponse = userPage.createOrderOne(token, newIngredients)
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
        userPage.checkCorrectStatusCodeAndBody(validatableResponse, false, "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Create an order without authorization and with ingredients")
    @Description("Создание заказ без авторизациии с ингридиентами")
    public void createAnOrderWithoutAuthorizationAndWithIngredients() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(PathApi.FIRST_BUN);
        ingredients.add(PathApi.SAUCE);
        ingredients.add(PathApi.FIRST_MAIN);
        ingredients.add(PathApi.SECOND_MAIN);
        ingredients.add(PathApi.SECOND_BUN);
        Ingredients newIngredients = new Ingredients(ingredients);
        ValidatableResponse response = userPage.createOrderWithoutLogIn(newIngredients)
                .assertThat().statusCode(HttpStatus.SC_OK);
        userPage.checkCorrectStatusCodeAndBody(response, true, null);
    }

    @Test
    @DisplayName("Create an order with an invalid ingredient hash")
    @Description("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredientHash() {
        userPage.createUser(user);
        loginUser = new LoginUser(user.getEmail(), user.getPassword());
        ValidatableResponse response = userPage.loginUser(loginUser);
        token = response.extract().path("accessToken").toString();
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(PathApi.BAD_FIRST_BUN);
        ingredients.add(PathApi.BAD_FIRST_MAIN);
        Ingredients newIngredients = new Ingredients(ingredients);
        ValidatableResponse validatableResponse = userPage.createOrderOne(token, newIngredients)
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
        userPage.checkCorrectStatusCodeAndBody(validatableResponse, false, "One or more ids provided are incorrect");
    /*
    при создании заказа, не выдает статус 500, только 400.
    В Postman так же выдает статус 400, при указании неверного хэша ингредиентов
    В файле word приложил скрин из Postman
    */
    }
}
