package com.androiderstack.smartcontacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.androiderstack.custom_view.MyActionbar;
import com.androiderstack.prefs.AppSharedPrefs;
import com.kyleduo.switchbutton.SwitchButton;

@SuppressWarnings("deprecation")
public class SettingActivity extends AppCompatActivity implements OnCheckedChangeListener, View.OnClickListener{

	SwitchButton appEnable;

	MyActionbar myActionbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setting_layout);

		initializeViews();
		addActionBar();

		setUI();
		
	}

	private void initializeViews()
    {
		appEnable = (SwitchButton) findViewById(R.id.setting_enable);

        appEnable.setOnCheckedChangeListener(this);
	}


	private void addActionBar() {

		try
		{
			myActionbar = new MyActionbar(SettingActivity.this);
			myActionbar.setClickListener(this);
			myActionbar.setActionbar("Settings", true, false);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void setUI()
	{
        try
        {
            appEnable.setChecked(AppSharedPrefs.getInstance().isAppEnable());
            appEnable.setBackColor(getResources().getColorStateList(AppSharedPrefs.getInstance().isAppEnable() ? R.color.blue_color : android.R.color.darker_gray));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
    {
		switch (compoundButton.getId())
		{
			case R.id.setting_enable:
				AppSharedPrefs.getInstance().setAppEnable(isChecked);
                appEnable.setBackColor(getResources().getColorStateList(AppSharedPrefs.getInstance().isAppEnable() ? R.color.blue_color : android.R.color.darker_gray));
				break;
		}
	}

	@Override
	public void onClick(View view) {

		switch (view.getId())
		{
			case R.id.actionbarLayout_backButton:
				onBackPressed();
				break;
		}
	}
}
