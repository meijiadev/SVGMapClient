package ddr.example.com.svgmapclient.protocobuf.dispatcher;

import android.content.Context;

import com.google.protobuf.GeneratedMessageLite;

import java.util.HashMap;
import java.util.Map;

import DDRCommProto.BaseCmd;
import ddr.example.com.svgmapclient.other.Logger;
import ddr.example.com.svgmapclient.protocobuf.GuIdInfo;
import ddr.example.com.svgmapclient.protocobuf.processor.BaseProcessor;

/**
 * 事件分发基类
 */
public class BaseMessageDispatcher {
    protected Map<String,BaseProcessor> m_ProcessorMap=new HashMap<>();

    public void dispatcher(Context context, BaseCmd.CommonHeader commonHeader, String typeName, GeneratedMessageLite msg){
        if (m_ProcessorMap.containsKey(typeName)){
            m_ProcessorMap.get(typeName).process(context,commonHeader,msg);
            GuIdInfo.getInstance().setGuId(commonHeader.getGuid());
        }

    }
}
