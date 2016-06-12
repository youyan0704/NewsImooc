package com.jingyan.newsimooc;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private final static String TAG = "MainActivity";
    private ListView listView;
    private static String URL = "http://www.imooc.com/api/teacher?type=4&num=30";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        new NewsAsyncTask().execute(URL);
    }

    private List<NewsBean> getJsonData(String url){
        List<NewsBean> newsBeanList = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(url).openStream());
            Log.i("xyz", jsonString);
            JSONObject jsonObject;
            NewsBean newsBean;
            try {
                jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0;i < jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    newsBean = new NewsBean();
                    newsBean.setNewsIcon(jsonObject.getString("picSmall"));
                    newsBean.setNewsTilte(jsonObject.getString("name"));
                    newsBean.setNewsContent(jsonObject.getString("description"));
                    newsBeanList.add(newsBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsBeanList;
    }

    private String readStream(InputStream inputStream){
        InputStreamReader inputStreamReader;
        String result = "";
        try {
            String line = "";
            inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while ((line = bufferedReader.readLine()) != null){
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
    class NewsAsyncTask extends AsyncTask<String,String,List<NewsBean>>{

        @Override
        protected List<NewsBean> doInBackground(String... strings) {
            return getJsonData(strings[0]);
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBean) {
            super.onPostExecute(newsBean);
            NewsAdapter adapter = new NewsAdapter(MainActivity.this,newsBean);
            listView.setAdapter(adapter);
        }
    }
}
