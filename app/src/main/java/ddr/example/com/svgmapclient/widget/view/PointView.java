package ddr.example.com.svgmapclient.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;


import org.greenrobot.eventbus.EventBus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import ddr.example.com.svgmapclient.R;
import ddr.example.com.svgmapclient.entity.MessageEvent;
import ddr.example.com.svgmapclient.entity.info.NotifyBaseStatusEx;
import ddr.example.com.svgmapclient.entity.point.PathLine;
import ddr.example.com.svgmapclient.entity.point.TargetPoint;
import ddr.example.com.svgmapclient.entity.point.XyEntity;
import ddr.example.com.svgmapclient.other.Logger;

/**
 * time ：2019/11/13
 * desc : 绘制点
 */
public class PointView   {
    public static PointView pointView;
    private List<TargetPoint> targetPoints1;
    private List<TargetPoint> targetPoints;

    private Paint pointPaint,textPaint;
    private TargetPoint targetPoint;
    private NotifyBaseStatusEx notifyBaseStatusEx;
    private PathLine.PathPoint pathPoint;


    private Bitmap autoBitmap;
    private float x,y;
    private float angle; //角度
    public boolean isRuning;
    private Matrix matrix=new Matrix();
    private Bitmap directionBitmap,directionBitmap1;
    private Bitmap targetBitmap,targetBitmap1;
    private int bitmapW,bitmapH;





    /**
     * 显示被选中的点（多选）
     * @param targetPoints
     */
    public void setTargetPoints(List<TargetPoint> targetPoints){
        this.targetPoints1=targetPoints;
    }


    /**
     * 显示被选中的点（多选）
     * @param targetPoints
     */
    public void setTargetPoints1(List<TargetPoint> targetPoints){
        this.targetPoints=targetPoints;

    }


    /**
     * 显示点击的该点（单选）
     * @param targetPoint
     */
    public void setPoint(TargetPoint targetPoint){
        this.targetPoint=targetPoint;
    }



    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void update(MessageEvent messageEvent){
        switch (messageEvent.getType()){
            case updateBaseStatus:
                x=notifyBaseStatusEx.getPosX();
                y=notifyBaseStatusEx.getPosY();
                angle=radianToangle(notifyBaseStatusEx.getPosDirection());
                break;
        }
    }


    /**
     * 单例模式 避免频繁实例化该类
     * @param context
     * @return
     */
    public static PointView getInstance(Context context){
        if (pointView==null){
            synchronized (PointView.class){
                if (pointView==null){
                    pointView=new PointView(context);
                }
            }
        }
        return pointView;
    }

    private PointView(Context context) {
        pointPaint=new Paint();
        pointPaint.setStrokeWidth(5);
        pointPaint.setColor(Color.GRAY);
        pointPaint.setAntiAlias(true);
        textPaint=new Paint();
        textPaint.setStrokeWidth(8);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(16);
        autoBitmap=BitmapFactory.decodeResource(context.getResources(), R.mipmap.auto_default);
        directionBitmap=BitmapFactory.decodeResource(context.getResources(), R.mipmap.direction);
        targetBitmap=BitmapFactory.decodeResource(context.getResources(), R.mipmap.target_point);
        notifyBaseStatusEx=NotifyBaseStatusEx.getInstance();
        EventBus.getDefault().register(this);
        bitmapW=targetBitmap.getWidth();

    }


    public void drawPoint(Canvas canvas,ZoomImageView zoomImageView){
        if (targetPoints1 != null) {
            for (int i=0;i<targetPoints1.size();i++){
                    XyEntity xyEntity=zoomImageView.toXorY(targetPoints1.get(i).getX(),targetPoints1.get(i).getY());
                    xyEntity=zoomImageView.coordinate2View(xyEntity.getX(),xyEntity.getY());
                    float x= xyEntity.getX();
                    float y=  xyEntity.getY();
                    angle=radianToangle(targetPoints1.get(i).getTheta());
                    matrix.setRotate(-angle);
                    Logger.d("------朝向："+-targetPoints1.get(i).getTheta());
                    targetBitmap1=Bitmap.createBitmap(targetBitmap,0,0,targetBitmap.getWidth(),targetBitmap.getWidth()  ,matrix,true);
                    canvas.drawBitmap(targetBitmap1,x-bitmapW/2,y-bitmapH/2,pointPaint);
                    canvas.drawText(targetPoints1.get(i).getName(),x,y+15,textPaint);
            }
        }
        if (targetPoints != null) {
            for (int i=0;i<targetPoints.size();i++){
                XyEntity xyEntity=zoomImageView.toXorY(targetPoints.get(i).getX(),targetPoints.get(i).getY());
                xyEntity=zoomImageView.coordinate2View(xyEntity.getX(),xyEntity.getY());
                float x= xyEntity.getX();
                float y=  xyEntity.getY();
                canvas.drawCircle(x,y,7,pointPaint);
                canvas.drawText(targetPoints.get(i).getName(),x,y+15,textPaint);
            }
        }



        if (targetPoint!=null){
            XyEntity xyEntity=zoomImageView.toXorY(targetPoint.getX(),targetPoint.getY());
            xyEntity=zoomImageView.coordinate2View(xyEntity.getX(),xyEntity.getY());
            float x=  xyEntity.getX();
            float y=  xyEntity.getY();
            canvas.drawBitmap(autoBitmap,x-autoBitmap.getWidth()/2,y-autoBitmap.getHeight()/2,pointPaint);
            canvas.drawText(targetPoint.getName(),x,y+15,textPaint);
        }



    }





    /**
     * 弧度转角度
     */
    public float radianToangle(float angle){
        return (float)(180/Math.PI*angle);
    }

    public void clearDraw(){
        targetPoint=null;
        targetPoints1=null;
        isRuning=false;
        pathPoint=null;
    }

}
