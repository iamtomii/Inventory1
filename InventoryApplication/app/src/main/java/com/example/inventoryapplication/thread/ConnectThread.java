package com.example.inventoryapplication.thread;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.telecom.Call;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ProgressBar;

import com.example.inventoryapplication.common.constants.Config;
import com.example.inventoryapplication.interfaces.Callable;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ConnectThread extends Thread {
    Callable mCallable;
    UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static BluetoothSocket bTSocket;
    private HashSet<String> listTag = new HashSet<>();
    private static boolean isConnection = false;
    public static Thread thread;
    private static final int LENGTH_RFID = 8;
    private static final String SIGNAL_START_CHARACTER = "~As";
    Set<String> setCustom = new HashSet<>();
    public boolean connect(BluetoothDevice bTDevice, Context mContext,Callable callable) {
        this.mCallable = callable;
        BluetoothSocket temp = null;
        try {
            temp = bTDevice.createRfcommSocketToServiceRecord(mUUID);
            bTSocket = temp;
        } catch (IOException e) {
            Log.d("CONNECTTHREAD", "Could not create RFCOMM socket:" + e.toString());
            return false;
        }
        try {
            bTSocket.connect();
            isConnection = true;
            if(isConnection == true){
                callable.call(true);
            }
            thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (isConnection) {
                        try {

                            // length buffer
                            byte[] buffer = new byte[2048];

                            // stop the process for 1 second to catch the signal from the user
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                            }

                            // receive input signal from bluetooth rfid scanner
                            InputStream inputStream = bTSocket.getInputStream();

                           /* // read input signal
                            inputStream.read(buffer);

                            // decode rfid from buffer data
                            String readerInputStream = new String(buffer, StandardCharsets.UTF_8);*/

                            //NEW PROCESS
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                            while(reader.ready()){
                                String line  = reader.readLine();
                                System.out.println(line);
                                if(line.contains("~eT")) {
                                    setCustom.add(line.substring(7));
                                }
                            }
                          /*  if(readerInputStream.contains("~eT")){
                                System.out.println("AAAAAAAA: "+readerInputStream);
                                //String listTag = readerInputStream.substring(getIndexStart(readerInputStream),getIndexEnd(readerInputStream));
                                String[] splitString = readerInputStream.split("\r\n");
                                for(int i = 0 ;i <splitString.length-1;i++){
                                    if(!splitString[i].equals("~Af0000") && !splitString[i].equals("~As0000") && splitString[i].length()>=13)
                                        setCustom.add(splitString[i].substring(7));
                                }
                            }*/
                            // #HUYNHQUANGVINH send list rfid to api
                            JSONArray jsonArray = null;
                            if(!setCustom.isEmpty()) {
                                try {
                                    jsonArray = new JSONArray();
                                    for (String i : setCustom) {
                                        jsonArray.put(i);
                                    }

                                    // bug1
                                    if (jsonArray.length() != 0) {
                                        Log.d("data_arr", jsonArray.toString());
                                        new HttpPostRfid(mContext).execute(Config.CODE_LOGIN, Config.HTTP_SERVER_SHOP+Config.API_RFID_TO_JAN, jsonArray.toString());
                                      /*  for(int i =0;i<jsonArray.length();i++) {
                                            String item = jsonArray.getString(i);
                                            new HttpRegister(mContext).execute(Config.CODE_LOGIN, Config.HTTP_SERVER_SHOP + "/api/v1/insert_rfid_master", item);
                                        }*/
                                        setCustom.clear();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }


                        } catch (IOException e) {
                            isConnection = false;
                            System.out.println("Error read data: " + e.toString());
                        }
                    }
                }
            });
            thread.start();
        } catch (IOException e) {
            Log.d("CONNECTTHREAD", "Could not connect: " + e.toString());
            try {
                bTSocket.close();
            } catch (IOException close) {
                Log.d("Try close socket", "Could not close connection:" + e.toString());
                return false;
            }
        }
        return true;
    }

    public boolean cancel() {
        try {
            if(isConnection==true){
                isConnection = false;
                bTSocket.close();
            }
        } catch (IOException e) {
            Log.d("CONNECTTHREAD", "Could not close connection:" + e.toString());
            return false;
        }
        return true;
    }

    private int getIndexStart(String buffer){
       int index = buffer.indexOf("~As");
       return index;
    }
    private int getIndexEnd(String buffer){
        int index = buffer.indexOf("~Af");
        return index;
    }

}