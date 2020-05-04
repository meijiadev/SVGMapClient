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
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class MapDetailActivity : DDRActivity() {
    private lateinit var targetPointAdapter: TargetPointAdapter
    private lateinit var notifyBaseStatusExS: NotifyBaseStatusExS
    private lateinit var mapFileStatus: MapFileStatus
    private lateinit var targetPoints: List<TargetPoint>
    //机器人当前位置列表
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
            MessageEvent.Type.getSpecificPoint-> toast("开始前往")
            MessageEvent.Type. getSpecificPoint1->toast("添加任务成功，等待前往")
            MessageEvent.Type. getSpecificPoint2->toast("当前无任务！")
            MessageEvent.Type. getSpecificPoint3->toast("当前没有定位")
            MessageEvent.Type. getSpecificPoint4->toast("生成路径失败")
            MessageEvent.Type. getSpecificPoint5->toast("当前处于自标定")
            MessageEvent.Type. getSpecificPoint8->toast("返回待机点");
            MessageEvent.Type. getSpecificPoint9->toast("完成当前任务，开始时段任务")
            MessageEvent.Type. getSpecificPoint10-> toast("无任务，原地待命")
            MessageEvent.Type. getSpecificPoint11-> toast("开始前往")
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
        PointView.getInstance(context).setTargetPoints1(targetPoints)
        zoomImage.invalidate()

    }

    /**
     * 列表点击时间
     */
    private fun onClickItem(){
        targetPointAdapter.setOnItemClickListener { _, _, position ->
            var targetPoint = targetPoints[position]
            MessageDialog.Builder(this)
                    .setMessage("是否前往目标点")
                    .setListener(object :MessageDialog.OnListener{
                        override fun onConfirm(dialog: BaseDialog?) {
                            toast("即将前往指定目标点")
                            goPointLet(targetPoint,1,CmdSchedule.ROBOT_1,1)
                            goPointLet(targetPoint,1,CmdSchedule.ROBOT_2,1)
                            PointView.getInstance(context).setPoint(targetPoint)
                            zoomImage.invalidate()
                        }
                        override fun onCancel(dialog: BaseDialog?) {
                            toast("取消本次操作")
                        }
                    }).show()

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
            R.id.btGoPoint->{
                MessageDialog.Builder(this)
                        .setMessage("将呼叫距离此处最近的机器人")
                        .setListener(object :MessageDialog.OnListener{
                            override fun onCancel(dialog: BaseDialog?) {
                                toast("取消前往指定点")
                            }
                            override fun onConfirm(dialog: BaseDialog?) {
                                val xyEntity=zoomImage.gaugePoint
                                val targetPoint= TargetPoint()
                                targetPoint.x=xyEntity.x
                                targetPoint.y=xyEntity.y
                                targetPoint.name=getRandomString(2)
                                targetPoint.theta=0
                                val robotName=getDistance(targetPoint)
                                Logger.e("--------距离最近的机器人：$robotName")
                                PointView.getInstance(context).setPoint(targetPoint)
                                goPointLet(targetPoint,1,robotName,0)
                            }

                        }).show()
            }
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
    private fun goPointLet(targetPoint: TargetPoint, type: Int,guid:String,type1: Int) {
        var x=targetPoint.x
        var y=targetPoint.y
        val theta=targetPoint.theta.toFloat()
        val pointName=ByteString.copyFromUtf8(targetPoint.name)
        val eRunSpecificPointTyp: DDRVLNMap.eRunSpecificPointType = when (type) {
            1 -> DDRVLNMap.eRunSpecificPointType.eRunSpecificPointTypeAdd
            2 -> DDRVLNMap.eRunSpecificPointType.eRunSpecificPointTypeResume
            else -> throw IllegalStateException("Unexpected value: $type")
        }
        if (type1==1){ when(guid){CmdSchedule.ROBOT_2-> x -= 1 } }
        val spacePointed = DDRVLNMap.space_pointEx.newBuilder()
                .setX(x)
                .setY(y)
                .setTheta(theta)
                .build()
        val targetPtItem = DDRVLNMap.targetPtItem.newBuilder()
                .setPtName(pointName)
                .setPtData(spacePointed)
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

    /**
     * 找出距离该点最近的设备
     * @param targetPoint 指定的坐标点
     */
    fun getDistance(targetPoint: TargetPoint):String{
        var position=0
        var minDistance=0f
        for (index in locations.indices){
            var distance= sqrt((targetPoint.x-locations[index].x).pow(2)+(targetPoint.y-locations[index].y).pow(2))
            if (index==0){
                minDistance=distance
            }else if (minDistance>=distance){
                minDistance=distance
                position=index
            }
        }
        return locations[position].name
    }

    /**
     * 随机出一个字符串
     * @param length 指定随机的字符串的长度
     */
    fun getRandomString(length: Int): String? {
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = Random()
        val sb = StringBuffer()
        // 等价于 for (int i = 0 ; i <length ; i++)
        for (i in 0 until length) {
            val number: Int = random.nextInt(62)
            sb.append(str[number])
        }
        return sb.toString()
    }


}
