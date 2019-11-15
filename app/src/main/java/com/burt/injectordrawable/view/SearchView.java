package com.burt.injectordrawable.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

/*
 * 搜索View
 * 2019-11-15
 * XuBoBo
 * */
public class SearchView extends View {

    private Path mSearchPath = new Path(); // 搜索path
    private Path mCirclePath = new Path(); // 外部圆圈path
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private float mWidth, mHeight; // 宽高
    private float mCenterX, mCenterY; //中点
    private float mCircleLength;
    private float mSearchLength;

    private float mAnimatedValue = 0;
    private PathMeasure measure;
    private State mCurrentState = State.NONE;
    private ValueAnimator mStartAnimator;
    private ValueAnimator mSearchAnimator;
    private ValueAnimator mEndAnimator;
    private int defaultDuration = 1500;

    private Path dst = new Path();

    public enum State {
        NONE,
        STARTING,
        SEARCHING,
        END
    }


    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initPaint();

        initAnimator();
    }


    private void initPaint() {
        mPaint.setColor(0xaaFF00FF);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    private void initAnimator() {

        ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        };


        Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d("bbb", "onAnimationStart: START");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 当动画结束后，切换另一个动画
                switchAnimator();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };


        // 开始动画
        mStartAnimator = ValueAnimator.ofFloat(0, 1);
        mStartAnimator.addUpdateListener(animatorUpdateListener);
        mStartAnimator.addListener(animatorListener);
        mStartAnimator.setDuration(defaultDuration);
        mStartAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        // 搜索动画
        mSearchAnimator = ValueAnimator.ofFloat(0, 1);
        mSearchAnimator.addUpdateListener(animatorUpdateListener);
        mSearchAnimator.addListener(animatorListener);
        mSearchAnimator.setDuration(defaultDuration);
        mSearchAnimator.setInterpolator(new AccelerateDecelerateInterpolator());


        // 结束动画
        mEndAnimator = ValueAnimator.ofFloat(1, 0);
        mEndAnimator.addUpdateListener(animatorUpdateListener);
        mEndAnimator.addListener(animatorListener);
        mEndAnimator.setDuration(defaultDuration);
        mEndAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

    }

    // 切换动画
    private void switchAnimator() {

        switch (mCurrentState) {
            case STARTING:

                mCurrentState = State.SEARCHING;
                mSearchAnimator.start();
                break;
            case SEARCHING:

                mCurrentState = State.END;
                mEndAnimator.start();
                break;
            case END:
                break;
        }

    }


    private void initPath() {
        float rCircle = Math.min(mWidth, mHeight) / 2 * 0.8f;

        float rSearch = rCircle / 2f;

        RectF circlrRect = new RectF(-rCircle, -rCircle, rCircle, rCircle);
        mCirclePath.addArc(circlrRect, 45, -359.9f);

        RectF searchRect = new RectF(-rSearch, -rSearch, rSearch, rSearch);
        mSearchPath.addArc(searchRect, 45, 359.9f);

        measure = new PathMeasure();
        measure.setPath(mCirclePath, false);

        // 连接放大镜尾巴
        float[] pos = new float[2];
        measure.getPosTan(0, pos, null);

        mSearchPath.lineTo(pos[0], pos[1]);

        // 计算外部圆的长度
        mCircleLength = measure.getLength();

        // 计算放大镜的长度
        measure.setPath(mSearchPath, false);
        mSearchLength = measure.getLength();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterX = w / 2f;
        mCenterY = h / 2f;

        initPath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mCenterX, mCenterY);
        canvas.drawColor(0xaa87CEFA);


        drawSearch(canvas);

    }

    private void drawSearch(Canvas canvas) {

        dst.reset();
        switch (mCurrentState) {
            case NONE:
                canvas.drawPath(mSearchPath, mPaint);
                break;
            case STARTING:

                measure.setPath(mSearchPath, false);
//                Path dst = new Path();
                measure.getSegment(mSearchLength * mAnimatedValue, mSearchLength, dst, true);
                canvas.drawPath(dst, mPaint);
                break;

            case SEARCHING:

                measure.setPath(mCirclePath, false);
//                Path dstCircle = new Path();

                float stop = measure.getLength() * mAnimatedValue;
                float start = (float) (stop - ((0.5 - Math.abs(mAnimatedValue - 0.5)) * 200f));

                measure.getSegment(start, stop, dst, true);
                canvas.drawPath(dst, mPaint);
                break;
            case END:
                measure.setPath(mSearchPath, false);
//                Path dst2 = new Path();
                measure.getSegment(mSearchLength * mAnimatedValue, mSearchLength, dst, true);
                canvas.drawPath(dst, mPaint);

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mCurrentState);
        }


    }

    public void readyToRoll() {

        mStartAnimator.start();

        // 修改当前状态
        mCurrentState = State.STARTING;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                readyToRoll();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }
        return true;
    }
}
