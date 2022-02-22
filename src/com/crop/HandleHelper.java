package com.crop;

import android.graphics.Rect;

/*����������������ü����ھ���ϵĲ�����*/ 
abstract class HandleHelper {
    private Edge mHorizontalEdge;
    private Edge mVerticalEdge;

//��Pair�������Ϊ��Ա�����Ա���ʵ����
//ÿ�ε���getActiveEdge����ʱ���ᴴ��һ���¶��� 
    private EdgePair mActiveEdges;

    HandleHelper(Edge horizontalEdge, Edge verticalEdge) {
        mHorizontalEdge = horizontalEdge;
        mVerticalEdge = verticalEdge;
        mActiveEdges = new EdgePair(mHorizontalEdge, mVerticalEdge);
    }

/*ͨ��ֱ�����ñ����������²ü����ڡ�*/
    void updateCropWindow(float x,float y,Rect imageRect,float snapRadius) {

        final EdgePair activeEdges = getActiveEdges();
        final Edge primaryEdge = activeEdges.primary;
        final Edge secondaryEdge = activeEdges.secondary;

        if (primaryEdge != null)
            primaryEdge.adjustCoordinate(x, y, imageRect, snapRadius);

        if (secondaryEdge != null)
            secondaryEdge.adjustCoordinate(x, y, imageRect, snapRadius);
    }

/*��ȡ��˾�������ıߣ����϶��˾��ʱӦ�ƶ��ıߣ����ڲ������ݺ�ȵ������ʹ�á�*/ 
    EdgePair getActiveEdges() {return mActiveEdges;}
}
