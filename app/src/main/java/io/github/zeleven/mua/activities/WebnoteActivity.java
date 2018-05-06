package io.github.zeleven.mua.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

import io.github.zeleven.mua.R;

public class WebnoteActivity extends Activity {

    private AgentWeb mAgentWeb;
    private LinearLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webnote);
        view=(LinearLayout) findViewById(R.id.webview);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(view, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go("http://xzacjy.pythonanywhere.com/myapp/login");
    }
    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}
