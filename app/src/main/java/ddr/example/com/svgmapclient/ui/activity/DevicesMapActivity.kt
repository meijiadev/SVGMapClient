package ddr.example.com.svgmapclient.ui.activity

import android.content.Intent
import android.view.View
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import androidx.recyclerview.widget.GridLayoutManager

import ddr.example.com.svgmapclient.R
import ddr.example.com.svgmapclient.base.BaseDialog
import ddr.example.com.svgmapclient.common.DDRActivity
import ddr.example.com.svgmapclient.entity.MessageEvent
import ddr.example.com.svgmapclient.entity.info.MapFileStatus
import ddr.example.com.svgmapclient.entity.info.MapInfo
import ddr.example.com.svgmapclient.entity.info.NotifyBaseStatusEx
import ddr.example.com.svgmapclient.helper.ActivityStackManager
import ddr.example.com.svgmapclient.other.Logger
import ddr.example.com.svgmapclient.protocobuf.dispatcher.ClientMessageDispatcher
import ddr.example.com.svgmapclient.socket.TcpClient
import ddr.example.com.svgmapclient.ui.adapter.MapAdapter
import ddr.example.com.svgmapclient.ui.dialog.WaitDialog
import kotlinx.android.synthetic.main.activity_devices_map.*

/**
 * time:2020/04/22
 * desc:设备地图
 */
class DevicesMapActivity : DDRActivity() {
    private var mapAdapter: MapAdapter? = null        //地图列表适配器
    private var mapIfs: List<MapInfo>? = null
    private var mapFileStatus: MapFileStatus? = null
    private var waitDialog: BaseDialog? = null
    private var waitDialog1: BaseDialog? = null
    private var tcpClient: TcpClient? = null

    private lateinit var mIntent: Intent


    private var waitRunnable: Runnable? = null


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(messageEvent: MessageEvent) {
        when (messageEvent.type) {
            MessageEvent.Type.updateMapList -> postDelayed({
                if (waitDialog != null) {
                    if (waitDialog!!.isShowing) {
                        cancleDelay(waitRunnable)
                        waitDialog!!.dismiss()
                    }
                }
                Logger.e("当前正在运行的地图：" + NotifyBaseStatusEx.getInstance().curroute)
                mapIfs = mapFileStatus!!.mapInfos
                mapAdapter!!.setNewData(mapIfs)
            }, 500)
            MessageEvent.Type.updateDDRVLNMap -> {
                if (ActivityStackManager.getInstance().topActivity == this){
                    if (waitDialog1 != null) {
                        if (waitDialog1!!.isShowing) {
                            cancleDelay(waitRunnable)
                            waitDialog1!!.dismiss()
                        }
                    }
                    toast("加载成功！")
                    startActivity(mIntent)
                }
            }
            else-> Logger.d("Nothing to update!")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_devices_map
    }

    override fun initView() {
        super.initView()
        tcpClient = TcpClient.getInstance(context, ClientMessageDispatcher.getInstance())
        mapFileStatus = MapFileStatus.getInstance()
        mapIfs = mapFileStatus!!.mapInfos
        mapAdapter = MapAdapter(R.layout.item_map_recycler, context)
        val gridLayoutManager = GridLayoutManager(this, 4)
        recyclerMap!!.layoutManager = gridLayoutManager
        recyclerMap!!.adapter = mapAdapter
        mapAdapter!!.setNewData(mapIfs)
        showLoadDialog()

    }

    override fun initData() {
        super.initData()
        onItemClick()
    }


    /**
     * 列表点击事件
     */
    private fun onItemClick() {
        mapAdapter!!.setOnItemClickListener { _, _, position ->
            tcpClient!!.getMapInfo(mapIfs!![position].mapName)
            waitDialog1 = WaitDialog.Builder(this)
                    .setMessage("正在加载地图信息...")
                    .show()
            mIntent = Intent(this@DevicesMapActivity, MapDetailActivity::class.java)
            mIntent.putExtra("mapIndex", position)
            waitRunnable= Runnable {
                if (waitDialog1 != null) {
                    waitDialog1!!.dismiss()
                    toast("加载失败，请重试！")
                }

            }
            postDelayed(waitRunnable, 30000)
        }
    }

    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.iv_back -> finish()
        }
    }

    private fun showLoadDialog() {
        if (mapIfs!!.isEmpty()) {
            waitDialog = WaitDialog.Builder(this)
                    .setMessage("地图正在加载中...")
                    .show()
            waitRunnable = Runnable {
                if (waitDialog != null) {
                    waitDialog!!.dismiss()
                    toast("加载失败，请重试！")
                }
            }
            postDelayed(waitRunnable, 32000)
        }
    }
}
