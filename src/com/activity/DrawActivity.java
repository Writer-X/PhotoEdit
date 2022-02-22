/**
 * @author liyizhuo
 * @date 2021/5/3
 * 
 * RevolveActivity
 * 功能：
 * 实现在图片上涂鸦，提供修改画笔粗细、样式、颜色的功能以及贴图功能
 * 
 * 当用户于主界面点击“涂鸦”按钮时调用该功能
 * 
 * 数据类型及使用：
 * 位图，用于存储原图片数据、画布数据、背景图片数据
 * 包含画笔各项属性的自定义数据类casualWaterUtil，用于存储画笔的粗细、颜色、样式等属性
 * 
 * 主要函数：
 * creatDrawPainter，接收画笔类型、画布位图数据以及颜色数据，修改或生成对应画笔
 * 
 * 最后修改日期：2021/6/6
 * 修改内容：完善该功能的内部文档
 */

package com.activity;

import com.palette.SelectColorPopup;
import com.scrawl.DrawAttribute;
import com.scrawl.DrawingBoardView;
import com.scrawl.ScrawlTools;
import com.utils.FileUtils;
import com.utils.OperateUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DrawActivity extends Activity implements OnClickListener
{ 
	// 绘画视图
	private DrawingBoardView drawView; 
	private LinearLayout drawLayout;
	
	private ImageButton button_back;
	private Button button_save,button_save_as;
	
	ScrawlTools casualWaterUtil = null;
	String picturePath;
	private Bitmap resultImage;
	
	private SelectColorPopup menuWindow;
	/**
	 * 初始化函数
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw);

		drawView = (DrawingBoardView) findViewById(R.id.drawView);
		drawLayout = (LinearLayout) findViewById(R.id.drawLayout);

		button_back = (ImageButton) findViewById(R.id.button_back);
		button_back.setOnClickListener(this);
		
		button_save = (Button) findViewById(R.id.button_save);
		button_save.setOnClickListener(this);
		
		button_save_as = (Button) findViewById(R.id.button_save_as);
		button_save_as.setOnClickListener(this);
		
		Intent intent = getIntent();
		picturePath = intent.getStringExtra("picturePath");
		
		compressed();
	}
	/**
	 * 初始化图片图层和绘画图层
	 */
	private void compressed()
	{
		OperateUtils operateUtils = new OperateUtils(this);
		Bitmap bit = BitmapFactory.decodeFile(picturePath);
		Bitmap resizeBmp = operateUtils.compressionFiller(bit, drawLayout);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resizeBmp.getWidth(), resizeBmp.getHeight());
		drawView.setLayoutParams(layoutParams);
		casualWaterUtil = new ScrawlTools(this, drawView, resizeBmp);
		Bitmap paintBitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.crayon);
		casualWaterUtil.creatDrawPainter(DrawAttribute.DrawStatus.PEN_WATER,paintBitmap, 0xffadb8bd);

	}
	/**
	 * 响应函数
	 */
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
			case R.id.submit:
				if (menuWindow != null)
				{
					Toast.makeText(DrawActivity.this, "更改颜色完成", Toast.LENGTH_SHORT).show();
					Bitmap paintBitmap4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker);
					casualWaterUtil.creatDrawPainter(DrawAttribute.DrawStatus.PEN_WATER, paintBitmap4,menuWindow.getColor());					
				}
				else
				{
					Bitmap paintBitmap4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker);
					casualWaterUtil.creatDrawPainter(DrawAttribute.DrawStatus.PEN_WATER, paintBitmap4,0xff002200);
				}
				menuWindow.dismiss();
			default :
				break;
		}
	}
	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 1, 1, "画笔1");
		menu.add(0, 2, 2, "画笔2");
		menu.add(0, 3, 3, "画笔3");
		menu.add(0, 4, 4, "画笔颜色");
		menu.add(0, 5, 5, "贴图");
		menu.add(0, 6, 6, "橡皮擦");

		return super.onCreateOptionsMenu(menu);
	}
	/**
	 * 菜单响应函数
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case 1 :
				Bitmap paintBitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker);
				casualWaterUtil.creatDrawPainter(DrawAttribute.DrawStatus.PEN_WATER, paintBitmap1,0xffadb8bd);
				break;
			case 2 :
				Bitmap paintBitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.crayon);
				casualWaterUtil.creatDrawPainter(DrawAttribute.DrawStatus.PEN_CRAYON, paintBitmap2,0xffadb8bd);
				break;
			case 3 :
				Options option = new Options();
				option.inSampleSize = 2;
				Bitmap paintBitmap3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker, option);
				casualWaterUtil.creatDrawPainter(DrawAttribute.DrawStatus.PEN_WATER, paintBitmap3,0xffadb8bd);
				break;
			case 4 :
				menuWindow = new SelectColorPopup(DrawActivity.this,DrawActivity.this);
				int[] location = new int[2];
				drawView.getLocationOnScreen(location);
				menuWindow.showAtLocation(DrawActivity.this.findViewById(R.id.drawView),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,location[0], location[1] - drawView.getHeight());
				break;
			case 5 :
				int[] res = new int[]{R.drawable.stamp0star,R.drawable.stamp1star, R.drawable.stamp2star,R.drawable.stamp3star};
				casualWaterUtil.creatStampPainter(DrawAttribute.DrawStatus.PEN_STAMP, res, 0xff00ff00);
				break;

			case 6 :
				Bitmap paintBitmap6 = BitmapFactory.decodeResource(this.getResources(), R.drawable.eraser);
				casualWaterUtil.creatDrawPainter(DrawAttribute.DrawStatus.PEN_ERASER, paintBitmap6,0xffadb8bd);		
				break;

			default :
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}