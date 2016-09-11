package com.unioncloud.newpay.presentation.ui.welcome;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.SingleFragmentActivity;

public class WelcomeActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createContentFragment() {
		return WelcomeFragment.newInstance();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hideToolbar();
	}
}
