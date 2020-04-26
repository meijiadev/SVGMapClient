package ddr.example.com.svgmapclient.protocobuf.processor;

import android.content.Context;

import com.google.protobuf.GeneratedMessageLite;

import org.greenrobot.eventbus.EventBus;

import DDRCommProto.BaseCmd;
import ddr.example.com.svgmapclient.entity.MessageEvent;
import ddr.example.com.svgmapclient.other.Logger;

/**
 * time ：2019/11/21
 * desc ：修改地图返回结果
 */
public class RspMapOperationalProcessor extends BaseProcessor {
    @Override
    public void process(Context context, BaseCmd.CommonHeader commonHeader, GeneratedMessageLite msg) {
        super.process(context, commonHeader, msg);
        Logger.e("地图修改成功");
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.mapOperationalSucceed));
    }
}
