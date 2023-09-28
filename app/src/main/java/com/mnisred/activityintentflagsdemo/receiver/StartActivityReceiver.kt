package com.mnisred.activityintentflagsdemo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mnisred.activityintentflagsdemo.activity.StandardActivity

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/09/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class StartActivityReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        StandardActivity.startNewTask(context)
    }
}