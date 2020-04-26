package ddr.example.com.svgmapclient.ui.activity

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.View
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import ddr.example.com.svgmapclient.R
import ddr.example.com.svgmapclient.base.BaseDialog
import ddr.example.com.svgmapclient.common.DDRActivity
import ddr.example.com.svgmapclient.entity.MessageEvent
import ddr.example.com.svgmapclient.other.Logger
import ddr.example.com.svgmapclient.protocobuf.CmdSchedule
import ddr.example.com.svgmapclient.protocobuf.dispatcher.ClientMessageDispatcher
import ddr.example.com.svgmapclient.socket.TcpClient
import ddr.example.com.svgmapclient.socket.UdpClient
import ddr.example.com.svgmapclient.ui.dialog.WaitDialog
import kotlinx.android.synthetic.main.activity_login.*


/**
 * time   : 2019/10/26
 * desc   : 登录页
 */
class LoginActivity : DDRActivity() {
    var tcpPort = 0
    private var accountName = ""
    private var passwordName = ""
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    var tcpClient: TcpClient? = null
    private var waitDialog: BaseDialog? = null


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun update(messageEvent: MessageEvent) {
        when (messageEvent.type) {
            MessageEvent.Type.wanLoginSuccess -> {
                UdpClient.getInstance(context, ClientMessageDispatcher.getInstance()).close()
                editor!!.putString("password", passwordName)
                editor!!.commit()
                Logger.e("广域网登录成功")
                postDelayed({
                    if (waitDialog != null && waitDialog!!.isShowing) {
                        waitDialog!!.dismiss()
                    }
                    startActivity(ChinaMapActivity::class.java)
                }, 1000)
            }
            MessageEvent.Type.tcpConnected -> {
                Logger.e("--------tcp连接成功")
                tcpClient!!.sendData(null, CmdSchedule.remoteLogin(accountName, passwordName))
            }
            else-> Logger.d("Nothing to update ")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        //判断是否注册监听
        if (!EventBus.getDefault().isRegistered(this)){
            Logger.d("EvenBus未注册")
            EventBus.getDefault().register(this)
        }
    }

    override fun initData() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = sharedPreferences!!.edit()
        password!!.setText(sharedPreferences!!.getString("password", ""))
        tcpClient = TcpClient.getInstance(context, ClientMessageDispatcher.getInstance())

    }

    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.login_in -> {
                accountName = account!!.text.toString().trim { it <= ' ' }
                passwordName = password!!.text.toString().trim { it <= ' ' }
                if ((accountName == "") and (passwordName == "")) {
                    toast("用户名和密码不能为空")
                } else {
                    if (tcpClient!!.isConnected) {
                        tcpClient!!.disConnect()
                    } else {
                        tcpClient!!.createConnect(CmdSchedule.broadcastServerIP, CmdSchedule.broadcastServerPort)      //连接地方服务器
                        waitDialog = WaitDialog.Builder(this)
                                .setMessage("登录中...")
                                .show()
                        postDelayed({
                            if (waitDialog!!.isShowing) {
                                toast("登录失败，请检查网络后重新登录")
                                waitDialog!!.dismiss()
                            }
                        }, 5000)
                    }
                }
            }
        }
    }


    override fun onRestart() {
        super.onRestart()

    }

    override fun onDestroy() {
        super.onDestroy()
        tcpClient = null
        EventBus.getDefault().removeAllStickyEvents()
        if (EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        tcpClient!!.disConnect()
    }

    /**
     * 状态栏是否启动深色字体
     * @return false 不启动
     */
    override fun statusBarDarkFont(): Boolean {
        return false
    }

}