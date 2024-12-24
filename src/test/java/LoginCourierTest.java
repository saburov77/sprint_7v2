import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import model.CourierApi;
import model.CourierData;
import model.CourierResponseData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static model.Constants.BASE_URI;
import static org.hamcrest.CoreMatchers.is;


public class LoginCourierTest {

    protected Integer courierId;
    protected String login;
    protected String password;
    protected String firstName;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        login = "Leo" + RandomStringUtils.randomAlphanumeric(3);
        password = RandomStringUtils.randomAlphanumeric(4);
        firstName = "Vasya" + RandomStringUtils.randomAlphabetic(4);
        CourierData courierData = new CourierData(login, password, firstName);
        CourierApi courierApi = new CourierApi();
        ValidatableResponse response = courierApi.createCourier(courierData);
    }

    @After
    public void cleanUp() {
        CourierApi courierApi = new CourierApi();
        CourierData loginData = new CourierData(login, password);
        Response courierLogin = courierApi.courierLogin(loginData);
        courierId = courierLogin.body().as(CourierResponseData.class).getId();

        if (courierId != null) {
            courierApi.deleteCourier(courierId);
        } else { System.out.println("В базе данных нет курьера с логином " + login);}

    }
    @Test
    @DisplayName("Тест на авторизацию курьера с валидными значениями")
    public void authorizationTest() {
        CourierApi courierApi = new CourierApi();
        CourierData loginData = new CourierData(login, password);
        ValidatableResponse courierLogin = courierApi.courierValidLogin(loginData);
        courierLogin.assertThat().log().all()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    @DisplayName("Тест на авторизацию курьера с не валидным login")
    public void notValidLoginTest() {
        CourierApi courierApi = new CourierApi();
        CourierData loginData = new CourierData(login + "1", password);
        ValidatableResponse courierLogin = courierApi.courierValidLogin(loginData);
        courierLogin.assertThat().log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));;
    }

    @Test
    @DisplayName("Тест на авторизацию курьера с не валидным password")
    public void notValidPasswordTest() {
        CourierApi courierApi = new CourierApi();
        CourierData loginData = new CourierData(login, password + "1");
        ValidatableResponse courierLogin = courierApi.courierValidLogin(loginData);
        courierLogin.assertThat().log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));;
    }

    @Test
    @DisplayName("Тест на авторизацию курьера с отсутствием login")
    public void withoutLoginTest() {
        CourierApi courierApi = new CourierApi();
        CourierData loginData = new CourierData("", password);
        ValidatableResponse courierLogin = courierApi.courierValidLogin(loginData);
        courierLogin.assertThat().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));;
    }

    @Test
    @DisplayName("Тест на авторизацию курьера с отсутствием password")
    public void withoutPasswordTest() {
        CourierApi courierApi = new CourierApi();
        CourierData loginData = new CourierData(login, "");
        ValidatableResponse courierLogin = courierApi.courierValidLogin(loginData);
        courierLogin.assertThat().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));;
    }

}
