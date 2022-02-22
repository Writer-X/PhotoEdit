package com.crop;

import android.graphics.Rect;

/*表示裁剪窗口中的边的枚举。*/
public enum Edge {
    LEFT,
    TOP,
    RIGHT,
    BOTTOM;

//一条边到另一条边的最小距离。这是一个任意值，可以简单地防止裁剪窗口变得太小。 
    public static final int MIN_CROP_LENGTH_PX = 40;

    private float mCoordinate;

    /*设置边的坐标*/
    public void setCoordinate(float coordinate) {mCoordinate = coordinate;}

    /*将给定数量的像素添加到此边的当前坐标位置*/
    public void offset(float distance) {mCoordinate += distance;}

    /*得到边的坐标*/
    public float getCoordinate() {return mCoordinate;}

    /*将边设置为给定的x-y坐标，但也调整为捕捉到图像边界和父视图边界约束。*/
    public void adjustCoordinate(float x, float y, Rect imageRect, float imageSnapRadius) {

        switch (this) {
            case LEFT:
                mCoordinate = adjustLeft(x, imageRect, imageSnapRadius);
                break;
            case TOP:
                mCoordinate = adjustTop(y, imageRect, imageSnapRadius);
                break;
            case RIGHT:
            	mCoordinate = adjustRight(x, imageRect, imageSnapRadius);
                break;
            case BOTTOM:
                mCoordinate = adjustBottom(y, imageRect, imageSnapRadius);
                break;
        }
    }
    /*将此边捕捉到给定的图像边界*/
    public float snapToRect(Rect imageRect) {

        final float oldCoordinate = mCoordinate;

        switch (this) {
            case LEFT:
                mCoordinate = imageRect.left;
                break;
            case TOP:
                mCoordinate = imageRect.top;
                break;
            case RIGHT:
                mCoordinate = imageRect.right;
                break;
            case BOTTOM:
                mCoordinate = imageRect.bottom;
                break;
        }

        final float offset = mCoordinate - oldCoordinate;
        return offset;
    }
  

    /*Gets the current width of the crop window.*/
    public static float getWidth() {return Edge.RIGHT.getCoordinate() - Edge.LEFT.getCoordinate();}

    /*Gets the current height of the crop window.*/
    public static float getHeight() {return Edge.BOTTOM.getCoordinate() - Edge.TOP.getCoordinate();}

    /*确定此边是否在给定边界矩形的内边距之外。*/
    public boolean isOutsideMargin(Rect rect, float margin) {

        boolean result = false;

        switch (this) {
            case LEFT:
                result = mCoordinate - rect.left < margin;
                break;
            case TOP:
                result = mCoordinate - rect.top < margin;
                break;
            case RIGHT:
                result = rect.right - mCoordinate < margin;
                break;
            case BOTTOM:
                result = rect.bottom - mCoordinate < margin;
                break;
        }
        return result;
    }

    private static float adjustLeft(float x, Rect imageRect, float imageSnapRadius) {

        float resultX = x;

        if (x - imageRect.left < imageSnapRadius)
            resultX = imageRect.left;

        else
        {   if (x >= Edge.RIGHT.getCoordinate() - MIN_CROP_LENGTH_PX)
                resultX = Edge.RIGHT.getCoordinate() - MIN_CROP_LENGTH_PX;}

        return resultX;
    }

    private static float adjustRight(float x, Rect imageRect, float imageSnapRadius) {

        float resultX = x;

        if (imageRect.right - x < imageSnapRadius)
            resultX = imageRect.right;

        else
        {   if (x <= Edge.LEFT.getCoordinate() + MIN_CROP_LENGTH_PX)
                resultX = Edge.LEFT.getCoordinate() + MIN_CROP_LENGTH_PX;}

        return resultX;
    }

    private static float adjustTop(float y, Rect imageRect, float imageSnapRadius) {

        float resultY = y;

        if (y - imageRect.top < imageSnapRadius)
            resultY = imageRect.top;

        else
        {   if (y >= Edge.BOTTOM.getCoordinate() - MIN_CROP_LENGTH_PX)
                resultY = Edge.BOTTOM.getCoordinate() - MIN_CROP_LENGTH_PX;}

        return resultY;
    }

    private static float adjustBottom(float y, Rect imageRect, float imageSnapRadius) {

        float resultY = y;

        if (imageRect.bottom - y < imageSnapRadius)
            resultY = imageRect.bottom;
        else
        {   if (y <= Edge.TOP.getCoordinate() + MIN_CROP_LENGTH_PX)
                resultY = Edge.TOP.getCoordinate() + MIN_CROP_LENGTH_PX;}

        return resultY;
    }
}
