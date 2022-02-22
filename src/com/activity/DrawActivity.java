/**
 * @author liyizhuo
 * @date 2021/5/3
 * 
 * RevolveActivity
 * ���ܣ�
 * ʵ����ͼƬ��Ϳѻ���ṩ�޸Ļ��ʴ�ϸ����ʽ����ɫ�Ĺ����Լ���ͼ����
 * 
 * ���û�������������Ϳѻ����ťʱ���øù���
 * 
 * �������ͼ�ʹ�ã�
 * λͼ�����ڴ洢ԭͼƬ���ݡ��������ݡ�����ͼƬ����
 * �������ʸ������Ե��Զ���������casualWaterUtil�����ڴ洢���ʵĴ�ϸ����ɫ����ʽ������
 * 
 * ��Ҫ������
 * creatDrawPainter�����ջ������͡�����λͼ�����Լ���ɫ���ݣ��޸Ļ����ɶ�Ӧ����
 * 
 * ����޸����ڣ�2021/6/6
 * �޸����ݣ����Ƹù��ܵ��ڲ��ĵ�
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
	// �滭��ͼ
	private DrawingBoardView drawView; 
	private LinearLayout drawLayout;
	
	private ImageButton button_back;
	private Button button_save,button_save_as;
	
	ScrawlTools casualWaterUtil = null;
	String picturePath;
	private Bitmap resultImage;
	
	private SelectColorPopup menuWindow;
	/**
	 * ��ʼ������
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
	 * ��ʼ��ͼƬͼ��ͻ滭ͼ��
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
	 * ��Ӧ����
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
					Toast.makeText(DrawActivity.this, "������ɫ���", Toast.LENGTH_SHORT).show();
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
	 * �����˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 1, 1, "����1");
		menu.add(0, 2, 2, "����2");
		menu.add(0, 3, 3, "����3");
		menu.add(0, 4, 4, "������ɫ");
		menu.add(0, 5, 5, "��ͼ");
		menu.add(0, 6, 6, "��Ƥ��");

		return super.onCreateOptionsMenu(menu);
	}
	/**
	 * �˵���Ӧ����
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