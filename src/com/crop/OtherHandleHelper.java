package com.crop;

import android.graphics.Rect;

class OtherHandleHelper extends HandleHelper {

    OtherHandleHelper(Edge horizontalEdge, Edge verticalEdge) {super(horizontalEdge, verticalEdge);}

    /*ͨ��ֱ�����ñ����������²ü����ڡ�*/
    @Override
    void updateCropWindow(float x,float y,Rect imageRect,float snapRadius) {

        final EdgePair activeEdges = getActiveEdges();
        final Edge primaryEdge = activeEdges.primary;
        final Edge secondaryEdge = activeEdges.secondary;

        if (primaryEdge != null)
            primaryEdge.adjustCoordinate(x, y, imageRect, snapRadius);

        if (secondaryEdge != null)
            secondaryEdge.adjustCoordinate(x, y, imageRect, snapRadius);
    }
    }
