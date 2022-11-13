package com.example.inventoryapplication.common.entities;


import com.example.inventoryapplication.common.constants.Constants;

/**
 * Product code after scan in RegisterData screen
 *
 * @author cong-pv
 * @since 2019/06/20
 */
public class InforProductEntity {

    private String ShelfCode;
    private String goodName;
    private String BarcodeCD1;
    private String BarcodeCD2;
    private int BasePrice;
    private int TaxIncludePrice;
    private int Quantity;
    private String RfidCode;

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getShelfCode() {
        return ShelfCode;
    }

    public void setShelfCode(String shelfCode) {
        ShelfCode = shelfCode;
    }

    public String getBarcodeCD1() {
        return BarcodeCD1;
    }

    public void setBarcodeCD1(String barcodeCD1) {
        BarcodeCD1 = barcodeCD1;
    }

    public String getBarcodeCD2() {
        return BarcodeCD2;
    }

    public void setBarcodeCD2(String barcodeCD2) {
        BarcodeCD2 = barcodeCD2;
    }

    public int getBasePrice() {
        return BasePrice;
    }

    public void setBasePrice(int basePrice) {
        BasePrice = basePrice;
    }

    public int getTaxIncludePrice() {
        return TaxIncludePrice;
    }

    public void setTaxIncludePrice(int taxIncludePrice) {
        TaxIncludePrice = taxIncludePrice;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getRfidCode() {
        return RfidCode;
    }

    public void setRfidCode(String rfidCode) {
        RfidCode = rfidCode;
    }

    @Override
    public String toString() {
        return "InforProductEntity{" +
                "ShelfCode='" + ShelfCode + '\'' +
                ", goodName='" + goodName + '\'' +
                ", BarcodeCD1='" + BarcodeCD1 + '\'' +
                ", BarcodeCD2='" + BarcodeCD2 + '\'' +
                ", BasePrice=" + BasePrice +
                ", TaxIncludePrice=" + TaxIncludePrice +
                ", Quantity=" + Quantity +
                ", RfidCode='" + RfidCode + '\'' +
                '}';
    }
}
