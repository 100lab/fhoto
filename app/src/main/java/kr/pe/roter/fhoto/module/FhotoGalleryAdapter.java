package kr.pe.roter.fhoto.module;

import java.util.ArrayList;

import kr.pe.roter.fhoto.MainActivity;
import kr.pe.roter.fhoto.R;
import kr.pe.roter.fhoto.content.DefaultTextContainer;
import kr.pe.roter.fhoto.content.FhotoBitmapFactory;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class FhotoGalleryAdapter extends BaseAdapter {
	public static final String TAG = "FhotoGalleryAdapter";

	private Context mContext;
	private Bitmap mOrgPhoto; //원본 사진
	private Bitmap mThumbnail; //썸네일
	private Bitmap mEditedPhoto = null; //합성 된 사진
	private ImageView mImageView;


	//정보 입력
	private DefaultTextContainer mContainer = null;

	//가장 최근 로드된 사진
	private int mRecentLoadedPhotoIndex = -1;


	//상수. 갤러리 순서대로 값을 갖는다.
	public static int fhoto_content_null = -1;
	public static int fhoto_content_index = 0;
	public static final int fhoto_content_samedream = fhoto_content_index++;
	public static final int fhoto_content_superman = fhoto_content_index++;
	public static final int fhoto_content_sayhello = fhoto_content_index++;
	public static final int fhoto_content_mate01 = fhoto_content_index++;
	public static final int fhoto_content_mudo01 = fhoto_content_index++;
	public static final int fhoto_content_newsdesk01 = fhoto_content_index++;
	public static final int fhoto_content_bene = fhoto_content_index++;
	public static final int fhoto_content_humancinema = fhoto_content_index++;
	public static final int fhoto_content_movie = fhoto_content_index++;
	public static final int fhoto_content_hwasung = fhoto_content_index++;
	public static final int fhoto_content_lifegosu = fhoto_content_index++;



	//Image list
	/*
	private Integer[] mImageListID = {
			R.drawable.example1,
			R.drawable.example2,
			R.drawable.example3,
			R.drawable.example4,
			R.drawable.example5,
			R.drawable.example6
	};
	*/

	private Bitmap[] mThumbnailContent = null;



	//title
	private String[] mTitle = {"1","2","3","4","5","6"};

	public FhotoGalleryAdapter(Context context){
		mContext = context;


	}

	public FhotoGalleryAdapter(Context context, Bitmap photo, Bitmap thumbnail, ImageView imageView){
		mContext = context;
		mOrgPhoto = photo;
		mThumbnail = thumbnail;
		mImageView = imageView;

		if(mOrgPhoto == null || mThumbnail == null){
			Toast.makeText(context, "지원하지 않는 갤러리 어플리케이션입니다.", Toast.LENGTH_SHORT);
			return;
		}

		Log.v(TAG,"photoW:"+mOrgPhoto.getWidth() + "photoH:"+mOrgPhoto.getHeight());



		mThumbnailContent = new Bitmap[fhoto_content_index];

		//나중에 여기 쓰레드로 하면서 outofmemory나면 만들던거 멈추자


		//여긴 썸네일만 만들자. 어떻게? 바로 리사이즈하고 리사이클?
		try{
			mThumbnailContent[fhoto_content_mudo01] = FhotoBitmapFactory.makeMudo01(context, mThumbnail); //무한도전
			mThumbnailContent[fhoto_content_newsdesk01] = FhotoBitmapFactory.makeNewsdesk01(context, mThumbnail, null); //MBC뉴스
			mThumbnailContent[fhoto_content_bene] = FhotoBitmapFactory.makeBene(context, mThumbnail);
			mThumbnailContent[fhoto_content_humancinema] = FhotoBitmapFactory.makeHumanCinema(context, mThumbnail, null);
			mThumbnailContent[fhoto_content_movie] = FhotoBitmapFactory.makeMovie01(context, mThumbnail, null);
			mThumbnailContent[fhoto_content_hwasung] = FhotoBitmapFactory.makeHwasung(context, mThumbnail, null);
			mThumbnailContent[fhoto_content_lifegosu] = FhotoBitmapFactory.makeLifeGosu(context, mThumbnail, null);
			mThumbnailContent[fhoto_content_samedream] = FhotoBitmapFactory.makeLifeGosu(context, mThumbnail, null);
			mThumbnailContent[fhoto_content_mate01] = FhotoBitmapFactory.makeMate01(context, mThumbnail, null);
			mThumbnailContent[fhoto_content_superman] = FhotoBitmapFactory.makeSuperman(context, mThumbnail, null);
			mThumbnailContent[fhoto_content_sayhello] = FhotoBitmapFactory.makeSayHello(context, mThumbnail, null);
		} catch(OutOfMemoryError e){
			e.printStackTrace();
		}
		//mBitmapContent[2] = mBackground; //MBC뉴스
		//...


	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mThumbnailContent!=null)
			return mThumbnailContent.length;
		return 0;
	}

	@Override
	public Object getItem(int position) {

		if(mThumbnailContent!=null)
			return mThumbnailContent[position];
		return null;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	/**
	 * 가장 최근 로드된 포토
	 * @return
	 */
	public int getRecentLoadedContent(){
		return mRecentLoadedPhotoIndex;
	}



	/**
	 * 사진에 정보를 입력한다.
	 * @param index pos와 일치한다.
	 * @param container
	 */
	public void putContainer(int index, DefaultTextContainer container){
		mEditedPhoto = makeEditedPhoto(index, container);
	}


	//deprecated
	/**
	 * 짝 대화 정보를 입력한다.
	 * @param container
	 */
	public void putMate01Container(DefaultTextContainer container){
		mEditedPhoto = FhotoBitmapFactory.makeMate01(mContext, mOrgPhoto, container);
	}

	//deprecated
	/**
	 * Newdesk 사진에 정보를 입력한다.
	 * @param container
	 */
	public void putNewsdesk01Container(DefaultTextContainer container){
		mEditedPhoto = FhotoBitmapFactory.makeNewsdesk01(mContext, mOrgPhoto, container);
	}

	//deprecated
	/**
	 * 인간극장 사진에 정보를 입력한다.
	 * @param container
	 */
	public void putHuman01Container(DefaultTextContainer container){
		mEditedPhoto = FhotoBitmapFactory.makeHumanCinema(mContext, mOrgPhoto, container);
	}

	//deprecated
	/**
	 * 영화 자막 사진에 정보를 입력한다.
	 * @param container
	 */
	public void putMovie01Container(DefaultTextContainer container){
		mEditedPhoto = FhotoBitmapFactory.makeMovie01(mContext, mOrgPhoto, container);
	}

	//deprecated
	/**
	 * 화성인 바이러스 정보를 입력한다.
	 * @param container
	 */
	public void putHwasung01Container(DefaultTextContainer container){
		mEditedPhoto = FhotoBitmapFactory.makeHwasung(mContext, mOrgPhoto, container);
	}

	//deprecated
	/**
	 * 생활의 달인
	 * @param container
	 */
	public void putLifegosu01Container(DefaultTextContainer container){
		mEditedPhoto = FhotoBitmapFactory.makeLifeGosu(mContext, mOrgPhoto, container);
	}



	/**
	 * 메모리 누수 정리용
	 */
	public void recycle(){
		for(int i = 0;i < fhoto_content_index;i++){
			if(mThumbnailContent[i]!=null)
				mThumbnailContent[i].recycle();
		}

		if(mOrgPhoto != null)
			mOrgPhoto.recycle();
		if(mThumbnail != null)
			mThumbnail.recycle();
	}



	@Override
	public View getView(int position, View contentView, ViewGroup parent) {

		Display dis = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		//Point displaySize = new Point();
		int disWidth = dis.getWidth();
		//dis.getSize(displaySize);


		ImageView imageView = new ImageView(mContext);

		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

		imageView.setLayoutParams(new Gallery.LayoutParams(disWidth/5, LayoutParams.FILL_PARENT));
		imageView.setImageBitmap(mThumbnailContent[position]);

		//Toast.makeText(mContext, position+"", Toast.LENGTH_SHORT).show();

		final int pos = position;
		imageView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				mRecentLoadedPhotoIndex = pos;



				if(mEditedPhoto != null){
					mEditedPhoto.recycle();
					mEditedPhoto = null;
				}

				mEditedPhoto = makeEditedPhoto(pos, mContainer);


				//위에선 썸네일만 만들고, 여기서 실제로 만들어주자.

				if(mEditedPhoto!=null){
					mImageView.setImageBitmap(mEditedPhoto);
					//make~~~
				}
				return false;
			}
		});

		//이걸 안해주면 갤러리를 드래그 하는 것 만으로 그림이 바뀌더라.
		if(mEditedPhoto!=null)
			mImageView.setImageBitmap(mEditedPhoto);

		Log.v(TAG,"TOUCHED");

		return imageView;
	}



	/**
	 * 사진을 편집한다.
	 * @param pos
	 * @return
	 */
	private Bitmap makeEditedPhoto(int pos, DefaultTextContainer container) {

		mContainer = container;
		Bitmap bitmap = null;
		//짝
		if(pos == fhoto_content_mate01){
			bitmap = FhotoBitmapFactory.makeMate01(mContext, mOrgPhoto, container);
		}
		//무한도전
		else if(pos == fhoto_content_mudo01){
			bitmap = FhotoBitmapFactory.makeMudo01(mContext, mOrgPhoto);
		}
		//안녕하세요
		else if(pos == fhoto_content_sayhello){
			bitmap = FhotoBitmapFactory.makeSayHello(mContext, mOrgPhoto, container);
		}
		//슈퍼맨이돌아왔다
		else if(pos == fhoto_content_superman){
			bitmap = FhotoBitmapFactory.makeSuperman(mContext, mOrgPhoto, container);
		}
		//동상이몽
		else if (pos == fhoto_content_samedream){
			bitmap = FhotoBitmapFactory.makeSameDream(mContext, mOrgPhoto, container);
		}
		//MBC뉴스
		else if(pos == fhoto_content_newsdesk01){
			bitmap = FhotoBitmapFactory.makeNewsdesk01(mContext, mOrgPhoto, container);
		}
		//까페베네
		else if(pos == fhoto_content_bene){
			bitmap = FhotoBitmapFactory.makeBene(mContext, mOrgPhoto);
		}
		//인간극장
		else if(pos == fhoto_content_humancinema){
			bitmap = FhotoBitmapFactory.makeHumanCinema(mContext, mOrgPhoto, container);
		}
		//영화
		else if(pos == fhoto_content_movie){
			bitmap = FhotoBitmapFactory.makeMovie01(mContext, mOrgPhoto, container);
		}
		//화성인 바이러스
		else if(pos == fhoto_content_hwasung){
			bitmap = FhotoBitmapFactory.makeHwasung(mContext, mOrgPhoto, container);
		}
		//생활의 달인
		else if(pos == fhoto_content_lifegosu){
			bitmap = FhotoBitmapFactory.makeLifeGosu(mContext, mOrgPhoto, container);
		}
		return bitmap;
	}


}
