package com.peter.hitthelights;

import java.util.HashMap;
import java.util.Map;

import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.view.Menu;
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
				// Wifi
				turnOffWifi();
				// Sound
				turnOffSound();
				// Bluetooth
				turnOffBluetooth();
				// Set brightness
				turnDownBrightness();
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

	// Private helper method for onCheckedChanged
	// Turns down the brightness and saves the previous setting to the settings map
	private void turnDownBrightness() {
		int brightness = 0;
		try {
			brightness = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		// Hold current settings in Map<String, Boolean>
		settings.put("brightness", Integer.valueOf(brightness).toString());
		android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 0);
	}

	// Private helper method for onCheckedChanged
	// Turns off bluetooth and saves the previous setting to the settings map
	private void turnOffBluetooth() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// Hold current settings in Map<String, Boolean>
		settings.put("bluetooth", mBluetoothAdapter.isEnabled() + "");
		mBluetoothAdapter.disable();
	}

	// Private helper method for onCheckedChanged
	// Turns off the sound and saves the previous setting to the settings map
	// Only saves the setting if it was already silent
	@SuppressWarnings("static-access")
	private void turnOffSound() {
		AudioManager aManager=(AudioManager)getSystemService(AUDIO_SERVICE);
		// Hold current settings in Map<String, Boolean>
		settings.put("sound", Integer.valueOf(aManager.getRingerMode()).toString());
		if (aManager.getRingerMode() != 0) {
			aManager.setRingerMode(aManager.RINGER_MODE_SILENT);
		}
	}

	// Private helper method for onCheckedChanged
	// Turns off the wifi and saves the previous setting to the settings map
	private void turnOffWifi() {
		WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			// Hold current settings in Map<String, Boolean>
			settings.put("wifi", "on");
			// turn off wifi
			wifiManager.setWifiEnabled(false);
		}
	}

}
