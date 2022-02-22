package com.crop;

import com.activity.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.util.AttributeSet;

/*Ϊͼ���ṩ�ü����ܵ��Զ�����ͼ��*/
public class CropImageView extends FrameLayout {

    //����
    private static final Rect EMPTY_RECT = new Rect();

//���õ�����СʱҪ��ʾ��Ĭ��ͼ��׼��
//ȱʡ������
    public static final int DEFAULT_GUIDELINES = 1;
    private static final int DEFAULT_IMAGE_RESOURCE = 0;

    private ImageView mImageView;
    //�ü����ǲ���ͼ 
    private CropOverlayView mCropOverlayView;

    private Bitmap mBitmap;

    private int mLayoutWidth;
    private int mLayoutHeight;

    //�Զ������Ե�ʵ������ 
    private int mGuidelines = DEFAULT_GUIDELINES;
    private int mImageResource = DEFAULT_IMAGE_RESOURCE;

    //��ʼ��
    public CropImageView(Context context) {
        super(context);
        init(context);}
    
    public CropImageView(Context context, AttributeSet attrs) {super(context, attrs );init(context);}


    //����ʵ��״̬
    @Override
    public Parcelable onSaveInstanceState() {

        final Bundle bundle = new Bundle();

        bundle.putParcelable("instanceState", super.onSaveInstanceState());
    
        return bundle;}

    //��ԭʵ��״̬
    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {

            final Bundle bundle = (Bundle) state;

            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));}
        else {
            super.onRestoreInstanceState(state);}}
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        if (mBitmap != null) {
            final Rect bitmapRect = ImageViewUtil.getBitmapRectCenterInside(mBitmap, this);
            mCropOverlayView.setBitmapRect(bitmapRect);}
        else {
            mCropOverlayView.setBitmapRect(EMPTY_RECT);}}


    //�л������Android��ϣ������Զ�����ͼ�ж��ȡ���ڸ���ͼ�ṩ�Ĳ���Լ��
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //һ��MeasureSpec��װ�˸����ִ��ݸ��Ӳ��ֵĲ���Ҫ��
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (mBitmap != null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            //����ScrollView��ʹ��heightSize����Ϊ0ʱ���ƹ����˷ѽ�Ĵ���
            if (heightSize == 0)
                heightSize = mBitmap.getHeight();

            int desiredWidth;
            int desiredHeight;

            double viewToBitmapWidthRatio = Double.POSITIVE_INFINITY;
            double viewToBitmapHeightRatio = Double.POSITIVE_INFINITY;
            //����Ƿ���Ҫ�����Ȼ�߶� 
            if (widthSize < mBitmap.getWidth()) {
                viewToBitmapWidthRatio = (double) widthSize / (double) mBitmap.getWidth();}

            if (heightSize < mBitmap.getHeight()) {
                viewToBitmapHeightRatio = (double) heightSize / (double) mBitmap.getHeight();}

            //�������һ����Ҫ�̶�����ѡ����С���ʲ����м��� 
            if (viewToBitmapWidthRatio != Double.POSITIVE_INFINITY || viewToBitmapHeightRatio != Double.POSITIVE_INFINITY) {
                if (viewToBitmapWidthRatio <= viewToBitmapHeightRatio) {
                    desiredWidth = widthSize;
                    desiredHeight = (int) (mBitmap.getHeight() * viewToBitmapWidthRatio);}

                else {
                    desiredHeight = heightSize;
                    desiredWidth = (int) (mBitmap.getWidth() * viewToBitmapHeightRatio);}}

            //����ͼƬ��֡���ַ�Χ�ڡ�����Ŀ��ֻ��ͼƬ��С 
            else {
                desiredWidth = mBitmap.getWidth();
                desiredHeight = mBitmap.getHeight();}

            int width = getOnMeasureSpec(widthMode, widthSize, desiredWidth);
            int height = getOnMeasureSpec(heightMode, heightSize, desiredHeight);

            mLayoutWidth = width;
            mLayoutHeight = height;
            //��ȡ�ھ������ĵ�λͼ
            final Rect bitmapRect = ImageViewUtil.getBitmapRectCenterInside(mBitmap.getWidth(),
                                                                            mBitmap.getHeight(),
                                                                            mLayoutWidth,
                                                                            mLayoutHeight);
            mCropOverlayView.setBitmapRect(bitmapRect);
            mCropOverlayView.setBitmapSize(mBitmap.getWidth(), mBitmap.getHeight());

            // MUST CALL THIS
            setMeasuredDimension(mLayoutWidth, mLayoutHeight);} 

        else {
            mCropOverlayView.setBitmapRect(EMPTY_RECT);
            setMeasuredDimension(widthSize, heightSize);}
    }

    //��view�Ĳ��ַ���
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

        if (mLayoutWidth > 0 && mLayoutHeight > 0) {
    //��ȡԭʼ�������������²��� 
            final ViewGroup.LayoutParams origparams = this.getLayoutParams();
            origparams.width = mLayoutWidth;
            origparams.height = mLayoutHeight;
    //���ò��ֲ���
            setLayoutParams(origparams);
        }}

    public int getImageResource() {return mImageResource;}

    /*��ʾ�ü�ͼƬ */
    public void setImageBitmap(Bitmap bitmap) {

        mBitmap = bitmap;
        try{mImageView.setImageBitmap(mBitmap);}
        catch(Exception e){
        	e.getMessage();
        }
        
        if (mCropOverlayView != null) {
            mCropOverlayView.resetCropOverlayView();
        }}

   /* Sets a Drawable as the content of the CropImageView.
    * ���ü�����Դͼ*/
    public void setImageResource(int resId) {
        if (resId != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
            setImageBitmap(bitmap);
        }}


  /*��ȡ�ü�����ͼ*/
    public Bitmap getCroppedImage() {

        final Rect displayedImageRect = ImageViewUtil.getBitmapRectCenterInside(mBitmap, mImageView);

//��ȡʵ��λͼ�ߴ����ʾ�Ŀ�ȳߴ�֮��ı������ӡ� 
        final float actualImageWidth = mBitmap.getWidth();
        final float displayedImageWidth = displayedImageRect.width();
        final float scaleFactorWidth = actualImageWidth / displayedImageWidth;

        final float actualImageHeight = mBitmap.getHeight();
        final float displayedImageHeight = displayedImageRect.height();
        final float scaleFactorHeight = actualImageHeight / displayedImageHeight;

//��ȡ�������ʾͼ��Ĳü�����λ�á� 
        final float cropWindowX = Edge.LEFT.getCoordinate() - displayedImageRect.left;
        final float cropWindowY = Edge.TOP.getCoordinate() - displayedImageRect.top;
        final float cropWindowWidth = Edge.getWidth();
        final float cropWindowHeight = Edge.getHeight();

//���ü�����λ�����ŵ�λͼ��ʵ�ʴ�С�� 
        final float actualCropX = cropWindowX * scaleFactorWidth;
        final float actualCropY = cropWindowY * scaleFactorHeight;
        final float actualCropWidth = cropWindowWidth * scaleFactorWidth;
        final float actualCropHeight = cropWindowHeight * scaleFactorHeight;

//��ԭʼλͼ�ü��Ӽ��� 
        final Bitmap croppedBitmap = Bitmap.createBitmap(mBitmap,(int) actualCropX,(int) actualCropY,
                                                         (int) actualCropWidth,(int) actualCropHeight);

        return croppedBitmap;
    }


/*��CropOverlayView��׼������Ϊ�򿪡��رջ��ڵ���Ӧ�ó����Сʱ��ʾ��*/
    public void setGuidelines(int guidelines) {
        mCropOverlayView.setGuidelines(guidelines);}
    
    /* bit = null ʱ��Ĭ��Ϊ��ɫ�Ǳ���*/

    public void setCropOverlayCornerBitmap(Bitmap bit)
    {mCropOverlayView.setCropOverlayCornerBitmap(bit);}

//��ʼ������
    private void init(Context context) {
        //���ز��������
        final LayoutInflater inflater = LayoutInflater.from(context);
        //��XML����ת��Ϊview����
        final View v = inflater.inflate(R.layout.crop_view, this, true);
        //����view�����ҵ�ͼƬ��������ͼƬ
        mImageView = (ImageView) v.findViewById(R.id.ImageView_image);
        //����ͼ����Դ
        setImageResource(mImageResource);
        //��ȡ�ü����ǲ�
        mCropOverlayView = (CropOverlayView) v.findViewById(R.id.CropOverlayView);
        mCropOverlayView.setInitialAttributeValues(mGuidelines);}

    @Override
	public boolean onTouchEvent(MotionEvent event)
	{  return mCropOverlayView.onTouchEvent(event);  }

private static int getOnMeasureSpec(int measureSpecMode, int measureSpecSize, int desiredSize) {

    // Measure Width
    int spec;
    if (measureSpecMode == MeasureSpec.EXACTLY) {
        // Must be this size
        spec = measureSpecSize;
    } else if (measureSpecMode == MeasureSpec.AT_MOST) {
        // Can't be bigger than...; match_parent value
        spec = Math.min(desiredSize, measureSpecSize);
    } else {
        // Be whatever you want; wrap_content
        spec = desiredSize;
    }

    return spec;
}

}
