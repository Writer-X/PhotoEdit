package com.crop;

import android.graphics.Rect;

/*帮助程序类来处理裁剪窗口句柄上的操作。*/ 
abstract class HandleHelper {
    private Edge mHorizontalEdge;
    private Edge mVerticalEdge;

//将Pair对象另存为成员变量以避免实例化
//每次调用getActiveEdge（）时都会创建一个新对象。 
    private EdgePair mActiveEdges;

    HandleHelper(Edge horizontalEdge, Edge verticalEdge) {
        mHorizontalEdge = horizontalEdge;
        mVerticalEdge = verticalEdge;
        mActiveEdges = new EdgePair(mHorizontalEdge, mVerticalEdge);
    }

/*通过直接设置边坐标来更新裁剪窗口。*/
    void updateCropWindow(float x,float y,Rect imageRect,float snapRadius) {

        final EdgePair activeEdges = getActiveEdges();
        final Edge primaryEdge = activeEdges.primary;
        final Edge secondaryEdge = activeEdges.secondary;

        if (primaryEdge != null)
            primaryEdge.adjustCoordinate(x, y, imageRect, snapRadius);

        if (secondaryEdge != null)
            secondaryEdge.adjustCoordinate(x, y, imageRect, snapRadius);
    }

/*获取与此句柄关联的边（即拖动此句柄时应移动的边）。在不保持纵横比的情况下使用。*/ 
    EdgePair getActiveEdges() {return mActiveEdges;}
}
