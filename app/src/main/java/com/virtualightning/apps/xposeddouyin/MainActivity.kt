package com.virtualightning.apps.xposeddouyin

import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import com.virtualightning.apps.xposeddouyin.utils.LogUtils
import java.nio.charset.Charset
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 尝试生成 UUID
        val androidId = Settings.Secure.getString(contentResolver, "android_id");
        val bytes = androidId.toByteArray(Charset.forName("utf-8"))
        val uuid = UUID.nameUUIDFromBytes(bytes)
        LogUtils.log("生成的 UUID : $uuid")
    }
}
