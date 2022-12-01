package com.example.inventoryapplication.common.function;

import android.content.Context;
import android.os.Environment;

import com.example.inventoryapplication.common.entities.InforProductEntity;
import com.example.inventoryapplication.database.SQLiteDatabaseHandler;
import com.example.inventoryapplication.interfaces.Callable;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import jxl.write.Label;

public class CsvExport {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
    static SQLiteDatabaseHandler db;

    public static void writeData(Context context, Callable callable){
        db = new SQLiteDatabaseHandler(context);
        List<String[]> csvData = new ArrayList<String[]>();

        File sd = Environment.getExternalStorageDirectory();
        File directory = new File(sd.getAbsolutePath()+"/inventory_data");
        //create directory if not exist
        if (!directory.isDirectory()) {
            boolean rs = directory.mkdirs();
            System.out.println(rs);
        }

        for(InforProductEntity p : db.getAllProducts()){
            String[] row = new String[]{p.getRfidCode(),p.getBarcodeCD1(), p.getGoodName(),p.getQuantity()+""};
            System.out.println("tommy1: "+p.getRfidCode());
            csvData.add(row);
        }




        String csvFile = "/inventory_smartactive_"+sdf.format(new Date(System.currentTimeMillis()))+".csv";
        String fileName = directory+csvFile;

        File file = new File(fileName);
        //FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(file));
            writer.writeAll(csvData);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        callable.call(true);

    }
}
