package com.crop;

import android.content.Context;
import android.util.Pair;
import android.util.TypedValue;

/*使用句柄执行基本操作的实用程序类。*/ 
public class HandleUtil {

//手柄周围可接触区域的半径（dp）。我们是根据建议的48dp节奏来计算这个值的
    private static final int TARGET_RADIUS_DP = 24;

/*获取默认的目标半径（以像素为单位）。这是激活手柄时可以触摸的圆形区域的半径。
  *@返回目标半径（像素）*/ 
    public static float getTargetRadius(Context context) {

        final float targetRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                                             TARGET_RADIUS_DP,
                                                             context.getResources().getDisplayMetrics());
        return targetRadius;}

/**
*确定触摸时按下哪个手柄（如果有）
*@返回被按下的句柄；如果没有按下句柄，则返回null
*/ 
    public static Handle getPressedHandle(float x,float y,float left,float top,float right,float bottom,float targetRadius) {

        Handle pressedHandle = null;

        if (HandleUtil.isInCornerTargetZone(x, y, left, top, targetRadius)) {
            pressedHandle = Handle.TOP_LEFT;
        } else if (HandleUtil.isInCornerTargetZone(x, y, right, top, targetRadius)) {
            pressedHandle = Handle.TOP_RIGHT;
        } else if (HandleUtil.isInCornerTargetZone(x, y, left, bottom, targetRadius)) {
            pressedHandle = Handle.BOTTOM_LEFT;
        } else if (HandleUtil.isInCornerTargetZone(x, y, right, bottom, targetRadius)) {
            pressedHandle = Handle.BOTTOM_RIGHT;
        } else if (HandleUtil.isInCenterTargetZone(x, y, left, top, right, bottom) && focusCenter()) {
            pressedHandle = Handle.CENTER;
        } else if (HandleUtil.isInHorizontalTargetZone(x, y, left, right, top, targetRadius)) {
            pressedHandle = Handle.TOP;
        } else if (HandleUtil.isInHorizontalTargetZone(x, y, left, right, bottom, targetRadius)) {
            pressedHandle = Handle.BOTTOM;
        } else if (HandleUtil.isInVerticalTargetZone(x, y, left, top, bottom, targetRadius)) {
            pressedHandle = Handle.LEFT;
        } else if (HandleUtil.isInVerticalTargetZone(x, y, right, top, bottom, targetRadius)) {
            pressedHandle = Handle.RIGHT;
        } else if (HandleUtil.isInCenterTargetZone(x, y, left, top, right, bottom) && !focusCenter()) {
            pressedHandle = Handle.CENTER;

        }

        return pressedHandle;
    }

/*计算接触点相对于传感器精确位置的偏移量*/
    public static Pair<Float, Float> getOffset(Handle handle,float x,float y,float left,float top,float right,float bottom) {

        if (handle == null) {
            return null;}

        float touchOffsetX = 0;
        float touchOffsetY = 0;
//计算与相应控制柄的偏移量。 
        switch (handle) {

            case TOP_LEFT:
                touchOffsetX = left - x;
                touchOffsetY = top - y;
                break;
            case TOP_RIGHT:
                touchOffsetX = right - x;
                touchOffsetY = top - y;
                break;
            case BOTTOM_LEFT:
                touchOffsetX = left - x;
                touchOffsetY = bottom - y;
                break;
            case BOTTOM_RIGHT:
                touchOffsetX = right - x;
                touchOffsetY = bottom - y;
                break;
            case LEFT:
                touchOffsetX = left - x;
                touchOffsetY = 0;
                break;
            case TOP:
                touchOffsetX = 0;
                touchOffsetY = top - y;
                break;
            case RIGHT:
                touchOffsetX = right - x;
                touchOffsetY = 0;
                break;
            case BOTTOM:
                touchOffsetX = 0;
                touchOffsetY = bottom - y;
                break;
            case CENTER:
                final float centerX = (right + left) / 2;
                final float centerY = (top + bottom) / 2;
                touchOffsetX = centerX - x;
                touchOffsetY = centerY - y;
                break;
        }

        final Pair<Float, Float> result = new Pair<Float, Float>(touchOffsetX, touchOffsetY);
        return result;
    }

/*确定指定的坐标是否位于角控制柄的目标触摸区域中。*/ 
    private static boolean isInCornerTargetZone(float x,float y,float handleX,float handleY,float targetRadius) {

        if (Math.abs(x - handleX) <= targetRadius && Math.abs(y - handleY) <= targetRadius) {
            return true;
        }
        return false;}

    /*确定指定的坐标是否位于水平条控制柄的目标触摸区域中。*/
    private static boolean isInHorizontalTargetZone(float x,float y,float handleXStart,float handleXEnd,float handleY,float targetRadius) {

        if (x > handleXStart && x < handleXEnd && Math.abs(y - handleY) <= targetRadius) {return true;}
        return false;
    }

    /*确定指定的坐标是否位于垂直条控制柄的目标触摸区域中。*/
    private static boolean isInVerticalTargetZone(float x,float y,float handleX,float handleYStart,float handleYEnd,float targetRadius) {

        if (Math.abs(x - handleX) <= targetRadius && y > handleYStart && y < handleYEnd) {return true;}
        return false;}

    /*确定指定的坐标是否落在给定边界内的任何位置。*/
    private static boolean isInCenterTargetZone(float x,float y,float left,float top,float right,float bottom) {

        if (x > left && x < right && y > top && y < bottom) {return true;}

        return false;}
        
/*确定裁剪器应将焦点放在中心控制柄还是侧控制柄上。如果它是一个小的图像，集中在中心处理，以便用户可以移动它。如果这是一个大的图像，重点放在侧把手，以便用户可以抓住他们。对应于第三个指南规则的外观 */
    private static boolean focusCenter() {
        return (!CropOverlayView.showGuidelines());}
}
