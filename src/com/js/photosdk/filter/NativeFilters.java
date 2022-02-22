package com.js.photosdk.filter;

public class NativeFilters {
    /**
     * ��̬����filters.so
     */
	static {
		System.loadLibrary("filters");
	}
    
	/**
	 * �����˾�
	 * @param pixels ͼ�����ص㼯
	 * @param width  ͼ�����ص���
	 * @param height ͼ�����ص�߶�
	 * @param beita  
	 * @param factor �˾��仯�̶�
	 * @return
	 */
	public native int[] ToWhiteLOG(int[] pixels, int width, int height,int beita, float factor);	
	/**
	 * ��ɫ�˾�
	 * @param pixels  ͼ�����ص㼯
	 * @param width   ͼ�����ص���
	 * @param height  ͼ�����ص�߶�
	 * @param factor  �˾��仯�̶�(0< factor< 1)
	 * @return        ����˾����ͼ�����ص㼯
	 */
	public native int[] ToGray(int[] pixels, int width, int height, float factor);
	/*���ɷ��*/
	public native int[] ToNostalgic(int[] pixels, int width, int height,float factor);
	/*�������*/
	public native int[] ToComics(int[] pixels, int width, int height,float factor);
	/*�ڰ��˾�*/
	public native int[] ToBlackWhite(int[] pixels, int width, int height,float factor);
	/*��ɫЧ��*/
	public native int[] ToNegative(int[] pixels, int width, int height,float factor);
	/*������*/
	public native int[] ToBrown(int[] pixels, int width, int height,float factor);
	/*����Ǧ��*/
	public native int[] ToSketch(int[] pixels, int width, int height,float factor);
	/*�����ع�*/
	public native int[] ToOverExposure(int[] pixels, int width, int height,float factor);
	/*�ữ���*/
	public native int[] ToSoftness(int[] pixels, int width, int height,float factor);
	/*�޺�Ч��*/
	public native int[] ToNiHong(int[] pixels, int width, int height,float factor);
	/*���Ч��*/
	public native int[] ToCarving(int[] pixels, int width, int height,float factor);
	/*����Ч��*/
	public native int[] ToRelief(int[] pixels, int width, int height,float factor);
	/*�ͻ����*/
	public native int[] ToRuiHua(int[] pixels, int width, int height,float factor);
}
