package com.mnisred.activityintentflagsdemo

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.SparseArray
import com.mnisred.activityintentflagsdemo.event.ActivityDestroyEvent
import org.greenrobot.eventbus.EventBus
import java.util.Stack

/**
 * @Description: 自定义Application
 * 提供ActivityCallBack
 * @author: zhang
 * @date: 2023/6/21
 */
class CustomApplication : Application() {
    val taskMap = SparseArray<Stack<String>>()
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks{
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                pushTask(p0)
            }

            override fun onActivityStarted(p0: Activity) {

            }

            override fun onActivityResumed(p0: Activity) {

            }

            override fun onActivityPaused(p0: Activity) {

            }

            override fun onActivityStopped(p0: Activity) {

            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

            }

            override fun onActivityDestroyed(p0: Activity) {
                popTask(p0)
            }

        })
    }

    /**
     * 将最新activity对应名称投入taskMap中
     */
    private fun pushTask(activity: Activity){
        findTargetTask(activity){key,task->
            //猜测顺序是最新的task放在前面，所以直接遍历然后匹配退出
            //依赖顺序避免两个task出现相同类型的Activity导致map中数据不对应
            if (task.taskInfo.topActivity == activity.componentName){
                taskMap.get(key)?:taskMap.put(key,Stack())
                taskMap.get(key).push(activity::class.simpleName)
                return@findTargetTask
            }

        }
    }

    /**
     * destroy时移除taskMap
     * 因为此时已经删除，task中已经没有对应activity
     * 所以依靠taskMap对比activity来删除
     *
     * 引入EventBus，提示重新resume的Activity的View重新绘制
     * 原因在于上一个Activity的resume在销毁activity的destroy之前
     */
    private fun popTask(activity: Activity){
        findTargetTask(activity){key,task->
            //map里的key和activity对应上，就是要删除的
            val targetName = activity::class.simpleName
            taskMap.get(key)?.let {
                if (it.isEmpty()) return
                if (it.peek() == targetName){
                    it.pop()
                    if (it.isEmpty()) taskMap.remove(key)
                    EventBus.getDefault().post(ActivityDestroyEvent())
                    return@findTargetTask
                }
            }
        }
//        (activity.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)?.let{ manager->
//            for (task in manager.appTasks) {
//                //map里的key和activity对应上，就是要删除的
//                val targetName = activity::class.simpleName
//                val key = task.taskInfo.uniqueId
//
//                taskMap.get(key)?.let {
//                    if (it.isEmpty()) return
//                    if (it.peek() == targetName){
//                        it.pop()
//                        if (it.isEmpty()) taskMap.remove(key)
//                        return
//                    }
//
//                }
//            }
//        }
    }

    companion object{
        lateinit var INSTANCE : CustomApplication
    }
}

/**
 * 用内联+函数类型缩减通用代码
 */
inline fun findTargetTask(activity: Activity,action:(Int,ActivityManager.AppTask)->Unit){
    (activity.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)?.let{ manager->
        for (task in manager.appTasks) {
            val key = task.taskInfo.uniqueId
            action(key,task)
        }
    }
}