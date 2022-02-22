package com.crop;

import android.graphics.Rect;

/*表示裁剪窗口上可按、可拖动的句柄的枚举。*/
public enum Handle {

    TOP_LEFT(new OtherHandleHelper(Edge.TOP, Edge.LEFT)),
    TOP_RIGHT(new OtherHandleHelper(Edge.TOP, Edge.RIGHT)),
    BOTTOM_LEFT(new OtherHandleHelper(Edge.BOTTOM, Edge.LEFT)),
    BOTTOM_RIGHT(new OtherHandleHelper(Edge.BOTTOM, Edge.RIGHT)),
    LEFT(new OtherHandleHelper(null,Edge.LEFT)),
    TOP(new OtherHandleHelper(Edge.TOP,null)),
    RIGHT(new OtherHandleHelper(null,Edge.RIGHT)),
    BOTTOM(new OtherHandleHelper(Edge.BOTTOM,null)),
    CENTER(new CenterHandleHelper());

    private HandleHelper mHelper;

    Handle(HandleHelper helper) {mHelper = helper;}

    public void updateCropWindow(float x,float y,Rect imageRect,float snapRadius) {
                mHelper.updateCropWindow(x, y, imageRect, snapRadius);}
}