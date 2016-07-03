package kr.pe.roter.fhoto.module;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class FhotoUtils {

	public static String TAG = "FhotoUtils";

	/**
	 * Bitmap을 파일로 저장한다.
	 * @param bitmap
	 * @param strFolderPath 끝에 /를 붙여서 넣는다.
	 * @param strFileName
	 */
	public static String saveBitmapToFileCache(Bitmap bitmap, String strFolderPath, String strFileName)
	{


		File fileFolder = new File(strFolderPath);
		if( !fileFolder.exists() )
			fileFolder.mkdirs();

		String strFullFilePath = "";
		strFullFilePath = strFolderPath + strFileName;
		File fileFullPath = new File(strFullFilePath);

		Log.v(TAG,strFullFilePath);

		OutputStream out = null;

		try
		{
			fileFullPath.createNewFile();
			out = new FileOutputStream(fileFullPath);

			bitmap.compress(CompressFormat.JPEG, 100, out);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				out.flush();
				out.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return strFullFilePath;
	}

	/**
	 * ElementBitmap을 리사이즈 한다.
	 * @param bitmap
	 * @param parentLongValue : 비율의 기준이 되는 길이. 주로 배경 그림의 가로 길이 중 긴 것
	 * @param rateX parentLongValue 대해 변환하려는 bitmap의 가로 길이가 몇의 비율인지. 1이면 100%이다.
	 */
	public static Bitmap resizeElement(Bitmap bitmap, int parentLongValue, double rateX){

		Bitmap resized = null;
		int resizedWidth, resizedHeight;

		resizedWidth = (int)(parentLongValue*rateX);
		resizedHeight = (int)(resizedWidth*bitmap.getHeight()/(float)bitmap.getWidth());

		resized = Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, true);
		return resized;
	}


	/**
	 * Image를 가로/세로 길이 중 큰 길이가 maxLength에 맞도록 resize한다.
	 * @param bitmap
	 * @param maxLength
	 * @return
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, int maxLength) throws OutOfMemoryError{


		Bitmap resized = null;

		int orgWidth, orgHeight;
		int resizedWidth, resizedHeight;
		orgWidth = bitmap.getWidth();
		orgHeight = bitmap.getHeight();
		Log.v(TAG,"WIDTH:"+orgWidth+"HEIGHT:"+orgHeight);
		//보다 작은 이미지는 그냥 리턴.
		if( Math.max(orgWidth, orgHeight) < maxLength )
			return bitmap;


		if(orgWidth > orgHeight){
			//Width가 더 긴 이미지의 경우 width를 maxLength에 맞춘다.
			Log.v(TAG,"WIDTH");
			resizedWidth = maxLength;
			resizedHeight = (int) (resizedWidth * orgHeight / (double)orgWidth);
		} else {
			Log.v(TAG,"HEIGHT");
			//Height가 더 긴 이미지의 경우 height를 maxLength에 맞춘다.
			resizedHeight = maxLength;
			resizedWidth = (int) (resizedHeight * orgWidth / (double)orgHeight);
		}

		resized = Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, true);
		//bitmap.recycle();

		return resized;
	}


	public static int GetExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;

		try {
			exif = new ExifInterface(filepath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);


			if (orientation != -1) {
				switch(orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						degree = 90;
						break;

					case ExifInterface.ORIENTATION_ROTATE_180:
						degree = 180;
						break;

					case ExifInterface.ORIENTATION_ROTATE_270:
						degree = 270;
						break;
				}
			}
		}

		Log.v(TAG,"degree:"+degree);
		return degree;
	}


	public static int getOrientation(Context context, Uri photoUri) throws NullPointerException{
        /* it's on the external media. */
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

		if (cursor.getCount() != 1) {
			return -1;
		}

		cursor.moveToFirst();
		return cursor.getInt(0);
	}


	/**
	 * Bitmap이미지를 회전한다.
	 * @param bitmap 비트맵 이미지
	 * @param degrees 회전 각도
	 * @return 회전된 이미지
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) throws OutOfMemoryError{
		Log.v(TAG,"degree:"+degrees);
		if(degrees != 0 && bitmap != null){
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) bitmap.getWidth()/2, (float)bitmap.getHeight());


			Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
			if( bitmap != converted ){
				bitmap.recycle();
				bitmap = converted;
			}

		}

		return bitmap;
	}


	/**
	 * 정당한 사이즈 밑으로 이미지를 로드한다.
	 * @param filePath
	 * @param maxSize
	 * @return
	 */
	public static Bitmap decodeBitmapProperSize(String filePath, int maxSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		int scale = 1;

		int longValue = Math.max(options.outHeight, options.outWidth);
		if( longValue > maxSize )
		{
			float scaleFactor = longValue / (float)maxSize;
			scale = (int)(scaleFactor+1);
		}
		/*
		if( options.outHeight > maxSize || options.outWidth > maxSize )
		{
		    scale = (int)Math.pow(  2,  (int)Math.round( Math.log( maxSize / (double)Math.max( options.outHeight, options.outWidth ) )
		    / Math.log( 0.5 ) ) );
		}
		*/
		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		Bitmap bitmap = null;

		try{
			bitmap = BitmapFactory.decodeFile(filePath, options);
		} catch ( OutOfMemoryError e){
			e.printStackTrace();
		} catch ( NullPointerException e ){
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 적당한 사이즈 밑으로 이미지를 로드한다.
	 * @param descriptor
	 * @param maxSize
	 * @return
	 */
	public static Bitmap decodeBitmapProperSize(FileDescriptor descriptor, int maxSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(descriptor, null, options);
		int scale = 1;
		int longValue = Math.max(options.outHeight, options.outWidth);
		if( longValue > maxSize )
		{
			float scaleFactor = longValue / (float)maxSize;
			scale = (int)(scaleFactor+1);
		}

		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		Bitmap bitmap = null;
		try{
			bitmap = BitmapFactory.decodeFileDescriptor(descriptor, null, options);
		}catch(OutOfMemoryError e) {
			e.printStackTrace();
		}

		return bitmap;
	}



	/**
	 * 적당한 사이즈로 이미지를 로드한다.
	 * @param res
	 * @param id
	 * @param maxSize
	 * @return
	 */
	public static Bitmap decodeBitmapProperSize(Resources res, int id, int maxSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, id, options);
		int scale = 1;

		int longValue = Math.max(options.outHeight, options.outWidth);
		if( longValue > maxSize )
		{
			float scaleFactor = longValue / (float)maxSize;
			scale = (int)(scaleFactor+1);
		}
		/*
		if( options.outHeight > maxSize || options.outWidth > maxSize )
		{
		    scale = (int)Math.pow(  2,  (int)Math.round( Math.log( maxSize / (double)Math.max( options.outHeight, options.outWidth ) )
		    / Math.log( 0.5 ) ) );
		}
		*/
		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		Bitmap bitmap = null;

		try{
			bitmap = BitmapFactory.decodeResource(res, id, options);
		} catch ( OutOfMemoryError e){
			e.printStackTrace();
		} catch ( NullPointerException e ){
			e.printStackTrace();
		}
		return bitmap;
	}







}
