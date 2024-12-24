package model;

public class Constants {
    //Адрес тестового стенда
    public static final String BASE_URI = "http://qa-scooter.praktikum-services.ru/";
    //Используемые в проекте URL запрсов к API
    public static final String COURIER_CREATE_URI = "/api/v1/courier";
    public static final String COURIER_LOGIN_URI = "/api/v1/courier/login";
    public static final String COURIER_DELETE_URI = "/api/v1/courier/%d";
    public static final String ORDER_CREATE_URI = "/api/v1/orders";
    public static final String GET_ORDERS_URI = "/api/v1/orders";
    public static final String ORDER_CANCEL = "/api/v1/orders/cancel";
    }
