package ddr.example.com.svgmapclient.protocobuf.processor;

import android.content.Context;

import com.google.protobuf.GeneratedMessageLite;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import DDRCommProto.BaseCmd;
import ddr.example.com.svgmapclient.entity.MessageEvent;
import ddr.example.com.svgmapclient.entity.other.Parameter;
import ddr.example.com.svgmapclient.entity.other.Parameters;

public class RspGetParameterProcessor extends BaseProcessor{
    private Parameters parameters;
    private List<Parameter> parameterList;

    @Override
    public void process(Context context, BaseCmd.CommonHeader commonHeader, GeneratedMessageLite msg) {
        super.process(context, commonHeader, msg);
        BaseCmd.rspConfigOperational rspConfigOperational= (BaseCmd.rspConfigOperational) msg;
        List<BaseCmd.configData> configDataList=rspConfigOperational.getDataList();
        parameters= Parameters.getInstance();
        parameterList=new ArrayList<>();
        //Logger.e("接受数量"+configDataList.size());
        for (int i=0;i<configDataList.size();i++){
            //Logger.e("key"+configDataList.get(1).getData().getKey());
            Parameter parameter=new Parameter();
            parameter.setKey(configDataList.get(i).getData().getKey().toStringUtf8());
            parameter.setValue(configDataList.get(i).getData().getValue().toStringUtf8());
            parameter.setdValue(configDataList.get(i).getData().getDefauleValue().toStringUtf8());
            parameter.setWritable(configDataList.get(i).getData().getWritable().toStringUtf8());
            parameter.setAlias(configDataList.get(i).getData().getAlias().toStringUtf8());
            parameterList.add(parameter);
        }
        parameters.setParameterList(parameterList);
        //Logger.e("实际数量"+parameters.getParameterList().size());
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.updateParameter));
    }
}
