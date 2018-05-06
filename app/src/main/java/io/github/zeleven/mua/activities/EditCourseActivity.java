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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.zeleven.mua.Model.CourseModel;
import io.github.zeleven.mua.R;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
public class EditCourseActivity extends Activity {
    @BindView(R.id.editcname)
    EditText editcname;
    @BindView(R.id.editschoolYear)
    EditText editschoolYear;
    @BindView(R.id.editterm)
    EditText editterm;
    @BindView(R.id.editcredit)
    EditText editcredit;
    @BindView(R.id.editintstartSection)
    EditText editintstartSection;
    @BindView(R.id.editendSection)
    EditText editendSection;
    @BindView(R.id.editstartWeek)
    EditText editstartWeek;
    @BindView(R.id.editendWeek)
    EditText editendWeek;
    @BindView(R.id.editdayOfWeek)
    EditText editdayOfWeek;
    @BindView(R.id.editclassroom)
    EditText editclassroom;
    @BindView(R.id.editteacher)
    EditText editteacher;
    @BindView(R.id.editcourse)
    Button editcourse;
    private CourseModel course;
    private String Url="http://xzacjy.pythonanywhere.com/myapp/updatecourseinfo";
    private String cnametext, schoolYeartext, termtext, credittext, intstartSectiontext, endSectiontext;
    private String startWeektext, endWeektext, dayOfWeektext, classroomtext, teachertext;
    private int startSectionint, endSectionint, startWeekint, endWeekint, dayOfWeekint;
    private Double creditdouble;
    private Integer [] array = {1, 2, 3, 4, 5, 6, 7};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        ButterKnife.bind(this);
        course = (CourseModel) getIntent().getSerializableExtra("course_data");
//        Toast.makeText(this,course.toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,course.getCid()+"", Toast.LENGTH_SHORT).show();
        initView();
    }

    public void initView() {
        editcname.setText(course.getCname());
        editschoolYear.setText(course.getSchoolYear());
        editterm.setText(course.getTerm());
        editcredit.setText(course.getCredit().toString());
        editintstartSection.setText(course.getStartSection()+"");
        editendSection.setText(course.getEndSection()+"");
        editstartWeek.setText(course.getStartWeek()+"");
        editendWeek.setText(course.getEndWeek()+"");
        editdayOfWeek.setText(course.getDayOfWeek()+"");
        editclassroom.setText(course.getClassroom());
        editteacher.setText(course.getTeacher());
    }

    //按钮的点击事件
    @OnClick(R.id.editcourse)
    public void onViewClicked() {
        if (preEdit()) {
            new EditCoursesAsyncTask().execute(Url);
        }
    }

    private boolean preEdit() {
        cnametext = editcname.getText().toString().trim();
        schoolYeartext = editschoolYear.getText().toString().trim();
        termtext = editterm.getText().toString().trim();
        credittext = editcredit.getText().toString().trim();
        intstartSectiontext = editintstartSection.getText().toString().trim();
        endSectiontext = editendSection.getText().toString().trim();
        startWeektext = editstartWeek.getText().toString().trim();
        endWeektext = editendWeek.getText().toString().trim();
        dayOfWeektext = editdayOfWeek.getText().toString().trim();
        teachertext = editteacher.getText().toString().trim();
        classroomtext = editclassroom.getText().toString().trim();
        if (TextUtils.isEmpty(cnametext) || TextUtils.isEmpty(schoolYeartext)
                || TextUtils.isEmpty(termtext) || TextUtils.isEmpty(credittext)
                || TextUtils.isEmpty(intstartSectiontext) || TextUtils.isEmpty(endSectiontext)
                || TextUtils.isEmpty(startWeektext) || TextUtils.isEmpty(endWeektext)
                || TextUtils.isEmpty(dayOfWeektext) || TextUtils.isEmpty(classroomtext)
                || TextUtils.isEmpty(teachertext)
                ) {
            TastyToast.makeText(EditCourseActivity.this, "请确保所有的字段不为空哦", TastyToast.LENGTH_SHORT,
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
                TastyToast.makeText(EditCourseActivity.this, "请确保所有的字段有效", TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR);
                return false;
            }
            boolean isContains = Arrays.asList(array).contains(dayOfWeekint);
            if (!isContains) {
                TastyToast.makeText(EditCourseActivity.this, "上课星期请确保到1到7之间", TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            TastyToast.makeText(EditCourseActivity.this, "请确保所有的字段有效", TastyToast.LENGTH_SHORT,
                    TastyToast.ERROR);
            return false;
        }
        return true;
    }

    private void EditCourse(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求
        Map<String, Object> map = new HashMap<>();
        map.put("cid",course.getCid());
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
        JSONObject paramJsonObject1 = new JSONObject(map);
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, url, paramJsonObject1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status = response.optInt("status");
//                判断注册的状态
                        if (status == 200) {
                            TastyToast.makeText(EditCourseActivity.this, "修改课程成功", TastyToast.LENGTH_SHORT,
                                    TastyToast.SUCCESS);
                            finish();
                        } else if (status == 400) {
                            TastyToast.makeText(EditCourseActivity.this, "修改课程失败", TastyToast.LENGTH_SHORT,
                                    TastyToast.ERROR);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                TastyToast.makeText(EditCourseActivity.this, "出错原因" + volleyError, TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request1);
    }
    public class EditCoursesAsyncTask extends AsyncTask<String, Void, Void> {
        //在线程之中所执行的方法
        @Override
        protected Void doInBackground(String... params) {
            EditCourse(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
