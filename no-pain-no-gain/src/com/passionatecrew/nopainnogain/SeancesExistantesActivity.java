package com.passionatecrew.nopainnogain;


import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SeancesExistantesActivity extends Activity {

	private EntrainementDbHelper bdd;
	private AdapteurListSeance adapteur;
	private HashMap<String, String> seance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seances_existantes);
		
		this.seance = new HashMap<String, String>();
		
		ListView l = (ListView) this.findViewById(R.id.listView1);
		this.bdd = new EntrainementDbHelper(this);
		
		SQLiteDatabase dbr = this.bdd.getReadableDatabase();
		
		String[] col = {"nom", "objectif"};
		String[] select={};
		Cursor curs = null;
		curs = dbr.query("Seances", col, "", select, null, null, "idSeance ASC");
		
		if(curs.moveToFirst()) {
			do{
				this.seance.put(curs.getString(curs.getColumnIndexOrThrow("nom")), curs.getString(curs.getColumnIndexOrThrow("objectif")));
			} while(curs.moveToNext());
		}
		curs.close();
		this.adapteur = new AdapteurListSeance(this, this.seance);
		
		l.setAdapter(this.adapteur);
		
		curs.close();
		dbr.close();
		
	}
	
	public void clickSupprimer(View v) {
		RelativeLayout r = (RelativeLayout) v.getParent();
		TextView titre = (TextView) r.getChildAt(1);
		
		SQLiteDatabase dbw = this.bdd.getWritableDatabase();
		//Recupere l'id de la seance
		Cursor curs1 = dbw.rawQuery("select idSeance from Seances where nom ='"+titre.getText().toString()+"';", null);
		curs1.moveToFirst();
		String idSeance = curs1.getString(curs1.getColumnIndexOrThrow("idSeance"));
		//Supprime de la table Lien
		dbw.execSQL("delete from Lien where idSean = '"+idSeance+"';");
		//Supprime de la table Seance
		dbw.execSQL("delete from Seances where nom = '"+titre.getText().toString()+"';");
		
		//Met a jour la listView
		this.seance.remove(titre.getText().toString());
		this.adapteur.notifyDataSetChanged();
		
		Toast.makeText(this, titre.getText().toString()+" a �t� supprim�", Toast.LENGTH_SHORT).show();
	}
	
	public void clickRecommencer(View v) {
		RelativeLayout r = (RelativeLayout) v;
		TextView titre = (TextView) r.getChildAt(1);
		
		SQLiteDatabase dbw = this.bdd.getWritableDatabase();
		//Recupere l'id de la seance
		Cursor curs1 = dbw.rawQuery("select idSeance, objectif from Seances where nom ='"+titre.getText().toString()+"';", null);
		curs1.moveToFirst();
		
		//Recupere les repetitions et les series de la s�ance
		int repet = 0;
		int serie = 0;
		if(curs1.getString(curs1.getColumnIndexOrThrow("objectif")).equals("Puissance")) {
			repet = 10;	
			serie = 3;
		} else if(curs1.getString(curs1.getColumnIndexOrThrow("objectif")).equals("Masse")) {
			repet = 12;	
			serie = 4;
		} else {
			repet = 20;	
			serie = 6;
		}
		
		//Recupere le nombre d'exercices de la seance
		Cursor cursNbr = dbw.rawQuery("select count(*) from Lien where idSean ='"+curs1.getString(curs1.getColumnIndexOrThrow("idSeance"))+"';", null);
		cursNbr.moveToNext();
		
		//Initialise la prochaine activity
		Intent intent = new Intent(SeancesExistantesActivity.this,EntrainementActivity.class);
		intent.putExtra("repet", String.valueOf(repet));	
		intent.putExtra("serie", String.valueOf(serie));
		intent.putExtra("idSeance", curs1.getString(curs1.getColumnIndexOrThrow("idSeance")));
		intent.putExtra("nbr_exo", cursNbr.getString(0));
		startActivity(intent);
		
		curs1.close();
		cursNbr.close();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.seances_existantes, menu);
		return true;
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
