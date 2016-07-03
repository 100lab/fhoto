package kr.pe.roter.fhoto.content;

import java.text.BreakIterator;
import java.util.ArrayList;

import kr.pe.roter.fhoto.R;
import kr.pe.roter.fhoto.module.FhotoUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.Toast;

public class FhotoBitmapFactory {

	public static final String TAG = "FhotoBitmapFactory";

	/**
	 * 무한도전01
	 * @param context
	 * @param photo
	 * @return
	 */
	public static Bitmap makeMudo01(Context context, Bitmap photo){



		int backgroundW = photo.getWidth();
		int backgroundH = photo.getHeight();

		int longValue = Math.max(backgroundW, backgroundH);
		Bitmap background = Bitmap.createBitmap(backgroundW,backgroundH,Bitmap.Config.ARGB_8888);
		Bitmap mudo01_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.mudo01_logo)).getBitmap(),longValue,0.15);
		Bitmap mudo01_mbc = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.mudo01_mbc)).getBitmap(),longValue,0.1);
		Bitmap mudo01_skull = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.mudo01_skull)).getBitmap(),longValue,0.3);
		Log.v(TAG,"3");
		/*
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(20);
		*/

		int imgW = photo.getWidth();
		int imgH = photo.getHeight();

		int logoX = imgW/10;
		int logoY = imgH/20;

		int mbcX = imgW*8/10;
		int mbcY = imgH/10;

		int skullX = (int) (imgW-mudo01_skull.getWidth()-imgW/10);
		int skullY = (int) (imgH-mudo01_skull.getHeight()-imgH/10);

		Canvas canvas = new Canvas(background);
		canvas.drawBitmap(photo, 0, 0,null);
		canvas.drawBitmap(mudo01_logo, logoX, logoY, null);
		canvas.drawBitmap(mudo01_mbc, mbcX, mbcY, null);
		canvas.drawBitmap(mudo01_skull, skullX,skullY, null);
		//canvas.drawText("바보바보바보야", 30, 30, paint);


		return background;

	}


	/**
	 * 뉴스데스크
	 * @param context
	 * @param photo
	 * @return
	 */
	public static Bitmap makeNewsdesk01(Context context, Bitmap photo, DefaultTextContainer container){
		int backgroundW = photo.getWidth();
		int backgroundH = photo.getHeight();

		int longValue = Math.max(backgroundW, backgroundH);
		Bitmap background = Bitmap.createBitmap(backgroundW,backgroundH,Bitmap.Config.ARGB_8888);
		Bitmap newsdesk01_mbc = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.mbcnews01_mbc)).getBitmap(),longValue,0.1);

		//자막 배경의 경우 가로길이에 무조건 맞춘다.
		Bitmap newsdesk01_lyrics = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.mbcnews01_lyrics)).getBitmap(),longValue,1);





		//이미지 영역
		int imgW = photo.getWidth();
		int imgH = photo.getHeight();



		int mbcX = imgW*8/10;
		int mbcY = imgH/10;

		int lyricsX = 0;
		int lyricsY = imgH - newsdesk01_lyrics.getHeight();

		final int spaceX = newsdesk01_lyrics.getWidth()/100; //기준
		final int spaceY = newsdesk01_lyrics.getHeight()/100;


		Canvas canvas = new Canvas(background);
		canvas.drawBitmap(photo, 0, 0,null);
		canvas.drawBitmap(newsdesk01_mbc, mbcX, mbcY, null);
		canvas.drawBitmap(newsdesk01_lyrics, lyricsX, lyricsY, null);

		//canvas.drawText("바보바보바보야", 30, 30, paint);

		//텍스트 영역
		String name = "이름";
		String job = "직업";
		String message = "텍스트를 바꿀려면 이미지를 클릭하세요";

		if(container!=null){
			Log.v(TAG,"ASDASDASDASDASDJW-------------");
			name = container.getName();
			job = container.getJob();
			message = container.getMessage();
		}


		float fontSize = longValue / 25;
		Paint paint = new Paint();

		//폰트가 위치하는 높이
		int nameY = (int) (lyricsY + fontSize);

		//name
		paint.setColor(Color.rgb(0, 0, 255));
		paint.setTextSize((int)fontSize);
		paint.setAntiAlias(true);


		//Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Bar.ttf");
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "NanumGothicBold.ttf");


		paint.setTypeface(Typeface.create(typeface, Typeface.BOLD));
		paint.setAntiAlias(true);
		canvas.drawText(name, lyricsX+spaceX*3, nameY, paint);
		int textWidth = (int)paint.measureText(name);
		fontSize -= spaceY*5;

		//job
		paint.setColor(Color.rgb(54, 87, 255));
		paint.setTextSize(fontSize);
		canvas.drawText(job, textWidth+spaceX*5, nameY, paint);


		//message
		fontSize = longValue / 28;
		paint.setTextSize(fontSize);
		paint.setColor(Color.WHITE);
		//paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

		ArrayList<String> arrText;
		arrText = inputEnterIntoText(paint,message,(int)(imgW-fontSize*3));

		int arrSize = arrText.size();
		for(int i = 0;i < arrSize;i++){
			canvas.drawText(arrText.get(i), lyricsX+spaceX*3, nameY + (i+1)*(fontSize + spaceY*6) + spaceY*5, paint);
		}

		//canvas.drawText(message, lyricsX+spaceX*3, nameY + fontSize + spaceY*8, paint);


		//paint.setTextSize(fontSize);


		return background;

	}



	/**
	 * 까페 베네 이미지 효과
	 * @param context
	 * @param photo
	 * @return
	 */
	public static Bitmap makeBene(Context context, Bitmap photo){

		int backgroundW = photo.getWidth();
		int backgroundH = photo.getHeight();


		int longValue = Math.max(backgroundW, backgroundH);

		Bitmap background = Bitmap.createBitmap(backgroundW,backgroundH,Bitmap.Config.ARGB_8888);

		Bitmap bene = FhotoUtils.resizeBitmap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.bene)).getBitmap(),longValue);



		int imgW = photo.getWidth();
		int imgH = photo.getHeight();


		Canvas canvas = new Canvas(background);


		//카파배네 로고

		Bitmap bene_resize = Bitmap.createScaledBitmap(bene, imgW/5*3, imgW/5, true);//사이즈를 설정한 값으로 줄여주고...

		//색상 변환
		float []array = {
				(float)198/255, 0,0,0,25,
				0,(float)190/255,0,0,25,
				0,0,(float)165/255,0,25,
				0,0,0,1,0

		};
		photo = toGrayScale(photo);
		Paint color = new Paint();
		ColorMatrix cm2 = new ColorMatrix(array);
		color.setColorFilter(new ColorMatrixColorFilter(cm2));
		canvas.drawBitmap(photo, 0, 0,color);

		canvas.drawBitmap(bene_resize, imgW/10*2, imgH-imgW/5-20,null);






		return background;

	}

	/**
	 * 인각 극장 테마를 입힌다.
	 * @param context
	 * @param photo
	 * @param container
	 * @return
	 */
	public static Bitmap makeHumanCinema(Context context, Bitmap photo, DefaultTextContainer container){
		int backgroundW = photo.getWidth();
		int backgroundH = photo.getHeight();

		int longValue = Math.max(backgroundW, backgroundH);
		Bitmap background = Bitmap.createBitmap(backgroundW,backgroundH,Bitmap.Config.ARGB_8888);
		Bitmap kbs_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.kbs_logo)).getBitmap(),longValue,0.1);
		Bitmap human_time = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.humanntime)).getBitmap(),longValue,0.1);

		//자막 배경의 경우 가로길이에 무조건 맞춘다.
		//Bitmap newsdesk01_lyrics = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.mbcnews01_lyrics)).getBitmap(),longValue,1);

		//이미지 영역
		int imgW = photo.getWidth();
		int imgH = photo.getHeight();


		int logoX = imgW/10;
		int logoY = imgH/20;

		int mbcX = imgW*8/10;
		int mbcY = imgH/10;


		int lyricsX = 0;
		int lyricsY = imgH*8/10;

		int infoX = 0;
		int infoY = imgH*85/100;


		Canvas canvas = new Canvas(background);
		canvas.drawBitmap(photo, 0, 0,null);
		canvas.drawBitmap(kbs_logo, mbcX, mbcY, null);
		canvas.drawBitmap(human_time, logoX, mbcY, null);





		//텍스트 영역
		String age= "7";
		String name = "이름";
		String job = "직업";
		String message = "텍스트를 바꿀려면 이미지를 클릭하세요. 이름을 입력하지 않으면 이름 및 나이가 표시되지 않습니다.";

		if(container!=null){

			name = container.getName();
			age = container.getAge();
			job = container.getJob();
			message = container.getMessage();
		}


		//폰트 생성 및 인터뷰 세로 길이 측정용
		float fontSize = longValue / 20;
		float spaceY = fontSize / 2;
		Paint paint = new Paint();
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Bar.ttf");
		paint.setTypeface(Typeface.create(typeface, Typeface.BOLD));
		paint.setTextSize(fontSize);

		ArrayList<String> arrText;
		message = String.format("“%s”", message);
		arrText = inputEnterIntoText(paint,message,(int)(imgW-fontSize*3));
		int arrSize = arrText.size();
		infoY -= fontSize*(arrSize-1);



		//인터뷰 첫줄의 가장 왼쪽에 정렬하기 위해 다음을 사용한다.
		if(arrSize != 0){
			infoX = (int) ((imgW - (int) paint.measureText(arrText.get(0)))/2 + fontSize);
		}




		//폰트 길이 측정용
		String strForWidth = String.format("%s(%s)/%s", name,age,job);
		float infoForAlignCenter = paint.measureText(strForWidth);
		//infoX = (int) ((imgW - infoForAlignCenter) / 2);



		if(!name.equals("")){
			//name		
			paint.setColor(Color.WHITE);
			paint.setTextSize(fontSize);
			paint.setAntiAlias(true);

			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth((float)4);
			canvas.drawText(name, infoX, infoY, paint);

			paint.setColor(Color.WHITE);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText(name, infoX, infoY, paint);


			//age + job
			int textWidth = (int)paint.measureText(name);
			String strAgeJob = String.format("(%s)/%s", age,job);
			fontSize = fontSize * 7 / 8;

			paint.setTextSize(fontSize);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth((float)4);

			canvas.drawText(strAgeJob, textWidth+infoX, infoY, paint);


			paint.setColor(Color.WHITE);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText(strAgeJob, textWidth+infoX, infoY, paint);
		}



		//interview
		fontSize = longValue / 20;
		//paint.setTextAlign(Align.CENTER);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth((float)4);


		lyricsX = (int) ((imgW - (int) paint.measureText(arrText.get(0)))/2);

		//첫줄만 가운데 정렬하고, 나머지 줄은 첫줄의 왼쪽에 맞춘다.
		for(int i = 0;i < arrSize;i++){
			canvas.drawText(arrText.get(i), lyricsX, infoY+fontSize*2/3 + i*fontSize + spaceY, paint);
		}






		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		for(int i = 0;i < arrSize;i++){
			canvas.drawText(arrText.get(i), lyricsX, infoY+fontSize*2/3 +i *fontSize + spaceY, paint);
		}



		return background;

	}






	/**
	 * cafebene용 그레이스케일 이미지를 만든다.
	 * @param photo
	 * @return
	 */
	private static Bitmap toGrayScale(Bitmap photo) {
		int width, height;
		height = photo.getHeight();
		width = photo.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(photo, 0, 0, paint);
		return bmpGrayscale;
	}



	/**
	 * 글씨 길이가 width를 넘어가면 엔터를 치게 만든다.
	 * @param paint
	 * @param text
	 * @param maxWidth
	 * @return
	 */
	public static ArrayList<String> inputEnterIntoText(Paint paint, String text, int maxWidth) {
		StringBuilder sb = new StringBuilder();
		int length = text.length();
		String spaceParcer = " ";
		ArrayList<String> arrText = new ArrayList<String>();
		int prev_i = 0;
		for(int i = 0;i < length;i++){

			String string = text.substring(prev_i, i);
			if((int)paint.measureText(string) > maxWidth){

				if(string.length() >= 1){
					Log.v(TAG,"HI:"+string.charAt(0)+"HI3");
					if(string.charAt(0) == ' '){
						prev_i++;
						i++;
						string = text.substring(prev_i, i);
					}
				}

				arrText.add(string);
				prev_i = i;
			}
		}


		//마지막 줄을 추가해준다.
		/*
		if(text.substring(prev_i, length).charAt(0) == ' '){//스페이스 제거용
			prev_i++;
		}
		*/
		String string = text.substring(prev_i, length);
		if(string.length() >= 1){
			if(string.charAt(0) == ' ')
				string = text.substring(prev_i+1, length);
		}
		arrText.add(string);

		for(int i = 0;i < arrText.size();i++)
			Log.v(TAG,arrText.get(i));

		return arrText;

	}



	/**
	 * 영화자막 테마를 입힌다.
	 * @param context
	 * @param photo
	 * @return
	 */

	public static Bitmap makeMovie01(Context context, Bitmap photo, DefaultTextContainer container){


		int backgroundW = photo.getWidth();
		int backgroundH = photo.getHeight();

		int longValue = Math.max(backgroundW, backgroundH);
		Bitmap background = Bitmap.createBitmap(backgroundW,backgroundH,Bitmap.Config.ARGB_8888);

		//String message = "텍스트를 바꿀려면 이미지를 클릭하세요";

		int imgW = photo.getWidth();
		int imgH = photo.getHeight();

		int spaceH = longValue / 100;

		float fontSize = longValue / 25;
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "NanumGothicBold.ttf");
		//Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Bar.ttf");


		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(fontSize);
		paint.setTypeface(Typeface.create(typeface, Typeface.BOLD));

		String message = "자막을 넣으시려면 이미지를 클릭하세요 ";

		if(container!=null){
			message = container.getMessage();
		}




		ArrayList<String> arrText;
		arrText = inputEnterIntoText(paint,message,(int)(imgW-fontSize*2));


		Canvas canvas = new Canvas(background);
		canvas.drawBitmap(photo, 0, 0,null);






		paint.setTextAlign(Align.CENTER);
		paint.setColor(Color.WHITE);
		paint.setTextSize(fontSize);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth((float)4);



		int arrSize = arrText.size();
		for(int i = 0;i < arrSize;i++){
			canvas.drawText(arrText.get(i), imgW/2, imgH*9/10 + i*(fontSize+spaceH) - fontSize*arrSize/2, paint);
		}

		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		for(int i = 0;i < arrSize;i++){
			canvas.drawText(arrText.get(i), imgW/2, imgH*9/10 + i*(fontSize+spaceH) - fontSize*arrSize/2, paint);
		}



		return background;

	}



	/**
	 * 화성인 바이러스
	 * @param context
	 * @param photo
	 * @return
	 */
	public static Bitmap makeHwasung(Context context, Bitmap photo, DefaultTextContainer container){

		int backgroundW = photo.getWidth();
		int backgroundH = photo.getHeight();

		int longValue = Math.max(backgroundW, backgroundH);
		Bitmap background = Bitmap.createBitmap(backgroundW,backgroundH,Bitmap.Config.ARGB_8888);
		Bitmap hwasung_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.hwasung_logo)).getBitmap(),longValue,0.15);
		Bitmap tvn_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.tvn_logo)).getBitmap(),longValue,0.1);
		Bitmap hwasung_lyrics = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.hwasung_lyrics)).getBitmap(),backgroundW,1);
		Log.v(TAG,"3");
		/*
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(20);
		*/

		int imgW = photo.getWidth();
		int imgH = photo.getHeight();

		int hwasungX = imgW/10;
		int hwasungY = imgH/20;

		int tvnX = imgW*8/10;
		int tvnY = imgH/10;

		int lyricsY = (int) (imgH*55/72.0);

		Canvas canvas = new Canvas(background);
		canvas.drawBitmap(photo, 0, 0,null);
		canvas.drawBitmap(hwasung_logo, hwasungX, hwasungY, null);
		canvas.drawBitmap(tvn_logo, tvnX, tvnY, null);
		canvas.drawBitmap(hwasung_lyrics, 0, lyricsY, null);
		//canvas.drawText("바보바보바보야", 30, 30, paint);



		String roughIntro = "간략정보 입력";
		String detailIntro = "상세정보 및 이름";
		if(container != null){
			roughIntro = container.getRoughIntro();
			detailIntro = container.getDetailIntro();
		}


		float fontSize = hwasung_lyrics.getHeight() / 5;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(fontSize);
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "NanumGothicBold.ttf");
		paint.setTypeface(Typeface.create(typeface, Typeface.BOLD));

		int roughIntroX = (int) (imgW * 200 / 800.0);
		int roughIntroY = (int) (imgH * 550 / 720.0 + fontSize);
		canvas.drawText(roughIntro, roughIntroX, roughIntroY, paint);



		fontSize = hwasung_lyrics.getHeight() / 3.0f ;
		int detailIntroX = (int)(imgW / 2.0);
		int detailIntroY = (int) (roughIntroY + fontSize*1.6);//(int) (imgH * 600 / 720.0 + fontSize);
		paint.setTextSize(fontSize);
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText(detailIntro, detailIntroX, detailIntroY, paint);

		return background;

	}


	/**
	 * 생활의 달인 테마 생성
	 * @param context
	 * @param photo
	 * @param container
	 * @return
	 */
	public static Bitmap makeLifeGosu(Context context, Bitmap photo, DefaultTextContainer container){

		int backgroundW = photo.getWidth();
		int backgroundH = photo.getHeight();

		int longValue = Math.max(backgroundW, backgroundH);
		Bitmap background = Bitmap.createBitmap(backgroundW,backgroundH,Bitmap.Config.ARGB_8888);
		Bitmap lifegosu_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.lifegosu_logo)).getBitmap(),backgroundW,0.4);
		Bitmap sbs_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.sbs_logo)).getBitmap(),longValue,0.1);

		int imgW = photo.getWidth();
		int imgH = photo.getHeight();

		int lifegosuX = imgW/20;
		int lifegosuY = imgH/15;

		int sbsX = imgW*8/10;
		int sbsY = imgH/10;

		int lyricsY = (int) (imgH*85/100.0);

		Canvas canvas = new Canvas(background);
		canvas.drawBitmap(photo, 0, 0,null);
		canvas.drawBitmap(lifegosu_logo, lifegosuX, lifegosuY, null);
		canvas.drawBitmap(sbs_logo, sbsX, sbsY, null);





		float fontSize = lifegosu_logo.getHeight()*2/5;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(fontSize);
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "NanumGothicBold.ttf");
		paint.setTypeface(Typeface.create(typeface, Typeface.BOLD));


		String roughIntro = "합성의 달인";
		String message = "자막을 넣으시려면 이미지를 클릭하세요";

		if(container!=null){
			Log.v(TAG,"ASDASDASDASDASDJW-------------");
			roughIntro = container.getRoughIntro();
			message = container.getMessage();
		}



		//소개
		int introX = lifegosuX + (int)(90*lifegosu_logo.getWidth()/340.0);
		int introY = lifegosuY + (int)(lifegosu_logo.getHeight() * 51 / 79.0);
		canvas.drawText(roughIntro, introX, introY, paint);

		//메세지 시작
		fontSize = lifegosu_logo.getHeight()/2;
		paint.setTextSize(fontSize);
		ArrayList<String> arrText;
		arrText = inputEnterIntoText(paint,message,(int)(imgW-fontSize*5));

		paint.setTextAlign(Align.CENTER);
		paint.setColor(Color.BLACK);

		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth((float)5);



		int arrSize = arrText.size();
		for(int i = 0;i <= arrSize-1;i++){
			canvas.drawText(arrText.get(i), imgW/2, lyricsY + i*(fontSize*5/4), paint);
		}

		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		for(int i = 0;i <= arrSize-1;i++){
			canvas.drawText(arrText.get(i), imgW/2, lyricsY + i*(fontSize*5/4), paint);
		}
		//메세지 끝



		return background;

	}




	/**
	 * 짝
	 * @param context
	 * @param photo
	 * @return
	 */
	public static Bitmap makeMate01(Context context, Bitmap photo ,DefaultTextContainer container){

		int backgroundW = photo.getWidth();
		int backgroundH = photo.getHeight();

		int longValue = Math.max(backgroundW, backgroundH);
		Bitmap background = Bitmap.createBitmap(backgroundW,backgroundH,Bitmap.Config.ARGB_8888);
		Bitmap mate_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.mate_logo)).getBitmap(),longValue,0.2);
		Bitmap mate_sbs = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.sbs_logo)).getBitmap(),longValue,0.1);
		Bitmap gender_logo;

		String message = "어머 텍스트를 변경하려면 이미지를 클릭해야해!";
		int gender = 0;


		if(container!=null){
			message = container.getMessage();
			gender = container.getGender();
		}




		if(gender == 0)//남자
			gender_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.mate_man_logo)).getBitmap(),longValue,0.12);
		else//여자
			gender_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.mate_woman_logo)).getBitmap(),longValue,0.12);

		int imgW = photo.getWidth();
		int imgH = photo.getHeight();

		int logoX = imgW/12;
		int logoY = imgH/20;

		int mbcX = imgW*8/10;
		int mbcY = imgH/10;

		int genderX = (int) (imgW/10);
		int genderY = (int) (imgH*8.5/10);


		int infoY = imgH*85/100;

		int lyricsX = 0;



		Canvas canvas = new Canvas(background);
		canvas.drawBitmap(photo, 0, 0, null);
		canvas.drawBitmap(mate_logo, logoX, logoY, null);
		canvas.drawBitmap(mate_sbs, mbcX, mbcY, null);


		//폰트 생성 및 인터뷰 세로 길이 측정용
		float fontSize = longValue / 25;
		float spaceY = fontSize / 2;
		Paint paint = new Paint();
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "NanumGothicBold.ttf");
		paint.setTypeface(Typeface.create(typeface, Typeface.BOLD));
		paint.setTextSize(fontSize);
		paint.setAntiAlias(true);

		ArrayList<String> arrText;
		message = String.format("%s", message);
		arrText = inputEnterIntoText(paint,message,(int)(imgW-fontSize*3));
		int arrSize = arrText.size();
		infoY -= fontSize*(arrSize-1);


		/////////////////////////
		//interview



		fontSize = longValue / 25;
		paint.setTextAlign(Align.LEFT);

		paint.setAntiAlias(true);
		if(gender==0)
			paint.setColor(Color.BLUE);
		else
			paint.setColor(Color.RED);


		paint.setStyle(Paint.Style.STROKE);
		paint.setTextSize(fontSize);

		if(arrSize != 0){
			lyricsX = (int) ((imgW - paint.measureText(arrText.get(0)))/2);
		}


		for(int i = 0;i < arrSize;i++){
			canvas.drawText(arrText.get(i), lyricsX, infoY+fontSize*2/3+2 + i*fontSize + spaceY, paint);
		}





		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		for(int i = 0;i < arrSize;i++){
			canvas.drawText(arrText.get(i), lyricsX, infoY+fontSize*2/3 +i *fontSize + spaceY, paint);
		}


		//성별을 텍스트의 제일 왼쪽에 맞추기 위해.
		if(arrSize>0){
			genderX = lyricsX;
		}

		canvas.drawBitmap(gender_logo, genderX, genderY-((arrSize-1)*fontSize + fontSize*4/5), null);



		return background;

	}




	/**
	 * 동상이몽 테마 생성
	 * @param context
	 * @param photo
	 * @param container
	 * @return
	 */
	public static Bitmap makeSameDream(Context context, Bitmap photo, DefaultTextContainer container){

		int backgroundW = photo.getWidth();
		int backgroundH = photo.getHeight();

		int longValue = Math.max(backgroundW, backgroundH);
		Bitmap background = Bitmap.createBitmap(backgroundW,backgroundH,Bitmap.Config.ARGB_8888);
		Bitmap samedream_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.dream_logo)).getBitmap(),backgroundW,0.4);
		Bitmap sbs_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.sbs_logo)).getBitmap(),longValue,0.1);

		int imgW = photo.getWidth();
		int imgH = photo.getHeight();

		int lifegosuX = imgW/20;
		int lifegosuY = imgH/15;

		int sbsX = imgW*8/10;
		int sbsY = imgH/10;

		int lyricsY = (int) (imgH*85/100.0);

		Canvas canvas = new Canvas(background);
		canvas.drawBitmap(photo, 0, 0,null);
		canvas.drawBitmap(samedream_logo, lifegosuX, lifegosuY, null);
		canvas.drawBitmap(sbs_logo, sbsX, sbsY, null);





		float fontSize = samedream_logo.getHeight()*1/4;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(fontSize);
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "NanumGothicBold.ttf");
		paint.setTypeface(Typeface.create(typeface, Typeface.BOLD));


		String roughIntro = "고오급 시계에 빠진 남친";
		String message = "자막을 넣으시려면 이미지를 클릭하세요";

		if(container!=null){
			Log.v(TAG,"ASDASDASDASDASDJW-------------");
			roughIntro = container.getRoughIntro();
			message = container.getMessage();
		}



		//소개
		int introX = lifegosuX + (int)(90*samedream_logo.getWidth()/240.0);
		int introY = lifegosuY + (int)(samedream_logo.getHeight() * 51 / 86.0);
		canvas.drawText(roughIntro, introX, introY, paint);

		//메세지 시작
		fontSize = samedream_logo.getHeight()/2;
		paint.setTextSize(fontSize);
		ArrayList<String> arrText;
		arrText = inputEnterIntoText(paint,message,(int)(imgW-fontSize*5));

		paint.setTextAlign(Align.CENTER);
		paint.setColor(Color.BLACK);

		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth((float) 5);



		int arrSize = arrText.size();
		for(int i = 0;i <= arrSize-1;i++){
			canvas.drawText(arrText.get(i), imgW/2, lyricsY + i*(fontSize*5/4), paint);
		}

		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		for(int i = 0;i <= arrSize-1;i++){
			canvas.drawText(arrText.get(i), imgW/2, lyricsY + i*(fontSize*5/4), paint);
		}
		//메세지 끝



		return background;

	}


	/**
	 * 슈퍼맨이돌아왔다 테마 생성
	 * @param context
	 * @param photo
	 * @param container
	 * @return
	 */
	public static Bitmap makeSuperman(Context context, Bitmap photo, DefaultTextContainer container){

		int backgroundW = photo.getWidth();
		int backgroundH = photo.getHeight();

		int longValue = Math.max(backgroundW, backgroundH);
		Bitmap background = Bitmap.createBitmap(backgroundW,backgroundH,Bitmap.Config.ARGB_8888);
		Bitmap superman_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.superman_logo)).getBitmap(),backgroundW,0.2);
		Bitmap sbs_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.kbs2_logo)).getBitmap(),longValue,0.1);

		int imgW = photo.getWidth();
		int imgH = photo.getHeight();

		int lifegosuX = imgW/20;
		int lifegosuY = imgH/15;

		int sbsX = imgW*8/10;
		int sbsY = imgH/10;

		int lyricsY = (int) (imgH*85/100.0);

		Canvas canvas = new Canvas(background);
		canvas.drawBitmap(photo, 0, 0,null);
		canvas.drawBitmap(superman_logo, lifegosuX, lifegosuY, null);
		canvas.drawBitmap(sbs_logo, sbsX, sbsY, null);





		float fontSize = superman_logo.getHeight()*1/3;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(fontSize);
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "NanumGothicBold.ttf");
		paint.setTypeface(Typeface.create(typeface, Typeface.BOLD));


		String roughIntro = "중박이네";
		String message = "까까 사달란 말예요 까까";

		if(container!=null){
			Log.v(TAG,"ASDASDASDASDASDJW-------------");
			roughIntro = container.getRoughIntro();
			message = container.getMessage();
		}



		//소개
		int introX = lifegosuX + (int)(superman_logo.getWidth()*100/240.0);
		int introY = lifegosuY + (int)(superman_logo.getHeight() * 51 / 91.0);
		canvas.drawText(roughIntro, introX, introY, paint);

		//메세지 시작
		fontSize = superman_logo.getHeight()/2;
		paint.setTextSize(fontSize);
		ArrayList<String> arrText;
		arrText = inputEnterIntoText(paint,message,(int)(imgW-fontSize*5));

		paint.setTextAlign(Align.CENTER);
		paint.setColor(Color.BLACK);

		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth((float) 5);



		int arrSize = arrText.size();
		for(int i = 0;i <= arrSize-1;i++){
			canvas.drawText(arrText.get(i), imgW/2, lyricsY + i*(fontSize*5/4), paint);
		}

		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		for(int i = 0;i <= arrSize-1;i++){
			canvas.drawText(arrText.get(i), imgW/2, lyricsY + i*(fontSize*5/4), paint);
		}
		//메세지 끝



		return background;

	}


	/**
	 * 안녕하세요 테마 생성
	 * @param context
	 * @param photo
	 * @param container
	 * @return
	 */
	public static Bitmap makeSayHello(Context context, Bitmap photo, DefaultTextContainer container){

		int backgroundW = photo.getWidth();
		int backgroundH = photo.getHeight();

		int longValue = Math.max(backgroundW, backgroundH);
		Bitmap background = Bitmap.createBitmap(backgroundW, backgroundH, Bitmap.Config.ARGB_8888);
		Bitmap superman_logo = FhotoUtils.resizeElement(((BitmapDrawable) context.getResources().getDrawable(R.drawable.sayhello_logo)).getBitmap(), backgroundW, 0.4);
		Bitmap sbs_logo = FhotoUtils.resizeElement(((BitmapDrawable)context.getResources().getDrawable(R.drawable.kbs2_logo)).getBitmap(),longValue,0.1);

		int imgW = photo.getWidth();
		int imgH = photo.getHeight();

		int lifegosuX = imgW/20;
		int lifegosuY = imgH/15;

		int sbsX = imgW*8/10;
		int sbsY = imgH/10;

		int lyricsY = (int) (imgH*85/100.0);

		Canvas canvas = new Canvas(background);
		canvas.drawBitmap(photo, 0, 0,null);
		canvas.drawBitmap(superman_logo, lifegosuX, lifegosuY, null);
		canvas.drawBitmap(sbs_logo, sbsX, sbsY, null);





		float fontSize = superman_logo.getHeight()*1/4;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(fontSize);
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "NanumGothicBold.ttf");
		paint.setTypeface(Typeface.create(typeface, Typeface.BOLD));


		String roughIntro = "게임에 중독된 아빠";
		String message = "게임좀 그만했으면 좋겠어요!";

		if(container!=null){
			Log.v(TAG,"ASDASDASDASDASDJW-------------");
			roughIntro = container.getRoughIntro();
			message = container.getMessage();
		}



		//소개
		int introX = lifegosuX + (int)(superman_logo.getWidth()/5);
		int introY = lifegosuY + (int)(superman_logo.getHeight() * 14 / 30);
		canvas.drawText(roughIntro, introX, introY, paint);

		//메세지 시작
		fontSize = superman_logo.getHeight()/2;
		paint.setTextSize(fontSize);
		ArrayList<String> arrText;
		arrText = inputEnterIntoText(paint,message,(int)(imgW-fontSize*5));

		paint.setTextAlign(Align.CENTER);
		paint.setColor(Color.BLACK);

		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth((float) 5);



		int arrSize = arrText.size();
		for(int i = 0;i <= arrSize-1;i++){
			canvas.drawText(arrText.get(i), imgW/2, lyricsY + i*(fontSize*5/4), paint);
		}

		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		for(int i = 0;i <= arrSize-1;i++){
			canvas.drawText(arrText.get(i), imgW/2, lyricsY + i*(fontSize*5/4), paint);
		}
		//메세지 끝



		return background;

	}


}




