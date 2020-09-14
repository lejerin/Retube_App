package lej.happy.retube.helper;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

// 네이버 기계번역 (Papago SMT) API 예제
public class TransferPapago {

    public String startTransfer(String text, int source_num, String target) {

        String source = "ko";

        switch (source_num){
            case 0: source = "ko";
                    break;
            case 1: source = "en";
                break;
            case 2: source = "ja";
                break;
            case 3: source = "vi";
                break;
            case 4: source = "zh-CN";
                break;
            case 5: source = "zh-TW";
                break;
            case 6: source = "id";
                break;
            case 7: source = "th";
                break;
            case 8: source = "de";
                break;
            case 9: source = "ru";
                break;
            case 10: source = "es";
                break;
            case 11: source = "it";
                break;
            case 12: source = "fr";
                break;
            default:
                break;
        }


        String clientId = "WpsgWfnfde9g6DQukZui";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "CgL9FauIu0";//애플리케이션 클라이언트 시크릿값";

        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";

        try {
            text = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String responseBody = post(apiURL, requestHeaders, text, source, target);
        JSONObject o = null;
        try {
            o = new JSONObject(responseBody);
            JSONObject a = o.getJSONObject("message");
            JSONObject b = a.getJSONObject("result");
            return b.getString("translatedText");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return responseBody;
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String text, String source, String target){
        HttpURLConnection con = connect(apiUrl);
        String postParams = "source="+source+"&target="+ target +"&text=" + text; //원본언어: 한국어 (ko) -> 목적언어: 영어 (en)

        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}