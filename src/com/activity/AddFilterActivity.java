/**
 * @author qiyihao
 * @date 2021/4/12
 * 
 * AddFilterActivity
 * 功能：
 * 为图片添加滤镜（目前已实现美白滤镜、灰色滤镜、怀旧风格滤镜、漫画风格滤镜、黑白滤镜、反色效果滤镜、素描铅笔滤镜）
 * 
 * 当用户于主界面点击“添加滤镜”按钮时调用该功能
 * 
 * 数据类型及使用：
 * 位图，用于存储图片数据
 * 滤镜种类，用于提供用户滤镜选择
 * 本地滤镜，用于对图片添加相应滤镜
 * 滑动条，用于设置滤镜透明度
 * 
 * 主要函数：
 * FilterOnClickListener，对滤镜进行选择
 * onStopTrackingTouch：设置滤镜透明度
 * updatePicture：为图片添加设置好的滤镜
 * 
 * 最后修改日期：2021/6/6
 * 修改内容：完善该功能的内部文档
 */

package com.activity;

import com.js.photosdk.filter.FilterType;
import com.js.photosdk.filter.NativeFilters;
import com.utils.FileUtils;
import com.view.VerticalSeekBar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AddFilterActivity extends Activity implements OnClickListener{
	private ImageView pictureShow;
	private String picturePath;
	
	private VerticalSeekBar mVerticalSeekBar;
	private TextView mSeekBarProgress;
	
	private ImageButton button_back;
	private Button button_save,button_save_as;
	
	// 初始化滤镜效果为美白效果，此情况用于未设置滤镜而拖动滑动条时的操作
	private int filterType = FilterType.FILTER4WHITELOG; 
	private TextView filterWhite,filterGray,filterNostalgic,filterComic,filterBlackWhite,filterNegative,filterSketch;
	
	private Bitmap pictureBitmap = null, newBitmap,resultImage;
	// 用于控制设置滤镜的宽高
	private int srcWidth, srcHeight;  
	private NativeFilters nativeFilters = new NativeFilters();
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.filter);
	    init();
	}
	/**
	 * 按钮响应函数
	 */
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.button_back :
				Intent cancelData = new Intent();
				cancelData.putExtra("picturePath", picturePath);
				setResult(RESULT_CANCELED, cancelData);
				this.finish();
				break;
			case R.id.button_save :
				FileUtils.writeImage(resultImage, picturePath, 100);
				Intent intent = new Intent();
				intent.putExtra("picturePath", picturePath);
				setResult(Activity.RESULT_OK, intent);
				this.finish();
				break;
			case R.id.button_save_as:
				picturePath = FileUtils.saveAsImage(resultImage);
				Intent saveAsData = new Intent();
				saveAsData.putExtra("picturePath", picturePath);
				setResult(Activity.RESULT_OK, saveAsData);
				this.finish();
				break;
			default :
				break;
		}
	}
	/**变量初始化*/
	private void init(){
		button_back = (ImageButton) findViewById(R.id.button_back);
		button_back.setOnClickListener(this);
		
		button_save = (Button) findViewById(R.id.button_save);
		button_save.setOnClickListener(this);
		
		button_save_as = (Button) findViewById(R.id.button_save_as);
		button_save_as.setOnClickListener(this);
		
		pictureShow = (ImageView) findViewById(R.id.picture_filter);
		
		Intent intent = getIntent();
		picturePath = intent.getStringExtra("picturePath");
		pictureShow.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		
		/*滤镜初始化*/
		filterWhite = (TextView) findViewById(R.id.filterWhite);
		filterWhite.setOnClickListener(new FilterOnClickListener());
		
		filterGray = (TextView) findViewById(R.id.filterGray);
		filterGray.setOnClickListener(new FilterOnClickListener());
		
		filterNostalgic = (TextView) findViewById(R.id.filterNostalgic);
		filterNostalgic.setOnClickListener(new FilterOnClickListener());	
		
		filterComic = (TextView) findViewById(R.id.filterComic);
		filterComic.setOnClickListener(new FilterOnClickListener());

		filterBlackWhite = (TextView) findViewById(R.id.filterBlackWhite);
		filterBlackWhite.setOnClickListener(new FilterOnClickListener());

		filterNegative = (TextView) findViewById(R.id.filterNegative);
		filterNegative.setOnClickListener(new FilterOnClickListener());
		
		filterSketch = (TextView) findViewById(R.id.filterSketch);
		filterSketch.setOnClickListener(new FilterOnClickListener());
		
		/*滑动条初始化*/
		mVerticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
		mSeekBarProgress = (TextView) findViewById(R.id.verticalSeekBarProgressText);
		mVerticalSeekBar.setMax(100);
		mVerticalSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			int mProgress = 0;
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				mSeekBarProgress.setText(mProgress + "%");
				float degree = mProgress / 100.0f;
				updatePicture(degree);
			}
			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}
			public void onProgressChanged(SeekBar seekBar,int progress, boolean fromUser)
			{
				mProgress = progress;
			}
		});
		
		/*窗口大小初始化*/
		Options option = new Options();
		option.inSampleSize = 1;
		pictureBitmap = BitmapFactory.decodeFile(picturePath, option);
		newBitmap = pictureBitmap;
		srcWidth = pictureBitmap.getWidth();
		srcHeight = pictureBitmap.getHeight();
	}
	/**
	 * 滤镜响应函数
	 */
	private class FilterOnClickListener implements OnClickListener
	{
		public void onClick(View view)
		{
			switch (view.getId())
			{
			    // 美白滤镜
				case R.id.filterWhite :
					filterType = FilterType.FILTER4WHITELOG;
					break;
				// 灰色滤镜
				case R.id.filterGray :
					filterType = FilterType.FILTER4GRAY;
					break;
				// 怀旧风格
				case R.id.filterNostalgic:
					filterType = FilterType.FILTER4NOSTALGIC;
					break;
				// 漫画风格
				case R.id.filterComic :
					filterType = FilterType.FILTER4COMICS;
					break;
				// 黑白滤镜
				case R.id.filterBlackWhite:
					filterType = FilterType.FILTER4BlackWhite;
					break;
				// 反色效果
				case R.id.filterNegative:
					filterType = FilterType.FILTER4NEGATIVE;
					break;
				// 素描铅笔
				case R.id.filterSketch:
					filterType = FilterType.FILTER4SKETCH;
					break;
				default :
					break;
		     }
			updatePicture(1);
			mVerticalSeekBar.setProgress(100);
			mSeekBarProgress.setText(100 + "%");
			mVerticalSeekBar.setProgressAndThumb(mVerticalSeekBar.getMax());
	    }	
	}
	/**
	 * 图片更新函数
	 * @param degree  滤镜强度，范围：0-100，由滑动条控制
	 */
	private void updatePicture(float degree)
	{
		int[] dataResult = null;
		int[] pix = new int[srcWidth * srcHeight];
		newBitmap.getPixels(pix, 0, srcWidth, 0, 0, srcWidth, srcHeight);
		
		switch (filterType)
		{
			case FilterType.FILTER4GRAY :
				dataResult = nativeFilters.ToGray(pix, srcWidth, srcHeight,degree);
				break;
			case FilterType.FILTER4WHITELOG :
				dataResult = nativeFilters.ToWhiteLOG(pix, srcWidth, srcHeight,FilterType.BeitaOfWhiteLOG, degree);
				break;
			case FilterType.FILTER4NEGATIVE:
				dataResult = nativeFilters.ToNegative(pix, srcWidth, srcHeight,degree);
				break;
			case FilterType.FILTER4COMICS :
				dataResult = nativeFilters.ToComics(pix, srcWidth, srcHeight,degree);
				break;
			case FilterType.FILTER4BlackWhite :
				dataResult = nativeFilters.ToBlackWhite(pix, srcWidth,srcHeight, degree);
				break;
			case FilterType.FILTER4NOSTALGIC :
				dataResult = nativeFilters.ToNostalgic(pix, srcWidth,srcHeight, degree);
				break;
			case FilterType.FILTER4SKETCH :
				dataResult = nativeFilters.ToSketch(pix, srcWidth, srcHeight,degree);
				break;
		}	
		resultImage = Bitmap.createBitmap(dataResult, srcWidth, srcHeight,Config.ARGB_8888);
		pictureShow.setImageBitmap(resultImage);
	}
}