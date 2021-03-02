package com.jiaopeng.gltitleview

import androidx.appcompat.app.AppCompatActivity
import java.util.*


/**
 * 描述：
 *
 * @author JiaoPeng by 2020/9/9
 */
class ActivityContainer private constructor() {

    companion object {
        val instance: ActivityContainer by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityContainer()
        }
    }

    private var activityStack: MutableList<AppCompatActivity>? = LinkedList()

    fun addActivity(aty: AppCompatActivity) {
        activityStack?.add(aty)
    }

    fun removeActivity(aty: AppCompatActivity) {
        activityStack?.remove(aty)
    }

    fun finishAllActivity() {
        activityStack?.let {
            activityStack?.forEach {
                if (!it.isFinishing)
                    it.finish()
                else
                    activityStack?.remove(it)
            }
        }
    }

    fun finishSingleActivity(aty: AppCompatActivity) {
        if (!aty.isFinishing)
            aty.finish()
        else
            activityStack?.remove(aty)
    }

    fun finishTopActivity() {
        val a = activityStack?.size?.minus(1)?.let { activityStack?.get(it) }
        a?.let {
            if (!it.isFinishing)
                a.finish()
            else
                activityStack?.remove(a)
        }
    }

}