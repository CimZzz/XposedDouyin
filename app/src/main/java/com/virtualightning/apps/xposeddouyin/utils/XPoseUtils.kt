package com.virtualightning.apps.xposeddouyin.utils

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.*

object XPosedUtils {
    fun findCls(any: Any?): Class<*>? {
        if(any == null)
            return null

        if(any is Class<*>)
            return any
        return any.javaClass
    }

    fun findFirstFieldByName(any: Any?, fieldName: String): Field? {
        val cls = findCls(any) ?: return null
        for(field in cls.declaredFields) {
            if (field.name == fieldName)
                return field
        }
        return null
    }

    fun findFirstFieldByTypeName(any: Any?, typeName: String): Field? {
        val cls = findCls(any) ?: return null

        for(field in cls.declaredFields) {
            if (field.type.name == typeName)
                return field
            else if (field.type.simpleName == typeName)
                return field
        }
        return null
    }


    fun findFirstMethod(cls: Class<*>?, methodName: String): Method? {
        if(cls == null)
            return null
        for(method in cls.declaredMethods) {
            if (method.name == methodName)
                return method
        }
        return null
    }


    fun simpleHookHelper(clsName: String, clsLoader: ClassLoader): SimpleHookHelper = SimpleHookHelper(clsName, clsLoader)

    fun checkPatchProxySupport(clsLoader: ClassLoader, params: Array<Any>, obj: Any?, redirect: Any?, bool: Boolean, number: Int, paramsTypeArr: Array<Class<*>>, returnType: Class<*>): Boolean {
        val proxyCls = XposedHelpers.findClass("com.meituan.robust.PatchProxy", clsLoader)
        val method = findFirstMethod(proxyCls, "isSupport")!!
        return method.invoke(null, params, obj, redirect, bool, number, paramsTypeArr, returnType) as Boolean
    }


    class SimpleHookHelper(private val clsName: String, private val clsLoader: ClassLoader) {
        fun hookConstructor(typeArr: Array<Any>, hookCallback: XC_MethodHook? = null): SimpleHookHelper {
            XposedHelpers.findAndHookConstructor(clsName, clsLoader, *typeArr, hookCallback?:SIMPLE_HOOK_CALL_BACK("$clsName constructor"))
            return this
        }
        fun hookConstructor(typeArr: Array<Any>): SimpleHookHelper {
            XposedHelpers.findAndHookConstructor(clsName, clsLoader, *typeArr, SIMPLE_HOOK_CALL_BACK("$clsName constructor"))
            return this
        }

        fun hookConstructor(hookCallback: XC_MethodHook? = null): SimpleHookHelper {
            XposedHelpers.findAndHookConstructor(clsName, clsLoader, hookCallback?:SIMPLE_HOOK_CALL_BACK("$clsName constructor"))
            return this
        }

        fun hookConstructor(): SimpleHookHelper {
            XposedHelpers.findAndHookConstructor(clsName, clsLoader, SIMPLE_HOOK_CALL_BACK("$clsName constructor"))
            return this
        }

        fun hookAllConstructor(hookCallback: XC_MethodHook? = null): SimpleHookHelper {
            XposedBridge.hookAllConstructors(XposedHelpers.findClass(clsName, clsLoader), hookCallback?:SIMPLE_HOOK_CALL_BACK("$clsName all constructor"))
            return this
        }

        fun hookAllConstructor(): SimpleHookHelper {
            XposedBridge.hookAllConstructors(XposedHelpers.findClass(clsName, clsLoader), SIMPLE_HOOK_CALL_BACK("$clsName all constructor"))
            return this
        }

        fun hookMethod(methodName: String, typeArr: Array<Any>, hookCallback: XC_MethodHook? = null): SimpleHookHelper {
            try {
                XposedHelpers.findAndHookMethod(
                    clsName,
                    clsLoader,
                    methodName,
                    *typeArr,
                    hookCallback ?: SIMPLE_HOOK_CALL_BACK("$clsName $methodName")
                )
            }
            catch(e: Exception){}
            return this
        }

        fun hookMethod(methodName: String, typeArr: Array<Any>): SimpleHookHelper {
            XposedHelpers.findAndHookMethod(clsName, clsLoader, methodName, *typeArr, SIMPLE_HOOK_CALL_BACK("$clsName $methodName"))
            return this
        }

        fun hookMethod(methodName: String, hookCallback: XC_MethodHook? = null): SimpleHookHelper {
            XposedHelpers.findAndHookMethod(clsName, clsLoader, methodName, hookCallback?:SIMPLE_HOOK_CALL_BACK("$clsName $methodName"))
            return this
        }

        fun hookMethod(methodName: String): SimpleHookHelper {
            XposedHelpers.findAndHookMethod(clsName, clsLoader, methodName, SIMPLE_HOOK_CALL_BACK("$clsName $methodName"))
            return this
        }


        private fun SIMPLE_HOOK_CALL_BACK(methodName: String) = object: XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                log("$methodName call: ${Arrays.toString(param.args)}")
            }

            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
            }
        }
    }
}