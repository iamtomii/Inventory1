package com.example.inventoryapplication.common.function;

import static java.nio.file.Files.readAllBytes;
import static java.util.Base64.getEncoder;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.example.inventoryapplication.common.constants.Config;
import com.example.inventoryapplication.common.entities.InforProductEntity;
import com.example.inventoryapplication.database.SQLiteDatabaseHandler;
import com.example.inventoryapplication.interfaces.Callable;
import com.example.inventoryapplication.thread.HttpPostBase64;
import com.example.inventoryapplication.thread.HttpPostRfid;
import com.opencsv.CSVWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import android.util.Base64.*;

import androidx.annotation.RequiresApi;

import jxl.write.Label;


public class CsvExport {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
    static SQLiteDatabaseHandler db;
    static String file_name;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void writeData(Context context,String[] header ,Callable callable){
        db = new SQLiteDatabaseHandler(context);
        List<String[]> csvData = new ArrayList<String[]>();

        File sd = Environment.getExternalStorageDirectory();

        csvData.add(header);

        if(header.length==4){
            file_name="/inventory_data";
            for(InforProductEntity p : db.getAllProducts()){
                String[] row = new String[]{p.getRfidCode(), p.getGoodName(),p.getQuantity()+"",p.getBarcodeCD1()};
                csvData.add(row);
            }
        }else {
            file_name="/incoming_data";
            for (InforProductEntity p : db.getAllProducts()) {
                String[] row = new String[]{p.getSerial(), p.getInventoryName(), p.getRfidCode(), p.getGoodName(), p.getQuantity() + "", p.getBarcodeCD1()};
                csvData.add(row);
            }
        }
        File directory = new File(sd.getAbsolutePath()+file_name);
        //create directory if not exist
        if (!directory.isDirectory()) {
            boolean rs = directory.mkdirs();
            System.out.println(rs);
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
        //endcode Base64
        try {
            String csv_base=encodeCSV(fileName);
            new HttpPostBase64(context).execute(Config.CODE_LOGIN, Config.HTTP_SEVER_ODOO+Config.API_ODOO, csv_base.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String encodeCSV(String path) throws Exception{
        FileInputStream fileInputStream=new FileInputStream(path);
        byte[] data =readAllBytes(fileInputStream);
        String csvString=Base64.getEncoder().encodeToString(data);
        return csvString;
    }
    public static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyAllBytes(in, out);/*w w  w . ja  v  a 2s. co m*/
        return out.toByteArray();
    }

    public static int copyAllBytes(InputStream in, OutputStream out)
            throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[4096];
        while (true) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            out.write(buffer, 0, read);
            byteCount += read;
        }
        return byteCount;
    }
}

