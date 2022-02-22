package com.js.photosdk.filter;

public class NativeFilters {
    /**
     * 静态引用filters.so
     */
	static {
		System.loadLibrary("filters");
	}
    
	/**
	 * 美白滤镜
	 * @param pixels 图像像素点集
	 * @param width  图像像素点宽度
	 * @param height 图像像素点高度
	 * @param beita  
	 * @param factor 滤镜变化程度
	 * @return
	 */
	public native int[] ToWhiteLOG(int[] pixels, int width, int height,int beita, float factor);	
	/**
	 * 灰色滤镜
	 * @param pixels  图像像素点集
	 * @param width   图像像素点宽度
	 * @param height  图像像素点高度
	 * @param factor  滤镜变化程度(0< factor< 1)
	 * @return        添加滤镜后的图像像素点集
	 */
	public native int[] ToGray(int[] pixels, int width, int height, float factor);
	/*怀旧风格*/
	public native int[] ToNostalgic(int[] pixels, int width, int height,float factor);
	/*漫画风格*/
	public native int[] ToComics(int[] pixels, int width, int height,float factor);
	/*黑白滤镜*/
	public native int[] ToBlackWhite(int[] pixels, int width, int height,float factor);
	/*反色效果*/
	public native int[] ToNegative(int[] pixels, int width, int height,float factor);
	/*流年风格*/
	public native int[] ToBrown(int[] pixels, int width, int height,float factor);
	/*素描铅笔*/
	public native int[] ToSketch(int[] pixels, int width, int height,float factor);
	/*过度曝光*/
	public native int[] ToOverExposure(int[] pixels, int width, int height,float factor);
	/*柔化风格*/
	public native int[] ToSoftness(int[] pixels, int width, int height,float factor);
	/*霓虹效果*/
	public native int[] ToNiHong(int[] pixels, int width, int height,float factor);
	/*雕刻效果*/
	public native int[] ToCarving(int[] pixels, int width, int height,float factor);
	/*浮雕效果*/
	public native int[] ToRelief(int[] pixels, int width, int height,float factor);
	/*油画风格*/
	public native int[] ToRuiHua(int[] pixels, int width, int height,float factor);
}
