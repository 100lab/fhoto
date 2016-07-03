package kr.pe.roter.fhoto;

/*
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import kr.pe.roter.fhoto.content.DefaultTextContainer;
import kr.pe.roter.fhoto.module.FhotoGalleryAdapter;
import kr.pe.roter.fhoto.module.FhotoUtils;
import kr.pe.roter.fhoto.module.GlobalVariable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private static final int CAMERA_PIC_REQUEST = 1337;
	private static final int GALLERY_PIC_REQUEST = 1338;


	//msg
	public static final int MSG_CHANGE_PHOTO = 0;



	private AdView mAdView = null;
	private Button mBtnTakePhoto = null;
	private Button mBtnLoadPhoto = null;
	private Button mBtnSavePhoto = null;
	private Button mBtnShare = null;



	//이미지 표시부
	private ImageView mIVcontent = null;

	//갤러리 리스트
	private Gallery mGalleryList = null;
	private FhotoGalleryAdapter mGalleryAdapter = null;

	//불러온 이미지
	private Bitmap mLoadedPhoto = null;
	private Bitmap mThumbnailPhoto = null;

	//저장 경로
	private String mRecentSavePath = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//광고 설정
		initAdmob();


		//Content 설정
		mBtnTakePhoto = (Button)findViewById(R.id.a_main_btn_take_photo);
		mBtnLoadPhoto = (Button)findViewById(R.id.a_main_btn_load_photo);
		mBtnSavePhoto = (Button)findViewById(R.id.a_main_btn_save_photo);
		mBtnShare = (Button)findViewById(R.id.a_main_btn_share);
		mIVcontent = (ImageView)findViewById(R.id.a_main_iv_content);

		mGalleryList = (Gallery)findViewById(R.id.a_main_gallery_list);




		mBtnTakePhoto.setOnClickListener(onClickBtnTakePhoto());
		mBtnLoadPhoto.setOnClickListener(onClickBtnLoadPhoto());
		mBtnSavePhoto.setOnClickListener(onClickBtnSave());
		mBtnShare.setOnClickListener(onClickBtnShare());

		mIVcontent.setOnClickListener(onClickImageViewContent());



		//mGalleryList.setAdapter(new FhotoGalleryAdapter(this));
		
		
		/*
		Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.example2);
		Bitmap b = FhotoBitmapFactory.makeMudo1(this, photo);
				
		mIVcontent.setImageBitmap(b);
		*/


	}



	@Override
	protected void onResume() {



		super.onResume();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//사진 촬영한 경우
		if( requestCode == CAMERA_PIC_REQUEST ){
			if( data != null ) {

				File file = new File(getLastCaptureImagePath());
				if(!file.exists()){
					Toast.makeText(this, "지원하지 않는 카메라 어플입니다.", Toast.LENGTH_SHORT).show();
				} else {

					Uri curImageURI = Uri.fromFile(new File(getLastCaptureImagePath()));
					Log.v(TAG,"haha:"+curImageURI.getPath());


					//thumnail 부르기
					Bitmap thumbnail = FhotoUtils.decodeBitmapProperSize(curImageURI.getPath(), GlobalVariable.PROPER_THUMNAIL_SIZE);
					//편집용 사진 부르기
					Bitmap photo = FhotoUtils.decodeBitmapProperSize(curImageURI.getPath(), GlobalVariable.PROPER_IMG_SIZE);

					//int degree = FhotoUtils.getOrientation(this, curImageURI);
					int degree = FhotoUtils.GetExifOrientation(curImageURI.getPath());
					if(degree != -1) {
						thumbnail = FhotoUtils.rotateBitmap(thumbnail, degree);
						photo = FhotoUtils.rotateBitmap(photo, degree);
					}

					if(mThumbnailPhoto != null)
						mThumbnailPhoto.recycle();
					if(mLoadedPhoto!=null)
						mLoadedPhoto.recycle();
					try{
						reloadPhoto(photo, thumbnail);
					} catch( OutOfMemoryError e) {
						e.printStackTrace();
						Toast.makeText(this, "프로그램이 너무 많이 실행되어 있습니다. 다른 프로그램을 종료 후 다시 실행해 주세요", Toast.LENGTH_LONG);
					}
				}
			}
		}
		//갤러리에서 가져온 경우
		else if( requestCode == GALLERY_PIC_REQUEST ){
			if( data != null){
				Uri curImageURI = data.getData();


				try {
					//그림 파일을 불러와서 읽는다.
					AssetFileDescriptor afd = getContentResolver().openAssetFileDescriptor(curImageURI, "r");


					//thumnail 부르기
					Bitmap thumbnail = FhotoUtils.decodeBitmapProperSize(afd.getFileDescriptor(), GlobalVariable.PROPER_THUMNAIL_SIZE);
					//편집용 사진
					Bitmap bitmap = FhotoUtils.decodeBitmapProperSize(afd.getFileDescriptor(), GlobalVariable.PROPER_IMG_SIZE);

					//int degree = FhotoUtils.GetExifOrientation(curImageURI.getPath()); //안먹힌다. 왜인지는 모르겠다.
					int degree = 0;
					try{
						degree = FhotoUtils.getOrientation(this, curImageURI);
					} catch (NullPointerException e){
						e.printStackTrace();
					}

					if(bitmap!=null && degree!=-1) {
						bitmap = FhotoUtils.rotateBitmap(bitmap, degree);
						thumbnail = FhotoUtils.rotateBitmap(thumbnail, degree);
					}


					//사진을 다시 로드한다.
					if(mThumbnailPhoto != null)
						mThumbnailPhoto.recycle();
					if(mLoadedPhoto!=null)
						mLoadedPhoto.recycle();
					reloadPhoto(bitmap, thumbnail);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
					Toast.makeText(this, "프로그램이 너무 많이 실행되어 있습니다. 다른 프로그램을 종료 후 다시 실행해 주세요", Toast.LENGTH_LONG);
				}
			}
		}


		super.onActivityResult(requestCode, resultCode, data);
	}


	/**
	 * 가장 최근 저장한 이미지를 찾는다.
	 * @return
	 */
	private String getLastCaptureImagePath(){

		String szDateTop = "";
		final String[] IMAGE_PROJECTION = {
				MediaStore.Images.ImageColumns.DATA,
				MediaStore.Images.Thumbnails.DATA
		};
		final Uri uriImages = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		final Uri uriImagesthum = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;

		try{
			final Cursor cursorImages = getContentResolver().query(uriImages, IMAGE_PROJECTION, null, null, null);
			if(cursorImages != null && cursorImages.moveToLast()){
				szDateTop = cursorImages.getString(0);
				cursorImages.close();
			}
		}catch(Exception e){}

		return szDateTop;
	}




	/**
	 * 사진 촬영
	 * @return
	 */
	private OnClickListener onClickBtnTakePhoto(){
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

			}
		};
	}

	/**
	 * 사진 로드
	 * @return
	 */
	private OnClickListener onClickBtnLoadPhoto(){
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {


				//미디어 스캐닝
				//sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
				//Intent albumIntent = new Intent( Intent.ACTION_PICK );
				Intent albumIntent = new Intent( Intent.ACTION_GET_CONTENT );
				//albumIntent.setType( android.provider.MediaStore.Images.Media.CONTENT_TYPE );
				albumIntent.setType("image/*");
				startActivityForResult(albumIntent, GALLERY_PIC_REQUEST);

			}
		};
	}

	/**
	 * 사진 저장
	 * @return
	 */
	private OnClickListener onClickBtnSave(){
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				int saveCount = GlobalVariable.getSaveCount(MainActivity.this);
				Log.v(TAG,"SAVELOCK:"+saveCount);
				//if(saveCount%10 != 0){
				if(mGalleryAdapter != null) {
					String folderpath = Environment.getExternalStorageDirectory() + "/Fhoto/";
					String filename = String.format("photo%03d.jpg", saveCount);

					Bitmap bitmap = null;

					//bitmap = mGalleryAdapter.getCurrentBitmap();
					bitmap = ((BitmapDrawable)mIVcontent.getDrawable()).getBitmap();
					if(bitmap != null){
						mRecentSavePath = FhotoUtils.saveBitmapToFileCache(bitmap, folderpath, filename);
						GlobalVariable.addSaveCount(MainActivity.this);
						Toast.makeText(MainActivity.this, mRecentSavePath+"에 저장되었습니다.", Toast.LENGTH_SHORT).show();
						//미디어 스캐닝
						sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+mRecentSavePath)));
					}
				} else {
					Toast.makeText(MainActivity.this, "사진을 먼저 불러오세요.", Toast.LENGTH_SHORT).show();
				}
				//} else {
				//Toast.makeText(MainActivity.this, "계속해서 저장하실려면 광고를 한번 클릭하셔야 합니다.", Toast.LENGTH_SHORT).show();
				//}


			}
		};
	}

	/**
	 * share 버튼
	 * @return
	 */
	private OnClickListener onClickBtnShare(){
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				/*
				String title = getResources().getString(R.string.app_name);
				String version = getResources().getString(R.string.app_version);
				String message = title + " " + version;
				AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
				dlg.setTitle(title);
				dlg.setMessage(message);
				dlg.create();
				dlg.show();
				*/

				//로드된 사진이 있을때만 한다.
				if(mGalleryAdapter != null){

					//이미 저장된 파일인 경우
					if(mRecentSavePath != null){
						Uri uri = Uri.fromFile(new File(mRecentSavePath));
						Intent shareIntent = new Intent();
						shareIntent.setAction(Intent.ACTION_SEND);
						shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
						shareIntent.setType("image/jpeg");
						startActivity(Intent.createChooser(shareIntent, "공유하기"));
					} else {
						Toast.makeText(MainActivity.this, "먼저 저장해주세요.", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "사진을 먼저 불러오세요.", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}


	/**
	 * 이미지 클릭시 발생하는 이벤트 제어
	 * @return
	 */
	private OnClickListener onClickImageViewContent(){
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(mGalleryAdapter != null){
					final int position = mGalleryAdapter.getRecentLoadedContent();

					Log.v(TAG, "position:" + position);
					if( position == FhotoGalleryAdapter.fhoto_content_null ){
						//클릭해도 변할게 없는 경우는 그냥 무시한다.
					}
					//뉴스데스크
					else if( position == FhotoGalleryAdapter.fhoto_content_newsdesk01 ){
						//이 경우 다이얼로그를 띄워준다.
						LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						final LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.dialog_newdesk01, null);

						AlertDialog dlg = new AlertDialog.Builder(MainActivity.this)
								.setTitle("뉴스데스크")
								.setView(linear)
								.setPositiveButton("확인", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {

										DefaultTextContainer container = new DefaultTextContainer();
										final EditText etName = (EditText)linear.findViewById(R.id.d_newdesk01_name);
										final EditText etJob = (EditText)linear.findViewById(R.id.d_newdesk01_job);
										final EditText etMessage = (EditText)linear.findViewById(R.id.d_newdesk01_message);

										container.setName(etName.getText().toString());
										container.setJob(etJob.getText().toString());
										container.setMessage(etMessage.getText().toString());

										//mGalleryAdapter.putNewsdesk01Container(container);
										mGalleryAdapter.putContainer(position, container);
										//mGalleryList.setAdapter(mGalleryAdapter);
										mGalleryList.refreshDrawableState();
									}
								})
								.setNegativeButton("취소", null)
								.create();

						dlg.show();
					}
					//인간극장
					else if( position == FhotoGalleryAdapter.fhoto_content_humancinema){
						//이 경우 다이얼로그를 띄워준다.
						LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						final LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.diaog_human01, null);

						AlertDialog dlg = new AlertDialog.Builder(MainActivity.this)
								.setTitle("인간극장")
								.setView(linear)
								.setPositiveButton("확인", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {

										DefaultTextContainer container = new DefaultTextContainer();
										final EditText etName = (EditText)linear.findViewById(R.id.d_human01_name);
										final EditText etJob = (EditText)linear.findViewById(R.id.d_human01_job);
										final EditText etAge = (EditText)linear.findViewById(R.id.d_human01_age);
										final EditText etMessage = (EditText)linear.findViewById(R.id.d_human01_message);

										container.setName(etName.getText().toString());
										container.setJob(etJob.getText().toString());
										container.setAge(etAge.getText().toString());
										container.setMessage(etMessage.getText().toString());

										//mGalleryAdapter.putHuman01Container(container);
										mGalleryAdapter.putContainer(position, container);
										mGalleryList.setAdapter(mGalleryAdapter);
										//Log.v(TAG,"ASDASD1");
										//Bitmap bitmap = FhotoBitmapFactory.makeNewsdesk01(MainActivity.this, mLoadedPhoto, container); //MBC뉴스

										//Log.v(TAG,"ASDASD2");
									}
								})
								.setNegativeButton("취소", null)
								.create();

						dlg.show();
					}
					//영화
					else if( position == FhotoGalleryAdapter.fhoto_content_movie ){
						//이 경우 다이얼로그를 띄워준다.
						LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						final LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.dialog_movie01, null);

						AlertDialog dlg = new AlertDialog.Builder(MainActivity.this)
								.setTitle("영화")
								.setView(linear)
								.setPositiveButton("확인", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {

										DefaultTextContainer container = new DefaultTextContainer();



										final EditText etMessage = (EditText)linear.findViewById(R.id.d_movie01_message);
										container.setMessage(etMessage.getText().toString());

										//mGalleryAdapter.putMovie01Container(container);
										mGalleryAdapter.putContainer(position, container);
										//mGalleryList.setAdapter(mGalleryAdapter);
										mGalleryList.refreshDrawableState();
									}
								})
								.setNegativeButton("취소", null)


								.create();

						dlg.show();
					}
					//화성인 바이러스
					else if( position == FhotoGalleryAdapter.fhoto_content_hwasung ){
						//이 경우 다이얼로그를 띄워준다.
						LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						final LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.diaog_hwasung01, null);

						AlertDialog dlg = new AlertDialog.Builder(MainActivity.this)
								.setTitle("화성인바이러스")
								.setView(linear)
								.setPositiveButton("확인", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {

										DefaultTextContainer container = new DefaultTextContainer();

										final EditText etRough = (EditText)linear.findViewById(R.id.d_hwasung01_roughintro);
										container.setRoughIntro(etRough.getText().toString());
										final EditText etDetail = (EditText)linear.findViewById(R.id.d_hwasung01_detailintro);
										container.setDetailIntro(etDetail.getText().toString());

										//mGalleryAdapter.putHwasung01Container(container);
										mGalleryAdapter.putContainer(position, container);
										mGalleryList.refreshDrawableState();
									}
								})
								.setNegativeButton("취소", null)


								.create();

						dlg.show();
					}
					//생활의 달인
					else if( position == FhotoGalleryAdapter.fhoto_content_lifegosu ){
						//이 경우 다이얼로그를 띄워준다.
						LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						final LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.diaog_lifegosu01, null);

						AlertDialog dlg = new AlertDialog.Builder(MainActivity.this)
								.setTitle("생활의달인")
								.setView(linear)
								.setPositiveButton("확인", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {

										DefaultTextContainer container = new DefaultTextContainer();

										final EditText etRough = (EditText)linear.findViewById(R.id.d_lifegosu01_roughintro);
										container.setRoughIntro(etRough.getText().toString());
										final EditText etMessage = (EditText)linear.findViewById(R.id.d_lifegosu01_interview);
										container.setMessage(etMessage.getText().toString());

										//mGalleryAdapter.putLifegosu01Container(container);
										mGalleryAdapter.putContainer(position, container);
										mGalleryList.refreshDrawableState();
									}
								})
								.setNegativeButton("취소", null)


								.create();

						dlg.show();
					}
					//짝 대화
					else if( position == FhotoGalleryAdapter.fhoto_content_mate01 ){
						//이 경우 다이얼로그를 띄워준다.
						LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						final LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.dialog_mate01, null);

						AlertDialog dlg = new AlertDialog.Builder(MainActivity.this)
								.setTitle("짝 대화")
								.setView(linear)
								.setPositiveButton("확인", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {

										DefaultTextContainer container = new DefaultTextContainer();

										final RadioGroup rgGender = (RadioGroup)linear.findViewById(R.id.d_mate01_radiogroup_gender);

										//남여 체크
										if(rgGender.getCheckedRadioButtonId() == R.id.d_mate01_radio_man)
											container.setGender(0);
										else
											container.setGender(1);



										final EditText etMessage = (EditText)linear.findViewById(R.id.d_mate01_dt_message);
										container.setMessage(etMessage.getText().toString());

										//mGalleryAdapter.putMate01Container(container);
										mGalleryAdapter.putContainer(position, container);
										mGalleryList.refreshDrawableState();
									}
								})
								.setNegativeButton("취소", null)


								.create();

						dlg.show();
					}





				}



			}
		};
	}


	@Override
	protected void onPause() {
		//mAdView.stopLoading();
		super.onPause();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
			case R.id.menu_rotate_photo_90:
				Log.v(TAG, "ROTATE90");
				if(mLoadedPhoto != null)
					reloadPhoto(FhotoUtils.rotateBitmap(mLoadedPhoto, 90), FhotoUtils.rotateBitmap(mThumbnailPhoto, 90));
				else
					Toast.makeText(this, "사진을 먼저 불러오세요.", Toast.LENGTH_SHORT).show();
				break;
			case R.id.menu_rotate_photo_270:
				Log.v(TAG, "ROTATE270");
				if(mLoadedPhoto != null)
					reloadPhoto(FhotoUtils.rotateBitmap(mLoadedPhoto, 270), FhotoUtils.rotateBitmap(mThumbnailPhoto, 270));
				else
					Toast.makeText(this, "사진을 먼저 불러오세요.", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
		}


		return super.onOptionsItemSelected(item);
	}


	/**
	 * 사진을 회전하여 다시 로드하게 되는 경우 호출한다.
	 * @param photo
	 */
	private void reloadPhoto(Bitmap photo, Bitmap thumbnail){

		Log.v(TAG,"RELOAD");



		mRecentSavePath = null; //최근 저장 없음으로 초기화.
		mLoadedPhoto = photo;
		mThumbnailPhoto = thumbnail;

		if(mGalleryAdapter != null)
			mGalleryAdapter.recycle();
		mGalleryAdapter = new FhotoGalleryAdapter(this, mLoadedPhoto, mThumbnailPhoto, mIVcontent);
		mGalleryList.setAdapter(mGalleryAdapter);
		mIVcontent.setImageBitmap(mLoadedPhoto);
	}


	private void initAdmob() {
		MobileAds.initialize(getApplicationContext(), "ca-app-pub-5411061752769220/1181099483");

		mAdView = (AdView) findViewById(R.id.a_main_ad_admob);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);


	}

	/**
	 * daum광고인 adam을 init한다.
	 */
	/*
	private void initAdam() {
		// Ad@m sdk 초기화 시작
		mAdView = (AdView) findViewById(R.id.a_main_ad_adam);
		// 광고 리스너 설정
		// 1. 광고 클릭시 실행할 리스너
		mAdView.setOnAdClickedListener(new OnAdClickedListener() {
			@Override
			public void OnAdClicked() {
				Log.i(TAG, "광고를 클릭했습니다.");
				GlobalVariable.addSaveCount(MainActivity.this);
			}
		});
		// 2. 광고 내려받기 실패했을 경우에 실행할 리스너
		mAdView.setOnAdFailedListener(new OnAdFailedListener() {
			@Override
			public void OnAdFailed(AdError error, String message) {
				Log.w(TAG, message);
			}


		});
		// 3. 광고를 정상적으로 내려받았을 경우에 실행할 리스너
		mAdView.setOnAdLoadedListener(new OnAdLoadedListener() {
			@Override
			public void OnAdLoaded() {
				Log.i(TAG, "광고가 정상적으로 로딩되었습니다.");
			}
		});
		// 4. 광고를 불러올때 실행할 리스너
		mAdView.setOnAdWillLoadListener(new OnAdWillLoadListener() {
			@Override
			public void OnAdWillLoad(String url) {
				Log.i(TAG, "광고를 불러옵니다. : " + url);
			}
		});
		// 5. 전면형 광고를 닫았을때 실행할 리스너
		mAdView.setOnAdClosedListener(new OnAdClosedListener() {
			@Override
			public void OnAdClosed() {
				Log.i(TAG, "광고를 닫았습니다.");
			}
		});
		mAdView.setAdCache(false);
		// Animation 효과 : 기본 값은 AnimationType.NONE
		mAdView.setAnimationType(AnimationType.FLIP_HORIZONTAL);
		mAdView.setVisibility(View.VISIBLE);
	}
*/

}
