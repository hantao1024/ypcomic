package com.youpin.comic.loginpage.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.youpin.comic.R;
import com.youpin.comic.base.StepFragment;
import com.youpin.comic.loginpage.bean.RegisterBean;
import com.youpin.comic.loginpage.event.CaptchaEvents;
import com.youpin.comic.loginpage.event.RegisterPageEvents;
import com.youpin.comic.loginpage.manager.LoginPageManager;
import com.youpin.comic.publicevent.EventBusUtils;
import com.youpin.comic.publicutils.MineUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by hantao on 2018/5/2.
 */

public class RegisterFragment extends StepFragment {


    EditText edit_register_tel,edit_set_verification_code,edit_password;
    TextView edit_get_verification_code;
    Button btn_register;
    MyCountDownTimer myCountDownTimer;
    public static final String MARK ="RegisterFragment";
    @Override
    protected void onHandleMessage(Message msg) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
    }

    View v;

    @Override
    protected View createContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_register, null);
        }
        return v;
    }

    @Override
    protected void findViews() {
        edit_register_tel=(EditText) v.findViewById(R.id.edit_register_tel);
        edit_set_verification_code=(EditText) v.findViewById(R.id.edit_set_verification_code);
        edit_password=(EditText) v.findViewById(R.id.edit_password);
        btn_register=(Button) v.findViewById(R.id.btn_register);
        edit_get_verification_code=(TextView) v.findViewById(R.id.edit_get_verification_code);
    }

    @Override
    protected void initData() {
        //new倒计时对象,总共的时间,每隔多少秒更新一次时间
        myCountDownTimer = new MyCountDownTimer(60000, 1000);
    }

    private void loadData(final boolean more) {

    }

    @Override
    protected void setListener() {
        edit_get_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = edit_register_tel.getText().toString();
                if (MineUtils.isPhoneNumber(tel)) {
                    LoginPageManager.getInstance().doCaptcha(tel,MARK);
                } else {
                    Toast.makeText(getStepActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               doGetRegister();
            }
        });
    }

    private void doGetRegister(){
        String tel = edit_register_tel.getText().toString();
        if (!MineUtils.isPhoneNumber(tel)) {
            Toast.makeText(getStepActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        String captcha = edit_set_verification_code.getText().toString();
        if (TextUtils.isEmpty(captcha)) {
            Toast.makeText(getStepActivity(), "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwd = edit_password.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(getStepActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        showOprationDialogFragment("");
        LoginPageManager.getInstance().doRegisterPost(tel,pwd,captcha);
    }

    @Override
    public void free() {
        EventBusUtils.unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    /**
     * 接受传递过来的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CaptchaEvents event) {
        if (event!=null&&MARK.equals(event.getMark())) {
            if (event.isSuccess()) {
                edit_set_verification_code.setText(event.getData());
                if (myCountDownTimer != null) {
                    myCountDownTimer.start();
                }
            } else {
                if (myCountDownTimer != null) {
                    refreshCode();
                }
            }
        }
    }
    /**
     * 接受传递过来的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RegisterPageEvents event) {
        if (event!=null) {
            cancelOprationDialogFragment();
            Toast.makeText(getStepActivity(), event!=null?event.getMessage():"注册失败", Toast.LENGTH_SHORT).show();
            if (event!=null&&event.isSuccess()) {
                RegisterBean registerBean=event.getRegisterBean();

            }
        }
    }
    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            edit_get_verification_code.setTextColor(getStepActivity().getResources().getColor(R.color.comm_gray_low));
            edit_get_verification_code.setClickable(false);
            edit_get_verification_code.setText(l / 1000 + "s");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            edit_get_verification_code.setTextColor(getStepActivity().getResources().getColor(R.color.game_blue));
            //重新给Button设置文字
            edit_get_verification_code.setText("重新获取验证码");
            //设置可点击
            edit_get_verification_code.setClickable(true);
        }
    }

    private void refreshCode(){
        try {
            if (myCountDownTimer!=null) {
                myCountDownTimer.cancel();
            }
            edit_get_verification_code.setTextColor(getStepActivity().getResources().getColor(R.color.game_blue));
            //重新给Button设置文字
            edit_get_verification_code.setText("获取验证码");
            //设置可点击
            edit_get_verification_code.setClickable(true);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}
