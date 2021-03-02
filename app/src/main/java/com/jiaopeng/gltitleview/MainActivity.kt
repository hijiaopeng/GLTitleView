package com.jiaopeng.gltitleview

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //透明状态栏
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        setContentView(R.layout.activity_main)

        ActivityContainer.instance.addActivity(this)

        glTitle?.startViewClickListener = {
            Toast.makeText(this@MainActivity, "左侧点击", Toast.LENGTH_SHORT).show()
        }

        glTitle?.endViewClickListener = {}

        glTitle?.setImmersive(true)

//        glTitle?.customizeView?.findViewById<TextView>(R.id.tv)?.text = title

        btn?.setOnClickListener {
            glTitle?.isShowBottomLine = false
        }

        show?.setOnClickListener {
            glTitle?.isShowBottomLine = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityContainer.instance.removeActivity(this)
    }
}