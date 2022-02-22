/**
/*@author xutong
 *@date 2021/3/29 
 *
 *开发详述：
 *1.项目名称PhotoEdit
 *2.开发目的：旨在开发一款简单的图片处理软件
 *3.开发人员：赵子汉（项目经理）、徐通、李奕灼、戚怡昊
 *4.代码用途：仅山东大学2018级软件系统课程设计
 *5.项目参考：jarlen——PhotoEditDemo项目
 *6.项目引用： (1)滤镜filter：Daichi Furiya——android-gpuimage
 *            (2)调色盘palette：LarsWerkman
 *
 * MainActivity
 * 功能：
 * 1.从相册、相机中获取图片并显示
 * 2.提供主界面到其他界面的接口
 * 
 * 主要函数：
 * getPictureFromGallery：从相册中获取图片
 * getPictureFromCamera：从相机中获取图片
 * 
 * 最后修改日期：2021/6/6
 * 修改内容：完善该功能的内部文档
 */
package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import com.utils.FileUtils;

public class MainActivity extends Activity implements OnClickListener{

	private ImageView pictureShow;   
	private String picturePath; 
	private ImageButton gallery,camera; 
	private Class<?> intentClass;  
	private int intentType = 0;   
	private Uri outputFileUri,selectedImage;
	private Bitmap bitmap;
	
	private static final int PHOTO_PICKED = 1000; 
	private static final int CAMERA_PICKED = 1007;
	private static final int ADD_FILTER = 1001;
	private static final int ADD_TEXT = 1002;
	private static final int PHOTO_CROP = 1004;
	private static final int PHOTO_REVOLVE = 1006; 
	private static final int PHOTO_DRAW = 1003;
	private static final int ADD_WATERMARK = 1005;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setOverflowShowingAlways();

        pictureShow = (ImageView) findViewById(R.id.picture_main);
		gallery = (ImageButton) findViewById(R.id.gallery);
		gallery.setOnClickListener(this);
		camera = (ImageButton) findViewById(R.id.camera);
		camera.setOnClickListener(this);
    }

	public void onClick(View view) {
		switch (view.getId())
		{
			case R.id.gallery :
				getPictureFromGallery();
				break;
			case R.id.camera:	
				getPictureFromCamera();
				break;
			default :
				break;
		}
	}
	/**从相册中获取图片*/
	private void getPictureFromGallery(){
		Intent openPhotoIntent = new Intent(Intent.ACTION_PICK);
		openPhotoIntent.setType("image/*");
		startActivityForResult(openPhotoIntent, PHOTO_PICKED);
	}
	/**从相机中获取图片*/
	private void getPictureFromCamera(){
	    File file = FileUtils.createImageFile();
	    outputFileUri = Uri.fromFile(file);
	    picturePath = file.getPath();
	    
	    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
	    startActivityForResult(captureIntent, CAMERA_PICKED);
	}
	/**回调函数 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case PHOTO_PICKED :
				super.onActivityResult(requestCode,resultCode,data);	
				if(resultCode==RESULT_OK && null!= data)
				{
	     			selectedImage=data.getData();
			     	String[]filePathColumn={MediaStore.Images.Media.DATA};
			    	Cursor cursor=getContentResolver().query(selectedImage,filePathColumn,null,null,null);
			    	cursor.moveToFirst();
			    	int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
			    	picturePath=cursor.getString(columnIndex);
			    	cursor.close();
			    	pictureShow.setImageBitmap(BitmapFactory.decodeFile(picturePath));
				}
				break;		
			case CAMERA_PICKED:
			    try {
				    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri);
			    } catch (FileNotFoundException e) {
				    e.printStackTrace();
			    } catch (IOException e) {
				    e.printStackTrace();
			    }
			    pictureShow.setImageBitmap(bitmap);
				break;
			case PHOTO_DRAW:
			case PHOTO_REVOLVE :
			case ADD_FILTER:
			case ADD_WATERMARK:
			case ADD_TEXT:
			case PHOTO_CROP:
				if(resultCode==RESULT_OK && null!= data)
				{
					String resultPath = data.getStringExtra("picturePath");
					Bitmap resultBitmap = BitmapFactory.decodeFile(resultPath);
					pictureShow.setImageBitmap(resultBitmap);
				}
			    break;
			default:
				break;
		}
	}
	/**菜单显示函数，右上角三点显示*/
	private void setOverflowShowingAlways() 
	{
		try{
		    ViewConfiguration config = ViewConfiguration.get(this);
		    Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
		    menuKeyField.setAccessible(true);
		    menuKeyField.setBoolean(config,false);
	    }
		catch(Exception e) {
	    	e.printStackTrace();
		}
	}
	
	/**菜单创建函数*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{   
	    MenuInflater inflater=getMenuInflater();
	    inflater.inflate(R.menu.options_menu,menu);
	    menu.add(0, 1, 1, "添加滤镜");
	    menu.add(0, 2, 2, "添加文字");
     	menu.add(0, 3, 3, "添加涂鸦");
    	menu.add(0, 4, 4, "图片裁剪");
    	menu.add(0, 5, 5, "添加水印");
    	menu.add(0, 6, 6, "图片旋转");
    	
    	return true;
    }
	/**菜单响应函数*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (picturePath == null)
		{
			Toast.makeText(MainActivity.this, "请首先添加图片", Toast.LENGTH_SHORT).show();
			return true;
		}
		switch (item.getItemId())
		{
		    case 1:
		    	intentClass = AddFilterActivity.class;
		    	intentType = ADD_FILTER;
		    	break;
		    case 2:
		    	intentClass = AddTextActivity.class;
		    	intentType = ADD_TEXT;
		    	break;
		    case 3:
		    	intentClass = DrawActivity.class;
		    	intentType = PHOTO_DRAW;
		    	break;
		    case 4:
		    	intentClass = CropActivity.class;
		    	intentType = PHOTO_CROP;
		    	break;
		    case 5:
		    	intentClass = AddWatermarkActivity.class;
		    	intentType = ADD_WATERMARK;
		    	break;
			case 6:
				intentClass = RevolveActivity.class;
				intentType = PHOTO_REVOLVE;
				break;
		}
		Intent photoFrameIntent = new Intent(MainActivity.this,intentClass);
		photoFrameIntent.putExtra("picturePath", picturePath);
		MainActivity.this.startActivityForResult(photoFrameIntent,intentType);
		return true;
	}

}