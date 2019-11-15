package com.burt.injectordrawable.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/*
 * 雷达图View
 * */
public class RadarView extends View {
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mWidth;
    private float mHeight;
    private float mCenterX;
    private float mCenterY;
    private float r;
    private PointF mStartPoint = new PointF();
    private PointF mEndPoint = new PointF();

    private Path mPath = new Path();
    private Path linePath = new Path();
    private float mScale = 0.8f; // 缩放比例

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(0xaa00FA9A);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        mCenterX = mWidth / 2f;
        mCenterY = mHeight / 2f;
        r = Math.min(mCenterX, mCenterY) / 2 * 0.9f;

        // 把角度转换为弧度 Math.toRadians(30)
        mStartPoint.x = (float) -(r * Math.sin(Math.toRadians(30)));
        mStartPoint.y = (float) -(r * Math.cos(Math.toRadians(30)));
        Log.d("aaa", "onDrawX: " + mStartPoint.x + "onDrawY:" + mStartPoint.y);

        mEndPoint.x = -mStartPoint.x;
        mEndPoint.y = mStartPoint.y;

        mPath.moveTo(mStartPoint.x, mStartPoint.y);
        mPath.lineTo(mEndPoint.x, mEndPoint.y);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mCenterX, mCenterY);

        canvas.drawCircle(0, 0, r, mPaint);

        linePath.moveTo(mStartPoint.x, mStartPoint.y);

        for (int i = 0; i < 6; i++) {
            canvas.drawLine(0, 0, mStartPoint.x, mStartPoint.y, mPaint);

            canvas.rotate(60);
        }

        mPaint.setColor(0xaa00FA9A);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                canvas.drawPath(mPath, mPaint);
                canvas.rotate(60);
            }
            // 缩放画布
            canvas.scale(mScale, mScale);
        }

        Log.d("aaa", "onDrawX: " + mStartPoint.x + "onDrawY:" + mStartPoint.y);
    }
}
