package io.github.zeleven.mua.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.zeleven.mua.R;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public String Url = "http://xzacjy.pythonanywhere.com/myapp/register";
    private FloatingActionButton fab;
    private CardView cvAdd;
    private EditText et_username;
    private EditText et_password;
    private EditText et_repeatpassword;
    private Button bt_go;
    public String username, password, repeatpassword, qqnumberString, qqnameString;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ShowEnterAnimation();
        initView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        cvAdd = (CardView) findViewById(R.id.cv_add);
        et_username = (EditText) findViewById(R.id.et_username);
        et_username.setOnClickListener(this);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password.setOnClickListener(this);
        et_repeatpassword = (EditText) findViewById(R.id.et_repeatpassword);
        et_repeatpassword.setOnClickListener(this);
        bt_go = (Button) findViewById(R.id.bt_go);
        bt_go.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_go:
//                实现注册的方法
                if (submit()) {
                    new myAsyncTask().execute(Url);
                }
                break;
        }
    }

    //简单的数据验证

    private boolean submit() {
        // validate
        username = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "用户密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        repeatpassword = et_repeatpassword.getText().toString().trim();
        if (TextUtils.isEmpty(repeatpassword)) {
            Toast.makeText(this, "重复密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
//        判断两次输入的密码值是否一样一样的话就
        if (!(password.equals(repeatpassword))) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    //实现异步注册的,成功之后返回到登录界面
    public void Register(String url) {
//       (1)使用的方法 创建一个请求队列

        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status = response.optInt("status");
//                判断注册的状态
                        if (status == 200) {
                            Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                            onBackPressed();
//                    QQ名字或者账号重复
                        } else if (status == 500) {
                            Toast.makeText(getApplicationContext(), "用户名重复或者QQ号码已经绑定", Toast.LENGTH_SHORT).show();
//                    出错了
                        } else if (status == 400) {
                            Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);
    }

    public class myAsyncTask extends AsyncTask<String, Void, Void> {
//在线程之中所执行的方法
        @Override
        protected Void doInBackground(String... params) {
            Register(params[0]);
            return null;
        }
    }
}
