/**
 * @author xutong
 * @date 2021/4/13
 * 
 * VerticalSeekBar
 * 功能：
 * 垂直滑动条类
 * 是一个自定义控件，用于实现滤镜功能，控制滤镜强度
 */
package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;


public class VerticalSeekBar extends SeekBar
{
	private int mLastProgress = 0;
	private OnSeekBarChangeListener mOnChangeListener;
	
	public VerticalSeekBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	/**
	 * OnDraw()函数，在界面加载时被调用
	 */
	@Override
	protected void onDraw(Canvas c)
	{
		c.rotate(-90);
		c.translate(-getHeight(), 0);
		super.onDraw(c);
	}
	/**
	 * 控件显示
	 */
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec)
	{
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}
	/**
	 * 设置大小
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(h, w, oldh, oldw);
	}
	/**
	 * 拖动滑动条的响应函数
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (!isEnabled())
		{
			return false;
		}

		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN :
				if (mOnChangeListener != null)
				{
					mOnChangeListener.onStartTrackingTouch(this);
				}
				setPressed(true);
				setSelected(true);
				break;
			case MotionEvent.ACTION_MOVE :
				super.onTouchEvent(event);
				int progress = getMax()- (int) (getMax() * event.getY() / getHeight());
                /*保证滑动不会超出滑动条的范围*/
				if (progress < 0)   
				{
					progress = 0;
				}
				if (progress > getMax())
				{
					progress = getMax();
				}
				setProgress(progress); 
				if (progress != mLastProgress)
				{
					mLastProgress = progress;
					if (mOnChangeListener != null)
					{
						mOnChangeListener.onProgressChanged(this, progress,true);
					}
				}
				onSizeChanged(getWidth(), getHeight(), 0, 0);
				setPressed(true);
				setSelected(true);
				break;
			case MotionEvent.ACTION_UP :
				if (mOnChangeListener != null)
				{
					mOnChangeListener.onStopTrackingTouch(this);
				}
				setPressed(false);
				setSelected(false);
				break;
			case MotionEvent.ACTION_CANCEL :
				super.onTouchEvent(event);
				setPressed(false);
				setSelected(false);
				break;
		}
		return true;
	}
	/**
	 * 设置最大值
	 * @param maximum 
	 */
	public synchronized void setMaximum(int maximum)
	{
		setMax(maximum);
	}
	/**
	 * 设置进度条和按钮位置
	 * @param progress 度/百分比
	 */
	public synchronized void setProgressAndThumb(int progress)
	{
		setProgress(progress);
		onSizeChanged(getWidth(), getHeight(), 0, 0);
		if (progress != mLastProgress)
		{
			mLastProgress = progress;
			if (mOnChangeListener != null)
			{
				mOnChangeListener.onProgressChanged(this, progress, true);
			}
		}
	}
	/**
	 * 监听函数
	 */
	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener onChangeListener)
	{
		this.mOnChangeListener = onChangeListener;
	}
}