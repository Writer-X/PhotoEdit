/**
 * @author liyizhuo
 * @date 2021/4/5
 * 
 * RevolveActivity
 * 功能：
 * 实现图片旋转，提供翻转和旋转功能
 * 
 * 当用户于主界面点击“旋转”按钮时调用该功能
 * 
 * 数据类型及使用：
 * 位图，用于存储图片数据
 * 二维矩阵，通过位图转换得到，用于进行旋转及翻转
 * 
 * 主要函数：
 * rotateImage，接收图片的位图数据和旋转度数，通过矩阵变换实现图片旋转，返回旋转后的位图数据
 * reverseImage：接收图片的位图数据和x、y轴的翻转情况，通过矩阵变换实现图片翻转，返回翻转后的位图数据
 * 
 * 最后修改日期：2021/6/6
 * 修改内容：完善该功能的内部文档
 */

package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.utils.FileUtils;

public class RevolveActivity extends Activity implements OnClickListener{
	private ImageView pictureShow;
	private String picturePath;
	
	private ImageButton button_back;
	private Button button_save,button_save_as;
	private Button refresh,revolve,flipUpDown,flipLeftRight;
	private Bitmap sourceBitmap,alterBitmap;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.revolve);
	    init();
	}
	public void onClick(View view) {
		switch (view.getId())
		{
			case R.id.button_back :
				Intent cancelData = new Intent();
				cancelData.putExtra("picturePath", picturePath);
				setResult(RESULT_CANCELED, cancelData);		
				this.finish();
				break;
			case R.id.button_save :
				FileUtils.writeImage(alterBitmap, picturePath, 100);
				Intent saveData = new Intent();
				saveData.putExtra("picturePath", picturePath);
				setResult(Activity.RESULT_OK, saveData);
				this.finish();
				break;
			case R.id.button_save_as:
				picturePath = FileUtils.saveAsImage(alterBitmap);
				Intent saveAsData = new Intent();
				saveAsData.putExtra("picturePath", picturePath);
				setResult(Activity.RESULT_OK, saveAsData);
				this.finish();
				break;
			case R.id.revolve :
				alterBitmap = rotateImage(alterBitmap, 90);
				pictureShow.setImageBitmap(alterBitmap);
				break;
			case R.id.flipLeftRight :
				alterBitmap = reverseImage(alterBitmap, -1, 1);
				pictureShow.setImageBitmap(alterBitmap);
				break;
			case R.id.flipUpDown :
				alterBitmap = reverseImage(alterBitmap, 1, -1);
				pictureShow.setImageBitmap(alterBitmap);
				break;
			case R.id.refresh :
				alterBitmap = sourceBitmap;
				pictureShow.setImageBitmap(alterBitmap);
				break;
			default :
				break;
		}		
	}
	
	/**
	 * 图片旋转函数
	 * @param bit 位图数据
	 * @param degrees 角度
	 * @return
	 */
	public static Bitmap rotateImage(Bitmap bit, int degrees)
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		Bitmap tempBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),bit.getHeight(), matrix, true);
		return tempBitmap;
	}
	
	/**
	 * 图片翻转函数
	 * @param bit 位图数据
	 * @param x x轴翻转情况
	 * @param y y轴翻转情况
	 * @return
	 */
	public static Bitmap reverseImage(Bitmap bit,int x,int y)
	{
		Matrix matrix = new Matrix();
		matrix.postScale(x, y);
		
		Bitmap tempBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),bit.getHeight(), matrix, true);
		return tempBitmap;
	}
	
	/**变量初始化*/
	private void init(){
		button_back = (ImageButton) findViewById(R.id.button_back);
		button_back.setOnClickListener(this);
		
		button_save = (Button) findViewById(R.id.button_save);
		button_save.setOnClickListener(this);
		
		button_save_as = (Button) findViewById(R.id.button_save_as);
		button_save_as.setOnClickListener(this);
		
		refresh = (Button) findViewById(R.id.refresh);
		refresh.setOnClickListener(this);
		
		revolve = (Button) findViewById(R.id.revolve);
		revolve.setOnClickListener(this);		
		
		flipUpDown = (Button) findViewById(R.id.flipUpDown);
		flipUpDown.setOnClickListener(this);	
		
		flipLeftRight = (Button) findViewById(R.id.flipLeftRight);
		flipLeftRight.setOnClickListener(this);
		
		pictureShow = (ImageView) findViewById(R.id.picture_revolve);
		Intent intent = getIntent();
		picturePath = intent.getStringExtra("picturePath");
		alterBitmap = BitmapFactory.decodeFile(picturePath);
		sourceBitmap = alterBitmap;
		pictureShow.setImageBitmap(alterBitmap);
	}
}