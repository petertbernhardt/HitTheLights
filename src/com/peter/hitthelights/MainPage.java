package com.peter.hitthelights;

import java.util.HashMap;
import java.util.Map;

import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainPage extends Activity implements OnCheckedChangeListener {

	// Fields
	private ToggleButton master, sound;
	private Map<String, String> settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		setUpButtons();
		settings = new HashMap<String, String>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_page, menu);
		return true;
	}

	// Private helper method for onCreate that handles setting up
	// the buttons.
	private void setUpButtons() {
		master = (ToggleButton) findViewById(R.id.master);
		sound = (ToggleButton) findViewById(R.id.sound);
		master.setOnCheckedChangeListener(this);
		sound.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton button, boolean state) {
		if (button.equals(master)) {
			if (state) {
				// Before turning off settings, remember what the user had turned on and off,
				// so when turning stuff back on, the app doesn't turn on things that
				// weren't originally on. EX: setting bluetooth on when it was originally off.
				/* TURN OFF:
				*	WIFI - done
				*	GPS - ?
				*	SOUND - done
				*	BLUETOOTH
				*  SET BRIGHTNESS TO 0
				*/
				// Hold current settings in Map<String, Boolean>
				// Wifi
				WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
				if (wifiManager.isWifiEnabled()) {
					// put in settings
					settings.put("wifi", "on");
					// turn off wifi
					wifiManager.setWifiEnabled(false);
				}
				// Sound
				AudioManager aManager=(AudioManager)getSystemService(AUDIO_SERVICE);
				settings.put("sound", Integer.valueOf(aManager.getRingerMode()).toString());
				aManager.setRingerMode(aManager.RINGER_MODE_SILENT);
			} else {
				// go through the settings map and turn everything that was on back on
				Toast.makeText(getApplicationContext(), "blahhhhh", Toast.LENGTH_LONG).show();
				for (String setting : settings.keySet()) {
					
				}
			}
		} else if (button.equals(sound)) {
			if (state) {
				// Remember previous setting so when resetting the volume, go back to the previous
				// level
				// TURN OFF SOUND
			} else {
				// turn the sound back on
				
			}
		}
	}

}
