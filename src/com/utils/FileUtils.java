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
	 * 图片路径写回，这种方式会将源文件删除，并且在源文件的地方创建一个新文件
	 * @param bitmap  位图数据
	 * @param destPath  要写回的路径
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
	 * 另存为功能,此功能未测试
	 * @param bitmap位图数据
	 * @param fileName文件名
	 * @param quality
	 */
	public static void rewriteImage(Bitmap bitmap,String fileName, int quality)
	{
		String filePath = "/sdcard/PEImage/" + fileName + ".jpg";//保存地址
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
	 * 删除一个文件
	 * @param filePath  文件路径
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
	 * 创建一个文件
	 * @param filePath 文件路径
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
	 * 调整位图文件
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
	 * 将模板View的图片转化为Bitmap
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
	 * 将生成的图片保存到内存中
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
	 * 创建图片文件，写到SD卡根目录
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
	   * 另存为
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