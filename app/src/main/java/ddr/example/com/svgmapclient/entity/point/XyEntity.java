package ddr.example.com.svgmapclient.entity.point;

/**
 * desc:坐标数据类型
 */
public class XyEntity {
    private float x;
    private float y;

    public XyEntity(float x,float y){
        this.x=x;
        this.y=y;
    }

    public XyEntity() {
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

}

