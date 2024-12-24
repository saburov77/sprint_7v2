package model;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static model.Constants.*;

public class CourierApi {


    @Step("Отправка запроса POST на создание курьера")
    public ValidatableResponse createCourier(CourierData courierData) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierData)
                .when()
                .post(COURIER_CREATE_URI)
                .then();
    }

     @Step("Отправка запроса POST на авторизацию курьера для получения id курьерв")
    public Response courierLogin(CourierData courierData) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierData)
                .when()
                .post(COURIER_LOGIN_URI);

    }

    @Step("Отправка запроса POST для тестов проверки авторизации")
    public ValidatableResponse courierValidLogin(CourierData courierData) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierData)
                .when()
                .post(COURIER_LOGIN_URI)
                .then();

    }

    @Step("Отправка запроса Delete для удаления курьера")
    public ValidatableResponse deleteCourier(Integer courierId) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .delete(String.format(COURIER_DELETE_URI, courierId))
                .then();

    }
}
