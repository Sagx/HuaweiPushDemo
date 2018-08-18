package com.huawei.hmsagent;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public abstract class AgentBaseActivity extends Activity implements View.OnClickListener {

    /**
     * 设置页面切换按钮点击事件
     * Set page toggle button click event
     */
    protected void setTabBtnClickListener() {
        findViewById(R.id.btn_game).setOnClickListener(this);
        findViewById(R.id.btn_iap).setOnClickListener(this);
        findViewById(R.id.btn_id).setOnClickListener(this);
        findViewById(R.id.btn_sns).setOnClickListener(this);
        findViewById(R.id.btn_push).setOnClickListener(this);
        findViewById(R.id.btn_opendevice).setOnClickListener(this);
    }

    /**
     * 处理页面切换按钮点击事件
     * Process page Toggle Button click event
     * @param btnId 被点击的按钮的id | The ID of the clicked button
     * @return 是否已经处理 | has been processed
     */
    protected boolean onTabBtnClickListener(int btnId) {
        switch (btnId) {
            case R.id.btn_game:
                startActivity(new Intent(this, GameActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            case R.id.btn_iap:
                startActivity(new Intent(this, PayActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            case R.id.btn_id:
                startActivity(new Intent(this, HwIDActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            case R.id.btn_sns:
                startActivity(new Intent(this, SnsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            case R.id.btn_push:
                startActivity(new Intent(this, PushActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            case R.id.btn_opendevice:
                startActivity(new Intent(this, OpendeviceActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            default:
        }

        return false;
    }

    StringBuffer sbLog = new StringBuffer();
    protected void showLog(String logLine) {
        DateFormat format = new java.text.SimpleDateFormat("MMddhhmmssSSS");
        String time = format.format(new Date());

        sbLog.append(time+":"+logLine);
        sbLog.append('\n');
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                View vText = findViewById(R.id.tv_log);

                if (vText != null && vText instanceof TextView) {
                    TextView tvLog = (TextView)vText;
                    tvLog.setText(sbLog.toString());
                }

                View vScrool = findViewById(R.id.sv_log);
                if (vScrool != null && vScrool instanceof ScrollView) {
                    ScrollView svLog = (ScrollView)vScrool;
                    svLog.fullScroll(View.FOCUS_DOWN);
                }
            }
        });
    }
}
