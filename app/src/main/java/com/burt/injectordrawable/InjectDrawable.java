package com.burt.injectordrawable;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class InjectDrawable extends Drawable {

    /**
     * 静止状态
     */
    public static final int STATE_STILL = 0;

    /**
     * 运行状态
     */
    public static final int STATE_RUNNING = 1;

    private int mState = STATE_STILL;//当前状态

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
    private float mStickOffset = 0;//推杆当前偏移量


    private float mProgress; //进度

    private float mDividerProgress = 0.3f;//分割线的进度
    private float mDividerHeight = 0; // 分割线高度
    private Path upBlockPath = new Path();//上方色块路径
    private Path downBlockPath = new Path();//下方色块路径
    private PointF secondPoint;
    private Paint mBlockPaint = new Paint(); // 色块的画笔

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
        mBowWidth = mBowHeight / 7F;
        mWingLength = mBowHeight / 10F;

        //推杆
        mStickWidth = mBowWidth;
        mStickHeight = mBowHeight * 1.1F;
        mStickWingLength = mBowWidth * .5F;
        mStickWingHeight = mStickHeight / 40F;

        //初始化容器的路径
        initContainerPath(mBowHeight, mBowWidth, mWingLength);

        //初始化推杆的路径
        initStickPath(mStickWidth, mStickHeight, mStickWingLength, mStickWingHeight);

        //初始化上方色块的路径
        initUpBlockPath(mBowHeight, mBowWidth);

        // 初始化下方色块的路径
        initDownBlockPath(mBowHeight, mBowWidth);

        invalidateSelf();
    }


    private void initContainerPath(float bowHeight, float bowWidth, float wingLength) {

        mContainerPath.reset();
        mContainerPath.moveTo(mCenterX - bowWidth / 2F - wingLength, mCenterY);
        mContainerPath.rLineTo(wingLength, 0);
        mContainerPath.rLineTo(0, bowHeight);
        mContainerPath.rLineTo(bowWidth, 0);
        mContainerPath.rLineTo(0, -bowHeight);
        mContainerPath.rLineTo(wingLength, 0);

    }

    private void initStickPath(float stickWidth, float stickHeight, float stickWingLength, float stickWingHeight) {

        mStickPath.reset();
        mStickPath.moveTo(mCenterX + mStickWidth / 2F, mCenterY);
        mStickPath.rLineTo(0, -stickHeight);
        mStickPath.rLineTo(stickWingLength, 0);
        mStickPath.rLineTo(0, -stickWingHeight);
        mStickPath.rLineTo(-(stickWidth + stickWingLength * 2), 0);
        mStickPath.rLineTo(0, stickWingHeight);
        mStickPath.rLineTo(stickWingLength, 0);
        mStickPath.rLineTo(0, stickHeight);
        mStickPath.close();

    }

    private void initUpBlockPath(float bowHeight, float bowWidth) {

        upBlockPath.reset();
        upBlockPath.moveTo(mCenterX - bowWidth / 2f, mCenterY);
        upBlockPath.rLineTo(0, mDividerProgress * bowHeight);

        secondPoint = new PointF(mCenterX - bowWidth / 2F, mCenterY + mDividerProgress * bowHeight);

        upBlockPath.rLineTo(bowWidth, 0);
        upBlockPath.rLineTo(0, -mDividerProgress * bowHeight);
        upBlockPath.close();
    }

    private void initDownBlockPath(float bowHeight, float bowWidth) {
        downBlockPath.reset();
        downBlockPath.moveTo(secondPoint.x, secondPoint.y);
        downBlockPath.rLineTo(0, (1 - mDividerProgress) * bowHeight);
        downBlockPath.rLineTo(bowWidth, 0);
        downBlockPath.rLineTo(0, -(1 - mDividerProgress) * bowHeight);
        downBlockPath.close();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

       mBlockPaint.set(mPaint);

    }

    @Override
    public void draw(@NonNull Canvas canvas) {


        if (mState == STATE_STILL) { //当静止的时候，执行画色块
            drawUpBlock(canvas);
            drawDownBlock(canvas);
        }

        drawContainer(canvas);

        updateStickOffset();
        drawStick(canvas);

    }

    private void drawDownBlock(Canvas canvas) {
        mBlockPaint.setColor(Color.GRAY);
        mBlockPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(downBlockPath, mBlockPaint);
    }

    private void drawUpBlock(Canvas canvas) {
        mBlockPaint.setColor(Color.RED);
        mBlockPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(upBlockPath, mBlockPaint);
    }


    // 画底座
    private void drawContainer(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new CornerPathEffect(10));
        canvas.drawPath(mContainerPath, mPaint);
    }

    // 更新推杆偏移量
    private void updateStickOffset() {

        float newOffset = mBowHeight * mProgress;

        mStickPath.offset(0, -mStickOffset);
        mStickPath.offset(0, newOffset);
        //更新本次偏移量
        mStickOffset = newOffset;
    }

    // 画推杆
    private void drawStick(Canvas canvas) {

        mPaint.setColor(Color.WHITE);
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

    public void setProgress(float progress) {
        if (progress > 1) {
            progress = 1;
        } else if (progress < 0) {
            progress = 0;
        }

        this.mProgress = progress;
        invalidateSelf();
    }

    public float getProgress() {

        return mProgress;
    }

    // 更新上下颜色分割线
    public void setDividerProgress(float progress) {
        if (progress > 1) {
            progress = 1;
        } else if (progress < 0) {
            progress = 0;
        }
        mDividerProgress = progress;
        mDividerHeight = progress * mBowHeight;
    }

    public void updateState(int state) {

        this.mState = state;
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
