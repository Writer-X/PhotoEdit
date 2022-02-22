package com.scrawl;

import android.graphics.Bitmap;

/*������*/
public class PaintBrush
{
	/*����ͼƬ*/
	private Bitmap paintBitmap;
	/*���ʴ�ϸ*/
	private int paintSize;
	/*������ɫ*/
	private int paintColor;
	/*���ʴ�ϸ����*/
	private int paintSizeTypeNo;
	
	/**
	 * ��ȡ����ͼƬ
	 * @return
	 */
	public Bitmap getPaintBitmap()
	{
		return paintBitmap;
	}
	
	/**
	 * ���û���ͼƬ
	 * @param paintBitmap
	 */
	public void setPaintBitmap(Bitmap paintBitmap)
	{
		this.paintBitmap = paintBitmap;
	}
	
	/**
	 * ��ȡ���ʴ�С
	 * @return
	 */
	public int getPaintSize()
	{
		return paintSize;
	}
	
	/**
	 * ���û��ʴ�С
	 * @param paintSize
	 */
	public void setPaintSize(int paintSize)
	{
		if(paintSize >= paintSizeTypeNo)
		{
			this.paintSize = paintSizeTypeNo;
		}
		else if(paintSize <= 0)
		{
			this.paintSize = 1;
		}
		else
		{
			this.paintSize = paintSize;
		}
		
	}
	
	/**
	 * ��ȡ������ɫ
	 * @return
	 */
	public int getPaintColor()
	{
		return paintColor;
	}
	/**
	 * ���û�����ɫ
	 * @param paintColor
	 */
	public void setPaintColor(int paintColor)
	{
		this.paintColor = paintColor;
	}
	/**
	 * ��ȡ���ʴ�ϸ����
	 * @return
	 */
	public int getPaintSizeTypeNo()
	{
		return paintSizeTypeNo;
	}
	/**
	 * ���û��ʴ�ϸ����
	 * @param paintSizeTypeNo
	 */
	public void setPaintSizeTypeNo(int paintSizeTypeNo)
	{
		this.paintSizeTypeNo = paintSizeTypeNo;
	}
}
