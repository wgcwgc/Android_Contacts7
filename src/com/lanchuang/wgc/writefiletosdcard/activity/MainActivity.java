package com.lanchuang.wgc.writefiletosdcard.activity;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lanchuang.wgc.android_contacts7.R;
import com.lanchuang.wgc.writefiletosdcard.service.FileOperate;
import com.lanchuang.wgc.writefiletosdcard.util.MyUtil;

public class MainActivity extends Activity
{
	private EditText fileName;
	private EditText fileContent;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);// 关联布局文件
		// 实例化控件信息
		initWidget();

		// 按钮的点击事件监听器
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v )
			{
				// 获取用户的输入
				String name = fileName.getText().toString().trim();
				String content = fileContent.getText().toString().trim();
				printLog(name ,content);
			}
		});
	}

	public void printLog(String fileName , String contents )
	{
		if(fileName != null && fileName.length() > 0 && contents.length() > 0 && contents != null)
		{
			FileOperate fileOperate = new FileOperate(MainActivity.this);
			try
			{
				fileOperate.writeToSdcard(fileName ,contents);
				// MyUtil.showMsg(MainActivity.this
				// ,getString(R.string.success));

				MyUtil.showMsg(MainActivity.this ,"文件保存成功！" ,1);
			}
			catch(IOException e)
			{
				// MyUtil.showMsg(MainActivity.this
				// ,getString(R.string.failed));
				MyUtil.showMsg(MainActivity.this ,"文件保存失败！" ,1);
			}

		}
		else
		{
			// MyUtil.showMsg(MainActivity.this ,getString(R.string.file_null));
			MyUtil.showMsg(MainActivity.this ,"文件名或者内容为空！" ,1);
		}
	}

	/**
	 * 实例化控件信息
	 */
	private void initWidget()
	{
		// fileName = (EditText) findViewById(R.id.file_name);
		// fileContent = (EditText) findViewById(R.id.file_content);
		// button = (Button) findViewById(R.id.button);
	}
}
