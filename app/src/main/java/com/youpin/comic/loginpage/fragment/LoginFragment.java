package com.youpin.comic.loginpage.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.youpin.comic.loginpage.activity.ForgetPWDActivity;
import com.youpin.comic.loginpage.bean.LoginUserBean;
import com.youpin.comic.loginpage.event.LoginPageEvents;
import com.youpin.comic.loginpage.manager.LoginPageManager;
import com.youpin.comic.openapi.AccessTokenKeeper;
import com.youpin.comic.openapi.AccessTokenKeeper4Tencent;
import com.youpin.comic.openapi.AccessTokenKeeper4Wechat;
import com.youpin.comic.openapi.SinaOpenApi;
import com.youpin.comic.openapi.TencentOpenApi;
import com.youpin.comic.openapi.WixinChatApi;
import com.youpin.comic.publicevent.EventBusUtils;
import com.youpin.comic.publicutils.MineUtils;
import com.youpin.comic.publicutils.ProtocolDectorDialog;
import com.youpin.comic.publicviews.MyImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by hantao on 2018/5/2.
 */

public class LoginFragment extends StepFragment {

    EditText edit_tel,edit_password;
    Button btn_login;
    TextView tv_forgot_pwd,tv_login_wing;
    MyImageView iv_wxlogin,iv_qqlogin,iv_sinalogin;
    private ProtocolDectorDialog protocolDectorDialog;
    public static final String MARK ="LoginFragment";
    /**
     * 腾讯开放API
     */
    private TencentOpenApi mTencentOpenApi;
    /**
     * 新浪开放API
     */
    private SinaOpenApi mSinaOpenApi;
    /**
     * 微信开放API
     */
    private WixinChatApi wixinChatApi;

    /**
     * 微信登录广播接收器
     **/
    private WechartBroadCastReceiver wechartBroadCastReceiver;

    @Override
    protected void onHandleMessage(Message msg) {
        switch (msg.what) {
            //获取到了QQ的AccessToken
            case TencentOpenApi.MSG_WHAT_QQTOKEN:
                onAccessTokenPrepared(2, AccessTokenKeeper4Tencent.readAccessToken(getActivity()).getOpenid(), AccessTokenKeeper4Tencent.readAccessToken(getActivity()).getAccess_token(),TencentOpenApi.APP_ID);
                break;
            //获取到了新浪的AccessToken
            case SinaOpenApi.MSG_WHAT_SINATOKEN:
                onAccessTokenPrepared(3, AccessTokenKeeper.readAccessToken(getActivity()).getUid(), AccessTokenKeeper.readAccessToken(getActivity()).getToken(),"");
                break;
            //微信得到了AccessToken,这里注意的是:这里没有uid,我们向  onAccessTokenPrepared 里传的是openid
            case WixinChatApi.MSG_WHAT_WECHATTOKEN:
                onAccessTokenPrepared(1, AccessTokenKeeper4Wechat.readAccessToken(getActivity()).getOpenid(), AccessTokenKeeper4Wechat.readAccessToken(getActivity()).getAccess_token(),"");
                break;
            default:
                break;
        }
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
            v = inflater.inflate(R.layout.fragment_login, null);
        }
        return v;
    }

    @Override
    protected void findViews() {
        edit_tel=(EditText) v.findViewById(R.id.edit_tel);
        edit_password=(EditText) v.findViewById(R.id.edit_password);
        btn_login=(Button) v.findViewById(R.id.btn_login);
        tv_forgot_pwd=(TextView) v.findViewById(R.id.tv_forgot_pwd);
        tv_login_wing=(TextView) v.findViewById(R.id.tv_login_wing);
        iv_wxlogin=(MyImageView) v.findViewById(R.id.iv_wxlogin);
        iv_qqlogin=(MyImageView) v.findViewById(R.id.iv_qqlogin);
        iv_sinalogin=(MyImageView) v.findViewById(R.id.iv_sinalogin);
    }

    @Override
    protected void initData() {

        mTencentOpenApi = new TencentOpenApi(getActivity(), getDefaultHandler());
        mSinaOpenApi = new SinaOpenApi(getActivity(), getDefaultHandler());
        wixinChatApi = new WixinChatApi(getActivity(), getDefaultHandler());

        //注册微信登录的监听
        wechartBroadCastReceiver = new WechartBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WixinChatApi.BOADCATST_WECHAT_GETCODE);
        getActivity().registerReceiver(wechartBroadCastReceiver, filter);

    }

    private void loadData(final boolean more) {

    }

    @Override
    protected void setListener() {
        tv_login_wing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv_forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ForgetPWDActivity.class);
                startActivity(intent);
            }
        });
        iv_wxlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_wxlogin();
            }
        });
        iv_qqlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_qqlogin();
            }
        });
        iv_sinalogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_sinalogin();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();

            }
        });

    }
    private void doLogin(){
        String tel = edit_tel.getText().toString();
        if (!MineUtils.isPhoneNumber(tel)) {
            Toast.makeText(getStepActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwd = edit_password.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(getStepActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        showOprationDialogFragment("");
        LoginPageManager.getInstance().doLoginPost(tel,pwd,MARK);
    }
    @Override
    public void free() {
        EventBusUtils.unregister(this);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    /**
     * 新浪登录
     */
    private void iv_sinalogin() {
        mSinaOpenApi.getAccessToken();
    }

    /**
     * qq登录
     */
    private void iv_qqlogin() {
        mTencentOpenApi.getAccessToken();
    }

    /**
     * 微信登录
     */
    private void iv_wxlogin() {
        if (protocolDectorDialog == null) {
            protocolDectorDialog = ProtocolDectorDialog.getDector(getActivity(), ProtocolDectorDialog.STYLE.NO_CLOSE_TXT);
        }
        protocolDectorDialog.show();
        wixinChatApi.login();
    }
    /**
     * 接受传递过来的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginPageEvents event) {
        if (event!=null&&MARK.equals(event.getMark())) {
            cancelOprationDialogFragment();
            Toast.makeText(getStepActivity(), event.getMessage(), Toast.LENGTH_SHORT).show();
            if (event!=null&&event.isSuccess()) {
                LoginUserBean loginUserBean=event.getLoginUserBean();
                free();
            }
        }
    }


    String unionid="";
    /**
     * 这里的是微信登录操作,因为微信登录的回调是在Activity中,我们直接使用listener的方式去监听不太友好,
     * 所以,除了QQ和新浪,使用Listenner的回调方式,微信,我们决定使用接收广播来处理登录
     */
    class WechartBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取到微信code
            if (intent.getAction().equals(WixinChatApi.BOADCATST_WECHAT_GETCODE)) {
                String code = intent.getStringExtra(WixinChatApi.INTENT_EXTRA_CODE);
                //https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
//                HttpUrlTypeUserProtocol userProtocol = new HttpUrlTypeUserProtocol(getActivity(), URL_ENUM.HttpUrlTypeWechatAccessToken);
//                userProtocol.enableDefaultLoading(STYLE.NO_CLOSE_TXT, null, null, false);
//                Bundle info = new Bundle();
//                info.putString("appid", WXEntryActivity.APP_ID);
//                info.putString("secret", WXEntryActivity.APP_SECRET);
//                info.putString("code", code);
//                info.putString("grant_type", "authorization_code");
//
//                userProtocol.runProtocol(null, info, new OnSuccessListener() {
//                    @Override
//                    public void onSuccess(Object response) {
//                        if (response instanceof JSONObject) {
//                            JSONObject resp = (JSONObject) response;
//                            /*String access_token = resp.optString("access_token");
//							String refresh_token = resp.optString("refresh_token");
//							String openid = resp.optString("openid");*/
//                            unionid=resp.optString("unionid");
//                            if (resp.has("access_token")) {
//                                AccessTokenKeeper4Wechat.writeAccessToken(getActivity(), resp);
//                                getDefaultHandler().sendEmptyMessage(WixinChatApi.MSG_WHAT_WECHATTOKEN);
//                            } else {
//                                //TODO 获取失败
//                            }
//                        }
//                        //获取用户信息,由服务器处理,但是比新浪和QQ多了一个openid,这一项和李杰商量一下
//                        //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
//                    }
//                }, new OnFailedListener() {
//                    @Override
//                    public void onFailed(Object response) {
//                    }
//                });

            }
        }
    }

    private void onAccessTokenPrepared(final int sign, final String openid, final String accesstoken, final String appkey) {
        LoginPageManager.getInstance().doOtherLoginPost(appkey,openid,accesstoken,sign,MARK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTencentOpenApi != null) {
            mTencentOpenApi.onActivityResult(requestCode, resultCode, data);
        }
        if (mSinaOpenApi != null) {
            mSinaOpenApi.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
