import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static model.Constants.BASE_URI;
import static model.Constants.GET_ORDERS_URI;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ScheduleOfOrderTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Тест на получение списка заказов")
    public  void getOrdersTest(){
        given()
                .get(GET_ORDERS_URI)
                .then().statusCode(HttpStatus.SC_OK)
                .body(notNullValue());
    }
}
