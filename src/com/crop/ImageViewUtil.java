package com.crop;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

/*����ImageView������ʵ�ó����ࡣ*/
public class ImageViewUtil {
/*��ȡλͼ�ľ���λ��*/ 
    public static Rect getBitmapRectCenterInside(Bitmap bitmap, View view) {

        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();
        final int viewWidth = view.getWidth();
        final int viewHeight = view.getHeight();

        return getBitmapRectCenterInsideHelper(bitmapWidth, bitmapHeight, viewWidth, viewHeight);
    }

/*��ȡλͼ�ľ���λ�ã����λͼ������������������Ϊ{@link ImageView#ScaleType#CENTER#u inside}����ͼ�С�*/ 
    public static Rect getBitmapRectCenterInside(int bitmapWidth,int bitmapHeight,int viewWidth,int viewHeight)

    { return getBitmapRectCenterInsideHelper(bitmapWidth, bitmapHeight, viewWidth, viewHeight);}

/*����λͼλ��*/
    private static Rect getBitmapRectCenterInsideHelper(int bitmapWidth,int bitmapHeight,int viewWidth,int viewHeight) {

        double resultWidth;
        double resultHeight;
        int resultX;
        int resultY;

        double viewToBitmapWidthRatio = Double.POSITIVE_INFINITY;
        double viewToBitmapHeightRatio = Double.POSITIVE_INFINITY;

        //��鳤�ȿ���Ƿ���Ҫ����
        if (viewWidth < bitmapWidth) {
            viewToBitmapWidthRatio = (double) viewWidth / (double) bitmapWidth;}

        if (viewHeight < bitmapHeight) {
            viewToBitmapHeightRatio = (double) viewHeight / (double) bitmapHeight;}

//�������һ����Ҫ�޸ģ���ѡ����С���ʲ����м��� 
        if (viewToBitmapWidthRatio != Double.POSITIVE_INFINITY || viewToBitmapHeightRatio != Double.POSITIVE_INFINITY)
        {
            if (viewToBitmapWidthRatio <= viewToBitmapHeightRatio) {
                resultWidth = viewWidth;
                resultHeight = (bitmapHeight * resultWidth / bitmapWidth);}

            else {
                resultHeight = viewHeight;
                resultWidth = (bitmapWidth * resultHeight / bitmapHeight);}
        }
//����ͼƬ��֡���ַ�Χ�ڡ�����Ŀ��ֻ��ͼƬ��С 
        else {
            resultHeight = bitmapHeight;
            resultWidth = bitmapWidth;}

//����λͼ��ImageView�е�λ�á� 
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
