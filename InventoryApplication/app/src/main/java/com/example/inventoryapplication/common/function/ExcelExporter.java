package com.example.inventoryapplication.common.function;

import android.content.Context;
import android.os.Environment;

import com.example.inventoryapplication.common.entities.InforProductEntity;
import com.example.inventoryapplication.database.SQLiteDatabaseHandler;
import com.example.inventoryapplication.interfaces.Callable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import jxl.Cell;
import jxl.CellFeatures;
import jxl.CellType;
import jxl.LabelCell;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.Font;
import jxl.format.Format;
import jxl.format.Orientation;
import jxl.format.Pattern;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelExporter {
    static Callable callable;
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
    static SQLiteDatabaseHandler db;
    public static void exportObj(Set<String> setNotFoundRfid, Context context, Callable callableInput){
        callable = callableInput;
        db = new SQLiteDatabaseHandler(context);
        File sd = Environment.getExternalStorageDirectory();
        String csvFile = "inventory_smartactive_"+sdf.format(new Date(System.currentTimeMillis()))+".xls";
       // String csvFile = "inventory_export.xls";

        File directory = new File(sd.getAbsolutePath()+"/inventory_data");
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        System.out.println(extStorageDirectory);
        //create directory if not exist
        if (!directory.isDirectory()) {
            boolean rs = directory.mkdirs();
            System.out.println(rs);
        }
        try {
            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale(Locale.GERMAN.getLanguage(), Locale.GERMAN.getCountry()));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);

            //SHEET 1
            //Excel rfid sheet
            WritableSheet sheet1 = workbook.createSheet("RFID", 0);

            sheet1.addCell(new Label(0,0,"NUM")); //NUM
            sheet1.addCell(new Label(1,0,"RFID")); //RFID
            sheet1.addCell(new Label(2,0,"BARCODE")); //BARCODE
            sheet1.addCell(new Label(3,0,"NAME")); //NAME
            sheet1.addCell(new Label(4,0,"QUANTITY")); //QUAN

            int i=1;
            for(InforProductEntity p : db.getAllProducts()){
                sheet1.addCell(new Label(0,i,i+"")); //NUM
                sheet1.addCell(new Label(1,i,p.getRfidCode())); //RFID
                sheet1.addCell(new Label(2,i,p.getBarcodeCD1())); //BARCODE
                sheet1.addCell(new Label(3,i,p.getGoodName())); //NAME
                sheet1.addCell(new Label(4,i,p.getQuantity()+"")); //QUAN
                i++;
            }
            sheet1.setColumnView(0,5); //num
            sheet1.setColumnView(1,27); // rfid
            sheet1.setColumnView(2,15); // barcode
            sheet1.setColumnView(3,20); // name
            sheet1.setColumnView(4,7); // quan


            //SHEET 2
            //Excel barcode sheet
            WritableSheet sheet2 = workbook.createSheet("BARCODE", 1);

            sheet2.addCell(new Label(0,0,"NUM")); //NUM
            sheet2.addCell(new Label(1,0,"NAME")); //RFID
            sheet2.addCell(new Label(2,0,"BARCODE")); //BARCODE
            sheet2.addCell(new Label(3,0,"QUANTITY")); //QUAN

            i=1;
            for(InforProductEntity p : db.getAllProductsGroupByBarcode()){
                sheet2.addCell(new Label(0,i,i+"")); //NUM
                sheet2.addCell(new Label(1,i,p.getGoodName())); //NAME
                sheet2.addCell(new Label(2,i,p.getBarcodeCD1())); //BARCODE
                sheet2.addCell(new Label(3,i,p.getQuantity()+"")); //QUAN
                i++;
            }
            sheet2.setColumnView(0,5); //num
            sheet2.setColumnView(1,27); // name
            sheet2.setColumnView(2,15); // barcode
            sheet2.setColumnView(3,20); // quan

            //SHEET 3
            //Excel error sheet
            WritableSheet sheetError = workbook.createSheet("ERROR", 2);

            sheetError.addCell(new Label(0,0,"NUM")); //NUM
            sheetError.addCell(new Label(1,0,"RFID")); //RFID
            sheetError.addCell(new Label(2,0,"MESSAGE")); //RFID

            // column and row titles
            i=1;
            for(String p : setNotFoundRfid){
                sheetError.addCell(new Label(0,i,i+"")); //NUM
                sheetError.addCell(new Label(1,i,p)); //RFID
                sheetError.addCell(new Label(2,i,"NOT IN RFID MASTER")); //RFID
                i++;
            }
            sheetError.setColumnView(0,5); //num
            sheetError.setColumnView(1,27); // rfid
            sheetError.setColumnView(2,27); // rfid

            // close workbook
            callable.call(true);
            workbook.write();
            workbook.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static CellFormat getCellFormatCustom(String pos){
        CellFormat cellFormat = new CellFormat() {
            @Override
            public Format getFormat() {
                return null;
            }

            @Override
            public Font getFont() {
                if(pos.equals("title"))
                return new WritableFont(WritableFont.createFont("Calibri"), 0, WritableFont.BOLD, false,
                        UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
                else return  null;
            }

            @Override
            public boolean getWrap() {
                return false;
            }

            @Override
            public Alignment getAlignment() {
                return null;
            }

            @Override
            public VerticalAlignment getVerticalAlignment() {
                return null;
            }

            @Override
            public Orientation getOrientation() {
                return null;
            }

            @Override
            public BorderLineStyle getBorder(Border border) {
                return null;
            }

            @Override
            public BorderLineStyle getBorderLine(Border border) {
                return null;
            }

            @Override
            public Colour getBorderColour(Border border) {
                return null;
            }

            @Override
            public boolean hasBorders() {
                return false;
            }

            @Override
            public Colour getBackgroundColour() {
                return null;
            }

            @Override
            public Pattern getPattern() {
                return null;
            }

            @Override
            public int getIndentation() {
                return 0;
            }

            @Override
            public boolean isShrinkToFit() {
                return false;
            }

            @Override
            public boolean isLocked() {
                return false;
            }
        };
        return null;
    }
}