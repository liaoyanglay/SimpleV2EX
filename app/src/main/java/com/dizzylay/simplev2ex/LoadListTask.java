package com.dizzylay.simplev2ex;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;


/**
 * a
 * Name
 * Created by liaoy on 2018/4/5.
 */
public class LoadListTask extends AsyncTask<Integer, Integer, Integer> {

    public static final int ADD_ITEM = 0;
    public static final int LOAD_LIST = 1;
    public static final int UPDATE_LIST = 2;
    private static final String TAG = "LoadListTask";

    private List<ListItem> itemList;
    private ItemAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    private SwipeRefreshLayout refreshLayout;

    public LoadListTask(List<ListItem> itemList, ItemAdapter adapter,
                        SwipeRefreshLayout refreshLayout) {
        this.itemList = itemList;
        this.adapter = adapter;
        this.refreshLayout = refreshLayout;
    }

    @Override
    protected final Integer doInBackground(Integer... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("https://www.v2ex.com/api/topics/hot.json");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            JSONArray jsonArray = new JSONArray(response.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                parseJSON(item, itemList);
                publishProgress(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return params[0];
    }

    @Override
    protected void onPostExecute(Integer action) {
        switch (action) {
            case LOAD_LIST:
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                break;
            case UPDATE_LIST:
                refreshLayout.setRefreshing(false);
                break;
            default:
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        adapter.notifyDataSetChanged();
    }

    public static void parseJSON(JSONObject item, List<ListItem> list) {
        try {
            String title = item.getString("title");
            String url = item.getString("url");
            int replies = item.getInt("replies");
            JSONObject member = item.getJSONObject("member");
            String username = member.getString("username");
            URL avatar_url = new URL("https:" + member.getString("avatar_normal"));
            JSONObject node = item.getJSONObject("node");
            String nodeTitle = node.getString("title");
            Bitmap avatar = getURLImage(avatar_url);
            list.add(new ListItem(avatar, title, nodeTitle, username
                    , String.valueOf(replies), url));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getURLImage(URL avatar_url) {
        InputStream is = null;
        HttpURLConnection connection = null;
        Bitmap avatar = null;
        try {
            connection = (HttpURLConnection) avatar_url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            is = connection.getInputStream();
            avatar = BitmapFactory.decodeStream(is);
        } catch (ProtocolException e) {
            e.printStackTrace();
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
            if (connection != null) {
                connection.disconnect();
            }
        }
        return avatar;
    }
}
