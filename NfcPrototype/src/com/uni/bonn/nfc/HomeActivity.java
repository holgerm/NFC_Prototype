package com.uni.bonn.nfc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.VideoView;

import com.uni.bonn.nfc4mg.NFCEventManager;

public class HomeActivity extends Activity {

	private static final String TAG = "HomeActivity";

	private static VideoView mVideoView;
	private String mVideoPath = "";

	private NFCEventManager mNFCEventManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_layout);

		try {
			mNFCEventManager = NFCEventManager.getInstance(this);
			mNFCEventManager.initialize(this, HomeActivity.this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mVideoView = (VideoView) findViewById(R.id.video_view);

		mVideoPath = "android.resource://" + getPackageName() + "/" + R.raw.one;
		mVideoView.setVideoURI(Uri.parse(mVideoPath));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (null != mNFCEventManager) {
			mNFCEventManager.removeNFCListener(HomeActivity.this);
		}

		if (null != mVideoView && mVideoView.isPlaying()) {
			mVideoView.pause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (null != mNFCEventManager) {
			mNFCEventManager.attachNFCListener(HomeActivity.this);
		}

		if (null != mVideoView) {
			mVideoView.start();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {

		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {

			Log.v(TAG, "Intent Action :: ACTION_TAG_DISCOVERED");
		}
	}
}
