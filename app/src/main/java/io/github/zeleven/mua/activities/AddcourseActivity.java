package io.github.zeleven.mua.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.zeleven.mua.Model.AppUser;
import io.github.zeleven.mua.R;

public class AddcourseActivity extends Activity {
    @BindView(R.id.cname)
    EditText cname;
    @BindView(R.id.schoolYear)
    EditText schoolYear;
    @BindView(R.id.term)
    EditText term;
    @BindView(R.id.credit)
    EditText credit;
    @BindView(R.id.intstartSection)
    EditText intstartSection;
    @BindView(R.id.endSection)
    EditText endSection;
    @BindView(R.id.startWeek)
    EditText startWeek;
    @BindView(R.id.endWeek)
    EditText endWeek;
    @BindView(R.id.dayOfWeek)
    EditText dayOfWeek;
    @BindView(R.id.classroom)
    EditText classroom;
    @BindView(R.id.teacher)
    EditText teacher;
    @BindView(R.id.addclass)
    Button addclass;
    private AppUser user;
    private String URL = "http://xzacjy.pythonanywhere.com/myapp/createcourse";
    private String cnametext, schoolYeartext, termtext, credittext, intstartSectiontext, endSectiontext;
    private String startWeektext, endWeektext, dayOfWeektext, classroomtext, teachertext;
    private int startSectionint, endSectionint, startWeekint, endWeekint, dayOfWeekint;
    private Double creditdouble;
    private Integer [] array = {1, 2, 3, 4, 5, 6, 7};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcourse_layout);
        ButterKnife.bind(this);
        user = (AppUser) getIntent().getSerializableExtra("person_data");
    }

    public boolean preSubimt() {
        cnametext = cname.getText().toString().trim();
        schoolYeartext = schoolYear.getText().toString().trim();
        termtext = term.getText().toString().trim();
        credittext = credit.getText().toString().trim();
        intstartSectiontext = intstartSection.getText().toString().trim();
        endSectiontext = endSection.getText().toString().trim();
        startWeektext = startWeek.getText().toString().trim();
        endWeektext = endWeek.getText().toString().trim();
        dayOfWeektext = dayOfWeek.getText().toString().trim();
        teachertext = teacher.getText().toString().trim();
        classroomtext = classroom.getText().toString().trim();
        if (TextUtils.isEmpty(cnametext) || TextUtils.isEmpty(schoolYeartext)
                || TextUtils.isEmpty(termtext) || TextUtils.isEmpty(credittext)
                || TextUtils.isEmpty(intstartSectiontext) || TextUtils.isEmpty(endSectiontext)
                || TextUtils.isEmpty(startWeektext) || TextUtils.isEmpty(endWeektext)
                || TextUtils.isEmpty(dayOfWeektext) || TextUtils.isEmpty(classroomtext)
                || TextUtils.isEmpty(teachertext)
                ) {
            TastyToast.makeText(AddcourseActivity.this, "请确保所有的字段不为空哦", TastyToast.LENGTH_SHORT,
                    TastyToast.ERROR);
            return false;
        }
//            如果已经有了数据需要判断一下数据的合法性。
        try {
            startSectionint = Integer.parseInt(intstartSectiontext);
            endSectionint = Integer.parseInt(endSectiontext);
            startWeekint = Integer.parseInt(startWeektext);
            endWeekint = Integer.parseInt(endWeektext);
            dayOfWeekint = Integer.parseInt(dayOfWeektext);
            creditdouble = Double.parseDouble(credittext);
            if (startSectionint > endSectionint || startWeekint > endWeekint) {
                TastyToast.makeText(AddcourseActivity.this, "请确保所有的字段有效", TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR);
                return false;
            }
            boolean isContains = Arrays.asList(array).contains(dayOfWeekint);
            if (!isContains) {
                TastyToast.makeText(AddcourseActivity.this, "请确保上课星期是到1到7之间", TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            TastyToast.makeText(AddcourseActivity.this, "请确保所有的字段有效", TastyToast.LENGTH_SHORT,
                    TastyToast.ERROR);
            return false;
        }
        return true;
    }

    public void CraeteCourse(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求

        Map<String, Object> map = new HashMap<>();
        map.put("uid", user.getUserid());
        map.put("cname", cnametext);
        map.put("schoolYear", schoolYeartext);
        map.put("term", termtext);
        map.put("credit", creditdouble);
        map.put("intstartSection", startSectionint);
        map.put("endSection", endSectionint);
        map.put("startWeek", startWeekint);
        map.put("endWeek", endWeekint);
        map.put("dayOfWeek", dayOfWeekint);
        map.put("classroom", classroomtext);
        map.put("teacher", teachertext);
        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status = response.optInt("status");
//                判断注册的状态
                        if (status == 200) {
                            TastyToast.makeText(AddcourseActivity.this, "添加课程成功", TastyToast.LENGTH_SHORT,
                                    TastyToast.SUCCESS);
                            finish();
                        } else if (status == 400) {
                            TastyToast.makeText(AddcourseActivity.this, "添加课程失败", TastyToast.LENGTH_SHORT,
                                    TastyToast.ERROR);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                TastyToast.makeText(AddcourseActivity.this, "出错原因" + volleyError, TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);
    }

    //按钮点击事件
    @OnClick(R.id.addclass)
    public void onViewClicked() {
        if (preSubimt()) {
            new CreateCoursesAsyncTask().execute(URL);
        }
    }

    public class CreateCoursesAsyncTask extends AsyncTask<String, Void, Void> {
        //在线程之中所执行的方法
        @Override
        protected Void doInBackground(String... params) {
            CraeteCourse(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
