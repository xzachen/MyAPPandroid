package io.github.zeleven.mua.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.zeleven.mua.Model.AppUser;
import io.github.zeleven.mua.Model.User;
import io.github.zeleven.mua.R;

public class Main2Activity extends AppCompatActivity {
    public String Url = "http://xzacjy.pythonanywhere.com/myapp/loginbyapp";
    private EditText etUsername;
    private EditText etPassword;
    private TextView loginwithQQ;
    private Button btGo;
    private CardView cv;
    private FloatingActionButton fab;
    //QQ登录的接入控件。
    private static final String TAG = "Main2Activity";
    //    QQ官方接入的ID号码
    private static final String APP_ID = "1106671771";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private User user;

    //
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        setListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fab.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
    }


    private void initView() {

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btGo = (Button) findViewById(R.id.bt_go);
        cv = (CardView) findViewById(R.id.cv);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        loginwithQQ = (TextView) findViewById(R.id.loginwithQQ);
        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID, Main2Activity.this.getApplicationContext());

    }


    private void setListener() {
        btGo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            使用账号加密码的模式登录
            @Override
            public void onClick(View view) {
                if (submit()) {
//                   这里进行登录
                    Explode explode = new Explode();
                    explode.setDuration(100);
                    getWindow().setExitTransition(explode);
                    getWindow().setEnterTransition(explode);
//                    执行登录的异步任务
                    new myLoginAsyncTask().execute(Url);
                }

            }
        });
        loginwithQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Explode explode = new Explode();
//                explode.setDuration(100);
//                getWindow().setExitTransition(explode);
//                getWindow().setEnterTransition(explode);
//                new LoginAsyncTask().execute();
            }
        });

//        打开另外一个页面
        fab.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Main2Activity.this, fab, fab.getTransitionName());
                startActivity(new Intent(Main2Activity.this, RegisterActivity.class), options.toBundle());
            }
        });
    }

    //    登录的方法
    public void Login(String url) {
//        (1)使用的方法 创建一个请求队列
        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status = response.optInt("status");
//                登录成功，跳转到其他的界面并且对用户进行存储。
                        if (status == 200) {
//                            获取返回的JSON数据
                            JSONObject content = response.optJSONObject("content");
                            String password = content.optString("password");
                            String username = content.optString("username");
                            int userid = content.optInt("userid");
//                            创建一个用户并且传递给Homeactivity。
                            AppUser Appuser = new AppUser(userid, username, password);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(Main2Activity.this);
                            Intent i2 = new Intent(Main2Activity.this, HomeActivity.class);
//                     这里吧用户的数据传入给HomeActivity。
//                     传递好之后settingFragemet跟新视图。
                            i2.putExtra("person_data", Appuser);
                            startActivity(i2, oc2.toBundle());
                        } else if (status == 400) {
                            TastyToast.makeText(Main2Activity.this, "用户名和密码出错", TastyToast.LENGTH_SHORT,
                                    TastyToast.ERROR);
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

    //QQ登录监听器
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                Toast.makeText(Main2Activity.this, openID.toString(), Toast.LENGTH_SHORT).show();
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        JSONObject info = (JSONObject) response;
                        try {
                            String nickName = info.getString("nickname");//获取用户昵称
                            String iconUrl = info.getString("figureurl_qq_2");//获取用户头像的url
                            user = new User(nickName, iconUrl);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(Main2Activity.this);
                            Intent i2 = new Intent(Main2Activity.this, HomeActivity.class);
//                     这里吧用户的数据传入给HomeActivity。
//                     传递好之后settingFragemet跟新视图。
                            i2.putExtra("person_data", user);
                            startActivity(i2, oc2.toBundle());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Toast.makeText(Main2Activity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Main2Activity.this, "登录取消", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(Main2Activity.this, "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(Main2Activity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

    }


    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //    简单的登录验证
    private boolean submit() {
        // validate
        username = etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "用户密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    public class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            mIUiListener = new BaseUiListener();
            mTencent.login(Main2Activity.this, "all", mIUiListener);
            return null;
        }
    }

    //
    public class myLoginAsyncTask extends AsyncTask<String, Void, Void> {
        //在线程之中所执行的方法
        @Override
        protected Void doInBackground(String... params) {
            Login(params[0]);
            return null;
        }
    }

}
