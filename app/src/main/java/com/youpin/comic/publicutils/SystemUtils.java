/*
 * File Name: SystemUtils.java 
 * History:
 * Created by Siyang.Miao on 2012-2-20
 */
package com.youpin.comic.publicutils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.youpin.comic.CApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class SystemUtils {
	
    private static final String KERNEL_PATH = "/proc/version";
    private static final String CPU_MAX_FREQ_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
    
    private static StatFs sStatFs = null;
    
    
    /**
	 * 获取状态栏高度
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		  int result = 0;
		  int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		  if (resourceId > 0) {
		      result = context.getResources().getDimensionPixelSize(resourceId);
		  }
		  return result;
	}
	
	/***
	 * 获取Android id 
	 * @param context
	 * @return
	 */
	public static String getAndroidId(Context context){
		 String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID); 
		 return ANDROID_ID ; 
	}
	

    /**
     * 获取系统配置参数
     * 
     * @param key
     *            参数key
     * @return 参数值
     */
    public static final String getSystemProperty(String key) {
        String pValue = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method m = c.getMethod("get", String.class);
            pValue = m.invoke(null, key).toString();
        } catch (ClassNotFoundException e) {
            LogUtils.e(e);
        } catch (SecurityException e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return pValue;
    }
    



    /***
     * 得到系统的时间,格式是YYYYMMDDhhmmss
     * @return
     */
    public static String getDataTimeStr(){
		StringBuilder builder=new StringBuilder();
		builder.append("" + Calendar.getInstance().get(Calendar.YEAR)).append(fDataNumer((Calendar.getInstance().get(Calendar.MONTH)+1)))
		.append(fDataNumer(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)))
		.append(fDataNumer(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)))
		.append(fDataNumer(Calendar.getInstance().get(Calendar.MINUTE)))
		.append(fDataNumer(Calendar.getInstance().get(Calendar.SECOND)));
		return builder.toString();
	}
	
	
	private static String fDataNumer(int num) {
		String n = "" + num;
		if (n.length() < 2) {
			n = "0" + n;
		}
		return n;
	}
	
	/***
	 * 得到手机的型号,如lenovo k900
	 * @return
	 */
	public static String getDeviceName(){
		return Build.MODEL;
	}
	
	/**
	 * 得到品牌
	 * @return
	 */
	public static String getBrand(){
		return Build.BRAND ;
	}
	
	/***
	 * 得到手机的版本数字号
	 * @return 7|8|9|10|12|....
	 */
	public static String getSDKVersionCode(){
		return Build.VERSION.SDK;
	}
	
	/***
	 * 得到系统的版本号,如4.2.2
	 * @return
	 */
	public static String getSystemCode(){
		return Build.VERSION.RELEASE;
	}
	
	
	/**
	 * 得到屏幕的宽度
	 * @param mContext
	 * @return
	 */
	public static int getScreenWidth(Activity mContext){
//		Display display = mContext.getWindowManager().getDefaultDisplay();
//		return display.getWidth();
        return CApplication.getInstance().getResources().getDisplayMetrics().widthPixels;
	}
	/**
	 * 得到屏幕的高度
	 * @param mContext
	 * @return
	 */
	public static int getScreenHeight(Activity mContext){
//		Display display = mContext.getWindowManager().getDefaultDisplay();
//		return display.getHeight();
        return CApplication.getInstance().getResources().getDisplayMetrics().heightPixels;
	}
	
	/**
	 * 得到屏幕的宽度
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context){
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.widthPixels;
	}
	/**
	 * 得到屏幕的高度
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context){
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.heightPixels;
	}
	
	
	
	/***
	 * 隐蔽软键盘
	 * @param context
	 * @param mView
	 */
	public static void hiddenInputMethod(Context context,View mView){
		InputMethodManager imm =(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
		imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
	}
	
	/***
	 * 显示软键盘
	 * @param context
	 * @param mView
	 */
	@Deprecated
	public static void showInputMethod(Context context,View mView){
		InputMethodManager imm =(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
		imm.showSoftInputFromInputMethod(mView.getWindowToken(), 0);
	}
	
	
	
//	/***
//	 * 获取IP地址
//	 * @param mContext
//	 * @return
//	 */
//	public static String getIpAddress(Activity mContext){
//		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
//		/*if (!wifiManager.isWifiEnabled()) {
//	        wifiManager.setWifiEnabled(true);
//	    }*/
//		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        int ipAddress = wifiInfo.getIpAddress();
//        String ip = intToIp(ipAddress);
//        return ip;
//	}
	
	
	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}



    /**
     * 获取以太网卡mac
     * 
     * @return eth0'Mac 以太网卡Mac
     */
    public static String getEthMacAddress() {
        String ethMac = null;
        File f = new File("/sys/class/net/eth0/address");
        if (f.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                ethMac = br.readLine();
                ethMac.trim();
                br.close();
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
        return ethMac;
    }
    
    /**
     * 根据packageName获取packageInfo
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        if (null == context) {
            return null;
        }
        PackageInfo info = null;
        PackageManager manager = context.getPackageManager();
        // 根据packageName获取packageInfo
        try {
            info = manager.getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
        } catch (NameNotFoundException e) {
            LogUtils.e(e);
        }
        return info;
    }

    /**
     * 判断是否是第三方软件
     * 
     * @param context
     *            Context
     * @param packageName
     *            软件的包名
     * @return 是第三方软件返回true，否则返回false
     */
    public static boolean isThirdPartyApp(Context context, String packageName) {
        if (null == context) {
            return false;
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = pm.getPackageInfo(packageName, 0);
            return isThirdPartyApp(packageInfo);
        } catch (NameNotFoundException e) {
            LogUtils.e(e);
            return false;
        }
    }
    
    /**
     * 判断是否是第三方软件
     * 
     * @param packageInfo
     *            软件的packageInfo
     * @return 是第三方软件返回true，否则返回false
     */
    public static boolean isThirdPartyApp(PackageInfo packageInfo) {
        if (null == packageInfo || null == packageInfo.applicationInfo) {
            return false;
        }

        return ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                || ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    /**
     * 读取指定路径下APK文件签名
     */
    @SuppressWarnings("unchecked")
    public static String getJarSignature(String filePath) throws Exception {
        if (null == filePath) {
            return null;
        }
        String resultSign = "";
        String resultKey = "";
        List<ZipEntry> names = new ArrayList<ZipEntry>();
        ZipFile zf = new ZipFile(filePath);
        Enumeration<ZipEntry> zi = (Enumeration<ZipEntry>) zf.entries();
        while (zi.hasMoreElements()) {
            ZipEntry ze = zi.nextElement();
            String name = ze.getName();
            if (name.startsWith("META-INF/") && (name.endsWith(".RSA") || name.endsWith(".DSA"))) {
                names.add(ze);
            }
        }
        Collections.sort(names, new Comparator<ZipEntry>() {
            @Override
            public int compare(ZipEntry obj1, ZipEntry obj2) {
                if (obj1 != null && obj2 != null) {
                    return obj1.getName().toUpperCase().compareTo(obj2.getName().toUpperCase());
                }
                return 0;
            }
        });
        for (ZipEntry ze : names) {
            // System.out.println("RSA file name " + ze.getName());
            InputStream is = zf.getInputStream(ze);
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                CertPath cp = cf.generateCertPath(is, "PKCS7");
                List<?> list = cp.getCertificates();
                for (Object obj : list) {
                    if (!(obj instanceof X509Certificate))
                        continue;
                    X509Certificate cert = (X509Certificate) obj;
                    StringBuffer sb = new StringBuffer();
                    sb.setLength(0);
                    byte[] key = getPKBytes(cert.getPublicKey());
                    for (int i = 0; i < key.length; i++)
                        sb.append(String.format("%02X", key[i]));
                    resultKey += sb.toString();
                    sb.setLength(0);
                    byte[] signature = cert.getSignature();

                    for (int i = 0; i < signature.length; i++)
                        sb.append(String.format("%02X", signature[i]));
                    resultSign += sb.toString();
                }
            } catch (CertificateException e) {
                LogUtils.e(e);
            }
            is.close();
        }
        if (!TextUtils.isEmpty(resultKey) && !TextUtils.isEmpty(resultSign)) {
            return hashCode(resultKey) + "," + hashCode(resultSign);
        }
        return null;
    }

    private static byte[] getPKBytes(PublicKey pk) {
        if (pk instanceof RSAPublicKey) {
            RSAPublicKey k = (RSAPublicKey) pk;
            return k.getModulus().toByteArray();
        } else if (pk instanceof DSAPublicKey) {
            DSAPublicKey k = (DSAPublicKey) pk;
            return k.getY().toByteArray();
        }
        return null;
    }

    /**
     * 字符串对应hash code
     * 
     * @param str
     * @return
     */
    public static int hashCode(String str) {
        int hash = 0;
        if (str != null) {
            int multiplier = 1;
            int _offset = 0;
            int _count = str.length();
            char[] _value = new char[_count];
            str.getChars(_offset, _count, _value, 0);
            for (int i = _offset + _count - 1; i >= _offset; i--) {
                hash += _value[i] * multiplier;
                int shifted = multiplier << 5;
                multiplier = shifted - multiplier;
            }
        }
        return hash;
    }

    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static long getExternalSpace() {
        long availableSpace = -1l;
        try {
            String externalStoragePath = Environment.getExternalStorageDirectory().getPath();
            if (sStatFs == null)
                sStatFs = new StatFs(externalStoragePath);
            else
                sStatFs.restat(externalStoragePath);
            availableSpace = sStatFs.getAvailableBlocks() * (long) sStatFs.getBlockSize();
        } catch (Exception e) {
            LogUtils.e(e);
        }

        return availableSpace;
    }

    public static int[] getResolution(Context context) {
        WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int[] res = new int[2];
        res[0] = windowMgr.getDefaultDisplay().getWidth();
        res[1] = windowMgr.getDefaultDisplay().getHeight();
        return res;
    }

    // ==========================================================================
    // Inner/Nested Classes
    // ==========================================================================


    /**
     * 获取格式化日期和时间
     * 
     * @param formatStr
     *            格式化字符串，例如"yyyy-MM-dd HH:mm:ss"
     * @return 格式化的日期时间
     */
    public static String getFormattedDateTime(String formatStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            Date date = new Date();
            return format.format(date.getTime());
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return "";
    }

    public static Resources getAPKResources(Context context, String apkPath) throws Exception {
        String PATH_AssetManager = "android.content.res.AssetManager";
        Class assetMagCls = Class.forName(PATH_AssetManager);
        Constructor assetMagCt = assetMagCls.getConstructor((Class[]) null);
        Object assetMag = assetMagCt.newInstance((Object[]) null);
        Class[] typeArgs = new Class[1];
        typeArgs[0] = String.class;
        Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", typeArgs);
        Object[] valueArgs = new Object[1];
        valueArgs[0] = apkPath;
        assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
        Resources res = context.getResources();
        typeArgs = new Class[3];
        typeArgs[0] = assetMag.getClass();
        typeArgs[1] = res.getDisplayMetrics().getClass();
        typeArgs[2] = res.getConfiguration().getClass();
        Constructor resCt = Resources.class.getConstructor(typeArgs);
        valueArgs = new Object[3];
        valueArgs[0] = assetMag;
        valueArgs[1] = res.getDisplayMetrics();
        valueArgs[2] = res.getConfiguration();
        res = (Resources) resCt.newInstance(valueArgs);
        return res;
    }

    /**
     * Gets the number of cores available in this device, across all processors. Requires: Ability to peruse the
     * filesystem at "/sys/devices/system/cpu"
     * 
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Default to return 1 core
            return 1;
        }
    }

    /*
     * 获取CPU频率
     */
    public static String getCpuFreq() {
        return readKernelInfo(CPU_MAX_FREQ_PATH);
    }

    /**
     * 获取内核信息
     */
    private static String readKernelInfo(String path) {
        FileReader fr = null;
        BufferedReader br = null;
        String kernelInfo = null;
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr, 1024);
            kernelInfo = br.readLine();
            if (path.equals(KERNEL_PATH)) {
                String[] temp = kernelInfo.split("\\s+");
                if (temp.length >= 3) {
                    kernelInfo = temp[2];
                }
            }
        } catch (FileNotFoundException e) {
            LogUtils.e(e);
        } catch (IOException e) {
            LogUtils.e(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
        return kernelInfo;
    }
    
    /**
     * 获取内存信息
     */
    public static long getRAM() {
        long total_memory = 0;
        String temp = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("/proc/meminfo");
            br = new BufferedReader(fr, 1024);
            temp = br.readLine();

            String[] str = temp.split("\\s+");
            total_memory = Math.round(Integer.valueOf(str[1]) / 1024f);
        } catch (Throwable e) {
            LogUtils.e(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
        
        return total_memory;
    }
    
    /**
     * 校验手机号
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles){  

    	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0,5-9]))\\d{8}$");  
    	
    	Matcher m = p.matcher(mobiles);  

    	return m.matches();  
    }
    

	public static boolean checkPermission(Context context, String permission) {
	    boolean result = false;
	    if (Build.VERSION.SDK_INT >= 23) {
	        try {
	            Class<?> clazz = Class.forName("android.content.Context");
	            Method method = clazz.getMethod("checkSelfPermission", String.class);
	            int rest = (Integer) method.invoke(context, permission);
	            if (rest == PackageManager.PERMISSION_GRANTED) {
	                result = true;
	            } else {
	                result = false;
	            }
	        } catch (Exception e) {
	            result = false;
	        }
	    } else {
	        PackageManager pm = context.getPackageManager();
	        if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
	            result = true;
	        }
	    }
	    return result;
	}
	
    /**
     *    * 获取当前时间的日
     */
    public static int getDay() {
        int day=0;
        try {
            Calendar cal = Calendar.getInstance();
            day = cal.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return day;
    }

}
