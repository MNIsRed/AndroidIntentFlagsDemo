package com.mnisred.activityintentflagsdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mnisred.activityintentflagsdemo.databinding.NormalActivityBinding
import com.mnisred.activityintentflagsdemo.setLaunchClickListener

/**
 * @Description: 启动模式-Standard
 * @author: zhang
 * @date: 2023/6/24
 */
class StandardActivity : BaseActivity(){
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
            val starter = Intent(context,StandardActivity::class.java)
            context.startActivity(starter)
        }

        @JvmStatic
        fun startNewTask(context: Context){
            val starter = Intent(context,StandardActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(starter)
        }
    }
}