package ddr.example.com.svgmapclient.entity.point;

import java.io.Serializable;
import java.util.List;

/**
 * time：2019/11/8
 * desc: 路径的数据类型
 */
public class PathLine extends BaseMode implements Serializable {
    private String name;
    private float velocity;    //速度
    private int pathModel;     // 路径模式  64 动态避障； 65 静态避障 ；66 贴边行驶路径（牛棚）
    private int pathType;      //1:手绘路径 2:选择目标点的路径，只有这里的路径才会在巡线模式下用到
    private String config="";    // 配置文件
    private List<PathPoint>pathPoints;
    private boolean bStartFromSeg0=false;         //false-不从第一段开始。 true-会从第一段开始  默认为false
    private boolean bNoCornerSmoothing=false;     // false-行走时会画弧形 true-不会画弧形，会原地旋转  默认为false


    public PathLine(int type) {
        super(type);
    }
    public PathLine(){
        super();

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public int getPathModel() {
        return pathModel;
    }

    public void setPathModel(int pathModel) {
        this.pathModel = pathModel;
    }

    public void setPathType(int pathType) {
        this.pathType = pathType;
    }

    public int getPathType() {
        return pathType;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public List<PathPoint> getPathPoints() {
        return pathPoints;
    }

    public void setPathPoints(List<PathPoint> pathPoints) {
        this.pathPoints = pathPoints;
    }

    public void setbStartFromSeg0(boolean bStartFromSeg0) {
        this.bStartFromSeg0 = bStartFromSeg0;
    }

    public boolean isbStartFromSeg0() {
        return bStartFromSeg0;
    }

    public void setbNoCornerSmoothing(boolean bNoCornerSmoothing) {
        this.bNoCornerSmoothing = bNoCornerSmoothing;
    }

    public boolean isbNoCornerSmoothing() {
        return bNoCornerSmoothing;
    }

    /**
     * 路径的组成点
     */
    public static class PathPoint implements Serializable{
        private String name="";          //自动生成的路径点名字
        private float x;
        private float y;
        private int pointType=8;  // 点的类型 动作属性 8:普通路径点
        private float rotationAngle;  //旋转角度
        private String config;        //配置文件

        public PathPoint(float x,float y,int pointType) {
            this.x=x;
            this.y=y;
            this.pointType=pointType;
        }

        public PathPoint() {

        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public int getPointType() {
            return pointType;
        }

        public void setPointType(int pointType) {
            this.pointType = pointType;
        }

        public float getRotationAngle() {
            return rotationAngle;
        }

        public void setRotationAngle(float rotationAngle) {
            this.rotationAngle = rotationAngle;
        }

        public String getConfig() {
            return config;
        }

        public void setConfig(String config) {
            this.config = config;
        }
    }
}
