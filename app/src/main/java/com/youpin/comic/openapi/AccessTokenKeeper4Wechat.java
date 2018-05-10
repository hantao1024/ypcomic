/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.youpin.comic.openapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONObject;

/**
 * 该类定义了微信信所需要的参数。
 * 
 * @author WECHAT
 * @since 2013-10-07
 */
public class AccessTokenKeeper4Wechat {
	
    private static final String PREFERENCES_NAME = "com_wechat_sdk_android";

    private static final String KEY_OPENID    = "openid";
    private static final String KEY_REFRESH_TOKEN    = "refresh_token";
    private static final String KEY_ACCESS_TOKEN    = "access_token";
    private static final String KEY_ACCESS_UNIONID    = "unionid";

    /**
     * 保存 Token 对象到 SharedPreferences。
     * 
     * @param context 应用程序上下文环境
     * @param token   Token 对象
     */
    public static void writeAccessToken(Context context, JSONObject token) {
        if (null == context || null == token) {
            return;
        }
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(KEY_OPENID, token.optString(KEY_OPENID));
        editor.putString(KEY_ACCESS_TOKEN, token.optString(KEY_ACCESS_TOKEN));
        editor.putString(KEY_REFRESH_TOKEN, token.optString(KEY_REFRESH_TOKEN));
        editor.putString(KEY_ACCESS_UNIONID, token.optString(KEY_ACCESS_UNIONID));
        editor.commit();
    }
    
    /**
     * 从 SharedPreferences 读取 Token 信息。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 返回 Token 对象
     */
    public static Oauth2AccessToken readAccessToken(Context context) {
        if (null == context) {
            return null;
        }
        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        token.setOpenid(pref.getString(KEY_OPENID, ""));
        token.setRefresh_token(pref.getString(KEY_REFRESH_TOKEN, "")) ; 
        token.setAccess_token(pref.getString(KEY_ACCESS_TOKEN, "")) ; 
        token.setUnionid(pref.getString(KEY_ACCESS_UNIONID, ""));
        return token;
    }
    
    public static class Oauth2AccessToken {
    	
    	private String openid ;
    	
    	private String refresh_token ;
    	
    	private String access_token ;
    	private String unionid ;

		public String getOpenid() {
			return openid;
		}
		public void setOpenid(String openid) {
			this.openid = openid;
		}
		public String getRefresh_token() {
			return refresh_token;
		}
		public void setRefresh_token(String refresh_token) {
			this.refresh_token = refresh_token;
		}
		public String getAccess_token() {
			return access_token;
		}
		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }
    }
    
    

    /**
     * 清空 SharedPreferences 中 Token信息。
     * 
     * @param context 应用程序上下文环境
     */
    public static void clear(Context context) {
        if (null == context) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
