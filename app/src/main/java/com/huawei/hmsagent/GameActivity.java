package com.huawei.hmsagent;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.CheckUpdateHandler;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.game.GameLoginSignUtil;
import com.huawei.android.hms.agent.game.handler.ICheckLoginSignHandler;
import com.huawei.android.hms.agent.game.handler.LoginHandler;
import com.huawei.android.hms.agent.game.handler.SaveInfoHandler;
import com.huawei.hms.support.api.entity.game.GamePlayerInfo;
import com.huawei.hms.support.api.entity.game.GameUserData;

public class GameActivity extends AgentBaseActivity {

    private static final String game_priv_key = IEvnValues.game_priv_key;
    private static final String game_public_key = IEvnValues.game_public_key;

    private static final String appId = IEvnValues.appId;
    private static final String cpId = IEvnValues.cpId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setTabBtnClickListener();
        Button btn = (Button) findViewById(R.id.btn_game);
        if (btn != null) {
            btn.setTextColor(Color.RED);
            btn.setEnabled(false);
        }


        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_show).setOnClickListener(this);
        findViewById(R.id.btn_hide).setOnClickListener(this);
        findViewById(R.id.btn_saveplayer).setOnClickListener(this);

        // 在首个界面，需要调用connect进行连接 | In the first page, you need to call connect
        HMSAgent.connect(this, new ConnectHandler() {
            @Override
            public void onConnect(int rst) {
                showLog("HMS connect end:" + rst);
            }
        });

        HMSAgent.checkUpdate(this, new CheckUpdateHandler() {
            @Override
            public void onResult(int rst) {
                showLog("check app update end:" + rst);
            }
        });
    }

    /**
     * 游戏登录示例 | Game Login Sample
     */
    private void login() {
        showLog("game login: begin");
        HMSAgent.Game.login(new LoginHandler() {
            @Override
            public void onResult(int retCode, GameUserData userData) {
                if (retCode == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS && userData != null) {
                    showLog("game login: onResult: retCode=" + retCode + "  user=" + userData.getDisplayName() + "|" + userData.getPlayerId() + "|" + userData.getIsAuth() + "|" + userData.getPlayerLevel());
                    // 当登录成功时，此方法会回调2次， | This method recalls 2 times when the login is successful.
                        // 第1次：只回调了playerid；特点：速度快；在要求快速登录，并且对安全要求不高时可以用此playerid登录 | 1th time: Only callback playerID; features: fast speed; You can log in with this playerID when you require fast logon and are not high on security requirements
                        // 第2次：回调了所有信息，userData.getIsAuth()为1；此时需要对登录结果进行验签 | 2nd time: Callback All information, Userdata.getisauth () is 1;
                    if (userData.getIsAuth() == 1) {
                        // 如果需要对登录结果进行验签，请发送请求到开发者服务器进行（安全起见，私钥要放在服务端保存）。| If you need to check your login results, send a request to the developer server (for security, the private key is stored on the service side).
                        // 下面工具方法仅为了展示验签请求的逻辑，实际实现应该放在开发者服务端。| The following tool method is intended only to show the logic of the verification request, and the actual implementation should be on the developer server.
                        GameLoginSignUtil.checkLoginSign(appId, cpId, game_priv_key, game_public_key, userData, new ICheckLoginSignHandler() {
                            @Override
                            public void onCheckResult(String code, String resultDesc, boolean isCheckSuccess) {
                                showLog("game login check sign: onResult: retCode=" + code + "  resultDesc=" + resultDesc + "  isCheckSuccess=" + isCheckSuccess);
                            }
                        });
                    }
                } else {
                    showLog("game login: onResult: retCode=" + retCode);
                }
            }

            @Override
            public void onChange() {
                // 此处帐号登录发生变化，需要重新登录 | Account login changed here, login required
                showLog("game login: login changed!");
                login();
            }
        }, 1);
    }

    /**
     * 游戏显示浮标示例 | Game Show Floating Indicator example
     */
    private void showfloat(){
        showLog("game showfloat");
        HMSAgent.Game.showFloatWindow(this);
    }

    /**
     * 游戏隐藏浮标示例 | Game Hidden Floating Indicator example
     */
    private void hidefloat(){
        showLog("game hidefloat");
        HMSAgent.Game.hideFloatWindow(this);
    }

    /**
     * 游戏保存玩家信息示例 | Game Save player Information sample
     */
    private void savePlayerInfo() {
        showLog("game savePlayerInfo: begin");
        GamePlayerInfo gpi = new GamePlayerInfo();
        gpi.area = "20";
        gpi.rank = "level 56";
        gpi.role = "hunter";
        gpi.sociaty = "Red Cliff II";
        HMSAgent.Game.savePlayerInfo(gpi, new SaveInfoHandler() {
            @Override
            public void onResult(int retCode) {
                showLog("game savePlayerInfo: onResult=" + retCode);
            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_game) {
            // 本页面切换到本页面的按钮事件不处理 | "This page switches to itself" button event does not need to be handled
            return;
        } else if (!onTabBtnClickListener(id)) {
            // 如果不是tab切换按钮则处理业务按钮事件 | Handle Business button events without the TAB toggle button
            switch (id) {
                case R.id.btn_login:
                    login();
                    break;
                case R.id.btn_show:
                    showfloat();
                    break;
                case R.id.btn_hide:
                    hidefloat();
                    break;
                case R.id.btn_saveplayer:
                    savePlayerInfo();
                    break;
                default:
            }
        }
    }
}
