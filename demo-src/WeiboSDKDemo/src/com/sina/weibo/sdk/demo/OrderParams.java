package com.sina.weibo.sdk.demo;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OrderParams {
     static  String url = "http://api.sc.weibo.com/v2/pay/test?seller_id=2915069635";
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String result = httpGet(url);
		System.out.println(result.substring(0, result.lastIndexOf("-")));
		System.out.println(result.substring(result.indexOf("-") + 1));
	}

	public static String httpGet(String urlString) {
        int httpStatus = -1;

        HttpURLConnection urlConnection = null;
        String result = "";
        URL url;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
        StringBuffer temp = new StringBuffer();
		try {
			url = new URL(urlString);
			urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setConnectTimeout(20000);
			urlConnection.setReadTimeout(20000);
			is = urlConnection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
            String line;

            while ((line = in.readLine()) != null) {
                temp.append(line);
            }
            
            
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}

        return temp.toString();
    }
}
