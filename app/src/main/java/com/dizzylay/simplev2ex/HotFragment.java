package com.dizzylay.simplev2ex;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * a
 * Name
 * Created by liaoy on 2018/4/2.
 */

public class HotFragment extends Fragment {
    private List<ListItem> itemList = new ArrayList<>();
    private ItemAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    private boolean ALREADY_LOAD = false;

    private static final String TAG = "HotFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hot_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!ALREADY_LOAD) {
            refreshLayout = getActivity().findViewById(R.id.hot_refresh);
            refreshLayout.setColorSchemeResources(R.color.colorPrimary);
            refreshLayout.setRefreshing(true);
            refreshLayout.setOnRefreshListener(() -> {
                itemList.clear();
                LoadListTask loadListTask = new LoadListTask(itemList, adapter, refreshLayout);
                loadListTask.execute(LoadListTask.UPDATE_LIST);
            });
            RecyclerView recyclerView = getActivity().findViewById(R.id.hot_list);
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
            LoadListTask loadListTask = new LoadListTask(itemList, adapter, refreshLayout);
            loadListTask.execute(LoadListTask.LOAD_LIST);
            ALREADY_LOAD = true;
        }
    }
}
