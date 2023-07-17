package orders_tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.page_constants.Constants;
import org.example.resources.Ingredients;
import org.example.resources.LoginUser;
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
        ingredients.add(Constants.FIRST_BUN);
        ingredients.add(Constants.SAUCE);
        ingredients.add(Constants.FIRST_MAIN);
        ingredients.add(Constants.SECOND_MAIN);
        ingredients.add(Constants.SECOND_BUN);
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
        ingredients.add(Constants.FIRST_BUN);
        ingredients.add(Constants.SAUCE);
        ingredients.add(Constants.FIRST_MAIN);
        ingredients.add(Constants.SECOND_MAIN);
        ingredients.add(Constants.SECOND_BUN);
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
        ingredients.add(Constants.BAD_FIRST_BUN);
        ingredients.add(Constants.BAD_FIRST_MAIN);
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
