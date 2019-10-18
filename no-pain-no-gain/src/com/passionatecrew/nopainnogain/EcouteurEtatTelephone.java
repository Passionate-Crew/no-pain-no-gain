package com.passionatecrew.nopainnogain;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class EcouteurEtatTelephone extends PhoneStateListener {

	private Context ctx;
	
	public EcouteurEtatTelephone(Context ctx) {
		this.ctx = ctx;
	}
	
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		if(state == TelephonyManager.CALL_STATE_RINGING) {
			Toast.makeText(this.ctx, "Appel du n�"+incomingNumber, Toast.LENGTH_SHORT).show();
		}
	}
}
