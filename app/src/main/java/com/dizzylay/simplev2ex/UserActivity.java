package com.dizzylay.simplev2ex;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";
    private String userDetail = "";
    private List<ListItem> topicList = new ArrayList<>();
    private List<ReplyInformation> informationList = new ArrayList<>();
    private ItemAdapter itemAdapter;
    private ReplyInformationAdapter informationAdapter;
    private Intent intent;

    private final static int UPDATE_HEAD = 0;
    private final static int ADD_TOPIC = 1;
    private final static int ADD_REPLY = 2;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_HEAD:
                    TextView detail = findViewById(R.id.detail_user);
                    detail.setText(userDetail);
                case ADD_TOPIC:
                    itemAdapter.notifyDataSetChanged();
                    break;
                case ADD_REPLY:
                    informationAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        intent = getIntent();
        ImageView avatar = findViewById(R.id.avatar_user);
        avatar.setImageBitmap(intent.getParcelableExtra("AVATAR"));
        TextView username = findViewById(R.id.name_user);
        username.setText(intent.getStringExtra("USERNAME"));

        RecyclerView topicView = findViewById(R.id.topic_recyclerView);
        topicView.setNestedScrollingEnabled(false);
        topicView.setLayoutManager(new LinearLayoutManager(this));
        topicView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        itemAdapter = new ItemAdapter(topicList);
        itemAdapter.setOnItemClickListener((v, position) -> {
            ListItem topic = topicList.get(position);
            Intent intent1 = new Intent(UserActivity.this, ContentActivity.class);
            intent1.putExtra("URL", topic.getUrl());
            intent1.putExtra("AVATAR", topic.getAvatar());
            intent1.putExtra("USERNAME", topic.getUsername());
            intent1.putExtra("TITLE", topic.getTitle());
            intent1.putExtra("NODE_TITLE", topic.getNodeTitle());
            startActivity(intent1);
        });
        topicView.setAdapter(itemAdapter);

        RecyclerView replyView = findViewById(R.id.replies_recyclerView);
        replyView.setNestedScrollingEnabled(false);
        replyView.setLayoutManager(new LinearLayoutManager(this));
        replyView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        informationAdapter = new ReplyInformationAdapter(informationList);
        replyView.setAdapter(informationAdapter);
//        informationAdapter.setOnItemClickListener((v, position) -> {
//            ReplyInformation information = informationList.get(position);
//        });
        parseUserDetail(intent.getStringExtra("USERNAME"));
    }

    private void parseUserDetail(String username) {
        new Thread(() -> {

            try {
                Document doc = Jsoup.connect("https://www.v2ex.com/member/" +
                        username).get();
                Element userInformation = doc.select("table").get(1).selectFirst("span.gray");
                userDetail = userInformation.text();
                Message msg = new Message();
                msg.what = UPDATE_HEAD;
                handler.sendMessage(msg);

                Elements topics = doc.select(".cell").select(".item");
                for (int i = 0; i < topics.size(); i++) {
                    Element topic = topics.get(i);
                    String title = topic.select("span.item_title").text();
                    String node = topic.select("a.node").text();
                    String replyNumber = topic.select("a.count_livid").text();
                    String url = "https://www.v2ex.com" + topic.select("span.item_title")
                            .select("a").attr("href");
                    topicList.add(new ListItem(intent.getParcelableExtra("AVATAR"), title, node,
                            username, replyNumber, url));
//                    Log.d(TAG, "parseUserDetail: " + title + node + username + replyNumber + url);
                    Message message = new Message();
                    message.what = ADD_TOPIC;
                    handler.sendMessage(message);
                }

                Elements repliesDetail = doc.select("div.dock_area");
                Elements repliesContent = doc.select("div.reply_content");
                for (int i = 0; i < repliesDetail.size(); i++) {
                    Element reply = repliesDetail.get(i);
                    String replyDetail = reply.selectFirst("td[style]").text();
                    String replyContent = repliesContent.get(i).text();
                    String url = "www.v2ex.com" + reply.selectFirst("td[style]").select
                            ("a[href]").last().attr("href");
                    informationList.add(new ReplyInformation(replyDetail, replyContent, url));
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
