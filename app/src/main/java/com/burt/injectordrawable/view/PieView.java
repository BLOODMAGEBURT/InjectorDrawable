package com.burt.injectordrawable.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.burt.injectordrawable.bean.PieData;

import java.util.ArrayList;

import java.util.Random;

public class PieView extends View {

    private Paint mPaint;
    private int mWidth, mHeight;

    private float mStartAngle = 0;
    private ArrayList<PieData> datas = new ArrayList<>();


    private int[] colors = {0xaaFFB6C1, 0xaaFF1493, 0xaa9400D3, 0xaa8A2BE2, 0xaa6A5ACD, 0xaa4169E1, 0xaa778899, 0xaa00BFFF, 0xaa00FFFF};

//    List<Integer> colors =  Arrays.asList(0xaaFFB6C1, 0xaaFF1493, 0xaa9400D3, 0xaa8A2BE2, 0xaa6A5ACD, 0xaa4169E1, 0xaa778899, 0xaa00BFFF, 0xaa00FFFF);

    private RectF rect;

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        float r = Math.min(mWidth, mHeight) / 2 * 0.8f;
        rect = new RectF(-r, -r, r, r);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == datas)
            return;

        canvas.translate(mWidth / 2, mHeight / 2);

        for (PieData data : datas) {
            mPaint.setColor(data.getColor());
            canvas.drawArc(rect, mStartAngle, data.getAngle(), true, mPaint);
            mStartAngle += data.getAngle();
        }

    }

    public void setData(ArrayList<PieData> data) {
        if (null == data || data.size() == 0) {
            return;
        }
        this.datas = data;
        initData(data);
        invalidate();
    }

    private void initData(ArrayList<PieData> datas) {
        float sumValue = 0;

        for (PieData data : datas) {
            sumValue += data.getValue();
        }

        for (PieData data : datas) {
            data.setPercentange(data.getValue() / sumValue);
            data.setAngle(data.getValue() / sumValue * 360);
            data.setColor(colors[new Random().nextInt(colors.length)]);
        }
    }

    public void setStartAngle(float startAngle) {
        this.mStartAngle = startAngle;
    }
}
