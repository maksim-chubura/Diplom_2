package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserPage {
    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given().log().all()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/register")
                .then().log().all();
    }
    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(LoginUser loginUser) {
        return given().log().all()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post("/api/auth/login")
                .then().log().all();
    }
    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser (String accessToken) {
        return given().log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then().log().all();
    }
    @Step("Обновление пользователя")
    public ValidatableResponse updateUser (String accessToken, User user) {
        return given().log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch("/api/auth/user")
                .then().log().all();
    }
    @Step("Получение данных пользователя")
    public ValidatableResponse getUserInfo (String accessToken) {
        return given().log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get("/api/auth/user")
                .then().log().all();
    }
    @Step("Отправка запроса")
    public ValidatableResponse sendPostRequestCreateUser(User user) {
        UserPage createUser = new UserPage();
        return createUser.createUser(user);
    }
    @Step("Проверка статус-кода")
    public void checkCorrectStatusCode(ValidatableResponse response, int statusCode, boolean message) {
        response
                .assertThat()
                .statusCode(statusCode)
                .and()
                .assertThat().body("success", equalTo(message));
    }
    @Step("Создание заказа для авторизированого пользователя")
    public ValidatableResponse createOrder (String accessToken) {
        return given().log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .body("{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6c\"," +
                        "\"61c0c5a71d1f82001bdaaa75\",\"61c0c5a71d1f82001bdaaa7a\"," +
                        "\"61c0c5a71d1f82001bdaaa71\",\"61c0c5a71d1f82001bdaaa6c\"]\n}")
                .post("/api/orders")
                .then().log().all();
    }
    @Step("Создание заказа для авторизированого пользователя без ингридиентов")
    public ValidatableResponse createOrderWithoutIngredients (String accessToken) {
        return given().log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .post("/api/orders")
                .then().log().all();
    }
    @Step("Создание заказа для неавторизированого пользователя")
    public ValidatableResponse createOrderWithoutLogIn() {
        return given().log().all()
                .header("Content-type", "application/json")
                .when()
                .body("{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6c\"," +
                        "\"61c0c5a71d1f82001bdaaa75\",\"61c0c5a71d1f82001bdaaa7a\"," +
                        "\"61c0c5a71d1f82001bdaaa71\",\"61c0c5a71d1f82001bdaaa6c\"]\n}")
                .post("/api/orders")
                .then().log().all();
    }
    @Step("Создание заказа с неверным хешем ингредиентов")
    public ValidatableResponse createOrderWithInvalidIngredientHash (String accessToken) {
        return given().log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .body("{\n\"ingredients\": [\"27c0c5a71d1f82001bdaaa6c\"," +
                        "\"27c0c5a71d1f82001bdaaa75\",\"27c0c5a71d1f82001bdaaa7a\"," +
                        "\"27c0c5a71d1f82001bdaaa71\",\"27c0c5a71d1f82001bdaaa6c\"]\n}")
                .post("/api/orders")
                .then().log().all();
    }
    @Step("Получение заказов авторизированого пользователя")
    public ValidatableResponse getOrderWithLogIn (String accessToken) {
        return given().log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders")
                .then().log().all();
    }
    @Step("Получение заказов неавторизированого пользователя")
    public ValidatableResponse getOrderWithoutLogIn() {
        return given().log().all()
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders")
                .then().log().all();
    }
}
