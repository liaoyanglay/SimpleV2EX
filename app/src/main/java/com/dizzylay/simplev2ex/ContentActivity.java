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
    private String contentHeader = "";
    private String repliesHeader = "";
    private String timeHeader = "";
    private HeaderAndFooterWrapper adapter;
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
                    TextView headerTime = findViewById(R.id.time_header);
                    headerContent.setText(contentHeader);
                    headerReplies.setText(repliesHeader);
                    headerTime.setText(timeHeader);
                    break;
                case ADD_REPLY:
//                    Log.w(TAG, "handleMessage: " + repliesItemList.size());
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
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RepliesAdapter repliesAdapter = new RepliesAdapter(repliesItemList);
        adapter = new HeaderAndFooterWrapper(repliesAdapter);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndOnScrollListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore: "+ "loadMore");
            }
        });
        parseHTML();
    }

    private void parseHTML() {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(intent.getStringExtra("URL") + "?p=1").get();
                Element header = doc.selectFirst("div.topic_content");
                Elements subtitle = doc.select("div.subtle");
//                    Log.d(TAG, "run: " + header.toString());
                if (header != null) {
                    contentHeader = header.text();
                }
                for (int i = 0; i < subtitle.size(); i++) {
                    String addContent = "\n\n" + subtitle.get(i).select("span.fade").text()
                            + "\n" + subtitle.get(i).select("div.topic_content").text();
                    contentHeader = contentHeader.concat(addContent);
                }

                Element main = doc.selectFirst("div#Main");
                timeHeader = main.selectFirst("small").ownText();
                Elements replies = doc.select("div.box").last().select("div.cell");
//                    Log.w(TAG, "run: " + replies.toString());
                if (replies.size() > 0) {
                    Elements tags = replies.get(0).select("a.tag");
                    if (tags != null) {
                        repliesHeader = replies.get(0).select("span").text() + " " + tags.text();
                    } else {
                        repliesHeader = replies.get(0).select("span").text();
                    }
                    replies = replies.select("div[id]");
                } else {
                    repliesHeader = "There's no reply";
                }

                Message msg = new Message();
                msg.what = UPDATE_HEAD;
                handler.sendMessage(msg);
//                    Log.d(TAG, "run: " + replies.size());
                for (int i = 0; i < replies.size(); i++) {
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
        }).start();
    }

}
