package com.jhsports.user.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by TQ on 2017/12/6.
 */
public class HttpHelper {
    public static String get(String urlString) {
        String responseBody = "";
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlString);
            // 打开和URL之间的连接
            URLConnection urlConnection = url.openConnection();
            // 设置通用的请求属性
//            urlConnection.setRequestProperty("Accept", "application/json");
//            urlConnection.setRequestProperty("Connection", "keep-alive");
//            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36");
            // 建立实际的连接
            urlConnection.connect();
            // 获取所有响应头字段
//            Map<String, List<String>> map = urlConnection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义BufferedReader输入流来读取URL的响应
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseBody += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseBody;
    }

    public static String post(String urlString, String param) {
//        PrintWriter printWriter = null;
        OutputStreamWriter outputStreamWriter = null;
//        DataOutputStream dataOutputStream = null;
        BufferedReader bufferedReader = null;
        String responseBody = "";
        try {
            URL url = new URL(urlString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            // 设置通用的请求属性
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Accept", "application/json");
//            connection.setRequestProperty("Connection", "keep-alive");
//            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36");
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
//            printWriter = new PrintWriter(connection.getOutputStream());
            outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
//            dataOutputStream = new DataOutputStream(connection.getOutputStream());
            // 发送请求参数
//            printWriter.print(param);
            outputStreamWriter.write(param);
//            dataOutputStream.writeBytes(param);
            // flush输出流的缓冲
//            printWriter.flush();
            outputStreamWriter.flush();
//            dataOutputStream.flush();
            // 定义BufferedReader输入流来读取URL的响应
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseBody += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try{
//                if(printWriter != null){
//                    printWriter.close();
//                }
                if(bufferedReader != null){
                    bufferedReader.close();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return responseBody;
    }

    public static void main(String[] args) throws IOException {
        String url = "http://test-account.9h-sports.com/Test/GetSMSCode?mobileNum=15210470906&appId=1";
        String responseBody = get(url);
        System.out.println(responseBody);

//        String url = "http://106.75.24.252:801/api/Manage/GrantCoins";
//        String param = "{ \"AppId\": 1, \"UnionUserId\": \"00000000-0000-0000-0000-000000000000\", \"Coins\": 10 }";
//        String responseBody = post(url, param);
//        System.out.println(responseBody);

    }
}
