package com.virtualightning.apps.xposeddouyin;

import android.content.Context;
import android.content.SharedPreferences;

import com.virtualightning.apps.xposeddouyin.utils.LogUtils;
import com.virtualightning.apps.xposeddouyin.utils.XPosedUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Anchor : Create by CimZzz
 * Time : 2019-08-20 17:42:56
 * Project : taoke_android
 * Since Version : Alpha
 */
public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if(lpparam.packageName.equals("com.xingin.xhs")) {
            LogUtils.INSTANCE.log("进入小红书");
            // 尝试生成 UUID
//            Context context, String str, int i2, boolean z2, a aVar
            try {
                XPosedUtils.INSTANCE.simpleHookHelper("com.xingin.shield.http.XhsHttpInterceptor", lpparam.classLoader)
                    .hookMethod("intercept", new Object[]{lpparam.classLoader.loadClass("okhttp3.Interceptor$Chain"), long.class}, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            try {
//                                Object chain = param.args[0];
//                                Method mRequest = chain.getClass().getDeclaredMethod("request");
//                                mRequest.setAccessible(true);
//                                Object request = mRequest.invoke(chain);
//                                Method mHeaders = request.getClass().getDeclaredMethod("headers");
//                                mHeaders.setAccessible(true);
//                                Object headers = mHeaders.invoke(request);
//                                Method mSize = headers.getClass().getDeclaredMethod("size");
//                                Method mName = headers.getClass().getDeclaredMethod("name", int.class);
//                                Method mValue = headers.getClass().getDeclaredMethod("value", int.class);
//
//                                Method mUrl = request.getClass().getDeclaredMethod("url");
//                                mUrl.setAccessible(true);
//                                Object url = mUrl.invoke(request);
//                                Method mEncodedPath = url.getClass().getDeclaredMethod("encodedPath");
//                                mEncodedPath.setAccessible(true);
//                                Method mEncodedQuery = url.getClass().getDeclaredMethod("encodedQuery");
//                                mEncodedQuery.setAccessible(true);
//                                int headerSize = (int) mSize.invoke(headers);
//                                String[] names = new String[headerSize];
//                                String[] values = new String[headerSize];
//                                for(int i = 0 ; i < headerSize ; i ++) {
//                                    names[i] = (String) mName.invoke(headers, i);
//                                    values[i] = (String) mValue.invoke(headers, i);
//                                }
//                                LogUtils.INSTANCE.log("url : " + mEncodedPath.invoke(url) + ", encodeQuery: " + mEncodedQuery.invoke(url) + ", header size: " + headerSize + ", names: " + Arrays.toString(names) + ", values: " + Arrays.toString(values));
//                            } catch (Exception e) {
//
//                            }

                            super.beforeHookedMethod(param);
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            try {

                                Object chain = param.args[0];
                                Method mRequest = chain.getClass().getDeclaredMethod("request");
                                mRequest.setAccessible(true);
                                Object request = mRequest.invoke(chain);
                                Method mHeaders = request.getClass().getDeclaredMethod("headers");
                                mHeaders.setAccessible(true);
                                Method mHeader = request.getClass().getDeclaredMethod("header", String.class);
                                mHeader.setAccessible(true);

                                Object headers = mHeaders.invoke(request);
                                Method mSize = headers.getClass().getDeclaredMethod("size");
                                Method mName = headers.getClass().getDeclaredMethod("name", int.class);
                                Method mValue = headers.getClass().getDeclaredMethod("value", int.class);

                                Method mUrl = request.getClass().getDeclaredMethod("url");
                                mUrl.setAccessible(true);
                                Object url = mUrl.invoke(request);
                                Method mEncodedPath = url.getClass().getDeclaredMethod("encodedPath");
                                mEncodedPath.setAccessible(true);
                                Method mEncodedQuery = url.getClass().getDeclaredMethod("encodedQuery");
                                mEncodedQuery.setAccessible(true);
                                String urlStr = (String) mEncodedPath.invoke(url);
                                if(!urlStr.equals("/api/sns/v1/system_service/launch")) {
                                    return;
                                }

                                int headerSize = (int) mSize.invoke(headers);
                                String[] names = new String[headerSize];
                                String[] values = new String[headerSize];
                                for(int i = 0 ; i < headerSize ; i ++) {
                                    names[i] = (String) mName.invoke(headers, i);
                                    values[i] = (String) mValue.invoke(headers, i);
                                }
                                Object response = param.getResult();
                                Method mResRequest = response.getClass().getDeclaredMethod("request");
                                mResRequest.setAccessible(true);
                                request = mResRequest.invoke(response);
                                LogUtils.INSTANCE.log("url : " + urlStr + ", response header: " + mHeader.invoke(request, "shield") + ", encodeQuery: " + mEncodedQuery.invoke(url) + ", header size: " + headerSize + ", names: " + Arrays.toString(names) + ", values: " + Arrays.toString(values));

                            } catch (Exception e) {
                                LogUtils.INSTANCE.log("error: " + e);
                            }

                        }
                    })
                    .hookMethod("initialize", new Object[]{String.class}, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Class cls = XposedHelpers.findClass("com.xingin.utils.XYUtilsCenter", lpparam.classLoader);
                            Field field = cls.getField("a");
                            field.setAccessible(true);
                            Context context = (Context) field.get(null);

                            SharedPreferences sp = context.getSharedPreferences("s", 0x0);
                            LogUtils.INSTANCE.log("main sp: " + sp.getString("main", "123"));
                            LogUtils.INSTANCE.log("main_hmac sp: " + sp.getString("main_hmac", ""));
                            super.beforeHookedMethod(param);
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            try {
                                Class cls = XposedHelpers.findClass("com.xingin.utils.XYUtilsCenter", lpparam.classLoader);
                                Field field = cls.getField("a");
                                field.setAccessible(true);
                                Context context = (Context) field.get(null);

                                SharedPreferences sp = context.getSharedPreferences("s", 0x0);
                                LogUtils.INSTANCE.log("main sp: " + sp.getString("main", "123"));
                                LogUtils.INSTANCE.log("main_hmac sp: " + sp.getString("main_hmac", ""));

                                LogUtils.INSTANCE.log("参数是: " + Arrays.toString(param.args) + ", 结果为: " + param.getResult());
                            } catch (Exception e) {
                                LogUtils.INSTANCE.log("HOOK 监控错误: " + e);
                            }
                        }
                    });

                XPosedUtils.INSTANCE.simpleHookHelper("l.w.v0.a.c$a", lpparam.classLoader)

                        .hookMethod("a", new Object[]{byte[].class, int.class, int.class, byte[].class}, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                LogUtils.INSTANCE.log("ab 算法参数1: " + Arrays.toString((byte[]) param.args[0]));
//                                LogUtils.INSTANCE.log("ab 算法参数2: " + param.args[1]);
//                                LogUtils.INSTANCE.log("ab 算法参数3: " + param.args[2]);
//                                LogUtils.INSTANCE.log("ab 算法参数4: " + Arrays.toString((byte[]) param.args[3]));
                                super.beforeHookedMethod(param);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
//                                LogUtils.INSTANCE.log("a 算法参数1: " + Arrays.toString((byte[]) param.args[0]));
//                                LogUtils.INSTANCE.log("a 算法参数2: " + param.args[1]);
//                                LogUtils.INSTANCE.log("a 算法参数3: " + param.args[2]);
//                                LogUtils.INSTANCE.log("a 算法参数4: " + Arrays.toString((byte[]) param.args[3]));
                                LogUtils.INSTANCE.log("a 算法结果: " + param.getResult());
                            }
                        });

//                XPosedUtils.INSTANCE.simpleHookHelper("com.xingin.shield.http.Base64Helper", lpparam.classLoader)
//                        .hookMethod("decode", new Object[]{String.class}, new XC_MethodHook() {
//                            @Override
//                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                super.afterHookedMethod(param);
//                                LogUtils.INSTANCE.log("decode 算法结果: " + Arrays.toString((byte[])param.getResult()));
//                            }
//                        });
                LogUtils.INSTANCE.log("HOOK 注入成功");
            }
            catch (Throwable e) {
                LogUtils.INSTANCE.log("HOOK 注入失败: " + e);
            }


        }
//
//        if (lpparam.packageName.equals("cn.soulapp.android")) {
//            LogUtils.INSTANCE.log("进入 SOUL APP");
//            try {
//                XPosedUtils.INSTANCE.simpleHookHelper("cn.soulapp.android.soulpower.SoulPowerful", lpparam.classLoader)
//                        .hookMethod("h", new Object[]{Context.class, int.class, String.class, String.class}, new XC_MethodHook() {
//                            @Override
//                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                super.afterHookedMethod(param);
//                                StringBuilder sb = new StringBuilder("SOUL h 方法调用,返回值: " + param.getResult() + " 参数:\n");
//
////                                Context context = (Context) param.args[0];
////                                PackageInfo info = context.getPackageManager().getPackageInfo("cn.soulapp.android", 0x40);
//                                for(Object arg : param.args) {
//                                    sb.append(arg).append('\n');
//                                }
//                                LogUtils.INSTANCE.log(sb);
//                                sb.append("##########################\n");
//                            }
//                        });
//                XPosedUtils.INSTANCE.simpleHookHelper("cn.soulapp.android.soulpower.SoulPowerful", lpparam.classLoader)
//                        .hookMethod("d")
//                        .hookMethod("e", new Object[]{Context.class})
//                        .hookMethod("c", new Object[]{String.class})
//                        .hookMethod("f", new Object[]{byte[].class, long.class})
//                        .hookMethod("g", new Object[]{Context.class, String.class, String.class, String.class, String.class, String.class, String.class})
//                        .hookMethod("i");
//
//                XPosedUtils.INSTANCE.simpleHookHelper("cn.soulapp.android.soulpower.imlib.ImEncryptUtils", lpparam.classLoader)
//                        .hookMethod("getUserIdKey", new Object[]{Context.class})
//                        .hookMethod("decryptByDes", new Object[]{Context.class, String.class})
//                        .hookMethod("encryptByDes", new Object[]{Context.class, String.class})
//                        .hookMethod("genImSign", new Object[]{Context.class, byte[].class, long.class});
//
//                XPosedUtils.INSTANCE.simpleHookHelper("cn.soulapp.android.soulpower.EnvDetection", lpparam.classLoader)
//                        .hookMethod("isHaveHookMoudle");
//
//                XPosedUtils.INSTANCE.simpleHookHelper("cn.soulapp.android.soulpower.SoulCrypt", lpparam.classLoader)
//                        .hookMethod("a", new Object[]{byte[].class, byte[].class, byte.class})
//                        .hookMethod("a", new Object[]{byte[].class, byte[].class, byte[].class})
//                        .hookMethod("b", new Object[]{byte[].class, byte[].class, byte.class})
//                        .hookMethod("b", new Object[]{byte[].class, byte[].class, byte[].class})
//                        .hookMethod("c", new Object[]{byte[].class, byte[].class, byte[].class})
//                        .hookMethod("d", new Object[]{byte[].class, byte[].class, byte[].class})
//                        .hookMethod("e", new Object[]{byte[].class, byte[].class, byte[].class})
//                        .hookMethod("f", new Object[]{byte[].class, byte[].class, byte[].class});
//                LogUtils.INSTANCE.log("注入成功");
//            }
//            catch (Throwable e) {
//                LogUtils.INSTANCE.log("注入失败，发生异常: " + e);
//            }
//        }
    }

}
