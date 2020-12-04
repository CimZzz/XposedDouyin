package com.virtualightning.apps.xposeddouyin.utils

import java.lang.RuntimeException
import java.lang.reflect.Field

class Modifier(
    private val classLoader: ClassLoader
) {
    fun belong(clsName: String): Handler? {
        try {
            return belong(Class.forName(clsName, true, classLoader))
        }
        catch(e: Throwable) {
            LogUtils.log("find cls $clsName error: $e")
            return null
        }
    }

    fun belong(cls: Class<*>?): Handler? {
        if(cls == null)
            return null
        return Handler(cls)
    }

    fun belong(obj: Any?): Handler? {
        if(obj == null)
            return null
        return Handler(obj::class.java)
    }
}


class Handler(
    private val cls: Class<*>
) {

    fun findStaticValue(valueName: String?): Valuer? {
        return findValue(null, valueName)
    }

    fun findValue(obj: Any?, valueName: String?): Valuer? {
        if(valueName == null)
            return null
        try {
            val field = cls.getDeclaredField(valueName) ?: throw RuntimeException("没有找到对应属性")
            field.isAccessible = true
            return Valuer(obj, field)
        }
        catch(e: Throwable) {
            LogUtils.log("find field $valueName error: $e")
            return null
        }
    }
}


class Valuer(
    private val obj: Any?,
    private val field: Field
){
    fun set(value: Any?) {
        try {
            field.set(obj, value)
        }
        catch(e: Throwable) {
            LogUtils.log("set field ${field.name} error: $e")
        }
    }

    fun get(): Any? {
        try {
            return field.get(obj)
        }
        catch(e: Throwable) {
            LogUtils.log("get field ${field.name} error: $e")
            return null
        }
    }
}