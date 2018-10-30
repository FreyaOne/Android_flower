package com.example.fling.flower;

import android.accounts.NetworkErrorException;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetConnection{     //网络连接

    public static String post(String url,String content){   //以post方式获取
        HttpURLConnection conn = null;
        try{
            URL mURL = new URL(url);
            conn = (HttpURLConnection)mURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(8000);
            conn.setDoOutput(true);

            String data = content;
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            }else{
                throw new NetworkErrorException("response status is" + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(conn != null){
                conn.disconnect();
            }
        }
        return null;
    }

    public static String get(String url){    //以get方式获取
        HttpURLConnection conn = null;
        try{
            URL mURL = new URL(url);
            conn = (HttpURLConnection)mURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(8000);

            int responseCode = conn.getResponseCode();
            if(responseCode == 200){

                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            }else{
                throw new NetworkErrorException("response status is" + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(conn != null){
                conn.disconnect();
            }
        }
        return null;
        }

        private static String getStringFromInputStream(InputStream is) throws IOException{    //获取数据流
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 01;
            while((len = is.read(buffer)) != -1){
                os.write(buffer,0,len);
            }
            is.close();
            String state = os.toString();
            os.close();
            return state;
        }
}
