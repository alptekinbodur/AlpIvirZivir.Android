package com.alp.alpivirzivir.Business;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import android.util.Log;
public class JsonParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    // constructor
    public JsonParser() {}
    // function get json from url
    // by making HTTP POST or GET mehtod
    public String Getir(String url, String method, List<NameValuePair> params) {
        // Making HTTP request
        Log.d("JsonParser", "Url: " + url);
        try {
            // check for request method
            if (method == "POST") {
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            } else if (method == "GET") {
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("Hata-UnsupportedEncodingException", "Hata Mesajı: " + e);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e("Hata-ClientProtocolException", "Hata Mesajı: " + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Hata-IOException", "Hata Mesajı: " + e);
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-9"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Hata-Buffer", "Hata Mesajı:" + e);
        }
        // return JSON String
        return json;
    }
}