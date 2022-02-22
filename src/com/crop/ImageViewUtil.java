package com.crop;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

/*处理ImageView操作的实用程序类。*/
public class ImageViewUtil {
/*获取位图的矩形位置*/ 
    public static Rect getBitmapRectCenterInside(Bitmap bitmap, View view) {

        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();
        final int viewWidth = view.getWidth();
        final int viewHeight = view.getHeight();

        return getBitmapRectCenterInsideHelper(bitmapWidth, bitmapHeight, viewWidth, viewHeight);
    }

/*获取位图的矩形位置，如果位图放置在缩放类型设置为{@link ImageView#ScaleType#CENTER#u inside}的视图中。*/ 
    public static Rect getBitmapRectCenterInside(int bitmapWidth,int bitmapHeight,int viewWidth,int viewHeight)

    { return getBitmapRectCenterInsideHelper(bitmapWidth, bitmapHeight, viewWidth, viewHeight);}

/*计算位图位置*/
    private static Rect getBitmapRectCenterInsideHelper(int bitmapWidth,int bitmapHeight,int viewWidth,int viewHeight) {

        double resultWidth;
        double resultHeight;
        int resultX;
        int resultY;

        double viewToBitmapWidthRatio = Double.POSITIVE_INFINITY;
        double viewToBitmapHeightRatio = Double.POSITIVE_INFINITY;

        //检查长度宽度是否需要修理
        if (viewWidth < bitmapWidth) {
            viewToBitmapWidthRatio = (double) viewWidth / (double) bitmapWidth;}

        if (viewHeight < bitmapHeight) {
            viewToBitmapHeightRatio = (double) viewHeight / (double) bitmapHeight;}

//如果其中一个需要修改，则选择最小比率并从中计算 
        if (viewToBitmapWidthRatio != Double.POSITIVE_INFINITY || viewToBitmapHeightRatio != Double.POSITIVE_INFINITY)
        {
            if (viewToBitmapWidthRatio <= viewToBitmapHeightRatio) {
                resultWidth = viewWidth;
                resultHeight = (bitmapHeight * resultWidth / bitmapWidth);}

            else {
                resultHeight = viewHeight;
                resultWidth = (bitmapWidth * resultHeight / bitmapHeight);}
        }
//否则，图片在帧布局范围内。所需的宽度只是图片大小 
        else {
            resultHeight = bitmapHeight;
            resultWidth = bitmapWidth;}

//计算位图在ImageView中的位置。 
        if (resultWidth == viewWidth) {
            resultX = 0;
            resultY = (int) Math.round((viewHeight - resultHeight) / 2);}
        else if (resultHeight == viewHeight) {
            resultX = (int) Math.round((viewWidth - resultWidth) / 2);
            resultY = 0;}
        else {
            resultX = (int) Math.round((viewWidth - resultWidth) / 2);
            resultY = (int) Math.round((viewHeight - resultHeight) / 2);}

        final Rect result = new Rect(resultX,resultY,resultX + (int) Math.ceil(resultWidth),resultY + (int) Math.ceil(resultHeight));

        return result;}}
