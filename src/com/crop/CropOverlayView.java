package com.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import android.graphics.Color;

/*表示裁剪窗口和裁剪窗口外的着色背景的自定义视图。*/
public class CropOverlayView extends View
{
	private static final int SNAP_RADIUS_DP = 6;
	private static final float DEFAULT_SHOW_GUIDELINES_LIMIT = 100;

//从PaintUtil获取默认值，设置一组值，以便正确绘制角点 
	private static final float DEFAULT_CORNER_THICKNESS_DP = 5;
	private static final float DEFAULT_LINE_THICKNESS_DP = 3;
	private static final float DEFAULT_CORNER_OFFSET_DP = (DEFAULT_CORNER_THICKNESS_DP / 2)
			- (DEFAULT_LINE_THICKNESS_DP / 2);
	private static final float DEFAULT_CORNER_EXTENSION_DP = DEFAULT_CORNER_THICKNESS_DP
			/ 2 + DEFAULT_CORNER_OFFSET_DP;
	private static final float DEFAULT_CORNER_LENGTH_DP = 20;

//mGuidelines枚举 
	private static final int GUIDELINES_OFF = 0;
	private static final int GUIDELINES_ON_TOUCH = 1;
	private static final int GUIDELINES_ON = 2;

//用来在裁剪区域周围绘制白色矩形的颜料。 
	private Paint mBorderPaint = new Paint();;

//按下时用于在裁剪区域内绘制指导线的涂料。 
	private Paint mGuidelinePaint = new Paint();;

//用来画边角的颜料 

	/*剪切框四角*/
	private Paint mCornerPaint = new Paint();

	/*背景蒙版*/
	private Paint mBackgroundPaint = new Paint(); ;

//正在裁剪的位图周围的边界框。 
	private Rect mBitmapRect= new Rect(10,100,100,10);;

//给定手柄周围触摸区的半径（以像素为单位）。 
	private float mHandleRadius;

//当裁剪窗口边缘与边界框边缘的距离小于或等于此距离（以像素为单位）时，裁剪窗口的边缘将捕捉到指定边界框的相应边缘。 
	private float mSnapRadius;
	
//保持精确触摸位置和激活的精确手柄位置之间的x和y偏移。可能有一个偏移量，因为我们在激活句柄时允许一些回旋余地（由mHandleRadius指定）。
//但是，我们希望在拖动控制柄时保持这些偏移值，以便控制柄不会跳转。 
	private Pair<Float, Float> mTouchOffset;

//当前按下的句柄；如果未按下句柄，则为空。 
	private Handle mPressedHandle;

	//自定义属性的实例变量 
	private int mGuidelines;

	//裁剪视图是否已首次初始化
	private boolean initializedCropWindow = false;

	//角点值的实例变量
	private float mCornerExtension;
	private float mCornerOffset;
	private float mCornerLength;

	public CropOverlayView(Context context)
	{
		super(context);
		init(context);}
	
	public CropOverlayView(Context context, AttributeSet attrs)
	{
		super(context,attrs);
		init(context);}

	public Bitmap mBitmap = null;
	//正在裁剪的位图周围的边界框。 
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{//在这里初始化裁剪窗口，因为我们需要确定视图的大小。 
		initCropWindow(mBitmapRect);}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
//为裁剪区域绘制半透明背景。 
		drawBackground(canvas, mBitmapRect);

		if (showGuidelines())
		{   // 决定线是否画出
			if (mGuidelines == GUIDELINES_ON)
			{   drawRuleOfThirdsGuidelines(canvas);
				drawCornerText(canvas);}

			else if (mGuidelines == GUIDELINES_ON_TOUCH)
			{   // 仅在调整大小时绘制 
				if (mPressedHandle != null)
				{   drawRuleOfThirdsGuidelines(canvas);
					drawCornerText(canvas);}}

			else if (mGuidelines == GUIDELINES_OFF){}}

		// 绘制主截切窗口边界
		canvas.drawRect(Edge.LEFT.getCoordinate(), Edge.TOP.getCoordinate(),
				Edge.RIGHT.getCoordinate(), Edge.BOTTOM.getCoordinate(),
				mBorderPaint);

		// mBorderPaint.

		drawCorners(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
//触摸屏幕引发的事件
		// 如果未启用此视图，则不允许触摸交互。
		if (!isEnabled())
		{return false;}

		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN :
				onActionDown(event.getX(), event.getY());
				return true;

			case MotionEvent.ACTION_UP :
			case MotionEvent.ACTION_CANCEL :
				getParent().requestDisallowInterceptTouchEvent(false);
				onActionUp();
				return true;

			case MotionEvent.ACTION_MOVE :
				onActionMove(event.getX(), event.getY());
				getParent().requestDisallowInterceptTouchEvent(true);
				return true;

			default :
				return false;
		}}

	private int mBitmapWidth = 0;
	private int mBitmapHeight = 0;

	private Bitmap mCornerBitmap = null;
	private int mCornerWidth = 0;
	private int mCornerHeight = 0;

	public void setBitmapSize(int width, int height)
	{
		this.mBitmapWidth = width;
		this.mBitmapHeight = height;}

//设置剪切框四角样式
	public void setCropOverlayCornerBitmap(Bitmap bitmap)
	{
		mCornerBitmap = bitmap;
		if (mCornerBitmap != null)
		{
			mCornerWidth = mCornerBitmap.getWidth();
			mCornerHeight = mCornerBitmap.getHeight();
		}}


/*通知CropOverlayView图像相对于ImageView的位置。这是绘制裁剪窗口所必需的调用。
图像的边界框*/
	public void setBitmapRect(Rect bitmapRect)
	{
		mBitmapRect = bitmapRect;
		initCropWindow(mBitmapRect);}
	
/*重置裁剪覆盖视图。*/
	public void resetCropOverlayView()
	{   if (initializedCropWindow)
		{   initCropWindow(mBitmapRect);
			//重绘函数
			invalidate();}}

/*将CropOverlayView的准则设置为打开、关闭或在调整应用程序大小时显示。
*一个整数，用于指示是打开、关闭准则，还是仅在调整大小时显示准则。*/
	public void setGuidelines(int guidelines)
	{
		if (guidelines < 0 || guidelines > 2)
		        throw new IllegalArgumentException("Guideline value must be set between 0 and 2. See documentation.");
		else
		{   mGuidelines = guidelines;
			if (initializedCropWindow)
			{   initCropWindow(mBitmapRect);
				invalidate();}}}

	/*设置所有初始值，但不调用initCropWindow重置视图。一开始就用一次来初始化属性。*/
	public void setInitialAttributeValues(int guidelines)
	{
		if (guidelines < 0 || guidelines > 2)
			throw new IllegalArgumentException("Guideline value must be set between 0 and 2. See documentation.");
		else      mGuidelines = guidelines;}


	private void init(Context context)
	{

		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

		mHandleRadius = HandleUtil.getTargetRadius(context);

		mSnapRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,SNAP_RADIUS_DP, displayMetrics);
        //创建各项实例
		final float lineThicknessPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_THICKNESS_DP, 
				context.getResources().getDisplayMetrics());
		mBorderPaint.setColor(Color.parseColor("#AAFFFFFF"));
		mBorderPaint.setStrokeWidth(lineThicknessPx);
		mBorderPaint.setStyle(Paint.Style.STROKE);
	
		mGuidelinePaint.setColor(Color.parseColor("#AAFFFFFF"));
		mGuidelinePaint.setStrokeWidth(1);
		
		mBackgroundPaint.setColor(Color.parseColor("#B0000000"));
		
		final float lineThicknessPy = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORNER_THICKNESS_DP,
				context.getResources().getDisplayMetrics());

		
		mCornerPaint.setColor(Color.WHITE);
		mCornerPaint.setStrokeWidth(lineThicknessPy);
		mCornerPaint.setStyle(Paint.Style.STROKE);

		// 设置角尺寸的值
		mCornerOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFAULT_CORNER_OFFSET_DP, displayMetrics);
		mCornerExtension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORNER_EXTENSION_DP,
				displayMetrics);
		mCornerLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFAULT_CORNER_LENGTH_DP, displayMetrics);

		//除非另有规定，否则将直线设置为默认值
		mGuidelines = CropImageView.DEFAULT_GUIDELINES;
	}
//未赋值
	private void initCropWindow(Rect bitmapRect)
	{
        //告诉属性函数裁剪窗口已经初始化 
		if (initializedCropWindow == false)
			initializedCropWindow = true;

			// Initialize crop window to have 10% padding w/ respect to image.
			//初始化裁剪窗口，使其相对于图像有10%的填充。 
			final float horizontalPadding = 0.1f * bitmapRect.width();
			final float verticalPadding = 0.1f * bitmapRect.height();

			Edge.LEFT.setCoordinate(bitmapRect.left + horizontalPadding);
			Edge.TOP.setCoordinate(bitmapRect.top + verticalPadding);
			Edge.RIGHT.setCoordinate(bitmapRect.right - horizontalPadding);
			Edge.BOTTOM.setCoordinate(bitmapRect.bottom - verticalPadding);
	}

	/*指示裁剪窗口是否足够小，以便显示准则。Public，因为此函数还用于确定是否应聚焦中心句柄。*/
	public static boolean showGuidelines()
	{
		if ((Math.abs(Edge.LEFT.getCoordinate() - Edge.RIGHT.getCoordinate()) < DEFAULT_SHOW_GUIDELINES_LIMIT)
				|| (Math.abs(Edge.TOP.getCoordinate() - Edge.BOTTOM.getCoordinate()) < DEFAULT_SHOW_GUIDELINES_LIMIT))
			return false;
		else
			return true;}

	private void drawRuleOfThirdsGuidelines(Canvas canvas)
	{

		final float left = Edge.LEFT.getCoordinate();
		final float top = Edge.TOP.getCoordinate();
		final float right = Edge.RIGHT.getCoordinate();
		final float bottom = Edge.BOTTOM.getCoordinate();

		//绘制垂直的指导线。
		final float oneThirdCropWidth = Edge.getWidth() / 3;

		final float x1 = left + oneThirdCropWidth;
		canvas.drawLine(x1, top, x1, bottom, mGuidelinePaint);
		final float x2 = right - oneThirdCropWidth;
		canvas.drawLine(x2, top, x2, bottom, mGuidelinePaint);

		final float oneThirdCropHeight = Edge.getHeight() / 3;

		final float y1 = top + oneThirdCropHeight;
		canvas.drawLine(left, y1, right, y1, mGuidelinePaint);
		final float y2 = bottom - oneThirdCropHeight;
		canvas.drawLine(left, y2, right, y2, mGuidelinePaint);
	}

	private void drawBackground(Canvas canvas, Rect bitmapRect)
	{

		final float left = Edge.LEFT.getCoordinate();
		final float top = Edge.TOP.getCoordinate();
		final float right = Edge.RIGHT.getCoordinate();
		final float bottom = Edge.BOTTOM.getCoordinate();

		/*-
		  -------------------------------------
		  |                top                |
		  -------------------------------------
		  |      |                    |       |
		  |      |                    |       |
		  | left |                    | right |
		  |      |                    |       |
		  |      |                    |       |
		  -------------------------------------
		  |              bottom               |
		  -------------------------------------
		 */

		// 绘制“上”、“下”、“左”和“右”象限。
		canvas.drawRect(bitmapRect.left, bitmapRect.top, bitmapRect.right, top,
				mBackgroundPaint);
		canvas.drawRect(bitmapRect.left, bottom, bitmapRect.right,
				bitmapRect.bottom, mBackgroundPaint);
		canvas.drawRect(bitmapRect.left, top, left, bottom, mBackgroundPaint);
		canvas.drawRect(right, top, bitmapRect.right, bottom, mBackgroundPaint);
	}
//绘图角文本 
	private void drawCornerText(Canvas canvas)
	{
    //长宽
		float displayedImageWidth = mBitmapRect.width();
		float displayedImageHeight = mBitmapRect.height();

		float scaleFactorWidth = mBitmapWidth / displayedImageWidth;
		float scaleFactorHeight = mBitmapHeight / displayedImageHeight;
    //实际长宽
		int actualCropWidth = (int) (Edge.getWidth() * scaleFactorWidth);
		int actualCropHeight = (int) (Edge.getHeight() * scaleFactorHeight);

		mBorderPaint.setARGB(255, 255, 255, 255);
		mBorderPaint.setStrokeWidth(0);
		mBorderPaint.setTextAlign(Align.CENTER);
		mBorderPaint.setTextSize(25);
		//绘制文字
		canvas.drawText(
				actualCropWidth + "x" + actualCropHeight,
				Edge.LEFT.getCoordinate() / 2 + +Edge.RIGHT.getCoordinate() / 2,
				Edge.TOP.getCoordinate() / 2 + Edge.BOTTOM.getCoordinate() / 2,
				mBorderPaint);}
//绘制角落
	private void drawCorners(Canvas canvas)
	{
		final float left = Edge.LEFT.getCoordinate();
		final float top = Edge.TOP.getCoordinate();
		final float right = Edge.RIGHT.getCoordinate();
		final float bottom = Edge.BOTTOM.getCoordinate();

		// 绘制边角线
		// Top left

		if (mCornerBitmap != null)
		{
			// Top left
			canvas.drawBitmap(mCornerBitmap, left - mCornerWidth / 2, top - mCornerHeight / 2, null);
			// Top right
			canvas.drawBitmap(mCornerBitmap, right - mCornerWidth / 2, top - mCornerHeight / 2, null);
			// Bottom left
			canvas.drawBitmap(mCornerBitmap, left - mCornerWidth / 2, bottom - mCornerHeight / 2, null);
			// Bottom right
			canvas.drawBitmap(mCornerBitmap, right - mCornerWidth / 2, bottom - mCornerHeight / 2, null);}
		 else
		{
			// Top left
			canvas.drawLine(left - mCornerOffset, top - mCornerExtension, left
					- mCornerOffset, top + mCornerLength, mCornerPaint);
			canvas.drawLine(left, top - mCornerOffset, left + mCornerLength,
					top - mCornerOffset, mCornerPaint);

			// Top right
			canvas.drawLine(right + mCornerOffset, top - mCornerExtension,
					right + mCornerOffset, top + mCornerLength, mCornerPaint);
			canvas.drawLine(right, top - mCornerOffset, right - mCornerLength,
					top - mCornerOffset, mCornerPaint);

			// Bottom left
			canvas.drawLine(left - mCornerOffset, bottom + mCornerExtension,
					left - mCornerOffset, bottom - mCornerLength, mCornerPaint);
			canvas.drawLine(left, bottom + mCornerOffset, left + mCornerLength,
					bottom + mCornerOffset, mCornerPaint);

			// Bottom right
			canvas.drawLine(right + mCornerOffset, bottom + mCornerExtension,
					right + mCornerOffset, bottom - mCornerLength, mCornerPaint);
			canvas.drawLine(right, bottom + mCornerOffset, right
					- mCornerLength, bottom + mCornerOffset, mCornerPaint);
		}

	}


/*处理{ACTION _DOWN}事件。*/
	private void onActionDown(float x, float y)
	{   final float left = Edge.LEFT.getCoordinate();
		final float top = Edge.TOP.getCoordinate();
		final float right = Edge.RIGHT.getCoordinate();
		final float bottom = Edge.BOTTOM.getCoordinate();

		mPressedHandle = HandleUtil.getPressedHandle(x, y, left, top, right,bottom, mHandleRadius);

		if (mPressedHandle == null) return;

//计算接触点相对于把手精确位置的偏移量。将这些值保存在成员变量中，因为我们希望在拖动控制柄时保持此偏移。 
		mTouchOffset = HandleUtil.getOffset(mPressedHandle, x, y, left, top,right, bottom);

		invalidate();}

	private void onActionUp()
	{   if (mPressedHandle == null)  return;
        mPressedHandle = null;
        invalidate();}

	private void onActionMove(float x, float y)
	{if (mPressedHandle == null)return;		

//调整手指位置偏移的坐标（即从初始触摸到精确手柄位置的距离）。
//我们希望保持初始触摸到按下手柄的距离，以便裁剪窗口大小不会“跳跃”。 
		x += mTouchOffset.first;
		y += mTouchOffset.second;
		mPressedHandle.updateCropWindow(x, y, mBitmapRect, mSnapRadius);
		invalidate();}}
