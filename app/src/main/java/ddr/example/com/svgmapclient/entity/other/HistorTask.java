package ddr.example.com.svgmapclient.entity.other;


public class HistorTask {
    private String routename;
    private String taskname;

    public HistorTask(String routename,String taskname){
        this.routename=routename;
        this.taskname=taskname;

    }
    public String getRoutename() {
        return routename;
    }
    public String getTaskname(){
        return taskname;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }
    public void setTaskname(String taskname){ this.taskname=taskname;}


}
