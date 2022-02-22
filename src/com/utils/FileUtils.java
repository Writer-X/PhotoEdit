package com.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.view.View;

public class FileUtils
{
	/**
	 * ͼƬ·��д�أ����ַ�ʽ�ὫԴ�ļ�ɾ����������Դ�ļ��ĵط�����һ�����ļ�
	 * @param bitmap  λͼ����
	 * @param destPath  Ҫд�ص�·��
	 * @param quality
	 */
	public static void writeImage(Bitmap bitmap, String destPath, int quality)
	{
		try
		{
			deletedFile(destPath);
			if (createdFile(destPath))
			{
				FileOutputStream out = new FileOutputStream(destPath);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out))
				{
					out.flush();
					out.close();
					out = null;
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * ���Ϊ����,�˹���δ����
	 * @param bitmapλͼ����
	 * @param fileName�ļ���
	 * @param quality
	 */
	public static void rewriteImage(Bitmap bitmap,String fileName, int quality)
	{
		String filePath = "/sdcard/PEImage/" + fileName + ".jpg";//�����ַ
		try
		{
			deletedFile(filePath);
			if (createdFile(filePath))
			{
				FileOutputStream out = new FileOutputStream(filePath);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out))
				{
					out.flush();
					out.close();
					out = null;
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * ɾ��һ���ļ�
	 * @param filePath  �ļ�·��
	 * @return
	 */
	public static boolean deletedFile(String filePath)
	{
		try
		{
			File file = new File(filePath);
			if (file.exists())
			{
				return file.delete();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * ����һ���ļ�
	 * @param filePath �ļ�·��
	 * @return
	 */
	public static boolean createdFile(String filePath)
	{
		try
		{
			File file = new File(filePath);
			if (!file.exists())
			{
				if (!file.getParentFile().exists())
				{
					file.getParentFile().mkdirs();
				}

				return file.createNewFile();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * ����λͼ�ļ�
	 * @param bitmap
	 * @param scale
	 * @return
	 */
	public static Bitmap ResizeBitmap(Bitmap bitmap, int scale)
	{
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.postScale(1/scale, 1/scale);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}
	
	/**
	 * ��ģ��View��ͼƬת��ΪBitmap
	 * @param v
	 * @return
	 */
	public static Bitmap getBitmapByView(View v)
	{
		Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		v.draw(canvas);
		return bitmap;
	}
	
	/**
	 * �����ɵ�ͼƬ���浽�ڴ���
	 * @param bitmap
	 * @param name
	 * @return
	 */
	public String saveBitmap(Bitmap bitmap, String name)
	{
		String filePath = Environment.getExternalStorageDirectory() + "/PictureTest/";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			File dir = new File(filePath);
			if (!dir.exists())
				dir.mkdir();
			File file = new File(filePath + name + ".jpg");
			FileOutputStream out;

			try
			{
				out = new FileOutputStream(file);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
				{
					out.flush();
					out.close();
				}
				return file.getAbsolutePath();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}
	/**
	 * ����ͼƬ�ļ���д��SD����Ŀ¼
	 * @return
	 */
	  public static File createImageFile() {
		    // Create an image file name
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		    String imageFileName = "JPEG_" + timeStamp + "_";
		    try {
		      File image = File.createTempFile(imageFileName,  /* prefix */
		          ".jpg",         /* suffix */
		          Environment.getExternalStorageDirectory()      /* directory */);
		      return image;
		    } catch (IOException e) {
		      //do noting
		      return null;
		    }
	  }
	  /**
	   * ���Ϊ
	   * @param bitmap
	   * @return
	   */
	 public static String saveAsImage(Bitmap bitmap){
		   String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		   String name = "JPEG_" + timeStamp + "_";
	       String filePath = Environment.getExternalStorageDirectory() + "/PictureTest/";
		   if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		   {
				File dir = new File(filePath);
				if (!dir.exists())
					dir.mkdir();
				File file = new File(filePath + name + ".jpg");
				FileOutputStream out;

				try
				{
					out = new FileOutputStream(file);
					if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
					{
						out.flush();
						out.close();
					}
					return file.getAbsolutePath();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			return null;
	}
}