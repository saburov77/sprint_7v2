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



public class CreatingCourierTest {

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
    @DisplayName("Тест на создание курьера")
    public void creatingCourierTest() {
        CourierData courierData = new CourierData(login, password, firstName);
        CourierApi courierApi = new CourierApi();

        ValidatableResponse response = courierApi.createCourier(courierData);
        response.assertThat().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));
    }

   @Test
   @DisplayName("Тест, что нельзя создать двух одинаковых курьеров")
    public void createSameCourierTest () {

       CourierData courierData = new CourierData(login, password, firstName);
       CourierApi courierApi = new CourierApi();

       ValidatableResponse responseFirst = courierApi.createCourier(courierData);
       responseFirst.assertThat().log().all()
               .statusCode(HttpStatus.SC_CREATED)
               .body("ok", is(true));

       ValidatableResponse responseSecond = courierApi.createCourier(courierData);
       responseSecond.assertThat()
               .statusCode(HttpStatus.SC_CONFLICT).log().all()
               .body("message", is("Этот логин уже используется. Попробуйте другой."));
   }

    @Test
    @DisplayName("Тест, что нельзя создать двух курьеров c одинаковым Login")
    public void createSameLoginTest () {
        CourierData courierData = new CourierData(login, password, firstName);
        CourierApi courierApi = new CourierApi();
        CourierData sameLoginData = new CourierData(login, password+"1", firstName+"1");

        ValidatableResponse responseFirst = courierApi.createCourier(courierData);
        responseFirst.assertThat().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));

        ValidatableResponse responseSecond = courierApi.createCourier(sameLoginData);
        responseSecond.assertThat()
                .statusCode(HttpStatus.SC_CONFLICT).log().all()
                .body("message", is("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Тест на создание курьера без поля FirstName")
    public void createWithoutFirstNameFieldTest() {
        CourierData courierData = new CourierData(login, password);
        CourierApi courierApi = new CourierApi();
        ValidatableResponse response = courierApi.createCourier(courierData);

        response.assertThat().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Тест на создание курьера без поля Login")
    public void createWithoutLoginFieldTest() {
        CourierData courierData = new CourierData("", password, firstName);
        CourierApi courierApi = new CourierApi();
        ValidatableResponse response = courierApi.createCourier(courierData);

        response.assertThat().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Тест на создание курьера без поля Password")
    public void createWithoutPasswordFieldTest() {
        CourierData courierData = new CourierData(login, "", firstName);
        CourierApi courierApi = new CourierApi();
        ValidatableResponse response = courierApi.createCourier(courierData);

        response.assertThat().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

}
