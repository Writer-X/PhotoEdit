/**
 * @author xutong
 * @date 2021/4/13
 * 
 * VerticalSeekBar
 * ���ܣ�
 * ��ֱ��������
 * ��һ���Զ���ؼ�������ʵ���˾����ܣ������˾�ǿ��
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
	 * OnDraw()�������ڽ������ʱ������
	 */
	@Override
	protected void onDraw(Canvas c)
	{
		c.rotate(-90);
		c.translate(-getHeight(), 0);
		super.onDraw(c);
	}
	/**
	 * �ؼ���ʾ
	 */
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec)
	{
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}
	/**
	 * ���ô�С
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(h, w, oldh, oldw);
	}
	/**
	 * �϶�����������Ӧ����
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
                /*��֤�������ᳬ���������ķ�Χ*/
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
	 * �������ֵ
	 * @param maximum 
	 */
	public synchronized void setMaximum(int maximum)
	{
		setMax(maximum);
	}
	/**
	 * ���ý������Ͱ�ťλ��
	 * @param progress ��/�ٷֱ�
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
	 * ��������
	 */
	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener onChangeListener)
	{
		this.mOnChangeListener = onChangeListener;
	}
}