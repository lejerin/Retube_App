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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lej.happy.retube.data.models.youtube.Comments;

public class DetectPapago {

    //현재 선택 언어 숫자 : 디폴트 -한국어
    private int nowSelectedLanNum = 0;
    //언어 선택 한국어, 영어, 일본어, 베트남어, 중국어 간체, 중국어 번체, 인도네시아어, 태국어, 독일어, 러시아어, 스페인어, 이탈리아어, 프랑스어
    private List<Comments.Item> koComments = new ArrayList<>();
    private List<Comments.Item> enComments = new ArrayList<>();
    private List<Comments.Item> jaComments = new ArrayList<>();
    private List<Comments.Item> viComments = new ArrayList<>();
    private List<Comments.Item> zhCNComments = new ArrayList<>();
    private List<Comments.Item> zhTWComments = new ArrayList<>();
    private List<Comments.Item> idComments = new ArrayList<>();
    private List<Comments.Item> thComments = new ArrayList<>();
    private List<Comments.Item> deComments = new ArrayList<>();
    private List<Comments.Item> ruComments = new ArrayList<>();
    private List<Comments.Item> esComments = new ArrayList<>();
    private List<Comments.Item> itComments = new ArrayList<>();
    private List<Comments.Item> frComments = new ArrayList<>();


    public void removeData(){
        koComments.clear();
        enComments.clear();
        jaComments.clear();
        viComments.clear();
        zhCNComments.clear();
        zhTWComments.clear();
        idComments.clear();
        thComments.clear();
        deComments.clear();
        ruComments.clear();
        esComments.clear();
        zhTWComments.clear();
        itComments.clear();
        frComments.clear();
    }

    public List<Comments.Item> analyzeList(List<Comments.Item> item){

        List<Comments.Item> list = new ArrayList<>();

        Thread thread = new Thread()
        {
            public void run() {
                for (int i = 0; i < item.size(); i++) {

                    String before = item.get(i).getSnippet().getTopLevelComment().getSnippet().getTextOriginal();
                    String code = startDetect(before);

                    switch (code){
                        case "ko":
                            if(nowSelectedLanNum == 0) list.add(item.get(i));
                            koComments.add(item.get(i));
                            break;

                        case "en":
                            if(nowSelectedLanNum == 1) list.add(item.get(i));
                            enComments.add(item.get(i));
                            break;

                        case "ja":
                            if(nowSelectedLanNum == 2) list.add(item.get(i));
                            jaComments.add(item.get(i));
                            break;

                        case "vi":
                            if(nowSelectedLanNum == 3) list.add(item.get(i));
                            viComments.add(item.get(i));
                            break;

                        case "zh-CN":
                            if(nowSelectedLanNum == 4) list.add(item.get(i));
                            zhCNComments.add(item.get(i));
                            break;

                        case "zh-TW":
                            if(nowSelectedLanNum == 5) list.add(item.get(i));
                            zhTWComments.add(item.get(i));
                            break;

                        case "id":
                            if(nowSelectedLanNum == 6) list.add(item.get(i));
                            idComments.add(item.get(i));
                            break;

                        case "th":
                            if(nowSelectedLanNum == 7) list.add(item.get(i));
                            thComments.add(item.get(i));
                            break;
                        case "de":
                            if(nowSelectedLanNum == 8) list.add(item.get(i));
                            deComments.add(item.get(i));
                            break;

                        case "ru":
                            if(nowSelectedLanNum == 9) list.add(item.get(i));
                            ruComments.add(item.get(i));
                            break;

                        case "es":
                            if(nowSelectedLanNum == 10) list.add(item.get(i));
                            esComments.add(item.get(i));
                            break;

                        case "it":
                            if(nowSelectedLanNum == 11) list.add(item.get(i));
                            itComments.add(item.get(i));
                            break;

                        case "fr":
                            if(nowSelectedLanNum == 12) list.add(item.get(i));
                            frComments.add(item.get(i));
                            break;

                        default:
                            break;
                    }




                }

            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        return list;
    }

    private String startDetect(String text) {
         String clientId = "WpsgWfnfde9g6DQukZui";//애플리케이션 클라이언트 아이디값";
         String clientSecret = "CgL9FauIu0";//애플리케이션 클라이언트 시크릿값";

        String query;
        try {
            query = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }
        String apiURL = "https://openapi.naver.com/v1/papago/detectLangs";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String responseBody = post(apiURL, requestHeaders, query);
        String result = responseBody.substring(responseBody.lastIndexOf(":\"")+2,responseBody.length()-2);
        JSONObject o = null;
        try {
            o = new JSONObject(responseBody);
            return o.getString("langCode");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Comments.Item> getLanList(int newSelectedLanNum){
        nowSelectedLanNum = newSelectedLanNum;
        switch (nowSelectedLanNum){
            case 0:
                //한국어
                return koComments;
            case 1:
                return enComments;
            case 2:
                return (jaComments);
            case 3:
                return (viComments);
            case 4:
                return (zhCNComments);
            case 5:
                return (zhTWComments);
            case 6:
                return idComments;
            case 7:
                return (thComments);
            case 8:
                return (deComments);
            case 9:
                return (ruComments);
            case 10:
                return (esComments);
            case 11:
                return (itComments);
            case 12:
                return (frComments);

        }

        return koComments;
    }

    private String post(String apiUrl, Map<String, String> requestHeaders, String text){
        HttpURLConnection con = connect(apiUrl);
        String postParams =  "query="  + text; //원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
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

    private HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body){
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