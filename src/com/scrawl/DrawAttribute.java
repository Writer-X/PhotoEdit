package com.scrawl;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;

/*画属性*/
public class DrawAttribute
{
	public static enum Corner
	{
		LEFTTOP, RIGHTTOP, LEFTBOTTOM, RIGHTBOTTOM, ERROR
	};
	/*画笔种类*/
	public static enum DrawStatus
	{
		PEN_NORMAL,PEN_WATER, PEN_CRAYON, PEN_COLOR_BIG, PEN_ERASER,PEN_STAMP
	};
	public final static int backgroundOnClickColor = 0xfff08d1e;
	public static int screenHeight;
	public static int screenWidth;
	public static Paint paint = new Paint();

	//从文件中获取图像
	public static Bitmap getImageFromAssetsFile(Context context,String fileName, boolean isBackground)
	{
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try
		{
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		if (isBackground)
			image = Bitmap.createScaledBitmap(image, DrawAttribute.screenWidth,
					DrawAttribute.screenHeight, false);
		return image;

	}
}
