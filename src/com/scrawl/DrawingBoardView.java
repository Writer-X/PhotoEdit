package com.scrawl;

import com.activity.R;
import com.scrawl.DrawAttribute;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class DrawingBoardView extends View
{
	private Bitmap backgroundBitmap = null;//画板背景
	private Bitmap paintBitmap = null;//绘画图层
	private Canvas paintCanvas = null;//绘画板
	/**手势监听*/
	private GestureDetector brushGestureDetector = null;
	private BrushGestureListener brushGestureListener = null;
	/**绘画类型*/
	private DrawAttribute.DrawStatus mDrawStatus; //画笔样式
	public DrawingBoardView(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);
		brushGestureListener = new BrushGestureListener();
		brushGestureDetector = new GestureDetector(context,brushGestureListener);
	}
	/**
	 * 绘画函数，在初始化时被调用
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		if(paintBitmap == null)
		{
			backgroundBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nullpicture);
			paintBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nullpicture);		
		    postInvalidateDelayed(100);
		}
		postInvalidate();
		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
		canvas.drawBitmap(paintBitmap, 0, 0, null);
	}
	/**
	 * 手势事件响应函数
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		brushGestureDetector.onTouchEvent(event);
		postInvalidate();
		return true;
	}
	/**
	 * 设置绘画的背景图片
	 */
	public void setBackgroundBitmap(Bitmap bitmap)
	{

		backgroundBitmap = bitmap;
		paintBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),
				backgroundBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		paintCanvas = new Canvas(paintBitmap);

	}
	/**
	 * 设置绘画类型
	 * @param drawStatus
	 */
	public void setDrawStatus(DrawAttribute.DrawStatus drawStatus)
	{
		this.mDrawStatus = drawStatus;
	}
	/**
	 * 设置画笔的颜色
	 */
	public void setPaintColor(int color)
	{
	}
	/**
	 * 设置画笔
	 * @param drawStatus 画笔状态
	 * @param brushBitmap 画笔
	 * @param brushColor  画笔颜色
	 */
	public void setBrushBitmap(DrawAttribute.DrawStatus drawStatus,Bitmap brushBitmap, int brushColor)
	{
		this.mDrawStatus = drawStatus;
		Bitmap tempBrush = null;
		int brushDistance = 0;
		Paint brushPaint = null;

		switch (mDrawStatus)
		{
			case PEN_WATER :

				brushDistance = 1;
				tempBrush = casualStroke(brushBitmap, brushColor);
				brushPaint = null;

				break;

			case PEN_CRAYON :

				brushDistance = brushBitmap.getWidth() / 2;
				tempBrush = casualStroke(brushBitmap, brushColor);
				brushPaint = null;

				break;

			case PEN_COLOR_BIG :
				tempBrush = casualStroke(brushBitmap, brushColor);
				brushDistance = 2;
				brushPaint = null;

				break;

			case PEN_ERASER :
				brushPaint = new Paint();
				brushPaint.setFilterBitmap(true);
				brushPaint.setXfermode(new PorterDuffXfermode(
						PorterDuff.Mode.DST_OUT));

				tempBrush = brushBitmap;
				brushDistance = brushBitmap.getWidth() / 4;

				break;

			default :
				break;
		}

		brushGestureListener.setBrush(tempBrush, brushDistance, brushPaint);
	}
	/**
	 * 设置邮票数据
	 * @param drawStatus
	 * @param res
	 * @param color
	 */
	public void setStampBitmaps(DrawAttribute.DrawStatus drawStatus, int[] res,
			int color)
	{
		Bitmap[] brushBitmaps = new Bitmap[4];
		brushBitmaps[0] = casualStroke(res[0], color);
		brushBitmaps[1] = casualStroke(res[1], color);
		brushBitmaps[2] = casualStroke(res[2], color);
		brushBitmaps[3] = casualStroke(res[3], color);

		brushGestureListener.setStampBrush(brushBitmaps);
		this.mDrawStatus = drawStatus;

	}
	/**
	 * 画图片
	 * @param drawableId
	 * @param color
	 * @return
	 */
	private Bitmap casualStroke(int drawableId, int color)
	{
		Bitmap mode = ((BitmapDrawable) this.getResources().getDrawable(drawableId)).getBitmap();
		Bitmap bitmap = mode.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);
		Paint paintUnder = new Paint();
		paintUnder.setColor(color);
		canvas.drawPaint(paintUnder);
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		canvas.drawBitmap(mode, 0, 0, paint);
		return bitmap;
	}
	/**
	 * 画图片
	 * @param mode 画笔
	 * @param color  画笔颜色
	 * @return 将画笔(图片)涂色
	 * 
	 */
	private Bitmap casualStroke(Bitmap paintBit, int color)
	{
		Bitmap bitmap = paintBit.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);
		Paint paintUnder = new Paint();
		paintUnder.setColor(color);
		canvas.drawPaint(paintUnder);
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		canvas.drawBitmap(paintBit, 0, 0, paint);
		paintBit.recycle();
		return bitmap;
	}
     /**
      * 画刷动作监听类
      */
	class BrushGestureListener extends GestureDetector.SimpleOnGestureListener
	{
		private Bitmap brushBitmap = null;
		private int brushDistance;
		
		//半个画刷宽度
		private int halfBrushBitmapWidth;
		//画刷
		private Paint brushPaint = null;
		//邮票
		private Bitmap[] stampBrushBitmaps = null;
		//是否是邮票
		private boolean isStamp = false;
		/**
		 * 画刷监听函数
		 */
		public BrushGestureListener()
		{
			super();
			isStamp = false;
		}
		/**
		 * 手势触摸事件
		 */
		@Override
		public boolean onDown(MotionEvent e)
		{

			switch (mDrawStatus)
			{
				case PEN_WATER :
				case PEN_CRAYON :
				case PEN_COLOR_BIG :
				case PEN_ERASER :
				{
					isStamp = false;
					break;
				}
				case PEN_STAMP :
				{
					isStamp = true;
					break;
				}
				default :
					isStamp = false;
					break;
			}
			if (isStamp)
			{
				paintSingleStamp(e.getX(), e.getY());
			} else
			{
				paintCanvas.drawBitmap(brushBitmap, e.getX()- halfBrushBitmapWidth,e.getY() - halfBrushBitmapWidth, brushPaint);
			}
			return true;
		}
		/**
		 * 处理对象内容区滚动事件
		 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,float distanceX, float distanceY)
		{

			if (isStamp)
			{
				//paintSingleStamp(e2.getX(), e2.getY());
			} else
			{
				float beginX = e2.getX();
				float beginY = e2.getY();
				float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
				float x = distanceX / distance, x_ = 0;
				float y = distanceY / distance, y_ = 0;
				while (Math.abs(x_) <= Math.abs(distanceX)&& Math.abs(y_) <= Math.abs(distanceY))
				{
					x_ += x * brushDistance;
					y_ += y * brushDistance;
					paintCanvas.save();
					paintCanvas.rotate((float) (Math.random() * 10000), beginX + x_, beginY + y_);
					paintCanvas.drawBitmap(brushBitmap, beginX + x_
							- halfBrushBitmapWidth, beginY + y_
							- halfBrushBitmapWidth, brushPaint);
					paintCanvas.restore();
				}
			}

			return true;
		}
		/**
		 * 设置画刷
		 * @param brushBitmap
		 * @param brushDistance
		 * @param brushPaint
		 */
		public void setBrush(Bitmap brushBitmap, int brushDistance,
				Paint brushPaint)
		{
			this.brushBitmap = brushBitmap;
			this.brushDistance = brushDistance;
			this.halfBrushBitmapWidth = brushBitmap.getWidth() / 2;
			this.brushPaint = brushPaint;
		}
		/**
		 * 设置邮票画刷
		 * @param brushBitmaps
		 */
		public void setStampBrush(Bitmap[] brushBitmaps)
		{
			this.stampBrushBitmaps = brushBitmaps;
			halfBrushBitmapWidth = brushBitmaps[0].getWidth() / 2;
		}
		/**
		 * 绘画单个邮票
		 * @param x
		 * @param y
		 */
		private void paintSingleStamp(float x, float y)
		{
			
			if (Math.random() > 0.1)
			{
				paintCanvas.drawBitmap(stampBrushBitmaps[0], x
						- halfBrushBitmapWidth, y - halfBrushBitmapWidth, null);
			}
			
			for (int i = 1; i < stampBrushBitmaps.length; i++)
			{
				if (Math.random() > 0.3)
				{
					paintCanvas.drawBitmap(stampBrushBitmaps[i], x - halfBrushBitmapWidth, y - halfBrushBitmapWidth,null);
				}
			}
		}	
	}
	/**
	 * 得到绘画图片
	 * @return
	 */
	public Bitmap getDrawBitmap()
	{
		Bitmap bitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),
				backgroundBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
		canvas.drawBitmap(paintBitmap, 0, 0, null);
		canvas.save();

		return bitmap;
	}
}
