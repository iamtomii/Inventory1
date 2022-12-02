package com.example.inventoryapplication.common.constants;

public class Config {

    public static String HTTP_SEVER_ODOO = "http://192.168.1.140:8015";
    public static String HTTP_SERVER_SHOP = "http://192.168.1.94:8080";   // Connect API server TONY THINH
   // private static final String HTTP_SERVER_SHOP = "http://192.168.1.244:8027";   // Connect API server TONY THINH
    /**
     * Method POST
     */
    public static final String METHOD_POST = "POST";

    /**
     * Property key
     */
    public static final String PROPERTY_KEY = "Content-Type";

    /**
     * Property value
     */
    //static final String PROPERTY_VALUE = "application/x-www-form-urlencoded";
    public static final String PROPERTY_VALUE = "application/json";

    /**
     * Property value post file
     */
    public static final String PROPERTY_VALUE_POST_FILE = "multipart/form-data";

    /**
     * Api key
     */
    public static final String API_KEY = "api_key";
    /**
     * Api key value
     */
    public static final String API_KEY_VALUE = "aip_rtsa_20220516_1";
    /**
     * API code login
     */
    public static final String CODE_LOGIN = "1";

    /**
     * #HUYNHQUANGVINH API RFID TO JAN
     */
    //public static final String API_RFID_TO_JAN = HTTP_SERVER_SHOP + "/api/v2/rfids_to_jans";
    public static final String API_RFID_TO_JAN = "/api/v2/rfids_to_jans";
    public static final String API_ODOO = "/inventory_controller/test_update";

}
