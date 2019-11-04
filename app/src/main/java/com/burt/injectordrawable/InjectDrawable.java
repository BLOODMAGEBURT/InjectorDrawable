package com.burt.injectordrawable;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class InjectDrawable extends Drawable {

    private int mWidth;//总宽
    private int mHeight;//总高
    private float mCenterX;//水平中心点
    private float mCenterY;//垂直中心点
    private Paint mPaint;
    private Path mContainerPath = new Path();//底座
    private Path mStickPath = new Path();//推杆
    private float mBowHeight; //底座高度
    private float mBowWidth;//底宽
    private float mWingLength;//底座翼长


    private float mStickWidth;//推杆宽
    private float mStickHeight;//推杆高
    private float mStickWingLength;//推杆尾翼长
    private float mStickWingHeight;//推杆尾翼高




    /**
     * 通过目标View创建ArrowDrawable对象
     * ArrowDrawable宽高=View的宽高
     */
    public static InjectDrawable create(final View targetView) {
        //关闭硬件加速（Paint的setMaskFilter方法不支持硬件加速）
        targetView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //底座长 取 总高度的 40%
        int bowHeight = (int) (targetView.getHeight() * .4F);
        final InjectDrawable drawable = new InjectDrawable(targetView.getWidth(), targetView.getHeight(), bowHeight);
        if (targetView.getWidth() == 0 || targetView.getHeight() == 0) {
            //无效宽高，等待布局完成后更新一次尺寸
            targetView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottowi, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (targetView.getWidth() > 0 || targetView.getHeight() > 0) {
                        //弓长 取 总宽度的 40%
                        int bowHeight = (int) (targetView.getHeight() * .4F);
                        //更新有效宽高
                        drawable.updateSize(targetView.getWidth(), targetView.getHeight(), bowHeight);
                        //移除监听器，不再需要
                        targetView.removeOnLayoutChangeListener(this);
                    }
                }
            });
        }
        return drawable;
    }



    private InjectDrawable(int width, int height, int bowLength) {


        //水平中心点
        initPaint();
        updateSize(width, height, bowLength);


    }


    /**
     * 更新ArrowDrawable的尺寸
     *
     * @param width     总宽度
     * @param height    总高度
     * @param bowHeight 底座长度
     */
    public void updateSize(int width, int height, int bowHeight) {
        mWidth = width;
        mHeight = height;

        //水平中心点
        mCenterX = mWidth / 2F;
        mCenterY = mHeight / 2F;
        //底座高度
        mBowHeight = bowHeight;
        mBowWidth = mBowHeight / 8F;
        mWingLength = mBowHeight / 10F;

        //推杆
        mStickWidth = mBowWidth;
        mStickHeight = mBowHeight * 1.1F;
        mStickWingLength = mBowWidth * .5F;
        mStickWingHeight = mStickHeight / 40F;


        initContainerPath(mBowHeight, mBowWidth, mWingLength);
        initStickPath(mStickWidth, mStickHeight, mStickWingLength, mStickWingHeight);


        invalidateSelf();
    }



    private void initContainerPath(float bowHeight, float bowWidth, float wingLength) {

        mContainerPath.reset();
        mContainerPath.moveTo(mCenterX- bowWidth / 2F - wingLength , mCenterY);
        mContainerPath.rLineTo( wingLength, 0);
        mContainerPath.rLineTo(0, bowHeight);
        mContainerPath.rLineTo(bowWidth, 0);
        mContainerPath.rLineTo(0, -bowHeight);
        mContainerPath.rLineTo(wingLength, 0);

    }

    private void initStickPath(float stickWidth, float stickHeight, float stickWingLength, float stickWingHeight) {

        mStickPath.reset();
        mStickPath.moveTo(mCenterX + mStickWidth /2F, mCenterY);
        mStickPath.rLineTo(0, - stickHeight);
        mStickPath.rLineTo(stickWingLength, 0);
        mStickPath.rLineTo(0, - stickWingHeight);
        mStickPath.rLineTo(- (stickWidth + stickWingLength*2), 0);
        mStickPath.rLineTo(0, stickWingHeight);
        mStickPath.rLineTo(stickWingLength, 0);
        mStickPath.rLineTo(0, stickHeight);
        mStickPath.close();

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        drawContainer(canvas);

        drawStick(canvas);
    }

    // 画底座
    private void drawContainer(Canvas canvas) {
        canvas.drawPath(mContainerPath, mPaint);
    }
    // 画推杆
    private void drawStick(Canvas canvas) {

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mStickPath, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
