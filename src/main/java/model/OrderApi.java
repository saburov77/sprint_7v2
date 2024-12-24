package model;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static model.Constants.ORDER_CANCEL;
import static model.Constants.ORDER_CREATE_URI;

public class OrderApi {

    @Step ("Отправка запроса POST на создание заказа")
    public Response createOrder(OrderData orderData) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(orderData)
                .when()
                .post(ORDER_CREATE_URI);
    }

    @Step ("Отправка запроса PUT для отмены заказа")
    public Response deleteOrders (Integer trackId) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(trackId)
                .when()
                .put(ORDER_CANCEL);
    }
}
