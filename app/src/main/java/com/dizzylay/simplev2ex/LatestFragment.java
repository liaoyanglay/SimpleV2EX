package com.dizzylay.simplev2ex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * a
 * Name
 * Created by liaoy on 2018/4/2.
 */

public class LatestFragment extends Fragment {

    private List<ListItem> itemList = new ArrayList<>();
    private ItemAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String TAG = "LatestFragment";
    private static final int ADD_ITEM = 0;
    private static final int LOAD_LIST = 1;
    private static final int UPDATE_LIST = 2;
    private boolean ALREADY_LOAD = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_ITEM:
                    adapter.notifyDataSetChanged();
                    break;
                case LOAD_LIST:
                    ALREADY_LOAD = true;
                    break;
                case UPDATE_LIST:
                    swipeRefreshLayout.setRefreshing(false);
                default:
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.latest_fragment, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!ALREADY_LOAD) {
            swipeRefreshLayout = getActivity().findViewById(R.id.latest_refresh);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                itemList.clear();
                sendRequest(UPDATE_LIST);
            });
            RecyclerView recyclerView = getActivity().findViewById(R.id.latest_list);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                    DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new ItemAdapter(itemList);
            adapter.setOnItemClickListener((v, position) -> {
                ListItem item = itemList.get(position);
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("URL", item.getUrl());
                intent.putExtra("AVATAR", item.getAvatar());
                intent.putExtra("USERNAME", item.getUsername());
                intent.putExtra("TITLE", item.getTitle());
                intent.putExtra("NODE_TITLE", item.getNodeTitle());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
            sendRequest(LOAD_LIST);
        }
    }

    public void sendRequest(final int action) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://www.v2ex.com/api/topics/latest.json");
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
                    LoadListTask.parseJSON(item, itemList);
                    Message message = new Message();
                    message.what = ADD_ITEM;
                    handler.sendMessage(message);
                }
                Message msg = new Message();
                msg.what = action;
                handler.sendMessage(msg);
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
        }).start();
    }
}
