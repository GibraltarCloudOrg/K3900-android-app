package com.example.mauiviewcontrol;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;

public class ConvertibleRuler {
    static public void setUpHorizontalRuler(ImageView horizontalRulerImageView, ImageView horizontalUnitMeasurementNumberLabelsTextView, boolean show) {
        int visible = show ? View.VISIBLE : View.INVISIBLE;
        horizontalRulerImageView.setVisibility(visible);
        if (!show)
            return;
        if (SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().isUnitTypeMetric())
            setUpHorizontalRulerInMillimeters(horizontalRulerImageView, horizontalUnitMeasurementNumberLabelsTextView);
        else
            setUpHorizontalRulerInInches(horizontalRulerImageView, horizontalUnitMeasurementNumberLabelsTextView);
    }

    static public void setUpVerticalRuler(ImageView verticalRulerImageView, ImageView verticalUnitMeasurementNumberLabelsTextView, boolean show) {
        int visible = show ? View.VISIBLE : View.INVISIBLE;
        verticalRulerImageView.setVisibility(visible);
        if (!show)
            return;
        if (SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().isUnitTypeMetric())
            setUpVerticalRulerInMillimeters(verticalRulerImageView, verticalUnitMeasurementNumberLabelsTextView);
        else
            setUpVerticalRulerInInches(verticalRulerImageView, verticalUnitMeasurementNumberLabelsTextView);
    }

    @SuppressLint("ResourceAsColor")
    static private void setUpHorizontalRulerInMillimeters(ImageView horizontalRulerImageView, ImageView horizontalUnitMeasurementNumberLabelsTextView/*, int width*/) {
        if (0 >= horizontalRulerImageView.getWidth() || 0 >= horizontalUnitMeasurementNumberLabelsTextView.getWidth())
            return;
        /*float mPixelSizeX = 0.0f;
        float mPixelSizeY = 0.0f;
        float mUpperLeftX = 0.0f;
        float mUpperLeftY = 0.0f;
        float mScale = 0.0f;*/
        Bitmap bitmap = Bitmap.createBitmap(horizontalUnitMeasurementNumberLabelsTextView.getWidth(), 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        Bitmap textBitmap = Bitmap.createBitmap(horizontalUnitMeasurementNumberLabelsTextView.getWidth(), 20, Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);
        textCanvas.drawColor(Color.BLACK);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.rgb(112, 255, 0));
        //textPaint.setColor(Color.rgb(153, 204, 0));
        //textPaint.setColor(Color.WHITE);
        //textPaint.setColor(Color.GREEN);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setStrokeWidth(1);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(18);
        Paint textForNumber0Paint = new Paint();
        textForNumber0Paint.setColor(Color.YELLOW);
        //textForNumber0Paint.setColor(android.R.color.holo_green_light);
        textForNumber0Paint.setStyle(Paint.Style.STROKE);
        textForNumber0Paint.setStrokeWidth(2);
        textForNumber0Paint.setAntiAlias(true);
        textForNumber0Paint.setTextSize(24);
        final int yPosition = 10;
        //final int yPosition = horizontalRulerImageView.getHeight() / 2;
        canvas.drawLine(0, yPosition, horizontalRulerImageView.getWidth(), yPosition, paint);
        float epsilon = 0.00001f;
        int mm, last_mm = 999;
        SwitchBackEndModel backend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
        float milli = backend.getUpperLeftX();
        final int MIN_SCALE_WIDTH = 60;
        final int cmMarkerSize = 10;
        final int mmMarkerSize = 5;
        final int CM_NUM_SPACE = 5;
        /*mPixelSizeX = backend.getPixelSizeX();
        mPixelSizeY = backend.getPixelSizeY();
        mUpperLeftX = backend.getUpperLeftX();
        mUpperLeftY = backend.getUpperLeftY();
        mScale = backend.getScale();*/
        //float m_pixel_size_mm_x = horizontalUnitMeasurementNumberLabelsTextView.getWidth() / (10000 * mScale);
        float pixelSizeMmX = 1024 * backend.getPixelSizeX() / (backend.getScale() * horizontalRulerImageView.getWidth());
        for(int i = 0; i < horizontalRulerImageView.getWidth(); ++i) {
            mm = java.lang.Math.round(milli);
            if (java.lang.Math.abs(milli - mm) <= (java.lang.Math.abs(pixelSizeMmX / 2) + epsilon) && mm != last_mm) {
                last_mm = mm;
                final float scale = 1.11f;
                float xPosition = i * scale;
                if (mm%10 == 0) {
                    //Log.d(TAG, "inside for loop i: " + i + ", " +  m_pixel_size_mm_x + ", " + milli);
                    int cent = mm/10;
                    String str = Integer.toString(cent);
                    final int offset = 5;
                    final int offsetForNumber0 = 9;
                    if (0 == cent)
                        textCanvas.drawText(str, xPosition - offset, yPosition + offsetForNumber0, textForNumber0Paint);
                    else
                        textCanvas.drawText(str, xPosition - offset, yPosition + offset, textPaint);
                    canvas.drawLine(xPosition, yPosition - cmMarkerSize / 2, xPosition, yPosition + cmMarkerSize / 2, paint);
                    //canvas.drawLine(i, 10/*m_vert_scale->width()/2*/ - CM_MARKER_SIZE/2, i, 10/*m_vert_scale->width()/2*/ + CM_MARKER_SIZE/2, paint);
                }
                else if (mm%2 == 0) {
                    // draw millimeter mark
                    canvas.drawLine(xPosition, yPosition - mmMarkerSize / 2, xPosition,yPosition + mmMarkerSize / 2,  paint);
                    //canvas.drawLine(i, /*m_vert_scale->width()/2*/10 - MM_MARKER_SIZE/2, i,/*m_vert_scale->width()/2*/10 + MM_MARKER_SIZE/2,  paint);
                }
            }
            milli += pixelSizeMmX;
        }
        horizontalRulerImageView.setImageBitmap(bitmap);
        horizontalUnitMeasurementNumberLabelsTextView.setImageBitmap(textBitmap);
    }

    static private void setUpHorizontalRulerInInches(ImageView horizontalRulerImageView, ImageView horizontalUnitMeasurementNumberLabelsTextView) {
        Bitmap bitmap = Bitmap.createBitmap(horizontalUnitMeasurementNumberLabelsTextView.getWidth(), 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        Bitmap textBitmap = Bitmap.createBitmap(horizontalUnitMeasurementNumberLabelsTextView.getWidth(), 20, Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);
        textCanvas.drawColor(Color.BLACK);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.YELLOW);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(1);
        textPaint.setAntiAlias(true);
        final int yPosition = 10;
        canvas.drawLine(0, yPosition, horizontalRulerImageView.getWidth(), yPosition, paint);
        float epsilon = 0.00001f;
        int a32thInch, last_a32thInch = 999;
        //int mm, last_mm = 999;
        SwitchBackEndModel backend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
        float decimalInch = backend.getUpperLeftX();
        final int inchMarkerSize = 12;
        final int halfInchMarkerSize = 10;
        final int quarterInchMarkerSize = 8;
        final int a8thInchMarkerSize = 6;
        final int a16thInchMarkerSize = 4;
        //final int cmMarkerSize = 10;
        //final int mmMarkerSize = 5;
        float pixelSizeMmX = 1024 * backend.getPixelSizeX() / (backend.getScale() * horizontalRulerImageView.getWidth());
        int previousPoint = -1;
        int a16thInch = 0;
        for(int i = 0; i < horizontalRulerImageView.getWidth(); ++i) {
            a32thInch = java.lang.Math.round(decimalInch);
            if (java.lang.Math.abs(decimalInch - a32thInch) <= (java.lang.Math.abs(pixelSizeMmX / 2) + epsilon) && a32thInch != last_a32thInch) {
                last_a32thInch = a32thInch;
                if (a32thInch % ((int)(32 * 0.8)) == 0) {
                    int inch = a32thInch / (int)(32 * 0.8) ;
                    String str = Integer.toString(inch);
                    final int offset = 5;
                    textCanvas.drawText(str, i - offset, yPosition, textPaint);
                    canvas.drawLine(i, yPosition - inchMarkerSize / 2, i, yPosition + inchMarkerSize / 2, paint);
                    //for (int a8thInch = 1; a8thInch < 15; ++a8thInch) {
                        //canvas.drawLine(i + a8thInch * pixelSizeMmX * 128, yPosition - a16thInchMarkerSize / 2, i + a8thInch * pixelSizeMmX * 128,yPosition + a16thInchMarkerSize / 2,  paint);
                    //}
                    if (0 < previousPoint)
                        a16thInch = (i - previousPoint) / 16;
                    previousPoint = i;
                    if (0 < a16thInch) {
                        for (int position = 1; position < 16; ++position) {
                            if (0 == position % 8)
                                canvas.drawLine(i + a16thInch * (position + .5f), yPosition - halfInchMarkerSize / 2, i + a16thInch * (position + .5f), yPosition + halfInchMarkerSize / 2,  paint);
                            else if (0 == position % 4)
                                canvas.drawLine(i + a16thInch * (position + .5f), yPosition - quarterInchMarkerSize / 2, i + a16thInch * (position + .5f), yPosition + quarterInchMarkerSize / 2,  paint);
                            else if (0 == position % 2)
                                canvas.drawLine(i + a16thInch * (position + .5f), yPosition - a8thInchMarkerSize / 2, i + a16thInch * (position + .5f), yPosition + a8thInchMarkerSize / 2,  paint);
                            else
                                canvas.drawLine(i + a16thInch * (position + .5f), yPosition - a16thInchMarkerSize / 2, i + a16thInch * (position + .5f), yPosition + a16thInchMarkerSize / 2,  paint);
                        }
                    }
                }
                /*else if (a32thInch % ((int)(16 * 0.8)) == 0)
                    canvas.drawLine(i, yPosition - halfInchMarkerSize / 2, i,yPosition + halfInchMarkerSize / 2,  paint);
                else if (a32thInch % ((int)(8 * 0.8)) == 0)
                    canvas.drawLine(i, yPosition - quarterInchMarkerSize / 2, i,yPosition + quarterInchMarkerSize / 2,  paint);
                else if (a32thInch % ((int)(4 * 0.8)) == 0)
                    canvas.drawLine(i, yPosition - a8thInchMarkerSize / 2, i,yPosition + a8thInchMarkerSize / 2,  paint);
                else if (a32thInch % ((int)(2 * 0.8)) == 0)
                    canvas.drawLine(i, yPosition - a16thInchMarkerSize / 2, i,yPosition + a16thInchMarkerSize / 2,  paint);*/
            }
            decimalInch += pixelSizeMmX;
        }
        horizontalRulerImageView.setImageBitmap(bitmap);
        horizontalUnitMeasurementNumberLabelsTextView.setImageBitmap(textBitmap);
    }

    /*static private void setUpHorizontalRulerInInches(ImageView horizontalRulerImageView, ImageView horizontalUnitMeasurementNumberLabelsTextView) {
        Bitmap bitmap = Bitmap.createBitmap(horizontalUnitMeasurementNumberLabelsTextView.getWidth(), 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        Bitmap textBitmap = Bitmap.createBitmap(horizontalUnitMeasurementNumberLabelsTextView.getWidth(), 20, Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);
        textCanvas.drawColor(Color.BLACK);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.GREEN);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(1);
        textPaint.setAntiAlias(true);
        final int yPosition = 10;
        canvas.drawLine(0, yPosition, horizontalRulerImageView.getWidth(), yPosition, paint);
        //float epsilon = 0.00000001f;
        float epsilon = 0.00001f;
        int decimalInch, last_decimalInch = 999;
        int mark = 0;
        SwitchBackEndModel backend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
        float milli = backend.getUpperLeftX();
        final int inchMarkerSize = 12;
        final int halfInchMarkerSize = 8;
        final int quarterInchMarkerSize = 6;
        final int fractionalInchMarkerSize = 4;
        float pixelSizeMmX = 1024 * backend.getPixelSizeX() / (backend.getScale() * horizontalRulerImageView.getWidth());
        for(int i = 0; i < horizontalRulerImageView.getWidth(); ++i) {
            decimalInch = java.lang.Math.round(milli);
            //mark = decimalInch * (5 / 8) * (20/16);
            mark = decimalInch * 25 / 32;
            if (java.lang.Math.abs(milli - decimalInch) <= (java.lang.Math.abs(pixelSizeMmX * 3/8) + epsilon) && decimalInch != last_decimalInch) {
                last_decimalInch = decimalInch;
                if (mark%20 == 0) {
                    int inch = mark / 20;
                    //int inch = decimalInch * 4 * 32 / 5;
                    String str = Integer.toString(inch);
                    final int offset = 5;
                    textCanvas.drawText(str, i - offset, yPosition, textPaint);
                    canvas.drawLine(i, yPosition - inchMarkerSize / 2, i, yPosition + inchMarkerSize / 2, paint);
                }
                else if (mark%10 == 0) {
                    canvas.drawLine(i, yPosition - halfInchMarkerSize / 2, i,yPosition + halfInchMarkerSize / 2,  paint);
                }
                else if (mark%5 == 0) {
                    canvas.drawLine(i, yPosition - quarterInchMarkerSize / 2, i,yPosition + quarterInchMarkerSize / 2,  paint);
                }
            }
            milli += pixelSizeMmX;
        }
        horizontalRulerImageView.setImageBitmap(bitmap);
        horizontalUnitMeasurementNumberLabelsTextView.setImageBitmap(textBitmap);
    }*/

    static private void setUpVerticalRulerInMillimeters(ImageView verticalRulerImageView, ImageView verticalUnitMeasurementNumberLabelsTextView/*, int height*/) {
        if (0 >= verticalRulerImageView.getWidth() || 0 >= verticalUnitMeasurementNumberLabelsTextView.getWidth())
            return;
        /*float mPixelSizeX = 0.0f;
        float mPixelSizeY = 0.0f;
        float mUpperLeftX = 0.0f;
        float mUpperLeftY = 0.0f;
        float mScale = 0.0f;*/
        Bitmap bitmap = Bitmap.createBitmap(20, verticalUnitMeasurementNumberLabelsTextView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        Bitmap textBitmap = Bitmap.createBitmap(20, verticalRulerImageView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);
        textCanvas.drawColor(Color.BLACK);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.GREEN);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(1);
        //textPaint.setStrokeWidth(0.75f);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(18);
        Paint textForNumber0Paint = new Paint();
        textForNumber0Paint.setColor(Color.YELLOW);
        //textForNumber0Paint.setColor(android.R.color.holo_green_light);
        textForNumber0Paint.setStyle(Paint.Style.STROKE);
        textForNumber0Paint.setStrokeWidth(2);
        textForNumber0Paint.setAntiAlias(true);
        textForNumber0Paint.setTextSize(24);
        final int xPosition = 10;
        //final int xPosition = verticalRulerImageView.getWidth() / 2;
        canvas.drawLine(xPosition, 0, xPosition, verticalUnitMeasurementNumberLabelsTextView.getHeight(), paint);
        float epsilon = 0.00001f;
        int mm, last_mm = 999;
        SwitchBackEndModel backend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
        float milli = backend.getUpperLeftY();
        final int MIN_SCALE_WIDTH = 60;
        final int cmMarkerSize = 10;
        final int mmMarkerSize = 5;
        final int CM_NUM_SPACE = 5;
        /*mPixelSizeX = backend.getPixelSizeX();
        mPixelSizeY = backend.getPixelSizeY();
        mUpperLeftX = backend.getUpperLeftX();
        mUpperLeftY = backend.getUpperLeftY();
        mScale = backend.getScale();*/
        //float m_pixel_size_mm_y = verticalUnitMeasurementNumberLabelsTextView.getHeight() / (10000 * mScale);
        float pixelSizeMmY = 1024 * backend.getPixelSizeY() / (backend.getScale() * verticalRulerImageView.getHeight());
        //float m_pixel_size_mm_x = 1024 * mPixelSizeX / (mScale * horizontalRulerImageView.getWidth());
        for(int i = 0; i < verticalRulerImageView.getHeight(); ++i)
        {
            mm = java.lang.Math.round(milli);
            if (java.lang.Math.abs(milli - mm) <= (java.lang.Math.abs(pixelSizeMmY / 2) + epsilon) && mm != last_mm) {
                last_mm = mm;
                final float scale = 1.11f;
                float yPosition = i * scale;
                if (mm%10 == 0) {
                    //Log.d(TAG, "inside for loop i: " + i + ", " +  m_pixel_size_mm_y + ", " + milli);
                    int cent = mm/10;
                    String str = Integer.toString(cent);
                    final int offset = 12;
                    final int offsetForNumber0 = 9;
                    if (0 == cent)
                        textCanvas.drawText(str, 0, yPosition + offset, textForNumber0Paint);
                    else
                        textCanvas.drawText(str, 0, yPosition + offset, textPaint);
                    canvas.drawLine(xPosition - cmMarkerSize / 2, yPosition, xPosition + cmMarkerSize / 2, yPosition, paint);
                    //canvas.drawLine(10/*m_vert_scale->width()/2*/ - CM_MARKER_SIZE/2, i, 10/*m_vert_scale->width()/2*/ + CM_MARKER_SIZE/2, i, paint);
                }
                else if (mm%2 == 0) {
                    // draw millimeter mark
                    canvas.drawLine(xPosition - mmMarkerSize / 2, yPosition, xPosition + mmMarkerSize / 2, yPosition, paint);
                    //canvas.drawLine(/*m_vert_scale->width()/2*/10 - MM_MARKER_SIZE/2, i, /*m_vert_scale->width()/2*/10 + MM_MARKER_SIZE/2, i, paint);
                }
            }
            milli += pixelSizeMmY;
        }
        verticalRulerImageView.setImageBitmap(bitmap);
        verticalUnitMeasurementNumberLabelsTextView.setImageBitmap(textBitmap);
    }

    static private void setUpVerticalRulerInInches(ImageView verticalRulerImageView, ImageView verticalUnitMeasurementNumberLabelsTextView/*, int height*/) {
        Bitmap bitmap = Bitmap.createBitmap(20, verticalUnitMeasurementNumberLabelsTextView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        Bitmap textBitmap = Bitmap.createBitmap(20, verticalRulerImageView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);
        textCanvas.drawColor(Color.BLACK);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.YELLOW);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(1);
        textPaint.setAntiAlias(true);
        final int xPosition = 10;
        canvas.drawLine(xPosition, 0, xPosition, verticalUnitMeasurementNumberLabelsTextView.getHeight(), paint);
        float epsilon = 0.00001f;
        int a32thInch, last_a32thInch = 999;
        //int mm, last_mm = 999;
        SwitchBackEndModel backend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
        //float milli = backend.getUpperLeftY();
        float decimalInch = backend.getUpperLeftY();
        final int inchMarkerSize = 12;
        final int halfInchMarkerSize = 10;
        final int quarterInchMarkerSize = 8;
        final int a8thInchMarkerSize = 6;
        final int a16thInchMarkerSize = 4;
        //final int cmMarkerSize = 10;
        //final int mmMarkerSize = 5;
        float pixelSizeMmY = 1024 * backend.getPixelSizeY() / (backend.getScale() * verticalRulerImageView.getHeight());
        for(int i = 0; i < verticalRulerImageView.getHeight(); ++i)
        {
            a32thInch = java.lang.Math.round(decimalInch);
            if (java.lang.Math.abs(decimalInch - a32thInch) <= (java.lang.Math.abs(pixelSizeMmY / 2) + epsilon) && a32thInch != last_a32thInch) {
                last_a32thInch = a32thInch;
                if (a32thInch % 32 == 0) {
                    int inch = a32thInch/32;
            /*mm = java.lang.Math.round(milli);
            if (java.lang.Math.abs(milli - mm) <= (java.lang.Math.abs(pixelSizeMmY / 2) + epsilon) && mm != last_mm) {
                last_mm = mm;
                if (mm%10 == 0) {
                    int cent = mm/10;*/
                    String str = Integer.toString(inch);
                    final int offset = 5;
                    textCanvas.drawText(str, 0, i + offset, textPaint);
                    canvas.drawLine(xPosition - inchMarkerSize / 2, i, xPosition + inchMarkerSize / 2, i, paint);
                }
                else if (a32thInch % 16 == 0)
                    canvas.drawLine(xPosition - halfInchMarkerSize / 2, i, xPosition + halfInchMarkerSize / 2, i, paint);
                else if (a32thInch % 8 == 0)
                    canvas.drawLine(xPosition - quarterInchMarkerSize / 2, i, xPosition + quarterInchMarkerSize / 2, i, paint);
                else if (a32thInch % 4 == 0)
                    canvas.drawLine(xPosition - a8thInchMarkerSize / 2, i, xPosition + a8thInchMarkerSize / 2, i, paint);
                else if (a32thInch % 2 == 0)
                    canvas.drawLine(xPosition - a16thInchMarkerSize / 2, i, xPosition + a16thInchMarkerSize / 2, i, paint);
            }
            decimalInch += pixelSizeMmY;
        }
        verticalRulerImageView.setImageBitmap(bitmap);
        verticalUnitMeasurementNumberLabelsTextView.setImageBitmap(textBitmap);
    }
}
