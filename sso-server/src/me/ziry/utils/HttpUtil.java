package me.ziry.utils;

import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Http工具
 * Created by Ziry on 2018/4/23.
 */
public class HttpUtil {

    /**
     * Get请求
     * @param url
     * @return
     */
    public static JSONObject doGet(String url) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        JSONObject resultJson = null;
        CloseableHttpResponse response = null;

        try {

            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity());// 返回json格式：
                resultJson = JSONObject.fromObject(result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return resultJson;
    }

    public static void main(String[] args) {

        JSONObject a = JSONObject.fromObject("{\"code\":\"error\",\"msg\":\"未找到token\"}");
        System.out.println(a);
    }

}