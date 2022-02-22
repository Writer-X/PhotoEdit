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

/*为图像提供裁剪功能的自定义视图。*/
public class CropImageView extends FrameLayout {

    //矩形
    private static final Rect EMPTY_RECT = new Rect();

//设置调整大小时要显示的默认图像准则
//缺省·参数
    public static final int DEFAULT_GUIDELINES = 1;
    private static final int DEFAULT_IMAGE_RESOURCE = 0;

    private ImageView mImageView;
    //裁剪覆盖层视图 
    private CropOverlayView mCropOverlayView;

    private Bitmap mBitmap;

    private int mLayoutWidth;
    private int mLayoutHeight;

    //自定义属性的实例变量 
    private int mGuidelines = DEFAULT_GUIDELINES;
    private int mImageResource = DEFAULT_IMAGE_RESOURCE;

    //初始化
    public CropImageView(Context context) {
        super(context);
        init(context);}
    
    public CropImageView(Context context, AttributeSet attrs) {super(context, attrs );init(context);}


    //保存实例状态
    @Override
    public Parcelable onSaveInstanceState() {

        final Bundle bundle = new Bundle();

        bundle.putParcelable("instanceState", super.onSaveInstanceState());
    
        return bundle;}

    //还原实例状态
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


    //有机会告诉Android你希望你的自定义视图有多大取决于父视图提供的布局约束
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //一个MeasureSpec封装了父布局传递给子布局的布局要求
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (mBitmap != null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            //当在ScrollView中使用heightSize设置为0时，绕过令人费解的错误。
            if (heightSize == 0)
                heightSize = mBitmap.getHeight();

            int desiredWidth;
            int desiredHeight;

            double viewToBitmapWidthRatio = Double.POSITIVE_INFINITY;
            double viewToBitmapHeightRatio = Double.POSITIVE_INFINITY;
            //检查是否需要处理宽度或高度 
            if (widthSize < mBitmap.getWidth()) {
                viewToBitmapWidthRatio = (double) widthSize / (double) mBitmap.getWidth();}

            if (heightSize < mBitmap.getHeight()) {
                viewToBitmapHeightRatio = (double) heightSize / (double) mBitmap.getHeight();}

            //如果其中一个需要固定，则选择最小比率并从中计算 
            if (viewToBitmapWidthRatio != Double.POSITIVE_INFINITY || viewToBitmapHeightRatio != Double.POSITIVE_INFINITY) {
                if (viewToBitmapWidthRatio <= viewToBitmapHeightRatio) {
                    desiredWidth = widthSize;
                    desiredHeight = (int) (mBitmap.getHeight() * viewToBitmapWidthRatio);}

                else {
                    desiredHeight = heightSize;
                    desiredWidth = (int) (mBitmap.getWidth() * viewToBitmapHeightRatio);}}

            //否则，图片在帧布局范围内。所需的宽度只是图片大小 
            else {
                desiredWidth = mBitmap.getWidth();
                desiredHeight = mBitmap.getHeight();}

            int width = getOnMeasureSpec(widthMode, widthSize, desiredWidth);
            int height = getOnMeasureSpec(heightMode, heightSize, desiredHeight);

            mLayoutWidth = width;
            mLayoutHeight = height;
            //获取在矩形中心的位图
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

    //子view的布局方法
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

        if (mLayoutWidth > 0 && mLayoutHeight > 0) {
    //获取原始参数，并创建新参数 
            final ViewGroup.LayoutParams origparams = this.getLayoutParams();
            origparams.width = mLayoutWidth;
            origparams.height = mLayoutHeight;
    //设置布局参数
            setLayoutParams(origparams);
        }}

    public int getImageResource() {return mImageResource;}

    /*显示裁剪图片 */
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
    * 设置剪切资源图*/
    public void setImageResource(int resId) {
        if (resId != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
            setImageBitmap(bitmap);
        }}


  /*获取裁剪区域图*/
    public Bitmap getCroppedImage() {

        final Rect displayedImageRect = ImageViewUtil.getBitmapRectCenterInside(mBitmap, mImageView);

//获取实际位图尺寸和显示的宽度尺寸之间的比例因子。 
        final float actualImageWidth = mBitmap.getWidth();
        final float displayedImageWidth = displayedImageRect.width();
        final float scaleFactorWidth = actualImageWidth / displayedImageWidth;

        final float actualImageHeight = mBitmap.getHeight();
        final float displayedImageHeight = displayedImageRect.height();
        final float scaleFactorHeight = actualImageHeight / displayedImageHeight;

//获取相对于显示图像的裁剪窗口位置。 
        final float cropWindowX = Edge.LEFT.getCoordinate() - displayedImageRect.left;
        final float cropWindowY = Edge.TOP.getCoordinate() - displayedImageRect.top;
        final float cropWindowWidth = Edge.getWidth();
        final float cropWindowHeight = Edge.getHeight();

//将裁剪窗口位置缩放到位图的实际大小。 
        final float actualCropX = cropWindowX * scaleFactorWidth;
        final float actualCropY = cropWindowY * scaleFactorHeight;
        final float actualCropWidth = cropWindowWidth * scaleFactorWidth;
        final float actualCropHeight = cropWindowHeight * scaleFactorHeight;

//从原始位图裁剪子集。 
        final Bitmap croppedBitmap = Bitmap.createBitmap(mBitmap,(int) actualCropX,(int) actualCropY,
                                                         (int) actualCropWidth,(int) actualCropHeight);

        return croppedBitmap;
    }


/*将CropOverlayView的准则设置为打开、关闭或在调整应用程序大小时显示。*/
    public void setGuidelines(int guidelines) {
        mCropOverlayView.setGuidelines(guidelines);}
    
    /* bit = null 时，默认为白色角边线*/

    public void setCropOverlayCornerBitmap(Bitmap bit)
    {mCropOverlayView.setCropOverlayCornerBitmap(bit);}

//初始化函数
    private void init(Context context) {
        //加载布局填充器
        final LayoutInflater inflater = LayoutInflater.from(context);
        //将XML布局转化为view对象
        final View v = inflater.inflate(R.layout.crop_view, this, true);
        //利用view对象找到图片，即设置图片
        mImageView = (ImageView) v.findViewById(R.id.ImageView_image);
        //设置图像资源
        setImageResource(mImageResource);
        //获取裁剪覆盖层
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
