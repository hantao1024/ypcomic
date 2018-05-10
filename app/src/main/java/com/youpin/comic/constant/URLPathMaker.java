package com.youpin.comic.constant;

public final class URLPathMaker {
	

	private static String getUrlWithInnerParams(String...params){
		String paramsStr = "";

		if (params==null || params.length == 0) {
			return paramsStr;
		}

		try {
			for (int i = 0; i < params.length; i++) {
				if (i != 0) {
					paramsStr = paramsStr +"&" + params[i].trim();
				}else {
					paramsStr = paramsStr + params[i].trim();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return paramsStr;
	}

	private static String getUrlWithInnerParam(String...params){
		String paramsStr = "";
		
		if (params==null || params.length == 0) {
			return paramsStr;
		}

		try {
			for (int i = 0; i < params.length; i++) {
                if (i != 0) {
                    paramsStr = paramsStr +"/" + params[i].trim();
                }else {
                    paramsStr = paramsStr + params[i].trim();
                }
            }
		} catch (Exception e) {
			e.printStackTrace();

		}
		return paramsStr;
	}

	private static String getUrlWithEqualParam(String...params){
		String paramsStr = "";

		if (params==null || params.length == 0) {
			return paramsStr;
		}

		try {
			for (int i = 0; i < params.length; i++) {
				if (i != 0) {
					paramsStr = paramsStr +"=" + params[i].trim();
				}else {
					paramsStr = paramsStr + params[i].trim();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return paramsStr;
	}
	public static String get_url(String url,String...params){
		return url + getUrlWithInnerParam(params);
	}


	public static String get_equal_url(String url,String...params){
		return url + getUrlWithEqualParam(params);
	}

}
