package com.youpin.comic.green.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.youpin.comic.mainpage.bean.User;
import com.youpin.comic.loginpage.bean.LoginUserBean;
import com.youpin.comic.mainpage.bean.SearchKeyWord;

import com.youpin.comic.green.gen.UserDao;
import com.youpin.comic.green.gen.LoginUserBeanDao;
import com.youpin.comic.green.gen.SearchKeyWordDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig loginUserBeanDaoConfig;
    private final DaoConfig searchKeyWordDaoConfig;

    private final UserDao userDao;
    private final LoginUserBeanDao loginUserBeanDao;
    private final SearchKeyWordDao searchKeyWordDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        loginUserBeanDaoConfig = daoConfigMap.get(LoginUserBeanDao.class).clone();
        loginUserBeanDaoConfig.initIdentityScope(type);

        searchKeyWordDaoConfig = daoConfigMap.get(SearchKeyWordDao.class).clone();
        searchKeyWordDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        loginUserBeanDao = new LoginUserBeanDao(loginUserBeanDaoConfig, this);
        searchKeyWordDao = new SearchKeyWordDao(searchKeyWordDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(LoginUserBean.class, loginUserBeanDao);
        registerDao(SearchKeyWord.class, searchKeyWordDao);
    }
    
    public void clear() {
        userDaoConfig.clearIdentityScope();
        loginUserBeanDaoConfig.clearIdentityScope();
        searchKeyWordDaoConfig.clearIdentityScope();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public LoginUserBeanDao getLoginUserBeanDao() {
        return loginUserBeanDao;
    }

    public SearchKeyWordDao getSearchKeyWordDao() {
        return searchKeyWordDao;
    }

}
