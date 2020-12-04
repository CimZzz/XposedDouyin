package com.virtualightning.apps.xposeddouyin.utils

import android.util.Log
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019-08-20 17:43:29
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object LogUtils {
    fun log(any: Any?) {
        val str = any.toString()
//        XposedBridge.log("CimZzz: $str")
        Log.v("CimZzz", str)
    }

    fun logDetail(any: Any?) {
        val str = any.toString()
//        XposedBridge.log("Detail: $str")
        Log.v("DETAIL", str)
    }

    fun logClass(cls: Class<*>?) {
        try {
            if(cls == null)
                return

            logDetail(">类 ${cls.name}")
            val fields = cls.declaredFields
            for(field in fields)
                logField(field)

            val methods = cls.declaredMethods
            for(method in methods)
                logMethod(method)

            val subClasses = cls.declaredClasses
            for(subCls in subClasses)
                logDetail("内部类: ${subCls.name}")
        }
        catch (e: Throwable) {
            logDetail("类 ${cls?.name} 发生错误 :$e")
        }
    }

    fun logField(field: Field?) {
        try {
            if(field == null)
                return

            logDetail(buildString {
                field.isAccessible = true
                append("成员: ")
                when {
                    Modifier.isPublic(field.modifiers) -> append("public ")
                    Modifier.isProtected(field.modifiers) -> append("protected ")
                    Modifier.isPrivate(field.modifiers) -> append("private ")
                }

                when {
                    Modifier.isFinal(field.modifiers) -> append("final ")
                    Modifier.isStatic(field.modifiers) -> append("static ")
                }

                append("${field.type.name} ${field.name}")
            })
        }
        catch (e: Throwable) {
            logDetail("成员 ${field?.name} 发生错误 :$e")
        }
    }

    fun logMethod(method: Method?) {
        try {
            if(method != null) {
                logDetail(buildString {
                    method.isAccessible = true
                    append("方法: ")
                    when {
                        Modifier.isPublic(method.modifiers) -> append("public ")
                        Modifier.isProtected(method.modifiers) -> append("protected ")
                        Modifier.isPrivate(method.modifiers) -> append("private ")
                    }

                    when {
                        Modifier.isAbstract(method.modifiers) -> append("abstract ")
                        Modifier.isFinal(method.modifiers) -> append("final ")
                        Modifier.isStatic(method.modifiers) -> append("static ")
                    }

                    append("${method.returnType.name} ${method.name}(")
                    var isFirst = true
                    for(params in method.parameterTypes) {
                        if(isFirst) {
                            isFirst = false
                        }
                        else append(", ")
                        append(params.name)
                    }
                    append(")")
                })
            }
        }
        catch (e: Throwable) {
            logDetail("方法 ${method?.name} 发生错误 :$e")
        }
    }

    fun logPkgName(any: Any?) {
        val str = any.toString()
        XposedBridge.log("CimZzz: PKGNAME: $str")
        Log.v("PKGNAME", str)
    }
}

fun log(any: Any?) = LogUtils.log(any)

fun logPkgName(any: Any?) = LogUtils.logPkgName(any)