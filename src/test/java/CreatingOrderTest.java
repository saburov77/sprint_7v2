import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.OrderApi;
import model.OrderData;
import model.OrderResponseData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static model.Constants.BASE_URI;


@RunWith(Parameterized.class)
public class CreatingOrderTest {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String rentTime;
    private String deliveryDate;
    private String comment;
    private final List<String> color;
    private Integer trackId;


    public CreatingOrderTest(List<String> color) {
        this.color = color;
    }


    @Parameterized.Parameters
    public static Object[][] paramsData () {
        return new Object[][] {
                {List.of()},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        this.firstName = "Мурлыка";
        this.lastName = "Пушистик";
        this.address = "Москва, Солнечная, 7";
        this.phone = "+79998885533";
        this.rentTime = "2";
        this.deliveryDate = "2024-12-25";
        this.comment = "Мяукать. Дверь не царапать!";
    }
    @Test
    @DisplayName("Тест на создание заказа")
    public void createOrderTest(){
        OrderApi orderApi = new OrderApi();
        OrderData orderData = new OrderData(firstName, lastName, address,
                phone, rentTime, deliveryDate, comment, color);
        Response createOrder = orderApi.createOrder(orderData);
        trackId = createOrder.body().as(OrderResponseData.class).getTrack();
        System.out.println(trackId);
        Assert.assertNotNull(trackId);
    }

    @After
    @DisplayName("Удаление тестовых данных")
    public void cancelOrder(){
        OrderApi orderApi = new OrderApi();
        orderApi.deleteOrders(trackId);
    }

}
