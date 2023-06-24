package com.mnisred.activityintentflagsdemo

import android.app.ActivityManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import com.mnisred.activityintentflagsdemo.activity.SingleInstanceActivity
import com.mnisred.activityintentflagsdemo.activity.SingleTaskActivity
import com.mnisred.activityintentflagsdemo.activity.SingleTopActivity
import com.mnisred.activityintentflagsdemo.activity.StandardActivity
import com.mnisred.activityintentflagsdemo.databinding.NormalActivityBinding

/**
 * @Description: 扩展函数聚合
 * @author: zhang
 * @date: 2023/6/22
 */

val ActivityManager.RecentTaskInfo.uniqueId : Int
    get(){
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            taskId
        }else{
            //不能用id，在退出时，id会变成-1
            persistentId
        }
    }


val Int.dp : Float
    get() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),Resources.getSystem().displayMetrics)
    }

fun NormalActivityBinding.setLaunchClickListener(context:Context){
    launchStandard.setOnClickListener {
        StandardActivity.start(context)
    }
    launchSingleTop.setOnClickListener {
        SingleTopActivity.start(context)
    }
    launchSingleTask.setOnClickListener {
        SingleTaskActivity.start(context)
    }
    launchSingleInstance.setOnClickListener {
        SingleInstanceActivity.start(context)
    }
}