package com.peter.hitthelights;

import java.util.HashMap;
import java.util.Map;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainPage extends Activity implements OnClickListener {

	// Fields
	private Button master, sound;
	private Map<String, Boolean> settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		setUpButtons();
		settings = new HashMap<String, Boolean>();
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
		master = (Button) findViewById(R.id.master);
		sound = (Button) findViewById(R.id.sound);
		master.setOnClickListener(this);
		sound.setOnClickListener(this);
	}

	@Override
	public void onClick(View button) {
		if (button.equals(master)) {
			// Before turning off settings, remember what the user had turned on and off,
			// so when turning stuff back on, the app doesn't turn on things that
			// weren't originally on. EX: setting bluetooth on when it was originally off.
			/* TURN OFF:
			*	WIFI
			*	GPS
			*	SOUND
			*	BLUETOOTH
			*  SET BRIGHTNESS TO 0
			*/
			// Hold current settings in Map<String, Boolean>
			// Wifi
			WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
			if (wifiManager.isWifiEnabled()) {
				// put in settings
				settings.put("wifi", true);
				// turn off wifi
				wifiManager.setWifiEnabled(false);
			}
			
			// After changing settings, change button text to say:
			// Turn on Everything!
		} else if (button.equals(sound)) {
			// Remember previous setting so when resetting the volume, go back to the previous
			// level
			// TURN OFF SOUND
			// After changing settings, change button text to say:
			// Yes sound!
		}
	}

}
