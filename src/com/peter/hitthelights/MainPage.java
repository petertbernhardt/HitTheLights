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
				turnOffWifi();
				turnOffSound();
				turnOffBluetooth();
				turnDownBrightness();
			} else {
				// go through the settings map and turn everything that was on back on
				for (String setting : settings.keySet()) {
					if (setting.equals("brightness")) {
						int brightness = Integer.valueOf(settings.get(setting));
						try {
							setBrightness(brightness);
						} catch (SettingNotFoundException e) {
							e.printStackTrace();
						}
					} else if (setting.equals("bluetooth")) {
						boolean btState = Boolean.valueOf(settings.get(setting));
						if (btState) {
							turnOnBluetooth();
						}
					} else if (setting.equals("sound")) {
						int soundLevel = Integer.valueOf(settings.get(setting));
						if (soundLevel != 0) {
							setSound(soundLevel);
						}
					} else if (setting.equals("wifi")) {
						String wifiState = settings.get(setting);
						if (wifiState.equals("on")) {
							turnOnWifi();
						}
					}
				}
			}
		} else if (button.equals(sound)) {
			if (state) {
				turnOffSound();
			} else {
				int soundLevel = Integer.valueOf(settings.get("sound"));
				if (soundLevel != 0) {
					setSound(soundLevel);
				}
			}
		}
	}

	// Turns on WiFi
	private void turnOnWifi() {
		WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
	}

	// Set the sound level to the previous value
	private void setSound(int soundLevel) {
		AudioManager aManager=(AudioManager)getSystemService(AUDIO_SERVICE);
		aManager.setRingerMode(soundLevel);
	}

	// Turns on bluetooth
	private void turnOnBluetooth() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mBluetoothAdapter.enable();
	}

	// Sets the screen's brightness to the given value
	private void setBrightness(int brightness) throws SettingNotFoundException {
		android.provider.Settings.System.putInt(getContentResolver(), 
				android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
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
		// Hold current settings in Map<String, String>
		settings.put("brightness", Integer.valueOf(brightness).toString());
		android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 0);
	}

	// Private helper method for onCheckedChanged
	// Turns off bluetooth and saves the previous setting to the settings map
	private void turnOffBluetooth() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// Hold current settings in Map<String, String>
		settings.put("bluetooth", mBluetoothAdapter.isEnabled() + "");
		mBluetoothAdapter.disable();
	}

	// Private helper method for onCheckedChanged
	// Turns off the sound and saves the previous setting to the settings map
	// Only saves the setting if it was already silent
	@SuppressWarnings("static-access")
	private void turnOffSound() {
		AudioManager aManager=(AudioManager)getSystemService(AUDIO_SERVICE);
		// Hold current settings in Map<String, String>
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
			// Hold current settings in Map<String, String>
			settings.put("wifi", "on");
			// turn off wifi
			wifiManager.setWifiEnabled(false);
		}
	}

}
