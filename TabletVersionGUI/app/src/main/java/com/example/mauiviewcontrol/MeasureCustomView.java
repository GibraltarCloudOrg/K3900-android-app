package com.example.mauiviewcontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MeasureCustomView extends View {
    private Paint mPaint;
    private int mPaintColor= Color.GREEN;
    private ArrayList<Float> mXCoordinates=new ArrayList<Float>();
    private ArrayList<Float> mYCoordinates=new ArrayList<Float>();
    private ArrayList<Float>mMeasurements=new ArrayList<Float>();
    //private static Path path=new Path();
    private int mTouchNumber=0;
    private static boolean sMeasurementShowing=false;
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    int mMeasurementNumber=0;
    //private static MeasureCustomView sMeasureCustomView=null;
    //private static Context mContext;
    //private static AttributeSet mAttrs;

    public MeasureCustomView(Context context, AttributeSet attrs){
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setUpPaint();
        mXCoordinates=new ArrayList<Float>();
        mYCoordinates=new ArrayList<Float>();
        mTouchNumber=0;
        //clearMeasurements();
        //mContext=context;
        //mAttrs=attrs;
    }

    //public static MeasureCustomView getMeasureCustomView(){
   //     return new MeasureCustomView(mContext, mAttrs);
   // }

    private void setUpPaint(){
        mPaint=new Paint();
        mPaint.setColor(mPaintColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextSize(35f);
    }

    @Override
    protected void onDraw(Canvas canvas){
        if(sMeasurementShowing) {
            //canvas.drawPath(path, mPaint);
            int measurementNumber = 1;
            for (int i = 0; i < mXCoordinates.size(); i++) {
                //canvas.drawCircle(mXCoordinates.get(i), mYCoordinates.get(i),3,mPaint);
                changeColor((measurementNumber-1)%5);
                canvas.drawRect(mXCoordinates.get(i) - 10, mYCoordinates.get(i) + .5f, mXCoordinates.get(i) + 10, mYCoordinates.get(i) - .5f, mPaint);
                canvas.drawRect(mXCoordinates.get(i) - .5f, mYCoordinates.get(i) + 10, mXCoordinates.get(i) + .5f, mYCoordinates.get(i) - 10, mPaint);
                if(i%2!=0){
                    //changeColor((measurementNumber-1)%4);
                    canvas.drawLine(mXCoordinates.get(i-1), mYCoordinates.get(i-1), mXCoordinates.get(i), mYCoordinates.get(i), mPaint);
                    canvas.drawText(Integer.toString(measurementNumber), mXCoordinates.get(i) + 20f, mYCoordinates.get(i) - 20f, mPaint);
                    measurementNumber++;
                    //changeColor((measurementNumber-1)%4);
                }else{
                    canvas.drawText(Integer.toString(measurementNumber), mXCoordinates.get(i) - 25f, mYCoordinates.get(i) - 20f, mPaint);
                    //measurementNumber++;
                }
            }
           /* for (int i = 0; i < mXCoordinates.size(); i = i + 2) {
                canvas.drawText(Integer.toString(measurementNumber), mXCoordinates.get(i) - 25f, mYCoordinates.get(i) - 20f, mPaint);
                measurementNumber++;
            }*/
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float pointX=event.getX();
        float pointY=event.getY();
        //mXCoordinates.add((int)pointX);
        //mYCoordinates.add((int)pointY);
        if(sMeasurementShowing){
            if(event.getAction()==MotionEvent.ACTION_DOWN) {
                if ((mTouchNumber % 2) == 0) {
                    //mMeasurementNumber++;
                    //changeColor(mMeasurementNumber);
                    mXCoordinates.add(pointX);
                    mYCoordinates.add(pointY);
                   // path.moveTo(pointX, pointY);
                } else {
                    mXCoordinates.add(pointX);
                    mYCoordinates.add(pointY);
                    //path.lineTo(pointX, pointY);
                    //mDistances.add(calculateDistance(mXCoordinates.get(mTouchNumber - 1), mYCoordinates.get(mTouchNumber - 1), mXCoordinates.get(mTouchNumber), mYCoordinates.get(mTouchNumber)));
                    int dx=calculateDeltaX(mXCoordinates.get(mTouchNumber - 1), mXCoordinates.get(mTouchNumber));
                    int dy=calculateDeltaY(mYCoordinates.get(mTouchNumber-1), mYCoordinates.get(mTouchNumber));
                    mBackend.onSendCursorMovement(dx,dy);
                    mBackend.onCaptureMeasurementMark();
                    //mMeasurements=mBackend.onGetMeasurements();
                    MeasureImagingDialog.getSingletonInstance(null, true).setMeasurementListView();
                }
                mTouchNumber = mTouchNumber + 1;
                postInvalidate();
            }
        }else{
            return super.onTouchEvent(event);
        }

       /* switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX,pointY);
                mXCoordinates.add((int)pointX);
                mYCoordinates.add((int)pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                break;
            case MotionEvent.ACTION_UP:
                mXCoordinates.add((int)pointX);
                mYCoordinates.add((int)pointY);
            default:
                return false;
        }*/
        return true;
    }


    private float calculateDistance(float x1, float y1, float x2, float y2){
        float xDistance=Math.abs(x1-x2);
        float yDistance=Math.abs(y1-y2);
        float squares=(xDistance*xDistance)+(yDistance*yDistance);
        float distance= (float) Math.sqrt(squares);
        return distance;
    }

    private int calculateDeltaX(float x1, float x2){
        return (int)Math.abs(x1-x2);
    }

    private int calculateDeltaY(float y1, float y2){
        return (int)Math.abs(y1-y2);
    }

    public static void setMeasurementShowing(boolean measurementShowing){
        sMeasurementShowing=measurementShowing;
    }

    private void changeColor(int measurementNumber){
        switch(measurementNumber){
            case 0:
                mPaint.setColor(Color.GREEN);
                break;
            case 1:
                mPaint.setColor(Color.RED);
                break;
            case 2:
                mPaint.setColor(Color.BLUE);
                break;
            case 3:
                //mPaint.setColor(Color.YELLOW);
                mPaint.setColor(Color.CYAN);
                break;
            case 4:
                mPaint.setColor(Color.MAGENTA);
        }
    }

    public void deleteMeasurement(int index){
        mXCoordinates.remove(index*2);
        mXCoordinates.remove((index*2));
        mYCoordinates.remove(index*2);
        mYCoordinates.remove((index*2));
        mTouchNumber=mTouchNumber-2;
        postInvalidate();
    }

    public void clearMeasurements(){
        mXCoordinates=new ArrayList<Float>();
        mYCoordinates=new ArrayList<Float>();
        mTouchNumber=0;
        //path.reset();
        MeasureImagingDialog.getSingletonInstance(null, true).setMeasurementListView();
        postInvalidate();
    }

    public void cancelMeasurement(){
        mXCoordinates.remove(mXCoordinates.size()-1);
        mYCoordinates.remove(mYCoordinates.size()-1);
        mTouchNumber=mTouchNumber-1;
    }
}
