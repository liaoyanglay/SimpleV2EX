package com.dizzylay.simplev2ex;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ContentActivity extends AppCompatActivity {

    private Intent intent;
    private String contentHeader;
    private String repliesHeader;
    private RepliesAdapter adapter;
    private List<RepliesItem> repliesItemList = new ArrayList<>();

    private static final String TAG = "ContentActivity";
    private static final int UPDATE_HEAD = 0;
    private static final int ADD_REPLY = 1;
    private static final int UPDATE_REPLY = 2;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_HEAD:
                    TextView headerContent = findViewById(R.id.content_header);
                    TextView headerReplies = findViewById(R.id.replies_header);
                    headerContent.setText(contentHeader);
                    headerReplies.setText(repliesHeader);
                    break;
                case ADD_REPLY:
                    Log.w(TAG, "handleMessage: " + repliesItemList.size());
                    adapter.notifyDataSetChanged();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView avatar = findViewById(R.id.avatar_header);
        avatar.setImageBitmap((Bitmap) intent.getParcelableExtra("AVATAR"));
        TextView title = findViewById(R.id.title_header);
        title.setText(intent.getStringExtra("TITLE"));
        TextView nodeTitle = findViewById(R.id.node_title_header);
        nodeTitle.setText(intent.getStringExtra("NODE_TITLE"));
        TextView username = findViewById(R.id.username_header);
        username.setText(intent.getStringExtra("USERNAME"));
        RecyclerView recyclerView = findViewById(R.id.replies_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RepliesAdapter(repliesItemList);
        recyclerView.setAdapter(adapter);
        parseHTML();
    }

    private void parseHTML() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(intent.getStringExtra("URL")).get();
                    Element header = doc.selectFirst("div.topic_content");
//                    Log.d(TAG, "run: " + header.toString());
                    contentHeader = header.text();
                    Elements replies = doc.select("div.box").last().select("div.cell");
//                    Log.w(TAG, "run: " + replies.toString());
                    if (replies.size() > 0) {
                        repliesHeader = replies.get(0).text();
                    } else {
                        repliesHeader = "There's no reply";
                    }
                    Message msg = new Message();
                    msg.what = UPDATE_HEAD;
                    handler.sendMessage(msg);
//                    Log.d(TAG, "run: " + replies.size());
                    for (int i = 1; i < replies.size(); i++) {
                        Element reply = replies.get(i);
//                        Log.d(TAG, "run: " + reply.toString());
                        URL avatarUrl = new URL("https:" + reply.selectFirst("img.avatar").attr
                                ("src"));
                        Bitmap avatar = LoadListTask.getURLImage(avatarUrl);
                        String username = reply.selectFirst("a.dark").text();
                        String replyTime = reply.selectFirst("span.ago").text();
                        String replyContent = reply.selectFirst("div.reply_content").text();
//                        Log.d(TAG, "run: " + username + replyContent);
                        repliesItemList.add(new RepliesItem(avatar, username, replyTime,
                                replyContent));
                        Message message = new Message();
                        message.what = ADD_REPLY;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
