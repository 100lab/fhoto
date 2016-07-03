package kr.pe.roter.fhoto.content;

public class DefaultTextContainer {

	private String mName = "이름";
	private String mJob = "직업";
	private String mAge = "7";
	private String mMessage = "수정하시려면 클릭하세요";
	private int mGender = 0;
	private String mRoughIntro = "간략 소개";
	private String mDetailIntro = "상세 소개";


	public DefaultTextContainer() {

	}
	public void setName(String name){mName = name;}
	public String getName(){return mName;}

	public void setJob(String job){mJob = job;}
	public String getJob(){return mJob;}


	public void setGender(int Gender){mGender = Gender;}
	public int getGender(){return mGender;}


	public void setAge(String age){mAge = age;}
	public String getAge(){return mAge;}

	public void setMessage(String message){mMessage = message;}
	public String getMessage(){return mMessage;}

	public void setRoughIntro(String intro){mRoughIntro = intro;}
	public String getRoughIntro(){return mRoughIntro;}

	public void setDetailIntro(String intro){mDetailIntro = intro;}
	public String getDetailIntro(){return mDetailIntro;}
}
