package com.lanchuang.wgc.android_contacts7;

public class UsersInformation
{

	private String userName;
	private String phoneNumber;
	private String otherImportantInformation;
	private String otherInformation;

	public UsersInformation()
	{
	}

	public UsersInformation(String userName , String phoneNumber , String otherImportantInformation , String otherInformation)
	{
		super();
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.otherImportantInformation = otherImportantInformation;
		this.otherInformation = otherInformation;

	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName )
	{
		this.userName = userName;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber )
	{
		this.phoneNumber = phoneNumber;
	}

	public String getOtherImportantInformation()
	{
		return otherImportantInformation;
	}

	public void setOtherImportantInformation(String otherImportantInformation )
	{
		this.otherImportantInformation = otherImportantInformation;
	}

	public String getOtherInformation()
	{
		return otherInformation;
	}

	public void setOtherInformation(String otherInformation )
	{
		this.otherInformation = otherInformation;
	}

	@Override
	public String toString()
	{

		// return userName + "\n" + phoneNumber + "\n" + otherInformation +
		// "\n";
		return otherInformation;
	}

}
