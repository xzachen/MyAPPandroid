package io.github.zeleven.mua.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.bignerdranch.android.multiselector.MultiSelector;

import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.github.zeleven.mua.Model.CourseModel;
import io.github.zeleven.mua.Model.Reminder;
import io.github.zeleven.mua.Model.Thing;
import io.github.zeleven.mua.R;
import io.github.zeleven.mua.activities.ReminderAddActivity;
import io.github.zeleven.mua.adapter.BaseRecyclerAdapter;
import io.github.zeleven.mua.adapter.ThingRecyclerAdapter;
import io.github.zeleven.mua.broadcasts.AlarmReceiver;
import io.github.zeleven.mua.db.ReminderDatabase;
import io.github.zeleven.mua.view.TimetableView;

/**
 * 这是一个测试的fragment
 * 测试全部加载成功 结束时间与2018/3/17。
 * BaseFragment
 */

public class BaseFragment extends Fragment implements IOnSearchClickListener, Toolbar.OnMenuItemClickListener {
    private WeekView mWeekView;
    private SearchFragment searchFragment;
    private TimetableView mTimetable;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ThingRecyclerAdapter recyclerAdapter;

    //    采用本地存储的方法实现的提醒。
//    提醒页面的控件声明。
    private RecyclerView mList;
    //    private SimpleAdapter mAdapter;
    private Toolbar mToolbar;
    private TextView mNoReminderView;
    private com.getbase.floatingactionbutton.FloatingActionButton mAddReminderButton;
    private int mTempPost;
    private LinkedHashMap<Integer, Integer> IDmap = new LinkedHashMap<>();
    private ReminderDatabase rb;
    private MultiSelector mMultiSelector = new MultiSelector();
    private AlarmReceiver mAlarmReceiver;
    private RecyclerView.LayoutManager layoutManager;

    //    结束控件声明
    public static BaseFragment newInstance(String info) {
        Bundle args = new Bundle();
        BaseFragment fragment = new BaseFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;

//        通过不同的newInstance传入的参数控制Fragment的布局信息。
//        注意信息的重复刷新。
//        信息的重复获取是本次毕业设计的最大的难点。
//        以及包括服务器的设计和架设。
//        这里将会是项目结构逻辑最复杂的实习地方。
//        写于2018/2/25   航海楼
//        学习fragment的笔记。
        if (getArguments().getString("info") == "设置") {
            view = inflater.inflate(R.layout.diagonallayout_activity_main, null);

        } else if (getArguments().getString("info") == "课表") {
            view = inflater.inflate(R.layout.testlayout, null);
//            找到个个布局的布局文件并进行视图渲染。
            mTimetable = (TimetableView) view.findViewById(R.id.timetable);
            mTimetable.loadCourses(getData());
        } else if (getArguments().getString("info") == "笔记") {
            view = inflater.inflate(R.layout.activity_test, null);
            initRecyclerView(view);
        } else if (getArguments().getString("info") == "提醒") {
            view = inflater.inflate(R.layout.activity_main, null);
//            调用不同的初始化方法来实现不同的时间点击。
//            第四个界面的页面布局和实现发现。
            initTodoview(view);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    //初始化笔记页面的布局并设置一些必要的监听
    public void initbookView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("学习助手");//标题
        toolbar.inflateMenu(R.menu.menu_main);
        setHasOptionsMenu(true);
        searchFragment = SearchFragment.newInstance();

        toolbar.setOnMenuItemClickListener(this);

        searchFragment.setOnSearchClickListener(this);

    }

    public void initRecyclerView(View view) {
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
//                    BaseActivity.of(getActivity()).animateHomeIcon(MaterialMenuDrawable.IconState.X);
                } else {
//                    Navigator.launchDetail(BaseActivity.of(getActivity()), view, item, recyclerView);
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //获得笔记  使用框架从网络端获取数据、现在是测试框架 未从服务器爬取数据
//    测试获得是否准确  之前有错误  出现在
    public List<Thing> getThings() {
        ArrayList<Thing> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new Thing("Thing " + i, null));
        }
        return list;
    }
//初始化提醒页面的布局


    public void initTodoview(View view) {
        rb = new ReminderDatabase(getActivity());
        // 初始化控件
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar1);
        mAddReminderButton = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.add_reminder);
        mList = (RecyclerView) view.findViewById(R.id.reminder_list);
        mNoReminderView = (TextView) view.findViewById(R.id.no_reminder_text);
        mToolbar.inflateMenu(R.menu.menu_add_reminder);
        setHasOptionsMenu(true);

        // To check is there are saved reminders
        // If there are no reminders display a message asking the user to create reminders
        List<Reminder> mTest = rb.getAllReminders();

        if (mTest.isEmpty()) {
            mNoReminderView.setVisibility(View.VISIBLE);
        }

        // Create recycler view
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerForContextMenu(mList);
//        mAdapter = new SimpleAdapter();
//        mAdapter.setItemCount(100);
//        mList.setAdapter(mAdapter);
        mToolbar.setTitle("学习助手");

        // On clicking the floating action button
        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReminderAddActivity.class);
                startActivity(intent);
            }
        });

        // 初始化时钟。
        mAlarmReceiver = new AlarmReceiver();
    }

    //    从网络Volley端获取数据------目前只是测试。
    private List<CourseModel> getData() {
        List<CourseModel> list = new ArrayList<>();

        CourseModel course1 = new CourseModel();
        course1.setCname("计算机网络");
        course1.setClassroom("1号楼103");
        course1.setStartSection(1);
        course1.setEndSection(4);
        course1.setDayOfWeek(1);
        course1.setStartWeek(1);
        course1.setEndWeek(12);
        list.add(course1);

        CourseModel course2 = new CourseModel();
        course2.setCname("计算机组成原理");
        course2.setClassroom("1号楼312");
        course2.setStartSection(7);
        course2.setEndSection(8);
        course2.setDayOfWeek(1);
        course2.setStartWeek(1);
        course2.setEndWeek(12);
        list.add(course2);


        CourseModel course3 = new CourseModel();
        course3.setCname("数据结构与算法");
        course3.setClassroom("1号楼201");
        course3.setStartSection(1);
        course3.setEndSection(2);
        course3.setDayOfWeek(2);
        course3.setStartWeek(4);
        course3.setEndWeek(18);
        list.add(course3);


        CourseModel course4 = new CourseModel();
        course4.setCname("高等数学");
        course4.setClassroom("3号楼602");
        course4.setStartSection(5);
        course4.setEndSection(6);
        course4.setDayOfWeek(2);
        course4.setStartWeek(1);
        course4.setEndWeek(12);
        list.add(course4);


        CourseModel course5 = new CourseModel();
        course5.setCname("离散数学");
        course5.setClassroom("1号楼311");
        course5.setStartSection(3);
        course5.setEndSection(4);
        course5.setDayOfWeek(3);
        course5.setStartWeek(1);
        course5.setEndWeek(12);
        list.add(course5);


        CourseModel course6 = new CourseModel();
        course6.setCname("无线网络技术");
        course6.setClassroom("1号楼210");
        course6.setStartSection(1);
        course6.setEndSection(2);
        course6.setDayOfWeek(4);
        course6.setStartWeek(4);
        course6.setEndWeek(8);
        list.add(course6);

        CourseModel course7 = new CourseModel();
        course7.setCname("大学体育");
        course7.setClassroom("运动场");
        course7.setStartSection(5);
        course7.setEndSection(6);
        course7.setDayOfWeek(4);
        course7.setStartWeek(4);
        course7.setEndWeek(12);
        list.add(course7);

        CourseModel course8 = new CourseModel();
        course8.setCname("操作系统");
        course8.setClassroom("2号楼303");
        course8.setStartSection(1);
        course8.setEndSection(4);
        course8.setDayOfWeek(5);
        course8.setStartWeek(1);
        course8.setEndWeek(12);
        list.add(course8);

        CourseModel course9 = new CourseModel();
        course9.setCname("Java语言程序设计");
        course9.setClassroom("2号楼104");
        course9.setStartSection(7);
        course9.setEndSection(8);
        course9.setDayOfWeek(5);
        course9.setStartWeek(1);
        course9.setEndWeek(12);
        list.add(course9);

        return list;
    }

    //实现具体的实现搜索方式。
    @Override
    public void OnSearchClick(String keyword) {
        Toast.makeText(getActivity(), keyword, Toast.LENGTH_SHORT).show();
    }

    //实现搜索文本的编码安排方式。
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search://点击搜索
                searchFragment.show(getFragmentManager(), SearchFragment.TAG);
                break;
            case R.id.discard_reminder:
                Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.save_reminder:
                Toast.makeText(getActivity(), "Test1", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
