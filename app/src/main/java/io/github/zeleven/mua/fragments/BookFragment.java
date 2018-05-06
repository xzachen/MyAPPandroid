package io.github.zeleven.mua.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import java.util.ArrayList;
import java.util.List;

import io.github.zeleven.mua.MainActivity;
import io.github.zeleven.mua.Model.Thing;
import io.github.zeleven.mua.R;
import io.github.zeleven.mua.activities.ThingDetailActivity;
import io.github.zeleven.mua.adapter.BaseRecyclerAdapter;
import io.github.zeleven.mua.adapter.ThingRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment implements IOnSearchClickListener, Toolbar.OnMenuItemClickListener {
    private SearchFragment searchFragment;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ThingRecyclerAdapter recyclerAdapter;

    public BookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book1, container, false);
        initBookView(view);
        return view;
    }

    //toolbar的搜索栏目
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    public void initBookView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.booktoolbar);
        toolbar.setTitle("学习助手");//标题
        toolbar.inflateMenu(R.menu.menu_main);
        setHasOptionsMenu(true);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        searchFragment = SearchFragment.newInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        toolbar.setOnMenuItemClickListener(this);
        searchFragment.setOnSearchClickListener(this);
        recyclerAdapter = new ThingRecyclerAdapter();
        recyclerAdapter.updateList(getThings());
        recyclerAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<Thing>() {
            @Override
            public void onItemClick(View view, Thing item, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(getActivity(), "这是长按测试", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in=new Intent(getActivity(), ThingDetailActivity.class);
                    startActivity(in);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

//        BaseActivity.of(getActivity()).fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigator.launchOverlay(BaseActivity.of(getActivity()), v, getActivity().findViewById(R.id.base_fragment_container));
//            }
//        });
//点击添加打开一个新的界面。
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
    }


    //    之后从服务器端获取数据，目前只是测试。
//获取数据的方法
    public List<Thing> getThings() {
        ArrayList<Thing> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new Thing("Thing " + i, null));
        }
        return list;

    }
//实现了搜索文本的实现
    @Override
    public void OnSearchClick(String keyword) {
        Toast.makeText(getActivity(), keyword, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search://点击搜索
                searchFragment.show(getFragmentManager(), SearchFragment.TAG);
                break;
        }
        return true;
    }
}
