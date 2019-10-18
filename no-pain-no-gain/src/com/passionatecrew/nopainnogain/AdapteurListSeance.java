package com.passionatecrew.nopainnogain;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapteurListSeance extends BaseAdapter {

	private Context context;
	private Map<String, String> seance;
	
	private static LayoutInflater inflater = null;
	
	public AdapteurListSeance(Context ctx, HashMap<String, String> a) {
		this.seance = a;
		this.context = ctx;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return this.seance.size();
	}

	@Override
	public Object getItem(int position) {
		return this.seance.values().toArray(new String[this.seance.size()])[position];
		//return this.valeurs[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View previousView, ViewGroup parent) {
		View previous = previousView;
		
		if(previous == null) {
			previous = inflater.inflate(R.layout.list_seance, null);
		}
		TextView text = (TextView) previous.findViewById(R.id.titreLigne);
		text.setText(this.seance.keySet().toArray(new String[this.seance.size()])[position]);
		
		TextView text2 = (TextView) previous.findViewById(R.id.valeurLigne);
		text2.setText(this.seance.values().toArray(new String[this.seance.size()])[position]);
		
		return previous;
	}

	public void supprimerSeance(String nom) {
		this.seance.remove(nom);
        this.notifyDataSetChanged();
    }

}
