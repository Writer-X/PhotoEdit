/**
 * @author qiyihao
 * @date 2021/5/10
 * 
 * CropActivity
 * 功能：
 * 实现图片裁剪
 * 随机裁剪方式，可以自由拖动设置裁剪框
 * 
 * 当用户于主界面点击“图片裁剪”按钮时调用该功能
 * 
 * 数据类型及使用：
 * 位图，用于存储图片数据
 * 剪切视图，用于对图片进行裁剪
 * 剪切覆盖面视图，用于设置裁剪窗口的大小及位置
 * 
 * 主要函数：
 * onTouchEvent，移动裁剪窗口边框，设置裁剪窗口的大小及位置
 * 
 * 最后修改日期：2021/6/6
 * 修改内容：完善该功能的内部文档
 */

package com.activity;

import com.crop.CropImageView;
import com.utils.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class CropActivity extends Activity implements OnClickListener
{
	private CropImageView cropImageView;
	
	private String picturePath;
	
	private ImageButton button_back;
	private Button button_save,button_save_as;
	private Bitmap resultBmp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crop);
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
				resultBmp = cropImageView.getCroppedImage();
				FileUtils.writeImage(resultBmp, picturePath, 100);
				
				Intent intent = new Intent();
				intent.putExtra("picturePath", picturePath);
				setResult(Activity.RESULT_OK, intent);
				this.finish();
				break;
			case R.id.button_save_as:
				resultBmp = cropImageView.getCroppedImage();
				picturePath = FileUtils.saveAsImage(resultBmp);
				Intent saveAsData = new Intent();
				saveAsData.putExtra("picturePath", picturePath);
				setResult(Activity.RESULT_OK, saveAsData);
				this.finish();
				break;
			default :
				break;
		}
	}
	
	void init()
	{
		button_back = (ImageButton) findViewById(R.id.button_back);
		button_back.setOnClickListener(this);
		
		button_save = (Button) findViewById(R.id.button_save);
		button_save.setOnClickListener(this);
		
		button_save_as = (Button) findViewById(R.id.button_save_as);
		button_save_as.setOnClickListener(this);
		
		Intent intent = getIntent();
		picturePath = intent.getStringExtra("picturePath");
		Bitmap bit = BitmapFactory.decodeFile(picturePath);
		
		cropImageView = (CropImageView) findViewById(R.id.cropimageView);
		cropImageView.setImageBitmap(bit);
	}
	
}