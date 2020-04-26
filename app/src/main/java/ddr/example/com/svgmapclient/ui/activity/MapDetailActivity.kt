package ddr.example.com.svgmapclient.ui.activity

import DDRCommProto.BaseCmd
import DDRVLNMapProto.DDRVLNMap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.protobuf.ByteString
import ddr.example.com.svgmapclient.R
import ddr.example.com.svgmapclient.base.BaseDialog
import ddr.example.com.svgmapclient.common.DDRActivity
import ddr.example.com.svgmapclient.entity.MessageEvent
import ddr.example.com.svgmapclient.entity.info.MapFileStatus
import ddr.example.com.svgmapclient.entity.info.NotifyBaseStatusEx
import ddr.example.com.svgmapclient.entity.info.NotifyBaseStatusExS
import ddr.example.com.svgmapclient.entity.point.TargetPoint
import ddr.example.com.svgmapclient.other.Logger
import ddr.example.com.svgmapclient.protocobuf.CmdSchedule
import ddr.example.com.svgmapclient.socket.TcpClient
import ddr.example.com.svgmapclient.ui.adapter.TargetPointAdapter
import ddr.example.com.svgmapclient.ui.dialog.MessageDialog
import ddr.example.com.svgmapclient.widget.view.PointView

import kotlinx.android.synthetic.main.activity_map_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

class MapDetailActivity : DDRActivity() {
    private lateinit var targetPointAdapter: TargetPointAdapter
    private lateinit var notifyBaseStatusExS: NotifyBaseStatusExS
    private lateinit var mapFileStatus: MapFileStatus
    private lateinit var targetPoints: List<TargetPoint>
    private lateinit var locations:MutableList<TargetPoint>
    private lateinit var notifyStatusList: MutableList<NotifyBaseStatusEx>
    private lateinit var tcpClient:TcpClient
    private lateinit var mapName:String



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(messageEvent: MessageEvent){
        when(messageEvent.type){
            MessageEvent.Type.updateBaseStatus ->{
                locations= mutableListOf()
                notifyStatusList=notifyBaseStatusExS.notifyBaseStatusExes
                for (i in notifyStatusList.indices){
                    var targetPoint=TargetPoint()
                    targetPoint.name=notifyStatusList[i].guId
                    targetPoint.x=notifyStatusList[i].posX
                    targetPoint.y=notifyStatusList[i].posY
                    targetPoint.theta= notifyStatusList[i].posDirection.toInt()
                    locations.add(targetPoint)
                }
                PointView.getInstance(context).setTargetPoints(locations)
                zoomImage.invalidate()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_map_detail
    }

    override fun initView() {
        super.initView()
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        targetPointAdapter = TargetPointAdapter(R.layout.item_area_devices)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerTarget.layoutManager = linearLayoutManager
        recyclerTarget.adapter = targetPointAdapter
        onClickItem()
    }

    override fun initData() {
        super.initData()
        tcpClient= TcpClient.tcpClient
        notifyBaseStatusExS=NotifyBaseStatusExS.getInstance()
        mapFileStatus = MapFileStatus.getInstance()
        targetPoints = mapFileStatus.targetPoints
        Logger.e("-----------:"+targetPoints.size)
        targetPointAdapter!!.setNewData(targetPoints)
        var index = intent!!.getIntExtra("mapIndex",0)
        mapName=mapFileStatus.mapInfos[index].mapName
        tvTitle.text=mapName
        val bitmaps = mapFileStatus.mapInfos[index].bytes
        val bitmap = getBitmapFromByte(bitmaps)
        zoomImage!!.setBitmap(bitmap)
        zoomImage.isRotation(false)

    }

    /**
     * 列表点击时间
     */
    private fun onClickItem(){
        targetPointAdapter.setOnItemClickListener { _, _, position -> 9
            var targetPoint = targetPoints[position]
            MessageDialog.Builder(this)
                    .setMessage("是否前往目标点")
                    .setListener(object :MessageDialog.OnListener{
                        override fun onConfirm(dialog: BaseDialog?) {
                            toast("即将前往指定目标点")
                            goPointLet(targetPoint,1,CmdSchedule.ROBOT_1)
                            goPointLet(targetPoint,1,CmdSchedule.ROBOT_4)
                            PointView.getInstance(context).setPoint(targetPoint)
                            zoomImage.invalidate()
                        }

                        override fun onCancel(dialog: BaseDialog?) {
                            toast("取消本次操作")
                        }
                    })
                    .show()

        }
    }


    /**
     * 字节流转图片
     */
    private fun getBitmapFromByte(temp: ByteArray?): Bitmap? {
        return if (temp != null) {
            BitmapFactory.decodeByteArray(temp, 0, temp.size)
        } else {
            null
        }
    }


    /**
     * 点击事件
     * 连接Xml里面的相关控件
     */
     fun onClickView(view: View) {
        when(view.id){
            R.id.iv_back->finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
        PointView.getInstance(context).clearDraw()

    }

    /**
     * 导航去目标点或者恢复
     * @param x
     * @param y
     * @param theta
     * @param pname
     * @param routeName
     */

    private fun goPointLet(targetPoint: TargetPoint, type: Int,guid:String) {
        val x=targetPoint.x
        val y=targetPoint.y
        val theta=targetPoint.theta.toFloat()
        val pname=ByteString.copyFromUtf8(targetPoint.name)
        val eRunSpecificPointTyp: DDRVLNMap.eRunSpecificPointType
        when (type) {
            1 -> eRunSpecificPointTyp = DDRVLNMap.eRunSpecificPointType.eRunSpecificPointTypeAdd
            2 -> eRunSpecificPointTyp = DDRVLNMap.eRunSpecificPointType.eRunSpecificPointTypeResume
            else -> throw IllegalStateException("Unexpected value: $type")
        }
        val space_pointEx = DDRVLNMap.space_pointEx.newBuilder()
                .setX(x)
                .setY(y)
                .setTheta(theta)
                .build()
        val targetPtItem = DDRVLNMap.targetPtItem.newBuilder()
                .setPtName(pname)
                .setPtData(space_pointEx)
                .build()
        val targetPtItemList = ArrayList<DDRVLNMap.targetPtItem>()
        targetPtItemList.add(targetPtItem)
        val reqRunSpecificPoint = DDRVLNMap.reqRunSpecificPoint.newBuilder()
                .setOnerouteName(ByteString.copyFromUtf8(mapName))
                .addAllTargetPt(targetPtItemList)
                .setBIsDynamicOA(true)
                .setOptType(eRunSpecificPointTyp)
                .build()
        tcpClient.sendData(CmdSchedule.commonHeader1(BaseCmd.eCltType.eModuleServer,guid), reqRunSpecificPoint)

    }


}
