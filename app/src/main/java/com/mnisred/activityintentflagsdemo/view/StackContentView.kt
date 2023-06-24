package com.mnisred.activityintentflagsdemo.view

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.core.util.keyIterator
import com.mnisred.activityintentflagsdemo.CustomApplication
import com.mnisred.activityintentflagsdemo.R
import com.mnisred.activityintentflagsdemo.dp
import com.mnisred.activityintentflagsdemo.event.ActivityDestroyEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Stack

/**
 * @Description: 显示 Task Stack和内部的task
 * 该View需要直接设置给Activity用于获取对应的活动和栈名称
 * @author: zhang
 * @date: 2023/6/21
 */
class StackContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    defStyleRes: Int = R.style.StackContentViewDefaultStyle
) : View(context, attrs, defStyleAttrs, defStyleRes) {
    private val paint = Paint()
    //单个任务栈宽度
    private val taskWidth = 150.dp
    //栈之间间隔
    private val taskPadding = 15.dp
    //栈底和View底部的间隔
    private val taskMarginBottom = 20.dp

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.dp
        paint.textSize = 13.dp
        //drawText出来太粗了，加一下字间距方便看清
        paint.letterSpacing = 0.1f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            drawImage(canvas)
        }
    }

    /**
     * 绘制当前栈
     */
    private fun drawImage(canvas: Canvas) {
        val totalWidth = CustomApplication.INSTANCE.taskMap.size() * (taskWidth + taskPadding) - taskPadding
        var startPosition = (width - totalWidth)/2
        val lineHeight = paint.fontMetrics.bottom - paint.fontMetrics.top

        //绘制栈内部Activity名称
        val drawActivity : (Stack<String>)->Unit = {names->
            var positionY = height - taskMarginBottom
            names.forEach {
                canvas.drawText(it,startPosition,positionY,paint)
                //因为是栈，所以后面元素在上方，纵坐标向上为减
                positionY -= lineHeight
            }
        }
        //绘制Task
        val drawFunc: (Int, Stack<String>) -> Unit = { key, names ->
            canvas.drawText(key.toString(), startPosition, lineHeight, paint)
            canvas.drawRect(startPosition, taskMarginBottom, startPosition+taskWidth, height - taskMarginBottom, paint)
            drawActivity(names)
            startPosition += taskWidth+taskPadding
        }
        CustomApplication.INSTANCE.taskMap.keyIterator().forEach {
            drawFunc(it, CustomApplication.INSTANCE.taskMap[it])
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNotifyDestroy(event:ActivityDestroyEvent){
        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        EventBus.getDefault().register(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        EventBus.getDefault().unregister(this)
    }

}