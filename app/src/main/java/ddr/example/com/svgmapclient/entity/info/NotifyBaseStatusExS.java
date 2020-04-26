package ddr.example.com.svgmapclient.entity.info;

import java.util.ArrayList;
import java.util.List;

public class NotifyBaseStatusExS {
    public static NotifyBaseStatusExS notifyBaseStatusExS;
    private List<NotifyBaseStatusEx> notifyBaseStatusExes=new ArrayList<>();

    public static NotifyBaseStatusExS getInstance(){
        if (notifyBaseStatusExS==null){
            synchronized (NotifyBaseStatusExS.class){
                if (notifyBaseStatusExS==null){
                    notifyBaseStatusExS=new NotifyBaseStatusExS();
                }
            }

        }
        return notifyBaseStatusExS;
    }

    public void setNotifyBaseStatusExS(List<NotifyBaseStatusEx> notifyBaseStatusExS){
         this.notifyBaseStatusExes=notifyBaseStatusExS;
    }

    public List<NotifyBaseStatusEx> getNotifyBaseStatusExes() {
        return notifyBaseStatusExes;
    }
}
