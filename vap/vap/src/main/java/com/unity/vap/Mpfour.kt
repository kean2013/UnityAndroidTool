package com.unity.vap

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewGroup
import com.tencent.qgame.animplayer.AnimConfig
import com.tencent.qgame.animplayer.AnimView
import com.tencent.qgame.animplayer.inter.IAnimListener
import com.tencent.qgame.animplayer.util.ALog
import com.tencent.qgame.animplayer.util.IALog
import com.tencent.qgame.animplayer.util.ScaleType
import java.io.File
import kotlin.math.log


class Mpfour : IAnimListener{

    data class VideoInfo(val fileName: String,val md5:String)

    private val TAG = "Mpfour"
    public  fun  play(activity: Activity, name: String, dir: String, mdfive: String)
    {
        val display: Display = activity.window.windowManager.defaultDisplay
        val para: ViewGroup.LayoutParams = ViewGroup.LayoutParams(display.width, display.height)
        Log.d(TAG, display.width.toString())
        Log.d(TAG, display.height.toString())

        animView = AnimView(activity.applicationContext)
        animView.layoutParams = para
        animView.visibility = View.VISIBLE
        Log.d(TAG, animView.visibility.toString())
        Log.d(TAG, animView.width.toString())
        Log.d(TAG, animView.height.toString())

        loadFile(activity, name, dir, mdfive)
    }
    // 动画View
    private lateinit var animView: AnimView

    private val uiHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    private fun init(name:String, dir: String, mdfive: String) {
        // 初始化日志
        initLog()
        // 居中（根据父布局按比例居中并全部显示，默认fitXY）
        animView.setScaleType(ScaleType.FIT_CENTER)
        // 注册动画监听
        animView.setAnimListener(this)

        /**
         * 开始播放主流程
         * ps: 主要流程都是对AnimView的操作，其它比如队列，或改变窗口大小等操作都不是必须的
         */
        playnow("$dir/$name", mdfive)
    }


    private fun playnow(filepath:String, mdfive: String) {
        // 播放前强烈建议检查文件的md5是否有改变
        // 因为下载或文件存储过程中会出现文件损坏，导致无法播放
        Thread {
            val file = File(filepath)
            val md5 = FileUtil.getFileMD5(file)
            if (file != null/*mdfive == md5*/) {
                // 开始播放动画文件
                animView.startPlay(file)
            } else {
                Log.e(TAG, "md5 is not match, error md5=$md5")
            }
        }.start()
    }


    /**
     * 视频信息准备好后的回调，用于检查视频准备好后是否继续播放
     * @return true 继续播放 false 停止播放
     */
    override fun onVideoConfigReady(config: AnimConfig): Boolean {
        return true
    }

    /**
     * 视频开始回调
     */
    override fun onVideoStart() {
        Log.i(TAG, "onVideoStart")
    }

    /**
     * 视频渲染每一帧时的回调
     * @param frameIndex 帧索引
     */
    override fun onVideoRender(frameIndex: Int, config: AnimConfig?) {
    }

    /**
     * 视频播放结束(失败也会回调onComplete)
     */
    override fun onVideoComplete() {
        Log.i(TAG, "onVideoComplete")
    }

    /**
     * 播放器被销毁情况下会调用onVideoDestroy
     */
    override fun onVideoDestroy() {
        Log.i(TAG, "onVideoDestroy")
    }

    /**
     * 失败回调
     * 一次播放时可能会调用多次，建议onFailed只做错误上报
     * @param errorType 错误类型
     * @param errorMsg 错误消息
     */
    override fun onFailed(errorType: Int, errorMsg: String?) {
        Log.i(TAG, "onFailed errorType=$errorType errorMsg=$errorMsg")
    }

    private fun initLog() {
        ALog.isDebug = false
        ALog.log = object : IALog {
            override fun i(tag: String, msg: String) {
                Log.i(tag, msg)
            }

            override fun d(tag: String, msg: String) {
                Log.d(tag, msg)
            }

            override fun e(tag: String, msg: String) {
                Log.e(tag, msg)
            }

            override fun e(tag: String, msg: String, tr: Throwable) {
                Log.e(tag, msg, tr)
            }
        }
    }

    private fun loadFile(activity:Activity, name: String, dir: String, mdfive: String) {
        val files = Array(1) {
            name
        }
        FileUtil.copyAssetsToStorage(activity, dir, files) {
            uiHandler.post {
                init(name, dir, mdfive)
            }
        }
    }


    private fun dp2px(context: Context, dp: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dp * scale + 0.5f
    }
}