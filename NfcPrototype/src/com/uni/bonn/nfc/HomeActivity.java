package com.uni.bonn.nfc;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.nfc.FormatException;
import android.os.Bundle;
import android.widget.VideoView;

import com.uni.bonn.nfc4mg.NFCEventManager;
import com.uni.bonn.nfc4mg.constants.TagConstants;
import com.uni.bonn.nfc4mg.exception.NfcTagException;
import com.uni.bonn.nfc4mg.nfctag.ParseTagListener;
import com.uni.bonn.nfc4mg.nfctag.TagHandler;

public class HomeActivity extends Activity implements ParseTagListener {

	private static final String TAG = "HomeActivity";

	private static VideoView mVideoView;

	private NFCEventManager mNFCEventManager = null;

	// Game parameters.
	private TagHandler mTagHamdler;
	private int mCurrentInteraction = 0;

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

		try {
			mTagHamdler = new TagHandler(this, this);
		} catch (NfcTagException e) {
			e.printStackTrace();
		}

		mVideoView = (VideoView) findViewById(R.id.video_view);

		String path = "android.resource://" + getPackageName() + "/"
				+ R.raw.one;
		setnPlayVideo(path);
	}

	private void setnPlayVideo(final String path) {
		mVideoView.setVideoURI(Uri.parse(path));
		mVideoView.start();
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (null != mNFCEventManager) {
			mNFCEventManager.removeNFCListener(HomeActivity.this);
		}

		/*
		 * if (null != mVideoView && mVideoView.isPlaying()) {
		 * mVideoView.pause(); }
		 */
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (null != mNFCEventManager) {
			mNFCEventManager.attachNFCListener(HomeActivity.this);
		}

		/*
		 * if (null != mVideoView) { mVideoView.start(); }
		 */
	}

	@Override
	protected void onNewIntent(Intent intent) {

		try {
			mTagHamdler.processIntent(intent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NfcTagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onStartParsing(String msg) {

	}

	@Override
	public void onParseComplete(int tagType) {

		switch (tagType) {

		case TagConstants.TAG_TYPE_INFO:

			String data = mTagHamdler.getmInfoTagModel().getData();
			handleInteraction(data);
			break;
		case TagConstants.TAG_TYPE_RESOURCE:

			String resName = mTagHamdler.getmResourceTagModel().getName();
			int resCount = mTagHamdler.getmResourceTagModel().getCount();
			handleResourceInteraction(resName, resCount);

			break;
		}
	}

	
	
	@Override
	protected Dialog onCreateDialog(int id) {

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		switch(id){
		
		case 1:
			alertBuilder.setTitle(R.string.alert_title);
			alertBuilder.setMessage(R.string.err_invalid_action);
			alertBuilder.setCancelable(true);
			alertBuilder.setPositiveButton(R.string.btn_ok, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss();

				}
			});
			break;
		case 2:

			alertBuilder.setMessage(R.string.ignite_candle);
			alertBuilder.setCancelable(true);
			alertBuilder.setPositiveButton(R.string.btn_yes, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss();
					// Set the interaction counter to next scene
					mCurrentInteraction = 4;
					String path = "android.resource://" + getPackageName() + "/"
							+ R.raw.five;
					setnPlayVideo(path);

				}
			}).setNegativeButton(R.string.btn_no, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.dismiss();
				}
			});

			
			break;
		}
		
		
		// create alert dialog
		AlertDialog alertDialog = alertBuilder.create();

		return alertDialog;
	}

	private void handleResourceInteraction(final String data, final int resCount) {

		if (mCurrentInteraction == 2 && "matchbox".equalsIgnoreCase(data)) {

			mCurrentInteraction = 3;
			if (0 != resCount) {
				String path = "android.resource://" + getPackageName() + "/"
						+ R.raw.four;
				setnPlayVideo(path);
			}
		}else if (mCurrentInteraction == 4 && "matchbox".equalsIgnoreCase(data)) {

			mCurrentInteraction = 5;
			if (0 != resCount) {
				String path = "android.resource://" + getPackageName() + "/"
						+ R.raw.six;
				setnPlayVideo(path);
			}
		}

	}

	private void handleInteraction(final String data) {

		if (mCurrentInteraction == 0 && "concert_ticket".equalsIgnoreCase(data)) {

			// Set the interaction counter to next scene
			mCurrentInteraction = 1;
			String path = "android.resource://" + getPackageName() + "/"
					+ R.raw.two;
			setnPlayVideo(path);

		} else if (mCurrentInteraction == 1
				&& "milchschnitte".equalsIgnoreCase(data)) {

			// Set the interaction counter to next scene
			mCurrentInteraction = 2;
			String path = "android.resource://" + getPackageName() + "/"
					+ R.raw.three;
			setnPlayVideo(path);
			
		} else if (mCurrentInteraction == 3
				&& "candle".equalsIgnoreCase(data)) {

			showDialog(2);
			
		}else if (mCurrentInteraction == 5
				&& "key".equalsIgnoreCase(data)) {
			
			mCurrentInteraction = 6;
			String path = "android.resource://" + getPackageName() + "/"
					+ R.raw.seven;
			setnPlayVideo(path);
			
		}else if (mCurrentInteraction == 6
				&& "maps".equalsIgnoreCase(data)) {
			
			mCurrentInteraction = 7;
			String path = "android.resource://" + getPackageName() + "/"
					+ R.raw.one;
			setnPlayVideo(path);
			
		}else if (mCurrentInteraction == 7
				&& "bike_key".equalsIgnoreCase(data)) {
			
			mCurrentInteraction = 8;
			String path = "android.resource://" + getPackageName() + "/"
					+ R.raw.two;
			setnPlayVideo(path);
			
		}else if (mCurrentInteraction == 8
				&& "number_lock".equalsIgnoreCase(data)) {
			
			mCurrentInteraction = 9;
			String path = "android.resource://" + getPackageName() + "/"
					+ R.raw.three;
			setnPlayVideo(path);
			
		} else {

			showDialog(1);
		}
	}

}
