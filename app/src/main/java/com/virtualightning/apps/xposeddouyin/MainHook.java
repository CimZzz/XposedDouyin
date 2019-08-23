package com.virtualightning.apps.xposeddouyin;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.virtualightning.apps.xposeddouyin.utils.LogUtils;
import com.virtualightning.apps.xposeddouyin.utils.XPosedUtils;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Anchor : Create by CimZzz
 * Time : 2019-08-20 17:42:56
 * Project : taoke_android
 * Since Version : Alpha
 */
public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        LogUtils.INSTANCE.logPkgName(lpparam.packageName);
        if(lpparam.packageName.equals("com.ss.android.ugc.aweme")) {
            LogUtils.INSTANCE.log("进入抖音");
//            LogUtils.INSTANCE.logClass(XposedHelpers.findClass("com.ss.android.ugc.aweme.discover.model.SearchResultParam", lpparam.classLoader));

            XPosedUtils.INSTANCE.simpleHookHelper("java.io.FileInputStream", lpparam.classLoader)
                    .hookAllConstructor();


            XPosedUtils.INSTANCE.simpleHookHelper("com.ss.android.ugc.aweme.sp.e", lpparam.classLoader)
//                    .hookMethod("getString", new Object[]{String.class, String.class}, new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            super.beforeHookedMethod(param);
//                            try {
//                                Field field = XPosedUtils.INSTANCE.findFirstFieldByName(param.thisObject.getClass(), "g");
//                                LogUtils.INSTANCE.log("当前map: " + field.get(param.thisObject));
//                            } catch (Exception e) {
//                                LogUtils.INSTANCE.log("发生错误");
//                            }
//                        }
//                    })
                    .hookMethod("a", new Object[]{File.class}, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            String path = ((File)param.args[0]).getAbsolutePath();
                            if(path.startsWith("/data/user/0/com.ss.android.ugc.aweme"))
                                LogUtils.INSTANCE.log("写入文件: " + path);
                            else
                                LogUtils.INSTANCE.log("写入文件2: " + path);
                        }
                    });

//            final Class<?> tt1 = XposedHelpers.findClass("com.ss.sys.ces.gg.tt$1", lpparam.classLoader);
//            XposedHelpers.findAndHookMethod(tt1, "a", String.class, Map.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    LogUtils.INSTANCE.log("----------tt$1.a(Init_Gorgon)_end:");
//                    LogUtils.INSTANCE.log(param.args[0]);
//                    Map<String, List<String>> properties = (Map<String, List<String>>) param.args[1];
//                    for (Map.Entry<String, List<String>> entry : properties.entrySet()) {
//                        if(entry.getKey().equals("cookie"))
//                            LogUtils.INSTANCE.log(entry.getKey() + "=" + entry.getValue());
//                    }
////                    LogUtils.INSTANCE.log("getResult:");
//                    HashMap map = (HashMap) param.getResult();
//                    for (Object key : map.keySet()) {
//                        LogUtils.INSTANCE.log(key + "=" + map.get(key));
//                    }
//                    LogUtils.INSTANCE.log("--------------");
//                    super.afterHookedMethod(param);
//                }
//            });
       }
    }
}
