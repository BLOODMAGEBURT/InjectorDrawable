package com.burt.injectordrawable.bean;

public class PieData {
    private String name;
    private float value;
    private float percentange;

    // 非用户关心数据
    private int color = 0;      // 颜色
    private float angle = 0;    // 角度

    public PieData(String name, float value) {
        this.name = name;
        this.value = value;
    }

    public float getPercentange() {
        return percentange;
    }

    public void setPercentange(float percentange) {
        this.percentange = percentange;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
    }
}
