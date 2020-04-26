package ddr.example.com.svgmapclient.protocobuf.processor;

import android.content.Context;

import com.google.protobuf.GeneratedMessageLite;

import org.greenrobot.eventbus.EventBus;

import DDRCommProto.BaseCmd;
import DDRVLNMapProto.DDRVLNMap;
import ddr.example.com.svgmapclient.entity.MessageEvent;
import ddr.example.com.svgmapclient.entity.info.MapFileStatus;
import ddr.example.com.svgmapclient.other.Logger;

/**
 * 获取某一地图下的信息
 */
public class RspGetDDRVLNMapExProcessor extends BaseProcessor {
    @Override
    public void process(Context context, BaseCmd.CommonHeader commonHeader, GeneratedMessageLite msg) {
        super.process(context, commonHeader, msg);
        Logger.e("-----接收地图信息");
        DDRVLNMap.rspGetDDRVLNMapEx rspGetDDRVLNMapEx= (DDRVLNMap.rspGetDDRVLNMapEx) msg;
        MapFileStatus mapFileStatus=MapFileStatus.getInstance();
        mapFileStatus.setRspGetDDRVLNMap(rspGetDDRVLNMapEx);
        EventBus.getDefault().postSticky(new MessageEvent(MessageEvent.Type.updateDDRVLNMap));
    }
}
