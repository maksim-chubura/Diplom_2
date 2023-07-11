package org.example.Steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.Constants.PathApi;
import org.example.Resources.Ingredients;
import org.example.Resources.LoginUser;
import org.example.Resources.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserPage extends BaseApi {
    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given(RequestSpecification())
                .body(user)
                .when()
                .post(PathApi.CREATE_USER)
                .then().log().all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(LoginUser loginUser) {
        return given(RequestSpecification())
                .body(loginUser)
                .when()
                .post(PathApi.LOGIN_USER)
                .then().log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given(RequestSpecification())
                .header("Authorization", accessToken)
                .when()
                .delete(PathApi.DELETE_USER)
                .then().log().all();
    }

    @Step("Обновление пользователя")
    public ValidatableResponse updateUser(String accessToken, User user) {
        return given(RequestSpecification())
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(PathApi.PATCH_USER)
                .then().log().all();
    }

    @Step("Получение данных пользователя")
    public ValidatableResponse getUserInfo(String accessToken) {
        return given(RequestSpecification())
                .header("Authorization", accessToken)
                .when()
                .get(PathApi.GET_INFO_USER)
                .then().log().all();
    }

    @Step("Проверка статус-кода")
    public void checkCorrectStatusCodeAndBody(ValidatableResponse response, boolean bool, String message) {
        response
                .assertThat().body("success", equalTo(bool))
                .and()
                .body("message", equalTo(message));
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrderOne(String accessToken, Ingredients ingredients) {
        return given(RequestSpecification())
                .header("Authorization", accessToken)
                .body(ingredients)
                .when()
                .post(PathApi.CREATE_ORDER)
                .then().log().all();
    }

    @Step("Создание заказа для неавторизированого пользователя")
    public ValidatableResponse createOrderWithoutLogIn(Ingredients ingredients) {
        return given(RequestSpecification())
                .when()
                .body(ingredients)
                .post(PathApi.CREATE_ORDER)
                .then().log().all();
    }

    @Step("Получение заказов авторизированого пользователя")
    public ValidatableResponse getOrderWithLogIn(String accessToken) {
        return given(RequestSpecification())
                .header("Authorization", accessToken)
                .when()
                .get(PathApi.GET_ORDER_USER)
                .then().log().all();
    }

    @Step("Получение заказов неавторизированого пользователя")
    public ValidatableResponse getOrderWithoutLogIn() {
        return given(RequestSpecification())
                .when()
                .get(PathApi.GET_ORDER_USER)
                .then().log().all();
    }
}
