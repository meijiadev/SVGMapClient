package ddr.example.com.svgmapclient.protocobuf.processor;

import android.content.Context;

import com.google.protobuf.GeneratedMessageLite;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import DDRCommProto.BaseCmd;
import ddr.example.com.svgmapclient.entity.MessageEvent;
import ddr.example.com.svgmapclient.entity.info.NotifyBaseStatusEx;
import ddr.example.com.svgmapclient.entity.info.NotifyBaseStatusExS;
import ddr.example.com.svgmapclient.other.Logger;
import ddr.example.com.svgmapclient.protocobuf.CmdSchedule;


public class NotifyBaseStatusExProcessor extends BaseProcessor {
    @Override
    public void process(Context context, BaseCmd.CommonHeader commonHeader, GeneratedMessageLite msg) {
        super.process(context, commonHeader, msg);
        String guid=commonHeader.getGuid();
        NotifyBaseStatusExS notifyBaseStatusExS=NotifyBaseStatusExS.getInstance();
        List<NotifyBaseStatusEx> notifyBaseStatusExList=notifyBaseStatusExS.getNotifyBaseStatusExes();
        BaseCmd.notifyBaseStatusEx notifyBaseStatusEx= (BaseCmd.notifyBaseStatusEx) msg;
        NotifyBaseStatusEx notifyBaseStatusEx1=new NotifyBaseStatusEx();
        notifyBaseStatusEx1.setGuId(guid);
        notifyBaseStatusEx1.setCurroute(notifyBaseStatusEx.getCurrroute().toStringUtf8());
        notifyBaseStatusEx1.setCurrpath(notifyBaseStatusEx.getCurrpath().toStringUtf8());
        notifyBaseStatusEx1.setMode(notifyBaseStatusEx.getModeValue());
        notifyBaseStatusEx1.setSonMode(notifyBaseStatusEx.getSonmodeValue());
        notifyBaseStatusEx1.seteDynamicOAStatus(notifyBaseStatusEx.getDynamicoaValue());
        notifyBaseStatusEx1.setStopStat(notifyBaseStatusEx.getStopstat());
        notifyBaseStatusEx1.setPosAngulauspeed(notifyBaseStatusEx.getPosangulauspeed());
        notifyBaseStatusEx1.setPosDirection(notifyBaseStatusEx.getPosdirection());
        notifyBaseStatusEx1.setPosLinespeed(notifyBaseStatusEx.getPoslinespeed());
        notifyBaseStatusEx1.setPosX(notifyBaseStatusEx.getPosx());
        notifyBaseStatusEx1.setPosY(notifyBaseStatusEx.getPosy());
        notifyBaseStatusEx1.seteSelfCalibStatus(notifyBaseStatusEx.getSelfcalibstatusValue());
        notifyBaseStatusEx1.setChargingStatus(notifyBaseStatusEx.getChargingStatus());
        notifyBaseStatusEx1.setTaskCount(notifyBaseStatusEx.getTaskCount());
        notifyBaseStatusEx1.setTaskDuration(notifyBaseStatusEx.getTaskDuration());
        notifyBaseStatusEx1.setExceptionValue(notifyBaseStatusEx.getRobotexceptionValue());
        notifyBaseStatusEx1.setLocationed(notifyBaseStatusEx.getBLocated());
        notifyBaseStatusEx1.setChargingType(notifyBaseStatusEx.getChargingTypeValue());
        notifyBaseStatusEx1.seteTaskMode(notifyBaseStatusEx.getTaskmodeValue());
        notifyBaseStatusEx1.setTemopTaskNum(notifyBaseStatusEx.getTemporaryTaskCount());
        EventBus.getDefault().postSticky(new MessageEvent(MessageEvent.Type.updateBaseStatus));
        if (notifyBaseStatusExList.size()==0){
            notifyBaseStatusExList.add(notifyBaseStatusEx1);
        }else {
            int size=notifyBaseStatusExList.size();
            List<String> guidlist=new ArrayList<>();
            for (NotifyBaseStatusEx notifyBaseStatusEx2:notifyBaseStatusExList){
                guidlist.add(notifyBaseStatusEx2.getGuId());
            }
            if (guidlist.contains(guid)){
                for (int i=0;i<size;i++){
                    if (guidlist.get(i).equals(guid)){
                        notifyBaseStatusExList.set(i,notifyBaseStatusEx1);
                    }
                }
            }else {
                notifyBaseStatusExList.add(notifyBaseStatusEx1);
            }
        }
        notifyBaseStatusExS.setNotifyBaseStatusExS(notifyBaseStatusExList);
        if (guid.equals(CmdSchedule.ROBOT_1)){
            NotifyBaseStatusEx.getInstance().setNotifyBaseStatusEx(notifyBaseStatusEx1);
        }else {
            CmdSchedule.ROBOT_1=guid;
        }
        Logger.d("当前消息来自："+guid+";"+notifyBaseStatusExList.size());
    }
}
