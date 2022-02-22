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

/*��ʾ�ü����ںͲü����������ɫ�������Զ�����ͼ��*/
public class CropOverlayView extends View
{
	private static final int SNAP_RADIUS_DP = 6;
	private static final float DEFAULT_SHOW_GUIDELINES_LIMIT = 100;

//��PaintUtil��ȡĬ��ֵ������һ��ֵ���Ա���ȷ���ƽǵ� 
	private static final float DEFAULT_CORNER_THICKNESS_DP = 5;
	private static final float DEFAULT_LINE_THICKNESS_DP = 3;
	private static final float DEFAULT_CORNER_OFFSET_DP = (DEFAULT_CORNER_THICKNESS_DP / 2)
			- (DEFAULT_LINE_THICKNESS_DP / 2);
	private static final float DEFAULT_CORNER_EXTENSION_DP = DEFAULT_CORNER_THICKNESS_DP
			/ 2 + DEFAULT_CORNER_OFFSET_DP;
	private static final float DEFAULT_CORNER_LENGTH_DP = 20;

//mGuidelinesö�� 
	private static final int GUIDELINES_OFF = 0;
	private static final int GUIDELINES_ON_TOUCH = 1;
	private static final int GUIDELINES_ON = 2;

//�����ڲü�������Χ���ư�ɫ���ε����ϡ� 
	private Paint mBorderPaint = new Paint();;

//����ʱ�����ڲü������ڻ���ָ���ߵ�Ϳ�ϡ� 
	private Paint mGuidelinePaint = new Paint();;

//�������߽ǵ����� 

	/*���п��Ľ�*/
	private Paint mCornerPaint = new Paint();

	/*�����ɰ�*/
	private Paint mBackgroundPaint = new Paint(); ;

//���ڲü���λͼ��Χ�ı߽�� 
	private Rect mBitmapRect= new Rect(10,100,100,10);;

//�����ֱ���Χ�������İ뾶��������Ϊ��λ���� 
	private float mHandleRadius;

//���ü����ڱ�Ե��߽���Ե�ľ���С�ڻ���ڴ˾��루������Ϊ��λ��ʱ���ü����ڵı�Ե����׽��ָ���߽�����Ӧ��Ե�� 
	private float mSnapRadius;
	
//���־�ȷ����λ�úͼ���ľ�ȷ�ֱ�λ��֮���x��yƫ�ơ�������һ��ƫ��������Ϊ�����ڼ�����ʱ����һЩ������أ���mHandleRadiusָ������
//���ǣ�����ϣ�����϶����Ʊ�ʱ������Щƫ��ֵ���Ա���Ʊ�������ת�� 
	private Pair<Float, Float> mTouchOffset;

//��ǰ���µľ�������δ���¾������Ϊ�ա� 
	private Handle mPressedHandle;

	//�Զ������Ե�ʵ������ 
	private int mGuidelines;

	//�ü���ͼ�Ƿ����״γ�ʼ��
	private boolean initializedCropWindow = false;

	//�ǵ�ֵ��ʵ������
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
	//���ڲü���λͼ��Χ�ı߽�� 
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{//�������ʼ���ü����ڣ���Ϊ������Ҫȷ����ͼ�Ĵ�С�� 
		initCropWindow(mBitmapRect);}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
//Ϊ�ü�������ư�͸�������� 
		drawBackground(canvas, mBitmapRect);

		if (showGuidelines())
		{   // �������Ƿ񻭳�
			if (mGuidelines == GUIDELINES_ON)
			{   drawRuleOfThirdsGuidelines(canvas);
				drawCornerText(canvas);}

			else if (mGuidelines == GUIDELINES_ON_TOUCH)
			{   // ���ڵ�����Сʱ���� 
				if (mPressedHandle != null)
				{   drawRuleOfThirdsGuidelines(canvas);
					drawCornerText(canvas);}}

			else if (mGuidelines == GUIDELINES_OFF){}}

		// ���������д��ڱ߽�
		canvas.drawRect(Edge.LEFT.getCoordinate(), Edge.TOP.getCoordinate(),
				Edge.RIGHT.getCoordinate(), Edge.BOTTOM.getCoordinate(),
				mBorderPaint);

		// mBorderPaint.

		drawCorners(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
//������Ļ�������¼�
		// ���δ���ô���ͼ����������������
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

//���ü��п��Ľ���ʽ
	public void setCropOverlayCornerBitmap(Bitmap bitmap)
	{
		mCornerBitmap = bitmap;
		if (mCornerBitmap != null)
		{
			mCornerWidth = mCornerBitmap.getWidth();
			mCornerHeight = mCornerBitmap.getHeight();
		}}


/*֪ͨCropOverlayViewͼ�������ImageView��λ�á����ǻ��Ʋü�����������ĵ��á�
ͼ��ı߽��*/
	public void setBitmapRect(Rect bitmapRect)
	{
		mBitmapRect = bitmapRect;
		initCropWindow(mBitmapRect);}
	
/*���òü�������ͼ��*/
	public void resetCropOverlayView()
	{   if (initializedCropWindow)
		{   initCropWindow(mBitmapRect);
			//�ػ溯��
			invalidate();}}

/*��CropOverlayView��׼������Ϊ�򿪡��رջ��ڵ���Ӧ�ó����Сʱ��ʾ��
*һ������������ָʾ�Ǵ򿪡��ر�׼�򣬻��ǽ��ڵ�����Сʱ��ʾ׼��*/
	public void setGuidelines(int guidelines)
	{
		if (guidelines < 0 || guidelines > 2)
		        throw new IllegalArgumentException("Guideline value must be set between 0 and 2. See documentation.");
		else
		{   mGuidelines = guidelines;
			if (initializedCropWindow)
			{   initCropWindow(mBitmapRect);
				invalidate();}}}

	/*�������г�ʼֵ����������initCropWindow������ͼ��һ��ʼ����һ������ʼ�����ԡ�*/
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
        //��������ʵ��
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

		// ���ýǳߴ��ֵ
		mCornerOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFAULT_CORNER_OFFSET_DP, displayMetrics);
		mCornerExtension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORNER_EXTENSION_DP,
				displayMetrics);
		mCornerLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFAULT_CORNER_LENGTH_DP, displayMetrics);

		//�������й涨������ֱ������ΪĬ��ֵ
		mGuidelines = CropImageView.DEFAULT_GUIDELINES;
	}
//δ��ֵ
	private void initCropWindow(Rect bitmapRect)
	{
        //�������Ժ����ü������Ѿ���ʼ�� 
		if (initializedCropWindow == false)
			initializedCropWindow = true;

			// Initialize crop window to have 10% padding w/ respect to image.
			//��ʼ���ü����ڣ�ʹ�������ͼ����10%����䡣 
			final float horizontalPadding = 0.1f * bitmapRect.width();
			final float verticalPadding = 0.1f * bitmapRect.height();

			Edge.LEFT.setCoordinate(bitmapRect.left + horizontalPadding);
			Edge.TOP.setCoordinate(bitmapRect.top + verticalPadding);
			Edge.RIGHT.setCoordinate(bitmapRect.right - horizontalPadding);
			Edge.BOTTOM.setCoordinate(bitmapRect.bottom - verticalPadding);
	}

	/*ָʾ�ü������Ƿ��㹻С���Ա���ʾ׼��Public����Ϊ�˺���������ȷ���Ƿ�Ӧ�۽����ľ����*/
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

		//���ƴ�ֱ��ָ���ߡ�
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

		// ���ơ��ϡ������¡������󡱺͡��ҡ����ޡ�
		canvas.drawRect(bitmapRect.left, bitmapRect.top, bitmapRect.right, top,
				mBackgroundPaint);
		canvas.drawRect(bitmapRect.left, bottom, bitmapRect.right,
				bitmapRect.bottom, mBackgroundPaint);
		canvas.drawRect(bitmapRect.left, top, left, bottom, mBackgroundPaint);
		canvas.drawRect(right, top, bitmapRect.right, bottom, mBackgroundPaint);
	}
//��ͼ���ı� 
	private void drawCornerText(Canvas canvas)
	{
    //����
		float displayedImageWidth = mBitmapRect.width();
		float displayedImageHeight = mBitmapRect.height();

		float scaleFactorWidth = mBitmapWidth / displayedImageWidth;
		float scaleFactorHeight = mBitmapHeight / displayedImageHeight;
    //ʵ�ʳ���
		int actualCropWidth = (int) (Edge.getWidth() * scaleFactorWidth);
		int actualCropHeight = (int) (Edge.getHeight() * scaleFactorHeight);

		mBorderPaint.setARGB(255, 255, 255, 255);
		mBorderPaint.setStrokeWidth(0);
		mBorderPaint.setTextAlign(Align.CENTER);
		mBorderPaint.setTextSize(25);
		//��������
		canvas.drawText(
				actualCropWidth + "x" + actualCropHeight,
				Edge.LEFT.getCoordinate() / 2 + +Edge.RIGHT.getCoordinate() / 2,
				Edge.TOP.getCoordinate() / 2 + Edge.BOTTOM.getCoordinate() / 2,
				mBorderPaint);}
//���ƽ���
	private void drawCorners(Canvas canvas)
	{
		final float left = Edge.LEFT.getCoordinate();
		final float top = Edge.TOP.getCoordinate();
		final float right = Edge.RIGHT.getCoordinate();
		final float bottom = Edge.BOTTOM.getCoordinate();

		// ���Ʊ߽���
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


/*����{ACTION _DOWN}�¼���*/
	private void onActionDown(float x, float y)
	{   final float left = Edge.LEFT.getCoordinate();
		final float top = Edge.TOP.getCoordinate();
		final float right = Edge.RIGHT.getCoordinate();
		final float bottom = Edge.BOTTOM.getCoordinate();

		mPressedHandle = HandleUtil.getPressedHandle(x, y, left, top, right,bottom, mHandleRadius);

		if (mPressedHandle == null) return;

//����Ӵ�������ڰ��־�ȷλ�õ�ƫ����������Щֵ�����ڳ�Ա�����У���Ϊ����ϣ�����϶����Ʊ�ʱ���ִ�ƫ�ơ� 
		mTouchOffset = HandleUtil.getOffset(mPressedHandle, x, y, left, top,right, bottom);

		invalidate();}

	private void onActionUp()
	{   if (mPressedHandle == null)  return;
        mPressedHandle = null;
        invalidate();}

	private void onActionMove(float x, float y)
	{if (mPressedHandle == null)return;		

//������ָλ��ƫ�Ƶ����꣨���ӳ�ʼ��������ȷ�ֱ�λ�õľ��룩��
//����ϣ�����ֳ�ʼ�����������ֱ��ľ��룬�Ա�ü����ڴ�С���ᡰ��Ծ���� 
		x += mTouchOffset.first;
		y += mTouchOffset.second;
		mPressedHandle.updateCropWindow(x, y, mBitmapRect, mSnapRadius);
		invalidate();}}
