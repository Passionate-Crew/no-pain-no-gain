package com.passionatecrew.nopainnogain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TelephonyManager telephone = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		telephone.listen(new EcouteurEtatTelephone(this), PhoneStateListener.LISTEN_CALL_STATE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void ouvrirAPropos(View v){
		Intent intent = new Intent(MainActivity.this,AproposActivity.class);
		startActivity(intent);
	}
	
	public void ouvrirParamSeance(View v){
		Intent intent = new Intent(MainActivity.this,SeanceActivity.class);
		startActivity(intent);
	}
	
	public void ouvrirSeanceExistante(View v) {
		Intent intent = new Intent(MainActivity.this,SeancesExistantesActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
