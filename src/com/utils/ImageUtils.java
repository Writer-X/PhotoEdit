package com.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtils
{
	/**
	 * ��ͼƬ����Ϊ�µĿ��
	 * @param bitmap ԴͼƬ
	 * @param newWidth	�µĿ��
	 * @param newHeight	�µĸ߶�
	 * @return �����������ÿ�ߵ�ͼƬ
	 */
	public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth, int newHeight)
	{
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}
}