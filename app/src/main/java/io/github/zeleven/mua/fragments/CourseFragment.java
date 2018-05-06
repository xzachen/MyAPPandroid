package io.github.zeleven.mua.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.github.zeleven.mua.MainActivity;
import io.github.zeleven.mua.Model.AppUser;
import io.github.zeleven.mua.Model.CourseModel;
import io.github.zeleven.mua.R;
import io.github.zeleven.mua.activities.AddcourseActivity;
import io.github.zeleven.mua.activities.Main2Activity;
import io.github.zeleven.mua.view.TimetableView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {
    private String Url = "http://xzacjy.pythonanywhere.com/myapp/querycourseinfo/";
    //    自定义的视图控件。
    private AppUser user;
    List<CourseModel> list = new ArrayList<>();
    private TimetableView mTimetable;
    private com.github.clans.fab.FloatingActionButton addnote, addcourse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        user = (AppUser) getActivity().getIntent().getSerializableExtra("person_data");
        initCourseView(view);
        return view;
    }

    public CourseFragment() {

    }


    //初始化页面并设置监听事件。
    public void initCourseView(View view) {
        mTimetable = (TimetableView) view.findViewById(R.id.timetable);
        addnote = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fab);
        addcourse = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fab1);
        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        addcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getActivity(), AddcourseActivity.class);
//                     这里吧用户的数据传入给HomeActivity。
//                     传递好之后settingFragemet跟新视图。
                i2.putExtra("person_data", user);
                startActivity(i2);
            }
        });
        new GetCoursesAsyncTask().execute(Url);
    }

    public void getDataformMyApp(String url) {
//        (1)使用的方法 创建一个请求队列
        RequestQueue queue = Volley.newRequestQueue(getActivity());
//       (2)使用相应的请求需求
        JsonObjectRequest jsrequest = new JsonObjectRequest(url + user.getUserid(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int status = response.optInt("status");
                if (status == 200) {
//                    开始解析返回来的Json数组。
                    try {
                        list.clear();
                        JSONArray content = response.optJSONArray("content");
                        for (int i = 0; i < content.length(); i++) {
                            JSONObject data = content.optJSONObject(i);
                            if (data != null) {

                                JSONObject fields = data.getJSONObject("fields");
                                int cid = data.optInt("pk");
                                String classroom = fields.optString("classroom");
                                String cname = fields.optString("cname");
                                String teacher = fields.optString("teacher");
                                String term = fields.optString("term");
                                String schoolYear = fields.optString("schoolYear");
                                Double credit = fields.optDouble("credit");
                                int dayOfWeek = fields.optInt("dayOfWeek");
                                int endSection = fields.optInt("endSection");
                                int intstartSection = fields.optInt("intstartSection");
                                int startWeek = fields.optInt("startWeek");
                                int endWeek = fields.optInt("endWeek");
                                int uid = fields.optInt("uid");
                                CourseModel course = new CourseModel(uid, cid, cname, schoolYear, term, credit, intstartSection, endSection, startWeek, endWeek, dayOfWeek, classroom, teacher);
                                list.add(course);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (status == 400) {
                    TastyToast.makeText(getActivity(), "你还没有任何课程哦", 100,
                            TastyToast.INFO);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                TastyToast.makeText(getActivity(), volleyError + "出错了！好崩溃", TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR);
            }
        });
//       （3）将请求加入到队列之中
        queue.add(jsrequest);
    }

    public class GetCoursesAsyncTask extends AsyncTask<String, Void, Void> {
        //在线程之中所执行的方法
        @Override
        protected Void doInBackground(String... params) {
            getDataformMyApp(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mTimetable.loadCourses(list);
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onStart() {
        new GetCoursesAsyncTask().execute(Url);
        super.onStart();
    }
}
