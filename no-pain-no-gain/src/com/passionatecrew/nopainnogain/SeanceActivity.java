package com.passionatecrew.nopainnogain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SeanceActivity extends Activity {
	
	 private List<String> listTypes;
	 private HashMap<String, List<String>> listExoTypes;
	 private EntrainementDbHelper bdd;
	 private int nbCheck;
	 private View header;
	 private List<String> listExoSelect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seance);
		
		this.nbCheck = 0;
		this.listExoSelect = new ArrayList<String>();
		
		//Recuperation de la base de donn�e SQLIte
		this.bdd = new EntrainementDbHelper(this);
		
		Resources r = this.getResources();
				
		//Nommer les types de s�ance de l'expendable list
		ExpandableListView liste = (ExpandableListView)this.findViewById(R.id.expandableListView1);
	    this.listTypes = new ArrayList<String>();
        this.listExoTypes = new HashMap<String, List<String>>();
		this.prepareListData();
		ExpandableListAdapter liste2 = new ExpandableListAdapter(this, this.listTypes, this.listExoTypes);
	    
		if(liste.getParent() instanceof LinearLayout) {
			//Ajout le header a la liste
			LayoutInflater inflater = getLayoutInflater();
			this.header = inflater.inflate(R.layout.header_list_view, liste, false);
			liste.addHeaderView(header, null, false);
		}
	    
	    //Nommer les boutons radios
  		RadioGroup rGroup = (RadioGroup)this.findViewById(R.id.radioGroupObj);
  		String[] tabRButton = r.getStringArray(R.array.nomsObjectifs);
  		for(int i = 0; i < 3; i++) {
  			RadioButton rButton = (RadioButton)rGroup.getChildAt(i);
  			rButton.setText(tabRButton[i]);
  		}
  		
	    liste.setAdapter(liste2);
	}
	
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		recreate();
	}
	
	public void prepareListData() {
		this.listTypes.add("Jambes");
		this.listTypes.add("Bras");
		this.listTypes.add("Epaules");
		this.listTypes.add("Dos");
		this.listTypes.add("Torse");
	 
		//Recupere les donn�es dans le mode ecriture
		SQLiteDatabase dbr = this.bdd.getReadableDatabase();
		
		//Recupere tous les nom d'exercices de type = "jambes"
	    Cursor curs1 = dbr.rawQuery("select distinct nom from Exercices where type ='Jambes'", null);
		List<String> jambes = new ArrayList<String>();
	    if(curs1.moveToFirst()) {
	    	do{
	    		jambes.add(curs1.getString(0));
	    	} while(curs1.moveToNext());
	    }
	    curs1.close();

	  //Recupere tous les nom d'exercices de type = "bras"
	    Cursor curs2 = dbr.rawQuery("select distinct nom from Exercices where type ='Bras'", null);
		List<String> bras = new ArrayList<String>();
	    if(curs2.moveToFirst()) {
	    	do{
	    		bras.add(curs2.getString(0));
	    	} while(curs2.moveToNext());
	    }
	    curs2.close();
	    
	  //Recupere tous les nom d'exercices de type = "epaules"
	    Cursor curs3 = dbr.rawQuery("select distinct nom from Exercices where type ='Epaules'", null);
		List<String> epaules = new ArrayList<String>();
	    if(curs3.moveToFirst()) {
	    	do{
	    		epaules.add(curs3.getString(0));
	    	} while(curs3.moveToNext());
	    }
	    curs3.close();
	    
	  //Recupere tous les nom d'exercices de type = "dos"
	    Cursor curs4 = dbr.rawQuery("select distinct nom from Exercices where type ='Dos'", null);
		List<String> dos = new ArrayList<String>();
	    if(curs4.moveToFirst()) {
	    	do{
	    		dos.add(curs4.getString(0));
	    	} while(curs4.moveToNext());
	    }
	    curs4.close();
	    
	  //Recupere tous les nom d'exercices de type = "torse"
	    Cursor curs5 = dbr.rawQuery("select distinct nom from Exercices where type ='Torse'", null);
		List<String> torse = new ArrayList<String>();
	    if(curs5.moveToFirst()) {
	    	do{
	    		torse.add(curs5.getString(0));
	    	} while(curs5.moveToNext());
	    }
	    curs5.close();
	    	 
	    this.listExoTypes.put(this.listTypes.get(0), jambes);
	    this.listExoTypes.put(this.listTypes.get(1), bras);
	    this.listExoTypes.put(this.listTypes.get(2), epaules);
	    this.listExoTypes.put(this.listTypes.get(3), dos);
	    this.listExoTypes.put(this.listTypes.get(4), torse);
	}
	
	public void maMethode(View v) {
		CheckBox c = (CheckBox) v;
		if(c.isChecked()) {
			this.nbCheck++;
		} else {
			this.nbCheck--;
		}
		this.MyHandler((View)v.getParent());
	}
	
	public void MyHandler(View v) {
		RelativeLayout rl = (RelativeLayout) v;
		TextView t = (TextView) rl.getChildAt(0);
		if(this.listExoSelect.contains(t.getText().toString())) {
			this.listExoSelect.remove(t.getText().toString());
		} else {
			this.listExoSelect.add(t.getText().toString());
		}
		this.btnDemarrerActif(v);
	}
	
	public void btnDemarrerActif(View v) {
		Button demarrer = (Button) this.findViewById(R.id.btnDemarrer);
		EditText nom = (EditText) this.findViewById(R.id.EditTextNom);
		RadioGroup btnRadios = (RadioGroup) this.findViewById(R.id.radioGroupObj);
		Integer radioButtonId = btnRadios.getCheckedRadioButtonId();
		if(this.nbCheck > 0 && !nom.getText().toString().equals("") && radioButtonId != null) {
			demarrer.setEnabled(true);
		} else {
			demarrer.setEnabled(false);
		}
	}
	
	public void clickDemarrer(View v) {
		//Recuperation des composants
		EditText nom = (EditText) this.findViewById(R.id.EditTextNom);
		RadioGroup radioGrp = (RadioGroup) this.findViewById(R.id.radioGroupObj);
		Integer radioButtonId = radioGrp.getCheckedRadioButtonId();
		RadioButton boutonSelec = (RadioButton)radioGrp.findViewById(radioButtonId);
		
		//Ajout a la base de donn�e la s�ance
		SQLiteDatabase db = this.bdd.getWritableDatabase();
		db.execSQL("insert into Seances(nom, objectif) values('"+nom.getText().toString()+"','"+boutonSelec.getText().toString()+"');");
		
		//Recupere l'id de la seance
		Cursor cursIdSeance = db.rawQuery("select idSeance from Seances where nom ='"+nom.getText().toString()+"' and objectif ='"+boutonSelec.getText().toString()+"'", null);
		String idSeance = "";
		cursIdSeance.moveToFirst();
		idSeance = cursIdSeance.getString(cursIdSeance.getColumnIndexOrThrow("idSeance"));
		cursIdSeance.close();
		
		//Ajout a la base de donn�e des liens s�ances-exercices
		for(int i = 0; i < this.listExoSelect.size(); i++) {
			Cursor cursIdEx = db.rawQuery("select idExercice from Exercices where nom='"+this.listExoSelect.get(i).toString()+"';",null);
			cursIdEx.moveToFirst();
			db.execSQL("insert into Lien values('"+idSeance+"','"+cursIdEx.getString(0)+"');");
		}
		int repet = 0;
		int serie = 0;
		//Initialisation de la repetition et serie
		switch(radioButtonId) {
			case R.id.radioButtonPuissance :
				repet = 10;	
				serie = 3;
			break;
			case R.id.radioButtonMasse :
				repet = 12;	
				serie = 4;
			break;
			case R.id.radioButtonRenforcement :
				repet = 20;	
				serie = 6;
			break;
		}
		
		//Initialise la prochaine activity
		Intent intent = new Intent(SeanceActivity.this,EntrainementActivity.class);
		intent.putExtra("repet", String.valueOf(repet));	
		intent.putExtra("serie", String.valueOf(serie));
		intent.putExtra("idSeance", idSeance);
		intent.putExtra("nbr_exo", String.valueOf(this.listExoSelect.size()));
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.seance, menu);
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
