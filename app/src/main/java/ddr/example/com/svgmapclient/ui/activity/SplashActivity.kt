package ddr.example.com.svgmapclient.ui.activity

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.gyf.immersionbar.BarHide
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import java.io.IOException
import ddr.example.com.svgmapclient.R
import ddr.example.com.svgmapclient.common.DDRActivity
import ddr.example.com.svgmapclient.protocobuf.dispatcher.ClientMessageDispatcher
import ddr.example.com.svgmapclient.socket.UdpClient
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * time:2019/10/26
 * desc:闪屏页面
 */
class SplashActivity : DDRActivity(), OnPermission, Animation.AnimationListener {
    private val port = 28888
    private lateinit var udpClient:UdpClient
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
        val alphaAnimation = AlphaAnimation(0.4f, 1.0f)
        alphaAnimation.duration = ANIM_TIME.toLong()
        alphaAnimation.setAnimationListener(this)
        iv_splash.startAnimation(alphaAnimation)
        statusBarConfig.hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
                .init()

    }

    /**
     * 沉浸式状态栏（已适配 ）
     */
    override fun initState(activity: Activity) {
        //Logger.e("启动沉浸式状态栏");
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        val actionBar = supportActionBar
        actionBar!!.hide()

    }

    /**
     * 接收广播
     */
    private fun receiveBroadcast() {
         udpClient = UdpClient.getInstance(this, ClientMessageDispatcher.getInstance())
        try {
            udpClient.connect(port)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    override fun onBackPressed() {
        //禁用返回键
        //super.onBackPressed();

    }


    /**
     * 请求权限
     */
    private fun requestPermission() {
        XXPermissions.with(this)
                .permission(*Permission.Group.STORAGE)
                .request(this)
    }

    /**
     * 权限通过
     * @param granted
     * @param isAll
     */
    override fun hasPermission(granted: List<String>?, isAll: Boolean) {
        startActivityFinish(LoginActivity::class.java)
    }

    override fun noPermission(denied: List<String>, quick: Boolean) {
        if (quick) {
            toast("授权失败")
            XXPermissions.gotoPermissionSettings(this@SplashActivity, true)
        } else {
            toast("请先授予权限")
            postDelayed({ this.requestPermission() }, 1000)
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (XXPermissions.isHasPermission(this@SplashActivity, *Permission.Group.STORAGE)) {
            hasPermission(null, true)
        } else {
            requestPermission()
        }
    }

    override fun onAnimationStart(animation: Animation) {

    }

    override fun onAnimationEnd(animation: Animation) {
        requestPermission()
    }

    override fun onAnimationRepeat(animation: Animation) {

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    companion object {
        private val ANIM_TIME = 1500
    }

}
