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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
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
    private TextView loadMore;

    private static final String TAG = "ContentActivity";
    private static final int UPDATE_HEAD = 0;
    private static final int ADD_REPLY = 1;
    private static final int UPDATE_REPLY = 2;

    public final static int LOADING = 0;
    public final static int LOADING_COMPLETE = 1;
    public final static int LOADING_END = 2;

    private static int ONE_TIME_LOAD = 20;
    private int loadState = LOADING_COMPLETE;
    private int loadedRepliesNumber = 0;
    private int page = 0;
    private boolean isFirstTimeLoad = true;

    private Document doc;

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
                case UPDATE_REPLY:
                    loadMore.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
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
        RecyclerView recyclerView = findViewById(R.id.replies_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RepliesAdapter repliesAdapter = new RepliesAdapter(repliesItemList);
        adapter = new HeaderAndFooterWrapper(repliesAdapter);
        recyclerView.setAdapter(adapter);
        View headView = createHeaderView(recyclerView);
        View footView = createFooterView(recyclerView);
        adapter.setHeaderView(headView);
        adapter.setFooterView(footView);
        recyclerView.addOnScrollListener(new EndOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (loadState == LOADING_COMPLETE) {
                    loadMore.setText("loading...");
                    loadMore.setVisibility(View.VISIBLE);
                    loadState = LOADING;
                    parseHTML();
                } else if (loadState == LOADING_END) {
                    loadMore.setText("--已经到底啦--");
                    loadMore.setVisibility(View.VISIBLE);
                }
            }
        });
        parseHTML();
    }

    private void parseHTML() {
        new Thread(() -> {
            try {
                if (loadedRepliesNumber >= page * 100) {
                    page++;
                    doc = Jsoup.connect(intent.getStringExtra("URL") + "?p=" + page).get();
                } else if (loadState == LOADING_END) {
                    doc = Jsoup.connect(intent.getStringExtra("URL") + "?p=" + page).get();
                }

                if (isFirstTimeLoad) {
                    parseHeader();
                    isFirstTimeLoad = false;
                    Message msg = new Message();
                    msg.what = UPDATE_HEAD;
                    handler.sendMessage(msg);
                }

                parseReply();
                Message msg = new Message();
                msg.what = UPDATE_REPLY;
                handler.sendMessage(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void parseHeader() {
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
        } else {
            repliesHeader = "There's no reply";
        }
    }

    private void parseReply() throws MalformedURLException {
        Elements replies = doc.select("div.box").last()
                .select("div.cell")
                .select("div[id]");
//                    Log.d(TAG, "run: " + replies.size());
        int loadNumber = loadedRepliesNumber - (page - 1) * 100;

        for (int i = loadNumber; i < replies.size() && i < loadNumber + ONE_TIME_LOAD; i++,
                loadedRepliesNumber++) {
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
        if (replies.size() < loadNumber + ONE_TIME_LOAD) {
            loadState = LOADING_END;
        } else {
            loadState = LOADING_COMPLETE;
        }
    }

    private View createHeaderView(ViewGroup parent) {
        View headView = LayoutInflater.from(this).inflate(R.layout.header_content, parent,
                false);
        ImageView avatar = headView.findViewById(R.id.avatar_header);
        avatar.setImageBitmap((Bitmap) intent.getParcelableExtra("AVATAR"));
        TextView title = headView.findViewById(R.id.title_header);
        title.setText(intent.getStringExtra("TITLE"));
        TextView nodeTitle = headView.findViewById(R.id.node_title_header);
        nodeTitle.setText(intent.getStringExtra("NODE_TITLE"));
        TextView username = headView.findViewById(R.id.username_header);
        username.setText(intent.getStringExtra("USERNAME"));
        return headView;
    }

    private View createFooterView(ViewGroup parent) {
        View footView = LayoutInflater.from(this).inflate(R.layout.footer_view, parent,
                false);
        loadMore = footView.findViewById(R.id.load_more);
        return footView;
    }

}
