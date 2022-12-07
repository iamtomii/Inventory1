package com.example.inventoryapplication.database;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.inventoryapplication.common.entities.InforProductEntity;
import com.example.inventoryapplication.interfaces.Callable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private Callable callable;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "inventory_db";

    // Country table name
    private static final String TABLE_PRODUCT= "product_info";

    // Country Table Columns names
/*    private static final String KEY_ID = "id";
    private static final String COUNTRY_NAME = "CountryName";
    private static final String POPULATION = "Population";*/
    private static String SHELF_CODE = "ShelfCode";
    private static String GOOD_NAME = "goodName";
    private static String BARCODE_01 = "BarcodeCD1";
    private static String BARCODE_02 = "BarcodeCD2";
    private static String BASE_PRICE = "BasePrice";
    private static String TAX_INCLUDE_PRICE = "TaxIncludePrice";
    private static String QUANTITY = "Quantity";
    private static String RFID_CODE = "RfidCode";
    private static String TYPE_TABLE= "TypeProduct";
    private static String DATE="Date";
    private static String IVT_NAME="InventoryName";
    private static String SERIAL_CODE="Serial";
    private static String ID="id";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COUNTRY_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
                + RFID_CODE + " TEXT,"
                + GOOD_NAME + " TEXT,"
                + TYPE_TABLE + " TEXT,"
                + DATE + " TEXT,"
                + IVT_NAME + " TEXT,"
                + SERIAL_CODE + " TEXT,"
                + BARCODE_01 + " TEXT,"
                + BARCODE_02 + " TEXT,"
                + BASE_PRICE + " INTEGER,"
                + TAX_INCLUDE_PRICE + " INTEGER,"
                + QUANTITY + " INTEGER"+ ")";
        db.execSQL(CREATE_COUNTRY_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        // Create tables again
        onCreate(db);
    }
    public void deleteTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new country
    public void insertProduct(InforProductEntity product) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(BASE_PRICE, product.getBasePrice());
            values.put(ID, product.getId());
            values.put(TYPE_TABLE, product.getTypeProduct());
            values.put(DATE, product.getDate());
            values.put(IVT_NAME, product.getInventoryName());
            values.put(SERIAL_CODE, product.getSerial());

            values.put(GOOD_NAME, product.getGoodName());
            values.put(RFID_CODE, product.getRfidCode());

            values.put(BARCODE_01, product.getBarcodeCD1());
            values.put(BARCODE_02, product.getBarcodeCD2());

            values.put(TAX_INCLUDE_PRICE, product.getTaxIncludePrice());
            values.put(QUANTITY, product.getQuantity());
            // Inserting Row
            db.insert(TABLE_PRODUCT, null, values);
            db.close(); // Closing database connection
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    // Adding new country
    public void insertAllProducts(LinkedList<InforProductEntity> products) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(InforProductEntity product : products){
            ContentValues values = new ContentValues();
            values.put(RFID_CODE, product.getRfidCode());
            values.put(GOOD_NAME, product.getGoodName());
            values.put(TYPE_TABLE, product.getTypeProduct());
            values.put(DATE, product.getDate());
            values.put(IVT_NAME, product.getInventoryName());
            values.put(SERIAL_CODE, product.getSerial());
            values.put(BARCODE_01, product.getBarcodeCD1());
            values.put(BARCODE_02, product.getBarcodeCD2());
            values.put(BASE_PRICE, product.getBasePrice());
            values.put(TAX_INCLUDE_PRICE, product.getTaxIncludePrice());
            values.put(QUANTITY, product.getQuantity());

            // Inserting Row
            db.insert(TABLE_PRODUCT, null, values);
        }
        db.close(); // Closing database connection
    }
    public void insertAllProductsinvent(LinkedList<InforProductEntity> products) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(InforProductEntity product : products){
            ContentValues values = new ContentValues();
            values.put(ID, product.getId());
            values.put(RFID_CODE, product.getRfidCode());
            values.put(GOOD_NAME, product.getGoodName());
            values.put(TYPE_TABLE, product.getTypeProduct());
            values.put(BARCODE_01, product.getBarcodeCD1());
            values.put(BARCODE_02, product.getBarcodeCD2());
            values.put(BASE_PRICE, product.getBasePrice());
            values.put(TAX_INCLUDE_PRICE, product.getTaxIncludePrice());
            values.put(QUANTITY, product.getQuantity());

            // Inserting Row
            db.insert(TABLE_PRODUCT, null, values);
        }
        db.close(); // Closing database connection
    }
    // Adding new country
    public void insertAllProductsCallBack(LinkedList<InforProductEntity> products, Callable callable) {
        this.callable = callable;
        SQLiteDatabase db = this.getWritableDatabase();
        for(InforProductEntity product : products){
            ContentValues values = new ContentValues();
            values.put(ID, product.getId());
            values.put(RFID_CODE, product.getRfidCode());
            values.put(GOOD_NAME, product.getGoodName());
            values.put(TYPE_TABLE, product.getTypeProduct());
            values.put(DATE, product.getDate());
            values.put(IVT_NAME, product.getInventoryName());
            values.put(SERIAL_CODE, product.getSerial());
            values.put(BARCODE_01, product.getBarcodeCD1());
            values.put(BARCODE_02, product.getBarcodeCD2());
            values.put(BASE_PRICE, product.getBasePrice());
            values.put(TAX_INCLUDE_PRICE, product.getTaxIncludePrice());
            values.put(QUANTITY, product.getQuantity());

            // Inserting Row
            db.insert(TABLE_PRODUCT, null, values);
        }
        callable.call(true);
        db.close(); // Closing database connection
    }

    // Getting single country
    InforProductEntity getProduct(String rfid) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCT, new String[] { ID, RFID_CODE,
                        GOOD_NAME,TYPE_TABLE,DATE,IVT_NAME,SERIAL_CODE, BARCODE_01, BARCODE_02, BASE_PRICE, TAX_INCLUDE_PRICE, QUANTITY }, RFID_CODE + "=?",
                new String[] { String.valueOf(rfid) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        InforProductEntity productEntity = new InforProductEntity();
        productEntity.setRfidCode(cursor.getString(0));
        productEntity.setGoodName(cursor.getString(1));
        productEntity.setTypeProduct(cursor.getString(2));
        productEntity.setDate(cursor.getString(3));
        productEntity.setInventoryName(cursor.getString(4));
        productEntity.setSerial(cursor.getString(5));
        productEntity.setBarcodeCD1(cursor.getString(6));
        productEntity.setBarcodeCD2(cursor.getString(7));
        productEntity.setBasePrice(Integer.parseInt(cursor.getString(8)));
        productEntity.setTaxIncludePrice(Integer.parseInt(cursor.getString(9)));
        productEntity.setQuantity(Integer.parseInt(cursor.getString(10)));
        // return product
        return productEntity;
    }

    public List<InforProductEntity> getProductByName(String name) {
        List<InforProductEntity> pList = new ArrayList<InforProductEntity>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCT, new String[] { ID, RFID_CODE,
                        GOOD_NAME,TYPE_TABLE,DATE,IVT_NAME,SERIAL_CODE, BARCODE_01, BARCODE_02, BASE_PRICE, TAX_INCLUDE_PRICE, QUANTITY }, GOOD_NAME + " like ?",
                new String[] { String.valueOf("%"+name+"%") }, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InforProductEntity productEntity = new InforProductEntity();
                productEntity.setId(Integer.parseInt(cursor.getString(0)));
                productEntity.setRfidCode(cursor.getString(1));
                productEntity.setGoodName(cursor.getString(2));
                productEntity.setTypeProduct(cursor.getString(3));
                productEntity.setDate(cursor.getString(4));
                productEntity.setInventoryName(cursor.getString(5));
                productEntity.setSerial(cursor.getString(6));
                productEntity.setBarcodeCD1(cursor.getString(7));
                productEntity.setBarcodeCD2(cursor.getString(8));
                productEntity.setBasePrice(Integer.parseInt(cursor.getString(9)));
                productEntity.setTaxIncludePrice(Integer.parseInt(cursor.getString(10)));
                productEntity.setQuantity(Integer.parseInt(cursor.getString(11)));
                // Adding country to list
                pList.add(productEntity);
            } while (cursor.moveToNext());
        }

        // return country list
        return pList;
    }
    public List<InforProductEntity> getProductByNameAndType(String name, String type) {
        List<InforProductEntity> pList = new ArrayList<InforProductEntity>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT +" WHERE " + GOOD_NAME + " LIKE ? AND " + TYPE_TABLE + " LIKE ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  new String[] { String.valueOf("%"+name+"%"),String.valueOf("%"+type+"%") });
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InforProductEntity productEntity = new InforProductEntity();
                productEntity.setId(Integer.parseInt(cursor.getString(0)));
                productEntity.setRfidCode(cursor.getString(1));
                productEntity.setGoodName(cursor.getString(2));
                productEntity.setTypeProduct(cursor.getString(3));
                productEntity.setDate(cursor.getString(4));
                productEntity.setInventoryName(cursor.getString(5));
                productEntity.setSerial(cursor.getString(6));
                productEntity.setBarcodeCD1(cursor.getString(7));
                productEntity.setBarcodeCD2(cursor.getString(8));
                productEntity.setBasePrice(Integer.parseInt(cursor.getString(9)));
                productEntity.setTaxIncludePrice(Integer.parseInt(cursor.getString(10)));
                productEntity.setQuantity(Integer.parseInt(cursor.getString(11)));
                // Adding country to list
                pList.add(productEntity);
            } while (cursor.moveToNext());
        }

        // return country list
        return pList;
    }

    // Getting single country
    public List<InforProductEntity> getProductByBarcode(String barcode) {
        List<InforProductEntity> pList = new ArrayList<InforProductEntity>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCT, new String[] { ID, RFID_CODE,
                        GOOD_NAME, TYPE_TABLE,DATE,IVT_NAME,SERIAL_CODE,BARCODE_01, BARCODE_02, BASE_PRICE, TAX_INCLUDE_PRICE, QUANTITY }, BARCODE_01 + " like ?",
                new String[] { String.valueOf("%"+barcode+"%") }, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InforProductEntity productEntity = new InforProductEntity();
                productEntity.setRfidCode(cursor.getString(0));
                productEntity.setGoodName(cursor.getString(1));
                productEntity.setTypeProduct(cursor.getString(2));
                productEntity.setDate(cursor.getString(3));
                productEntity.setInventoryName(cursor.getString(4));
                productEntity.setSerial(cursor.getString(5));
                productEntity.setBarcodeCD1(cursor.getString(6));
                productEntity.setBarcodeCD2(cursor.getString(7));
                productEntity.setBasePrice(Integer.parseInt(cursor.getString(8)));
                productEntity.setTaxIncludePrice(Integer.parseInt(cursor.getString(9)));
                productEntity.setQuantity(Integer.parseInt(cursor.getString(10)));
                // Adding country to list
                pList.add(productEntity);
            } while (cursor.moveToNext());
        }

        // return country list
        return pList;
    }
    public List<InforProductEntity> getProductByBarcodeAndType(String barcode,String type) {
        List<InforProductEntity> pList = new ArrayList<InforProductEntity>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery="SELECT * FROM " + TABLE_PRODUCT + " WHERE " + BARCODE_01 + " LIKE ? AND " + TYPE_TABLE +" LIKE ? ";
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf("%"+barcode+"%"),String.valueOf("%"+type+"%")});
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InforProductEntity productEntity = new InforProductEntity();
                productEntity.setRfidCode(cursor.getString(0));
                productEntity.setGoodName(cursor.getString(1));
                productEntity.setTypeProduct(cursor.getString(2));
                productEntity.setDate(cursor.getString(3));
                productEntity.setInventoryName(cursor.getString(4));
                productEntity.setSerial(cursor.getString(5));
                productEntity.setBarcodeCD1(cursor.getString(6));
                productEntity.setBarcodeCD2(cursor.getString(7));
                productEntity.setBasePrice(Integer.parseInt(cursor.getString(8)));
                productEntity.setTaxIncludePrice(Integer.parseInt(cursor.getString(9)));
                productEntity.setQuantity(Integer.parseInt(cursor.getString(10)));
                // Adding country to list
                pList.add(productEntity);
            } while (cursor.moveToNext());
        }

        // return country list
        return pList;
    }
    // Getting All Countries
    public List<InforProductEntity> getAllProducts() {
        List<InforProductEntity> pList = new ArrayList<InforProductEntity>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InforProductEntity productEntity = new InforProductEntity();
                productEntity.setId(Integer.parseInt(cursor.getString(0)));
                productEntity.setRfidCode(cursor.getString(1));
                productEntity.setGoodName(cursor.getString(2));
                productEntity.setTypeProduct(cursor.getString(3));
                productEntity.setDate(cursor.getString(4));
                productEntity.setInventoryName(cursor.getString(5));
                productEntity.setSerial(cursor.getString(6));
                productEntity.setBarcodeCD1(cursor.getString(7));
                productEntity.setBarcodeCD2(cursor.getString(8));
                productEntity.setBasePrice(Integer.parseInt(cursor.getString(9)));
                productEntity.setTaxIncludePrice(Integer.parseInt(cursor.getString(10)));
                productEntity.setQuantity(Integer.parseInt(cursor.getString(11)));
                // Adding country to list
                pList.add(productEntity);
            } while (cursor.moveToNext());
        }

        // return country list
        return pList;
    }
    public List<InforProductEntity> getAllProductsbyType(String type) {
        List<InforProductEntity> pList = new ArrayList<InforProductEntity>();
        // Select All Query

        //String selectQuery = "SELECT  * FROM '" + TABLE_PRODUCT+"'WHERE'"+TYPE_TABLE+"'='"+type+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, new String[] { ID, RFID_CODE,
                        GOOD_NAME, TYPE_TABLE,DATE,IVT_NAME,SERIAL_CODE,BARCODE_01, BARCODE_02, BASE_PRICE, TAX_INCLUDE_PRICE, QUANTITY }, TYPE_TABLE + " like ?",
                new String[] { String.valueOf("%"+type+"%") }, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InforProductEntity productEntity = new InforProductEntity();
                productEntity.setId(Integer.parseInt(cursor.getString(0)));
                productEntity.setRfidCode(cursor.getString(1));
                productEntity.setGoodName(cursor.getString(2));
                productEntity.setTypeProduct(cursor.getString(3));
                productEntity.setDate(cursor.getString(4));
                productEntity.setInventoryName(cursor.getString(5));
                productEntity.setSerial(cursor.getString(6));
                productEntity.setBarcodeCD1(cursor.getString(7));
                productEntity.setBarcodeCD2(cursor.getString(8));
                productEntity.setBasePrice(Integer.parseInt(cursor.getString(9)));
                productEntity.setTaxIncludePrice(Integer.parseInt(cursor.getString(10)));
                productEntity.setQuantity(Integer.parseInt(cursor.getString(11)));
                // Adding country to list
                pList.add(productEntity);
            } while (cursor.moveToNext());
        }

        // return country list
        return pList;
    }
    // Getting All Countries
    public List<InforProductEntity> getAllProductsGroupByBarcode() {
        List<InforProductEntity> pList = new ArrayList<InforProductEntity>();
        // Select All Query
        String selectQuery = "SELECT    "+ GOOD_NAME + ","+ BARCODE_01 + ","+"SUM("+QUANTITY+")" +
                " FROM " + TABLE_PRODUCT + " GROUP BY "+BARCODE_01;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InforProductEntity productEntity = new InforProductEntity();
                productEntity.setGoodName(cursor.getString(0));
                productEntity.setBarcodeCD1(cursor.getString(1));
                productEntity.setQuantity(Integer.parseInt(cursor.getString(2)));
                // Adding country to list
                pList.add(productEntity);
            } while (cursor.moveToNext());
        }

        // return country list
        return pList;
    }

    // Updating single country
    public int updateProduct(InforProductEntity product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, product.getId());
        values.put(RFID_CODE, product.getRfidCode());
        values.put(GOOD_NAME, product.getGoodName());
        values.put(TYPE_TABLE, product.getTypeProduct());
        values.put(DATE, product.getDate());
        values.put(IVT_NAME, product.getInventoryName());
        values.put(SERIAL_CODE, product.getSerial());
        values.put(BARCODE_01, product.getBarcodeCD1());
        values.put(BARCODE_02, product.getBarcodeCD2());
        values.put(BASE_PRICE, product.getBasePrice());
        values.put(TAX_INCLUDE_PRICE, product.getTaxIncludePrice());
        values.put(QUANTITY, product.getQuantity());

        // updating row
        return db.update(TABLE_PRODUCT, values, RFID_CODE + " = ?",
                new String[] { String.valueOf(product.getRfidCode()) });
    }

    // Deleting single country
    public void deleteProduct(InforProductEntity product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, RFID_CODE + " = ?",
                new String[] { String.valueOf(product.getRfidCode()) });
        db.close();
    }
    public void deleteProductbyTypeTable(InforProductEntity product,String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, RFID_CODE + " like ? AND "+ TYPE_TABLE + " like ? ",
                new String[] { String.valueOf(product.getRfidCode()),String.valueOf("%"+type+"%") });
        db.close();
    }


    // Deleting all countries
    public void deleteAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT,null,null);
        db.close();
    }
    public void deleteAllProductsbyTypeTable(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, TYPE_TABLE + " like ?",
                new String[] { String.valueOf("%"+type+"%") });
        db.close();
    }

    // Getting countries Count
    public int getProductsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
    public int getProductsbyTypeCount(String type) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_PRODUCT + " WHERE "+ TYPE_TABLE +" like ?",new String[]{ String.valueOf("%"+type+"%")});
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}