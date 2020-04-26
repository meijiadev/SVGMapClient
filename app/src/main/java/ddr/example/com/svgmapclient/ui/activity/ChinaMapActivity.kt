package ddr.example.com.svgmapclient.ui.activity

import android.view.View

import java.util.ArrayList

import androidx.recyclerview.widget.LinearLayoutManager
import ddr.example.com.svgmapclient.R
import ddr.example.com.svgmapclient.common.DDRActivity
import ddr.example.com.svgmapclient.protocobuf.dispatcher.ClientMessageDispatcher
import ddr.example.com.svgmapclient.socket.TcpClient
import ddr.example.com.svgmapclient.ui.adapter.StringAdapter
import ddr.example.com.svgmapclient.widget.view.ChinaMapView
import kotlinx.android.synthetic.main.activity_china_map.*

/**
 * time : 2020/04/22
 * desc : 区域地图
 */
class ChinaMapActivity : DDRActivity(), ChinaMapView.OnProvinceSelectedListener {
    private var stringAdapter: StringAdapter? = null
    private var tcpClient: TcpClient? = null


    private val areaList = ArrayList<String>()
    override fun getLayoutId(): Int {
        return R.layout.activity_china_map
    }

    override fun initView() {
        super.initView()
        val linearLayoutManager = LinearLayoutManager(this)
        recycler_devices!!.layoutManager = linearLayoutManager
        stringAdapter = StringAdapter(R.layout.item_area_devices)
        recycler_devices!!.adapter = stringAdapter
    }

    override fun initData() {
        super.initData()
        tcpClient = TcpClient.getInstance(context, ClientMessageDispatcher.getInstance())
        setAreaList()
        chinaView!!.setOnProvinceSelectedListener(this)
        stringAdapter!!.setNewData(areaList)
        onItemClick()
        tcpClient!!.requestFile1()
    }



    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.iv_back -> finish()
        }
    }

    private fun onItemClick() {
        stringAdapter!!.setOnItemClickListener { _, _, position ->
            if (position == 0) {
                startActivity(DevicesMapActivity::class.java)
            } else {
                toast("当前区域无设备连线")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onprovinceSelected(pArea: ChinaMapView.Area) {
        if (pArea.getName() == "北京") {
            toast(pArea.getName() + "当前设备两台")
        } else {
            toast(pArea.getName() + "当前无设备")
        }
    }


    private fun setAreaList() {
        val areas = chinaView!!.areas
        for (i in 0 until areas) {
            if (i == 0) {
                areaList.add(ChinaMapView.Area.valueOf(0)!!.getName() + "(2)")
            } else {
                areaList.add(ChinaMapView.Area.valueOf(i)!!.getName())
            }
        }
    }
}
