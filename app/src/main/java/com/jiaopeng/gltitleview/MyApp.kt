package com.jiaopeng.gltitleview

import android.app.Application
import android.widget.Toast

/**
 * 描述：
 *
 * @author JiaoPeng by 3/2/21
 */
class MyApp  : Application() {

    override fun onCreate() {
        super.onCreate()

        GLTitleView.initDefaultStartViewClickListener {
            Toast.makeText(this, "application", Toast.LENGTH_SHORT).show()
            ActivityContainer.instance.finishTopActivity()
        }
    }

}