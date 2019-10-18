package com.passionatecrew.nopainnogain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class PauseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pause);
		Horloge h = new Horloge();
		h.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.entrainement, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_quitter:
				Intent returnIntent = new Intent();
    			setResult(RESULT_CANCELED, returnIntent);
    			finish();
				return true;
			case R.id.menu_apropos:
				Intent intent2 = new Intent(PauseActivity.this,AproposActivity.class);
				startActivity(intent2);
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}
	
	private class Horloge extends Thread {
		public void run() {
			EditText t = (EditText) findViewById(R.id.editTextRepPause);
			while(!t.getText().toString().equals("1")) {
				Thread.yield();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//
				PauseActivity.this.runOnUiThread(new Runnable() {
		            public void run() {
		            	EditText champDuree = (EditText) findViewById(R.id.editTextRepPause);
						Integer tps_restant = Integer.valueOf(champDuree.getText().toString());
						champDuree.setText(String.valueOf(tps_restant-1));
		            }
				});
			}
			PauseActivity.this.runOnUiThread(new Runnable() {
	            public void run() {
	    			Intent returnIntent = new Intent();
	    			returnIntent.putExtra("result", "Pause termin�e");
	    			setResult(RESULT_OK, returnIntent);
	    			finish();
	            }
			});
		}
		
	}
}
