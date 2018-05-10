package com.youpin.comic.loginpage.dao;

import com.youpin.comic.CApplication;
import com.youpin.comic.green.gen.LoginUserBeanDao;
import com.youpin.comic.loginpage.bean.LoginUserBean;

import java.util.List;


/**
 * Created by handsome on 2016/4/19.
 */
public class LoginDao {


    /**
     * 添加数据
     *
     * @param loginUserBean
     */
    public static void insertUser(LoginUserBean loginUserBean) {
        CApplication.getDaoInstant().getLoginUserBeanDao().insert(loginUserBean);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteUser(LoginUserBean loginUserBean) {
        CApplication.getDaoInstant().getLoginUserBeanDao().delete(loginUserBean);
    }

    /**
     * 删除数据
     *
     */
    public static void deleteAll() {
        CApplication.getDaoInstant().getLoginUserBeanDao().deleteAll();
    }
    /**
     * 更新数据
     *
     * @param loginUserBean
     */
    public static void updateUser(LoginUserBean loginUserBean) {
        CApplication.getDaoInstant().getLoginUserBeanDao().update(loginUserBean);
    }

    /**
     * 查询所有的数据
     *
     * @return
     */
    public static List<LoginUserBean> queryUser() {
        return CApplication.getDaoInstant().getLoginUserBeanDao().queryBuilder().list();
    }

    /**
     * 查询活跃数据
     *
     * @return
     */
    public static LoginUserBean queryUserOne() {
        return CApplication.getDaoInstant().getLoginUserBeanDao().queryBuilder().where(LoginUserBeanDao.Properties.Status.eq(LoginUserBean.STATUS_ONLINE)).unique();
    }
}
