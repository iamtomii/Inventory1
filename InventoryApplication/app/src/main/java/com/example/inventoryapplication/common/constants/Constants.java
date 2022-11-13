package com.example.inventoryapplication.common.constants;


public class Constants {
    public static final Integer LIMIT_ONCE = 1000;
    public static Integer POWER_LEVEL = 33;

    public static String CONFIG_MAC_HANDWARE = "00:15:83:39:37:BB";
    public static String CONFIG_IP_ADDRESS = "192.168.1.244";
    public static String CONFIG_PORT = "8080";
    public static String CONFIG_POWER_LEVEL = "33";

    public static final String KEY_GOOD_NAME = "drgm_goods_name";
    public static final String KEY_COST = "drgm_cost_price";
    public static final String KEY_TAX = "drgm_price_tax_off";
    public static int POWER_LEVEL_INVENTORY_REGISTRATION_VJ = 16;

    // Format code CD1 & CD@
    public static final String CD1_1 = "978";
    public static final String CD1_2 = "491";
    public static final String CD2_1 = "191";
    public static final String CD2_2 = "192";
    public static final String CD2_3 = "19";

    // Format code CD1 & CD@
    public static final int CD1_978 = 978;
    public static final int CD1_491 = 491;
    public static final int CD1_111 = 111;  // https://vjppd.backlog.jp/view/SA-151#comment-1281542456 - ADD
    public static final int CD2_192 = 192;
    public static final int CD2_191 = 191;

    public static final String BLANK = "";

    public static final String DIVISION_STATUS_NEW = "新品";
    public static final String DIVISION_STATUS_OLD = "中古";

    // List view color
    public static final String BACKGROUND_COLOR_BLUE = "#2750F6";
    public static final String BACKGROUND_COLOR_WHITE = "#FFFFFF";
    public static final String BACKGROUND_COLOR_LIGHT_BLUE = "#EBEFFF";


    /**
     * Column Rfid (new)
     */
    public static final String COLUMN_RFID = "rfid";


    /**
     * Information table register product
     */
    public static final String TABLE_REGISTER_PRODUCT = "list_inventory";
    public static final String ROW_ID = "li_no";
    public static final String SHOP_ID = "li_shop_cd";
    public static final String STAFF_ID = "li_staff_id";
    public static final String INVENTORY_IMPLEMENTATION_DATE = "li_tana_date";
    public static final String DATA_CREATION_DATE = "li_tana_create_date";
    public static final String DATA_CREATION_TIME = "li_tana_create_time";
    public static final String TYPE_OF_DATA = "li_tana_data_type";
    public static final String PRODUCT_TYPE = "li_tana_goods_type";
    public static final String SINGLE_ITEM_CODE = "li_tanpin_cd";
    public static final String SHELF_CODE_NO = "li_tana_cd";
    public static final String PRODUCT_CODE_TYPE = "li_goods_cd_type";
    public static final String STATUS = "li_condition_type";
    public static final String PRODUCT_CODE_1 = "li_plu_cd";
    public static final String PRODUCT_CODE_2 = "li_plu_addon_cd";
    public static final String PRODUCT_QUANTITY = "li_goods_count";
    public static final String TOTAL_AMOUNT = "li_goods_amount";
    public static final String BASE_UNIT_PRICE = "li_price";
    public static final String TAX_PRICE = "li_include_price";
    public static final String BUSINESS_DIVISION = "li_work_type";

    /**
     * Barcode
     */
    public static final String BARCODE_INVENTORY_REGISTRATION = "9780000000002";


    /**
     * Alert toast
     */
    public static final String INVALID_BARCODE = "INVALID BARCODE";


    /*
     * Key Json API Module RFID
     */
    /* key json common */
    public static final String KEY_CODE = "code";
    public static final String KEY_DATA = "data";
    public static final String KEY_ERROR = "error";
    public static final String KEY_MESSAGE = "message";

    /* constant value of key_code */
    public static final String VALUE_CODE_OK = "00";
    public static final String VALUE_CODE_RFID_ERROR = "02";

    //KE RFID
    public static final String KEY_RFID = "rfid";

    // #rfid -> jan
    public static final String KEY_JANCODE_1 = "jancode_1";
    public static final String KEY_JANCODE_2 = "jancode_2";


    //#region http code
    /* constant http code */
    public static final String STT_CODE_OK = "200";
    public static final String STT_CODE_BAD_REQUEST = "400";
    public static final String STT_CODE_UNAUTHORIZED = "401";
    public static final String STT_CODE_FORBIDDEN = "403";
    public static final String STT_CODE_NOT_FOUND = "404";
    public static final String STT_CODE_Method_Not_Allowed = "405";
    public static final String STT_CODE_Proxy_Authentication_Required = "407";
    public static final String STT_CODE_REQUEST_TIMEOUT = "408";
    public static final String STT_CODE_CONFLICT = "409";

    // server error
    public static final String STT_CODE_SERVER_ERROR = "500";
    public static final String STT_CODE_Not_Implemented = "501";
    public static final String STT_CODE_Bad_Gateway = "502";
    public static final String STT_CODE_Service_Unavailable = "503";
    public static final String STT_CODE_Gateway_Timeout = "504";
    //#endregion
}
