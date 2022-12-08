package com.example.inventoryapplication.thread;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.inventoryapplication.common.constants.Config;
import com.example.inventoryapplication.common.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;


/**
 * HTTP post user login
 *
 * @author cong-pv
 * @since 2019-03-13
 */

public class HttpRegister extends AsyncTask<String, String, String> {

    /**
     * Http response.
     */
    private HttpRfidResponse response;

    /**
     * Constructor HttpPost.
     */
    public HttpRegister(Context c) {
        this.response = (HttpRfidResponse) c;
    }

    /**
     * Set progress dialog loading.
     */
    protected void onPreExecute() {
    }

    /**
     * Send request and get response to services API.
     */
    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL(params[1]);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setRequestMethod(Config.METHOD_POST);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty(Config.PROPERTY_KEY, Config.PROPERTY_VALUE);

            JSONObject ob = setParams(params);
            System.out.println(ob.toString());

            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(ob.toString());
            writer.flush();
            writer.close();
            os.close();

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));

                String line = in.readLine();

                return (line != null ? line : "");

            } else {
                return String.valueOf(responseCode);
            }
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }

    }

    /**
     * End progress loading.
     */
    @Override
    protected void onPostExecute(String result) {
        try{
            response.progressRfidFinish(result, 0, null);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Set request params.
     */
    private JSONObject setParams(String... params) {

        Log.d("LIST_RFID", params[2]);

        JSONObject jsonObject = new JSONObject();

        try {
            switch (params[0]) {
                case Config.CODE_LOGIN:
                    // #HUYNHQUANGVINH send list rfid
                    //jsonObject.put(Constants.COLUMN_RFID, new JSONArray(params[2]));
                    //jsonObject.put("force_update",  true);
                    //jsonObject.put("drgm_pos_shop_cd",  "SHOPTEST");
                    jsonObject.put("drgm_rfid_cd",  params[2]);
                    /*jsonObject.put("drgm_jan",  "2002000005526");
                    jsonObject.put("drgm_goods_name",  "Thuật giả kim - Tập "+numRan());
                    jsonObject.put("drgm_price_tax_off",  1);
                    jsonObject.put("drgm_cost_rate",  19.9);
                    jsonObject.put("drgm_cost_price",  65000);
                    jsonObject.put("drgm_media_cd",  "43800");
                    jsonObject.put("drgm_product_id",  "aaaa");*/
                    break;
                default:
                    break;
            }
            //jsonObject.put(Config.API_KEY, Config.API_KEY_VALUE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public int numRan(){
        Random rand = new Random();
        int n = rand.nextInt(5000);
        n += 1;
        return n;
    }
}
