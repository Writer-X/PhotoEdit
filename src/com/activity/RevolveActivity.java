/**
 * @author liyizhuo
 * @date 2021/4/5
 * 
 * RevolveActivity
 * ���ܣ�
 * ʵ��ͼƬ��ת���ṩ��ת����ת����
 * 
 * ���û���������������ת����ťʱ���øù���
 * 
 * �������ͼ�ʹ�ã�
 * λͼ�����ڴ洢ͼƬ����
 * ��ά����ͨ��λͼת���õ������ڽ�����ת����ת
 * 
 * ��Ҫ������
 * rotateImage������ͼƬ��λͼ���ݺ���ת������ͨ������任ʵ��ͼƬ��ת��������ת���λͼ����
 * reverseImage������ͼƬ��λͼ���ݺ�x��y��ķ�ת�����ͨ������任ʵ��ͼƬ��ת�����ط�ת���λͼ����
 * 
 * ����޸����ڣ�2021/6/6
 * �޸����ݣ����Ƹù��ܵ��ڲ��ĵ�
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
	 * ͼƬ��ת����
	 * @param bit λͼ����
	 * @param degrees �Ƕ�
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
	 * ͼƬ��ת����
	 * @param bit λͼ����
	 * @param x x�ᷭת���
	 * @param y y�ᷭת���
	 * @return
	 */
	public static Bitmap reverseImage(Bitmap bit,int x,int y)
	{
		Matrix matrix = new Matrix();
		matrix.postScale(x, y);
		
		Bitmap tempBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),bit.getHeight(), matrix, true);
		return tempBitmap;
	}
	
	/**������ʼ��*/
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