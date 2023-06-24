package com.mnisred.activityintentflagsdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mnisred.activityintentflagsdemo.databinding.NormalActivityBinding
import com.mnisred.activityintentflagsdemo.setLaunchClickListener
import com.mnisred.activityintentflagsdemo.view.StackContentView

/**
 * @Description: 启动模式为单任务模式的Activity
 * 一般都是其他应用或者桌面启动的Activity
 * @author: zhang
 * @date: 2023/6/21
 */
class SingleTaskActivity : BaseActivity(){
    private lateinit var binding : NormalActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NormalActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setLaunchClickListener(this)
    }

    companion object{
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context,SingleTaskActivity::class.java)
            context.startActivity(starter)
        }
    }
}