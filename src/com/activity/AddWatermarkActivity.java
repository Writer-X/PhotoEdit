/**
 * @author qiyihao
 * @date 2021/4/26
 * 
 * AddWatermarkActivity
 * 功能：
 * 实现添加水印（目前已实现处女座、神回复、怪蜀黍、相思、早安、新年、醉了、被玩坏了以上水印）
 * (1)水印可以拖动、缩放
 * (2)可以同时添加多个水印
 * 
 * 当用户于主界面点击“添加水印”按钮时调用该功能
 * 
 * 数据类型及使用：
 * 位图，用于存储图片数据
 * 图片对象，用于对水印进行拖动、缩放操作
 * 操作视图，对图片整体进行修改布局
 * 
 * 主要函数：
 * addWatermark，添加水印
 * 
 * 最后修改日期：2021/6/6
 * 修改内容：完善该功能的内部文档
 */

package com.activity;

import com.operate.ImageObject;
import com.operate.OperateView;
import com.utils.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddWatermarkActivity extends Activity implements OnClickListener{	

	private ImageButton button_back;
	private Button button_save,button_save_as;
	private LinearLayout content_layout;
	private OperateView operateView;
	private String picturePath;
	int watermarkPath;
	private Bitmap sourceBitmap,watermarkBitmap;
	private TextView chunvzuo,shenhuifu,guaishushu,xiangsi,zaoan,xinnian,zuile,beiwanhuaile;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.watermark);
	    init();
	}	
	public void onClick(View view) {
      	switch (view.getId()){
		    case R.id.button_back :
			    Intent cancelData = new Intent();
			    cancelData.putExtra("picturePath", picturePath);
			    setResult(RESULT_CANCELED, cancelData);		
			    this.finish();
			    break;
		    case R.id.button_save :
				operateView.save();
				watermarkBitmap = FileUtils.getBitmapByView(operateView);
			    FileUtils.writeImage(watermarkBitmap, picturePath, 100);
		    	Intent saveData = new Intent();
			    saveData.putExtra("picturePath", picturePath);
			    setResult(Activity.RESULT_OK, saveData);
		    	this.finish();
		    	break;
		    case R.id.button_save_as:
				operateView.save();
				watermarkBitmap = FileUtils.getBitmapByView(operateView);
				picturePath = FileUtils.saveAsImage(watermarkBitmap);
				Intent saveAsData = new Intent();
				saveAsData.putExtra("picturePath", picturePath);
				setResult(Activity.RESULT_OK, saveAsData);
				this.finish();
		    	break;
		    case R.id.chunvzuo:
				watermarkPath = R.drawable.watermark_chunvzuo;
		        addWatermark(watermarkPath);
		        break;
		    case R.id.shenhuifu:
				watermarkPath = R.drawable.watermark_shenhuifu;
		        addWatermark(watermarkPath);
		        break;
		    case R.id.guaishushu:
				watermarkPath = R.drawable.watermark_guaishushu;
		        addWatermark(watermarkPath);
		        break;
		    case R.id.xiangsi:
				watermarkPath = R.drawable.watermark_xiangsi;
		        addWatermark(watermarkPath);
		        break;
		    case R.id.zaoan:
				watermarkPath = R.drawable.watermark_zaoan;
		        addWatermark(watermarkPath);
		        break;
		    case R.id.xinnian:
				watermarkPath = R.drawable.watermark_xinnian;
		        addWatermark(watermarkPath);
		        break;
		    case R.id.zuile:
				watermarkPath = R.drawable.watermark_zuile;
		        addWatermark(watermarkPath);
		        break;
		    case R.id.beiwanhuaile:
				watermarkPath = R.drawable.watermark_beiwanhuaile;
		        addWatermark(watermarkPath);
		        break;
      	}
	}
	/**
	 * 获取水印图片，为其添加旋转删除按钮
	 * @param srcBmp 水印图片
	 * @param operateView 原图operateview
	 * @return
	 */
	public ImageObject getImageObject(Bitmap srcBmp, OperateView operateView)
	{
		Bitmap rotateBm = BitmapFactory.decodeResource(getResources(),R.drawable.rotate);
		Bitmap deleteBm = BitmapFactory.decodeResource(getResources(),R.drawable.delete);
		int width = operateView.getWidth();
		int height = operateView.getHeight();
		ImageObject imgObject = new ImageObject(srcBmp, width/2, height/2, rotateBm,deleteBm);
		Point point = new Point(20, 20);
		imgObject.setPoint(point);
		return imgObject;
	}
	/**
	 * 添加水印
	 * @param position 水印位置
	 */
	private void addWatermark(int position)
	{
		Bitmap watermarkBmp = BitmapFactory.decodeResource(getResources(), position);
		ImageObject imgObject = getImageObject(watermarkBmp, operateView);
		operateView.addItem(imgObject);
	}
	
	void init()
	{
		button_back = (ImageButton) findViewById(R.id.button_back);
		button_back.setOnClickListener(this);
		
		button_save = (Button) findViewById(R.id.button_save);
		button_save.setOnClickListener(this);
		
		content_layout = (LinearLayout) findViewById(R.id.Layout_watermark);
		button_save_as = (Button) findViewById(R.id.button_save_as);
		button_save_as.setOnClickListener(this);
		
		Intent intent = getIntent();
		picturePath = intent.getStringExtra("picturePath");

		sourceBitmap = BitmapFactory.decodeFile(picturePath);
		operateView = new OperateView(AddWatermarkActivity.this, sourceBitmap);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(sourceBitmap.getWidth(), sourceBitmap.getHeight());
		operateView.setLayoutParams(layoutParams);
		content_layout.addView(operateView);
		operateView.setMultiAdd(true);
		
		chunvzuo = (TextView) findViewById(R.id.chunvzuo);
		chunvzuo.setOnClickListener(this);
		
		shenhuifu = (TextView) findViewById(R.id.shenhuifu);
		shenhuifu.setOnClickListener(this);
		
		guaishushu = (TextView) findViewById(R.id.guaishushu);
		guaishushu.setOnClickListener(this);
		
		xiangsi = (TextView) findViewById(R.id.xiangsi);
		xiangsi.setOnClickListener(this);
		
		zaoan = (TextView) findViewById(R.id.zaoan);
		zaoan.setOnClickListener(this);
		
		xinnian = (TextView) findViewById(R.id.xinnian);
		xinnian.setOnClickListener(this);
		
		zuile = (TextView) findViewById(R.id.zuile);
		zuile.setOnClickListener(this);
		
		beiwanhuaile = (TextView) findViewById(R.id.beiwanhuaile);
		beiwanhuaile.setOnClickListener(this);
	}
}