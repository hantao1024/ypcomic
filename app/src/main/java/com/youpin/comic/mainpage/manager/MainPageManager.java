package com.youpin.comic.mainpage.manager;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.youpin.comic.CApplication;
import com.youpin.comic.mainpage.events.HomePageEvents;
import com.youpin.comic.mainpage.events.NewPageEvents;
import com.youpin.comic.publichttp.OkhttpHelper;
import com.youpin.comic.publicmanager.BaseManager;
import com.youpin.comic.constant.AppConfig;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 主页管理类
 * Created by hantao on 2018/2/7.
 */
public class MainPageManager extends BaseManager {
    private static final String TAG = "MainPageManager";
    private static MainPageManager instance;


    public MainPageManager() {
        instance = this;
    }

    public static MainPageManager getInstance() {
        return instance;
    }

    private boolean isRelease(){
        return AppConfig.RELEASE;
    }
    /**
     * @param url
     * @param myNetCall
     */
    public void doGet(final boolean isLoadmore,final String url,final OkhttpHelper.MyNetCall myNetCall) {
        if (!isRelease()) {
            Logger.d(url);
        }
        CApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取网络工具类实例
                OkhttpHelper netUtils = OkhttpHelper.getInstance();
                netUtils.getDataAsynFromNet(url, new OkhttpHelper.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        final String res;
                        try {
                            res = response.body().string();
//                            User user=new User();
//                            user.setName((String)res);
//                            user.setUrl("https://avatar.base.com/65/11/6511f02f4f8c737914f75f5aefc96196.png");
//                            UserNameDao.insertUser(user);
                            EventBus.getDefault().post(HomePageEvents.pullSuccess(isLoadmore,true,res));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Call call, IOException e) {

                    }
                });
            }
        });
    }

    /**
     * @param url
     * @param myNetCall
     */
    public void doGetThree(final boolean isLoadmore,final String url,final OkhttpHelper.MyNetCall myNetCall) {
        if (!isRelease()) {
            Logger.d(url);
        }
        CApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取网络工具类实例
                OkhttpHelper netUtils = OkhttpHelper.getInstance();
                netUtils.getDataAsynFromNet(url, new OkhttpHelper.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        final String res;
                        try {
                            res = response.body().string();
                            EventBus.getDefault().post(NewPageEvents.pullSuccess(isLoadmore,true,res));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Call call, IOException e) {

                    }
                });
            }
        });
    }


    /**
     * @param url
     * @param myNetCall
     */
    public void doGetTwo(final boolean isLoadmore,final String url,final OkhttpHelper.MyNetCall myNetCall) {
        if (!isRelease()) {
            Logger.d(url);
        }
        CApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取网络工具类实例
                OkhttpHelper netUtils = OkhttpHelper.getInstance();
                netUtils.getDataAsynFromNet(url, new OkhttpHelper.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        final String res;
                        try {
                            res = response.body().string();
                            EventBus.getDefault().post(HomePageEvents.pullSuccess(isLoadmore,true,res));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Call call, IOException e) {

                    }
                });
            }
        });
    }


    /**
     * @param url
     * @param myNetCall
     */
    public void doPost(final String url, final Map<String, String> bodyParams, final OkhttpHelper.MyNetCall myNetCall) {

//        //构造请求参数
//        Map<String, String> reqBody = new ConcurrentSkipListMap<>();
//        reqBody.put("num", "1");

        CApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取网络请求工具类实例
                OkhttpHelper netUtils = OkhttpHelper.getInstance();
                //提交数据
                netUtils.postDataAsynToNet("url", bodyParams, new OkhttpHelper.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        Log.i("tag", "success");
                        String result = response.body().string();
                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        Log.i("tag", "failed");
                    }
                });
            }
        });
    }
}
