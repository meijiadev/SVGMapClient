package ddr.example.com.svgmapclient.protocobuf;

import DDRCommProto.BaseCmd;
import DDRCommProto.RemoteCmd;

/**
 * 所有命令集合
 */
public class CmdSchedule {
    public static String broadcastServerIP="129.204.18.252";  //广域网远程服务器的Ip和端口
    public static String broadcastServerIP1="192.168.0.96";
    public static int broadcastServerPort=9999;
    public static String ROBOT_1="retail_1";
    public static String ROBOT_2="retail_2";
    public static String ROBOT_3="BLANK";
    public static String ROBOT_4="DDR011";


    /**
     * 心跳包的数据
     * @return
     */
    public static BaseCmd.HeartBeat heartBeat(){
        BaseCmd.HeartBeat hb=BaseCmd.HeartBeat.newBuilder()
                .setWhatever("hb")
                .build();
        return hb;
    }

    /**
     * 局域网登录
     * @param password
     * @param account
     * @return
     */
    public static BaseCmd.reqLogin localLogin(String account, String password){
        BaseCmd.reqLogin mreqLogin=BaseCmd.reqLogin.newBuilder()
                .setUsername(account)
                .setUserpwd(password)
                .setType(BaseCmd.eCltType.eLocalAndroidClient)
                .build();
        return mreqLogin;
    }

    /**
     * 广域网登陆
     * @param password
     * @param account
     * @return
     */
    public static RemoteCmd.reqRemoteLogin remoteLogin(String account, String password){
        RemoteCmd.reqRemoteLogin reqRemoteLogin=RemoteCmd.reqRemoteLogin.newBuilder()
                .setType(BaseCmd.eCltType.eRemoteAndroidClient)
                .setUsername(account)
                .setUserpwd(password)
                .build();
        return reqRemoteLogin;
    }

    /**
     * 获取本地区服务器列表
     * @param iPAddress 提供本机的IP地址
     * @return
     */
    public static RemoteCmd.reqRemoteServerList remoteServerList(String iPAddress){
        RemoteCmd.reqRemoteServerList reqRemoteServerList=RemoteCmd.reqRemoteServerList.newBuilder()
                .setFromip(iPAddress)
                .build();
        return reqRemoteServerList;
    }


    /**
     *获取rtmp 和语音对讲的IP 和端口
     * @return
     */
    public static BaseCmd.reqStreamAddr streamAddr(){
        BaseCmd.reqStreamAddr reqStreamAddr=BaseCmd.reqStreamAddr.newBuilder()
                .setNetworkType(BaseCmd.ChannelNetworkType.Local)
                .build();
        return reqStreamAddr;
    }


    /**
     * 命令的额外头部信息，决定命令发往那个服务
     * @return
     */
    public static BaseCmd.CommonHeader commonHeader(BaseCmd.eCltType eCltType){
        BaseCmd.CommonHeader header=BaseCmd.CommonHeader.newBuilder()
                .setFromCltType(BaseCmd.eCltType.eLocalAndroidClient)
                .setToCltType(eCltType)
                .addFlowDirection(BaseCmd.CommonHeader.eFlowDir.Forward)
                .build();
        return header;
    }

    /**
     * 命令的额外头部信息，决定命令发往那个服务 guid决定发往那个机器人
     * @return
     */
    public static BaseCmd.CommonHeader commonHeader1(BaseCmd.eCltType eCltType,String guid){

        BaseCmd.CommonHeader header=BaseCmd.CommonHeader.newBuilder()
                .setFromCltType(BaseCmd.eCltType.eLocalAndroidClient)
                .setToCltType(eCltType)
                .addFlowDirection(BaseCmd.CommonHeader.eFlowDir.Forward)
                .setGuid(guid)
                .build();
        return header;
    }


    /**
     * 语音对讲
     * @return
     */
    public static BaseCmd.reqAudioTalk audioTalk(BaseCmd.reqAudioTalk.eOpMode eOpMode){
        BaseCmd.reqAudioTalk reqAudioTalk=BaseCmd.reqAudioTalk.newBuilder()
                .setNetType(BaseCmd.reqAudioTalk.eNetType.eLocal)
                .setOpType(eOpMode)
                .build();
        return reqAudioTalk;
    }

    /**
     * 关机重启
     * @param eCmdIPCMode
     * @return
     */
    public static BaseCmd.reqCmdIPC cmdIPC(BaseCmd.eCmdIPCMode eCmdIPCMode ){
        BaseCmd.reqCmdIPC reqCmdIPC=BaseCmd.reqCmdIPC.newBuilder()
                .setMode(eCmdIPCMode)
                .build();
        return reqCmdIPC;
    }

}
