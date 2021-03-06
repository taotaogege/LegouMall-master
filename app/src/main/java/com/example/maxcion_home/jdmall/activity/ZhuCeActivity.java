package com.example.maxcion_home.jdmall.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.maxcion_home.jdmall.R;
import com.example.maxcion_home.jdmall.R;
import com.example.maxcion_home.jdmall.bean.RResult;
import com.example.maxcion_home.jdmall.cons.IdiyMessage;
import com.example.maxcion_home.jdmall.controls.UserController;
import com.example.maxcion_home.jdmall.receiver.NetWorkStateReceiver;
import com.example.maxcion_home.jdmall.util.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZhuCeActivity extends BaseActivity {

    @BindView(R.id.til_username_register)
    TextInputLayout mUserName ;
    @BindView(R.id.til_pwd_register)
    TextInputLayout mPwd ;
    @BindView(R.id.til_repwd_register)
    TextInputLayout mRepwd ;
    private NetWorkStateReceiver mNetWorkStateReceiver;

    @OnClick(R.id.bt_registClick) void registClick(){
        String userName = mUserName.getEditText().getText().toString();
        String pwd = mPwd.getEditText().getText().toString();
        String rePwd = mRepwd.getEditText().getText().toString();
        if (TextUtils.isEmpty(userName)||TextUtils.isEmpty(pwd)||TextUtils.isEmpty(rePwd)){
            tip("请输入完整信息");
            return;
        }
        if (!pwd.equals(rePwd)){
            tip("两次密码不相同");
            return;
        }
        UserController userControl = new UserController(this);
        userControl.setIModeChangeListener(this);
        userControl.sendAsyncMessage(IdiyMessage.REGIST_ACTION,userName,pwd);
    }

    @Override
    protected void handlerMessage(Message msg) {
        if (NetworkUtils.isNetworkAvalible(this) == false) {
            Toast.makeText(this, "没网", Toast.LENGTH_SHORT).show();
            Wangluo();
        } else {
            if (msg.what == IdiyMessage.REGIST_ACTION_RESULT) {
                RResult rResult = (RResult) msg.obj;
                if (rResult.success) {
                    tip("注册成功");
                    finish();
                } else {
                    tip("注册失败:" + rResult.errorMsg);
                }
            }
        }
    }

    private void Wangluo() {
        if (mNetWorkStateReceiver == null) {
            mNetWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetWorkStateReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
    }
}
