package com.lanchuang.wgc.android_contacts7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lanchuang.wgc.writefiletosdcard.service.FileOperate;
import com.lanchuang.wgc.writefiletosdcard.util.MyUtil;

public class MainActivity extends Activity
{

	String TAG = "LOG";
	String FileName = "contacts";

	public List < UsersInformation > addressList;
	public UsersInformation usersInformation;
	public Uri [] contacts = new Uri []
	{ ContactsContract.Contacts.CONTENT_URI, ContactsContract.CommonDataKinds.Phone.CONTENT_URI, ContactsContract.CommonDataKinds.Email.CONTENT_URI };
	public ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.listView01);
		// getSystemContacts();
		getContactsInformation();
		listView.setAdapter(new MyBaseAdapter());

	}

	/**
	 * 扩充功能
	 * 
	 * @param view
	 */
	public void getget(View view )
	{
		MyUtil.showMsg(MainActivity.this ,"扩展功能" ,1);
	}

	/**
	 * 获取并修改头像功能
	 * 
	 * @param view
	 */
	public void getPhoto()
	{
		// Toast.makeText(this ,"获取头像！！！" ,Toast.LENGTH_LONG).show();
		MyUtil.showMsg(MainActivity.this ,"获取并修改头像！！！" ,1);
	}

	/**
	 * 被调打电话或者发短信功能实现
	 * 
	 * @param userNumber
	 */
	public void getCallAndMessage(final String userName , final String userNumber )
	{
		String tips = userName + " : " + userNumber;
		final String call = "打电话";
		final String message = "发短信";

//		AlertDialog.Builder builder0 = new AlertDialog.Builder(this);
//
//		builder0.setTitle(tips);
//		builder0.setIcon(android.R.drawable.ic_search_category_default);
//		final String [] name =
//		{ call, message };
//		builder0.setItems(name ,new DialogInterface.OnClickListener()
//		{
//			@Override
//			public void onClick(DialogInterface dialog , int which )
//			{
//				if(name[which].equals(call))
//				{
//					callNumber(userNumber);
//				}
//				else
//				{
//					sendMessage(userNumber);
//				}
//			}
//		});
//
//		// builder0.setCancelable(false);
//		builder0.show();

		// ********************************************
		// *******************老子™是分界线****************
		// ********************************************

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String [] names =
		{ call, message };
		builder.setTitle(tips);
		builder.setIcon(android.R.drawable.ic_search_category_default);
		/**
		 * 参数（数据列表 ， 默认被选中的索引 （-1表示没有） ， 监听事件处理）
		 */
		builder.setSingleChoiceItems(names , -1 ,new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog , int which )
			{
				if(names[which].equals(call))
				{
					// callNumber(userNumber);
					callNumberDerict(userNumber);
				}
				else
				{
					sendMessage(userNumber);
				}
				dialog.cancel();
			}
		});

		// builder.setCancelable(false);
		builder.show();
	}

	/**
	 * 
	 * 打电话功能实现
	 * 
	 */
	public void callNumberDerict(String number )
	{

		Intent intent = new Intent(Intent.ACTION_CALL);
		// Intent intent = new Intent(Intent.ACTION_DIAL);
		// String number = "17195871521";
		intent.setData(Uri.parse("tel:" + number));
		startActivity(intent);
	}

	/**
	 * 
	 * 打电话功能实现
	 * 
	 */
	public void callNumber(String number )
	{

		// Intent intent = new Intent(Intent.ACTION_CALL);
		Intent intent = new Intent(Intent.ACTION_DIAL);
		// String number = "17195871521";
		intent.setData(Uri.parse("tel:" + number));
		startActivity(intent);
	}

	/**
	 * 
	 * 发送短信功能实现
	 * 
	 */
	public void sendMessage(String number )
	{
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		// String number = "17195871521";
		intent.setData(Uri.parse("smsto:" + number));
		intent.putExtra("sms_body" ,"");
		startActivity(intent);
	}

	/**
	 * 
	 * 遍历获取联系人详细信息 并保存到列表addressList中
	 * 
	 */
	public void getContactsInformation()
	{
		addressList = new ArrayList < UsersInformation >();
		String contactsId;
		String mimetype;
		/**
		 * 用于log.d(); pirntLog; 打印和调试通讯录信息 字符串temp
		 */
		String pirntInformationTemp = "";
		/**
		 * 用于printLog; 打印通讯录信息
		 */
		String printString = "*********\n";

		ContentResolver contentResolver = this.getContentResolver();
		Cursor cursor = contentResolver.query(android.provider.ContactsContract.Contacts.CONTENT_URI ,new String []
		{ android.provider.ContactsContract.Contacts._ID } ,null ,null ,"sort_key");
		while(cursor.moveToNext())
		{

			usersInformation = new UsersInformation();
			contactsId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor contactInfoCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI ,new String []
			{ ContactsContract.Data.CONTACT_ID, ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1 } ,ContactsContract.Data.CONTACT_ID + " = " + contactsId ,null ,null);
			/**
			 * 标记第一次出现
			 */
			boolean phoneNumberMark = true;
			boolean emailMark = true;
			boolean nicknameMark = true;
			boolean organizationMark = true;
			boolean postalMark = true;
			boolean contactMark = true;
			boolean noteMark = true;
			boolean websiteMark = true;
			boolean relationMark = true;
			boolean othersMark = true;
			/**
			 * setOtherInfromation; 详细信息按钮内容
			 */
			String moreString = "";
			/**
			 * setOtherImportantInformation; 重要信息显示
			 */
			String importantString = "";

			String numbersString = "";
			
			
			while(contactInfoCursor.moveToNext())
			{
				String value = contactInfoCursor.getString(contactInfoCursor.getColumnIndex(ContactsContract.Data.DATA1));
				if(value == "" || value == null)
				{
					continue;
				}
				else
				{
					mimetype = contactInfoCursor.getString(contactInfoCursor.getColumnIndex(ContactsContract.Data.MIMETYPE));

					if(mimetype.contains("/name"))
					{
						usersInformation.setUserName(value);

						moreString += "姓名\n\t\t\t\t" + value;

						pirntInformationTemp = "姓名\n\t\t\t\t" + value;
						// Log.d(TAG ,pirntInformationTemp);
					}
					else
						if(mimetype.contains("/phone"))
						{
							if(phoneNumberMark == true)
							{
								usersInformation.setPhoneNumber(value);

								moreString += "\n电话号码\n\t\t\t\t" + value;

								phoneNumberMark = false;

								pirntInformationTemp = "\n电话号码\n\t\t\t\t" + value;
								// Log.d(TAG ,pirntInformationTemp);
								
								
								numbersString = value;										
							}
							else
							{
								if(importantString.isEmpty())
								{
									importantString += value;
								}
								else
								{
									importantString += "\n" + value;
								}
								moreString += "\n\t\t\t\t" + value;

								pirntInformationTemp = "\n\t\t\t\t" + value;
								// Log.d(TAG ,pirntInformationTemp);
							}
						}
						else
							if(mimetype.contains("/email"))
							{
								if(emailMark == true)
								{
									moreString += "\n邮箱\n\t\t\t\t" + value;

									emailMark = false;

									pirntInformationTemp = "\n邮箱\n\t\t\t\t" + value;
									// Log.d(TAG ,pirntInformationTemp);
								}
								else
								{
									moreString += "\n\t\t\t\t" + value;

									pirntInformationTemp = "\n\t\t\t\t" + value;
									// Log.d(TAG ,pirntInformationTemp);
								}

								if(importantString.isEmpty())
								{
									importantString += value;
								}
								else
								{
									importantString += "\n" + value;
								}
							}
							else
								if(mimetype.contains("/nickname"))
								{

									if(nicknameMark == true)
									{
										moreString += "\n昵称\n\t\t\t\t" + value;

										nicknameMark = false;

										pirntInformationTemp = "\n昵称\n\t\t\t\t" + value;
										// Log.d(TAG ,pirntInformationTemp);
									}
									else
									{
										moreString += "\n\t\t\t\t" + value;

										pirntInformationTemp = "\n\t\t\t\t" + value;
										// Log.d(TAG ,pirntInformationTemp);
									}

									if(importantString.isEmpty())
									{
										importantString += value;
									}
									else
									{
										importantString += "\n" + value;
									}
								}
								else
									if(mimetype.contains("/organization"))
									{
										if(organizationMark == true)
										{
											moreString += "\n工作单位\n\t\t\t\t" + value;

											organizationMark = false;

											pirntInformationTemp = "\n工作单位\n\t\t\t\t" + value;
											// Log.d(TAG ,pirntInformationTemp);
										}
										else
										{
											moreString += "\n\t\t\t\t" + value;

											pirntInformationTemp = "\n\t\t\t\t" + value;
											// Log.d(TAG ,pirntInformationTemp);
										}

										if(importantString.isEmpty())
										{
											importantString += value;
										}
										else
										{
											importantString += "\n" + value;
										}
									}
									else
										if(mimetype.contains("/postal"))
										{
											if(postalMark == true)
											{
												moreString += "\n住址\n\t\t\t\t" + value;

												postalMark = false;

												pirntInformationTemp = "\n住址\n\t\t\t\t" + value;
												// Log.d(TAG
												// ,pirntInformationTemp);
											}
											else
											{
												moreString += "\n\t\t\t\t" + value;

												pirntInformationTemp = "\n\t\t\t\t" + value;
												// Log.d(TAG
												// ,pirntInformationTemp);
											}

											if(importantString.isEmpty())
											{
												importantString += value;
											}
											else
											{
												importantString += "\n" + value;
											}
										}
										else
											if(mimetype.contains("/contact"))
											{
												if(contactMark == true)
												{
													moreString += "\n出生日期\n\t\t\t\t" + value;

													contactMark = false;

													pirntInformationTemp = "\n出生日期\n\t\t\t\t" + value;
													// Log.d(TAG
													// ,pirntInformationTemp);
												}
												else
												{
													moreString += "\n\t\t\t\t" + value;

													pirntInformationTemp = "\n\t\t\t\t" + value;
													// Log.d(TAG
													// ,pirntInformationTemp);
												}

												if(importantString.isEmpty())
												{
													importantString += value;
												}
												else
												{
													importantString += "\n" + value;
												}
											}
											else
												if(mimetype.contains("/note"))
												{
													if(noteMark == true)
													{
														moreString += "\n备注\n\t\t\t\t" + value;

														noteMark = false;

														pirntInformationTemp = "\n备注\n\t\t\t\t" + value;
														// Log.d(TAG
														// ,pirntInformationTemp);
													}
													else
													{
														moreString += "\n\t\t\t\t" + value;

														pirntInformationTemp = "\n\t\t\t\t" + value;
														// Log.d(TAG
														// ,pirntInformationTemp);
													}

													if(importantString.isEmpty())
													{
														importantString += value;
													}
													else
													{
														importantString += "\n" + value;
													}
												}
												else
													if(mimetype.contains("/website"))
													{
														if(websiteMark == true)
														{
															moreString += "\n网址\n\t\t\t\t" + value;

															websiteMark = false;

															pirntInformationTemp = "\n网址\n\t\t\t\t" + value;
															// Log.d(TAG
															// ,pirntInformationTemp);
														}
														else
														{
															moreString += "\n\t\t\t\t" + value;

															pirntInformationTemp = "\n\t\t\t\t" + value;
															// Log.d(TAG
															// ,pirntInformationTemp);
														}

														if(importantString.isEmpty())
														{
															importantString += value;
														}
														else
														{
															importantString += "\n" + value;
														}
													}
													else
														if(mimetype.contains("/relation"))
														{
															if(relationMark == true)
															{
																moreString += "\n亲属关系\n\t\t\t\t" + value;

																relationMark = false;

																pirntInformationTemp = "\n亲属关系\n\t\t\t\t" + value;
																// Log.d(TAG
																// ,pirntInformationTemp);
															}
															else
															{
																moreString += "\n\t\t\t\t" + value;

																pirntInformationTemp = "\n\t\t\t\t" + value;
																// Log.d(TAG
																// ,pirntInformationTemp);
															}

															if(importantString.isEmpty())
															{
																importantString += value;
															}
															else
															{
																importantString += "\n" + value;
															}
														}
														else
															if( !mimetype.contains("vnd.android.cursor.item/vnd.com.tencent.mobileqq.voicecall.profile"))
															{
																if(othersMark == true)
																{
																	moreString += "\nothers\n\t\t\t\t" + value;

																	othersMark = false;

																	pirntInformationTemp = "\nothers\n\t\t" + mimetype + "\n\t\t\t\t" + value;
																	// Log.d(TAG
																	// ,pirntInformationTemp);
																}
																else
																{
																	moreString += "\n\t\t" + mimetype + "\n\t\t" + value;

																	pirntInformationTemp = "\n\t\t" + mimetype + "\n\t\t\t\t" + value;
																	// Log.d(TAG
																	// ,pirntInformationTemp);
																}

																if(importantString.isEmpty())
																{
																	importantString += value;
																}
																else
																{
																	importantString += "\n" + value;
																}
															}

					printString = printString + pirntInformationTemp + "\n";

				}

			}

			if(importantString.trim() == "")
			{
				importantString = numbersString;
//				importantString = "NULL";
			}
			usersInformation.setOtherImportantInformation(importantString);
			usersInformation.setOtherInformation(moreString);
			addressList.add(usersInformation);

			pirntInformationTemp = "\n*********\n";
			printString = printString + pirntInformationTemp + "\n";
			// Log.d(TAG ,pirntInformationTemp);

			contactInfoCursor.close();
		}
		// printLog(FileName ,printString);
		cursor.close();
	}

	/**
	 * 打印log信息
	 * 
	 * @param fileName
	 * @param contents
	 */
	public void printLog(String fileName , String contents )
	{
		String rand = new Random(57).toString().substring(17);
		String time = new Date().toString();
		// MyUtil.showMsg(MainActivity.this ,time ,2);
		fileName = fileName + "_wgcwgc_" + rand + ".txt";
		if(fileName != null && fileName.length() > 0 && contents.length() > 0 && contents != null)
		{
			FileOperate fileOperate = new FileOperate(MainActivity.this);
			try
			{
				String path = fileOperate.writeToSdcard(fileName ,contents);

				String tips = "通讯录已于\n\n" + time + "\n\n保存至\n\n" + path + "\n\n\t\t\t\t\t\t\t\t\t\t恭喜 保存成功！";
				int lenth = tips.toString().length();
				int delayTime = lenth / 17 + 1;
				MyUtil.showMsg(MainActivity.this ,lenth + "\n" + delayTime + "\n" + tips ,delayTime);
			}
			catch(IOException e)
			{
				MyUtil.showMsg(MainActivity.this ,"文件保存失败！" ,1);
			}

		}
		else
		{
			MyUtil.showMsg(MainActivity.this ,"文件名或者内容为空！" ,1);
		}
	}

	/**
	 * 适配器
	 * 
	 * @author Administrator
	 */
	class MyBaseAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return addressList.size();
		}

		@Override
		public Object getItem(int position )
		{
			return addressList.get(position);
		}

		@Override
		public long getItemId(int position )
		{
			return position;
		}

		/**
		 * 
		 * 匹配到相应视图中 在getView()方法中,给convert
		 * View设置setTag()，可以将position或者view设置完成后进去，然后在on
		 * Click方法中通过getTag()获得所设置的值，就完成获取任务了。
		 */
		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position , View convertView , ViewGroup parent )
		{
			Holder holder;
			if(convertView == null)
			{
				getLayoutInflater();
				convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.user_list ,null);
				holder = new Holder();
				holder.userName = (TextView) convertView.findViewById(R.id.userName);
				holder.userPhoneNumber = (TextView) convertView.findViewById(R.id.userPhone);
				holder.otherInformation = (TextView) convertView.findViewById(R.id.other);

				convertView.setTag(holder);
			}
			else
			{
				holder = (Holder) convertView.getTag();
			}
			holder.userName.setText(addressList.get(position).getUserName());
			holder.userPhoneNumber.setText(addressList.get(position).getPhoneNumber());
			holder.otherInformation.setText(addressList.get(position).getOtherImportantInformation());

			// TextView textView = (TextView)
			// convertView.findViewById(R.id.userName);
			convertView.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View view )
				{
					getCallAndMessage(addressList.get(position).getUserName() ,addressList.get(position).getPhoneNumber());
				}
			});

			final ImageView imageView = (ImageView) convertView.findViewById(R.id.more);
			imageView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v )
				{
					int delay = getItem(position).toString().length();
					int delayTime = delay / 17 + 1;
					MyUtil.showMsg(MainActivity.this ,delay + "\n" + delayTime + "\n" + getItem(position).toString() ,delayTime);
				}
			});

			return convertView;
		}

		class Holder
		{
			TextView userName , userPhoneNumber , otherInformation;
		}

	}

}
