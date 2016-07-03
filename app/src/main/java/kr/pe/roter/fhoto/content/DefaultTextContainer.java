package kr.pe.roter.fhoto.content;

public class DefaultTextContainer {

	private String mName = "";
	private String mJob = "";
	private String mAge = "";
	private String mMessage = "";
	private int mGender = 0;
	private String mRoughIntro = "";
	private String mDetailIntro = "";


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
