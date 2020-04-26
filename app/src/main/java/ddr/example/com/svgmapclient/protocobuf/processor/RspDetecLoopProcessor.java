package ddr.example.com.svgmapclient.protocobuf.processor;

import android.content.Context;

import com.google.protobuf.GeneratedMessageLite;

import org.greenrobot.eventbus.EventBus;

import DDRCommProto.BaseCmd;
import ddr.example.com.svgmapclient.entity.MessageEvent;

/**
 * time： 2020/02/20
 * desc: 接收回环检测的结果
 */
public class RspDetecLoopProcessor extends BaseProcessor {
    private int loopStatus;
    @Override
    public void process(Context context, BaseCmd.CommonHeader commonHeader, GeneratedMessageLite msg) {
        super.process(context, commonHeader, msg);
        BaseCmd.rspDetectLoop rspDetectLoop= (BaseCmd.rspDetectLoop) msg;
        loopStatus=rspDetectLoop.getLoopStat();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.updateDetectionLoopStatus,loopStatus));

    }
}
