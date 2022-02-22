package com.scrawl;

import android.graphics.Bitmap;

/*»­±ÊÀà*/
public class PaintBrush
{
	/*»­±ÊÍ¼Æ¬*/
	private Bitmap paintBitmap;
	/*»­±Ê´ÖÏ¸*/
	private int paintSize;
	/*»­±ÊÑÕÉ«*/
	private int paintColor;
	/*»­±Ê´ÖÏ¸ÖÖÀà*/
	private int paintSizeTypeNo;
	
	/**
	 * »ñÈ¡»­±ÊÍ¼Æ¬
	 * @return
	 */
	public Bitmap getPaintBitmap()
	{
		return paintBitmap;
	}
	
	/**
	 * ÉèÖÃ»­±ÊÍ¼Æ¬
	 * @param paintBitmap
	 */
	public void setPaintBitmap(Bitmap paintBitmap)
	{
		this.paintBitmap = paintBitmap;
	}
	
	/**
	 * »ñÈ¡»­±Ê´óÐ¡
	 * @return
	 */
	public int getPaintSize()
	{
		return paintSize;
	}
	
	/**
	 * ÉèÖÃ»­±Ê´óÐ¡
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
	 * »ñÈ¡»­±ÊÑÕÉ«
	 * @return
	 */
	public int getPaintColor()
	{
		return paintColor;
	}
	/**
	 * ÉèÖÃ»­±ÊÑÕÉ«
	 * @param paintColor
	 */
	public void setPaintColor(int paintColor)
	{
		this.paintColor = paintColor;
	}
	/**
	 * »ñÈ¡»­±Ê´ÖÏ¸ÖÖÀà
	 * @return
	 */
	public int getPaintSizeTypeNo()
	{
		return paintSizeTypeNo;
	}
	/**
	 * ÉèÖÃ»­±Ê´ÖÏ¸ÖÖÀà
	 * @param paintSizeTypeNo
	 */
	public void setPaintSizeTypeNo(int paintSizeTypeNo)
	{
		this.paintSizeTypeNo = paintSizeTypeNo;
	}
}
