/**
 * @author liyizhuo
 * @date 2021/4/19
 * 
 * RevolveActivity
 * 功能：
 * 实现在图片上添加文字，提供修改文字大小、样式、颜色的功能
 * 
 * 当用户于主界面点击“添加文字”按钮时调用该功能
 * 
 * 数据类型及使用：
 * 位图，用于存储图片数据
 * 包含文字各项属性的自定义数据类TextObject，用于存储用户输入的文字及文字的大小、颜色、样式等属性
 * 
 * 主要函数：
 * addfont，创建TextObject实例对象，获取用户输入的文字，并设置各项属性，将文字显示在界面上
 * showAtLocation，由选择颜色窗口类SelectColorPopup的对象menuWindow进行调用，在界面上弹出颜色选择窗口
 * 
 * 最后修改日期：2021/6/6
 * 修改内容：完善该功能的内部文档
 */

package com.activity;

import com.operate.OperateConstants;
import com.operate.OperateView;
import com.palette.SelectColorPopup;
import com.text.TextObject;
import com.utils.FileUtils;
import com.utils.OperateUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.content.DialogInterface;

public class AddTextActivity extends Activity implements OnClickListener
{
	OperateUtils operateUtils;
	OperateView operateView;
	private String picturePath;
	private LinearLayout content_layout,typeface_layout;
	private ImageButton button_back;
	private Button button_save,button_save_as;
	private Button text_color,text_typeface,addtext;
	private Button moren,caoshu,guifanzi;
	private SelectColorPopup menuWindow;
	private String typeface;
	private Bitmap resultBmp;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text);
		Intent intent = getIntent();
		picturePath = intent.getStringExtra("picturePath");
		operateUtils = new OperateUtils(this);
		init();
		fillContent();
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
				operateView.save();
				resultBmp = FileUtils.getBitmapByView(operateView);
				FileUtils.writeImage(resultBmp, picturePath, 100); 
				Intent intent = new Intent();
				intent.putExtra("picturePath", picturePath);
				setResult(Activity.RESULT_OK, intent);
				this.finish();
				break;
			case R.id.button_save_as:
				operateView.save();
				resultBmp = FileUtils.getBitmapByView(operateView);
				picturePath = FileUtils.saveAsImage(resultBmp);
				Intent saveAsData = new Intent();
				saveAsData.putExtra("picturePath", picturePath);
				setResult(Activity.RESULT_OK, saveAsData);
				this.finish();
				break;
			case R.id.addtext:
				//在上层添加文字
				addfont();
				break;
			case R.id.text_color:
				/*实例化选择颜色弹出窗口，并显示窗口*/
				menuWindow = new SelectColorPopup(AddTextActivity.this,AddTextActivity.this);
				menuWindow.showAtLocation(AddTextActivity.this.findViewById(R.id.main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			case R.id.submit:
				menuWindow.dismiss();
				break;
			case R.id.text_typeface:
				if (typeface_layout.getVisibility() == View.GONE)
				{typeface_layout.setVisibility(View.VISIBLE);} 
				else
				{typeface_layout.setVisibility(View.GONE);}
				break;
			case R.id.moren:
				typeface = null;
				typeface_layout.setVisibility(View.GONE);
				break;
			case R.id.caoshu:
				typeface = OperateConstants.CAOSHU;
				typeface_layout.setVisibility(View.GONE);
				break;
			case R.id.guifanzi:
				typeface = OperateConstants.GUIFANZI;
				typeface_layout.setVisibility(View.GONE);
				break;				
			default :
				break;
		}
	}

	private void init()
	{
		content_layout = (LinearLayout) findViewById(R.id.Layout_text);
		typeface_layout = (LinearLayout) findViewById(R.id.typeface_linear);
		
		button_back = (ImageButton) findViewById(R.id.button_back);
		button_back.setOnClickListener(this);
		
		button_save = (Button) findViewById(R.id.button_save);
		button_save.setOnClickListener(this);
		
		button_save_as = (Button) findViewById(R.id.button_save_as);
		button_save_as.setOnClickListener(this);
		
		addtext = (Button) findViewById(R.id.addtext);
		addtext.setOnClickListener(this);
		
		text_color = (Button) findViewById(R.id.text_color);
		text_color.setOnClickListener(this);
		
		text_typeface = (Button) findViewById(R.id.text_typeface);
		text_typeface.setOnClickListener(this);

		moren = (Button) findViewById(R.id.moren);
		moren.setTypeface(Typeface.DEFAULT);
		moren.setOnClickListener(this);
		
		caoshu = (Button) findViewById(R.id.caoshu);
		caoshu.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/"+ OperateConstants.CAOSHU + ".ttf"));
		caoshu.setOnClickListener(this);
		
		guifanzi = (Button) findViewById(R.id.guifanzi);
		guifanzi.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/"+ OperateConstants.GUIFANZI + ".ttf"));
		guifanzi.setOnClickListener(this);
	}
	/**
	 * 显示图片
	 */
	private void fillContent()
	{
		Bitmap resizeBmp = BitmapFactory.decodeFile(picturePath);
		operateView = new OperateView(AddTextActivity.this, resizeBmp);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resizeBmp.getWidth(), resizeBmp.getHeight());
		operateView.setLayoutParams(layoutParams);
		content_layout.addView(operateView);
		operateView.setMultiAdd(true); 
	}
	/**
	 * 添加文字响应函数
	 * @param tObject 文字
	 */
	private void alert(final TextObject tObject)
	{

		final EditText editText = new EditText(AddTextActivity.this);
		new AlertDialog.Builder(AddTextActivity.this).setView(editText)
				.setPositiveButton("确定", new DialogInterface.OnClickListener()
				{
					@SuppressLint("NewApi")
					public void onClick(DialogInterface dialog, int which)
					{
						String string = editText.getText().toString();
						tObject.setText(string);
						tObject.commit();
					}
				}).show();
	}
	/**
	 * 添加文字功能
	 */
	private void addfont()
	{
		final EditText editText = new EditText(AddTextActivity.this);
		new AlertDialog.Builder(AddTextActivity.this).setView(editText)
				.setPositiveButton("确定", new DialogInterface.OnClickListener()
				{
					@SuppressLint("NewApi")
					public void onClick(DialogInterface dialog, int which)
					{
						String string = editText.getText().toString();
						TextObject textObj = operateUtils.getTextObject(string,operateView, 5, 150, 100);
						if(textObj != null){
							if (menuWindow != null)
							{
								textObj.setColor(menuWindow.getColor());
							}
							textObj.setTypeface(typeface);
							textObj.commit();
							operateView.addItem(textObj);
							operateView.setOnListener(new OperateView.MyListener()
							{
								public void onClick(TextObject tObject)
								{
									alert(tObject);
								}
							});
						}
					}
				}).show();
	}
}