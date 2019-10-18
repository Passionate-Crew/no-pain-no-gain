package com.passionatecrew.nopainnogain;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EntrainementActivity extends Activity {
	
	private EntrainementDbHelper bdd;
	private Cursor curs1;
	private Integer dureeInitiale;
	private Integer nbrExoInitiale;
	private Integer nbrRepetitionsInitiale;
	private Integer nbrSerieInitiale;
	
	private Horloge threadHorloge;
	private Gestionnaire threadGestionnaire;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entrainement);
		//Cr�er la base
		this.bdd = new EntrainementDbHelper(this);
		
		//Toast de demarrage
		Toast.makeText(this, "D�marrage de la s�ance", Toast.LENGTH_LONG).show();
		
		//Recupere les champs a modifi�
		EditText rep = (EditText) findViewById(R.id.editTextRep);
		EditText ser = (EditText) findViewById(R.id.editTextSerie);
		TextView nomExo = (TextView) findViewById(R.id.typeExo);
		TextView dureeTexte = (TextView) findViewById(R.id.duree);
		ImageView image = (ImageView)this.findViewById(R.id.imageView1);
		
		//Recupere les valeurs pass�e en parametre
		String idSeance = getIntent().getStringExtra("idSeance");
		this.nbrRepetitionsInitiale = Integer.valueOf(getIntent().getStringExtra("repet"));
		this.nbrSerieInitiale = Integer.valueOf(getIntent().getStringExtra("serie"));
		
		//Modifie le texte avec les valeurs recup�r�es
		rep.setText(getIntent().getStringExtra("repet"));
		ser.setText(getIntent().getStringExtra("serie"));
		
		//Modification des champs pour le 1er exercice
		SQLiteDatabase dbr = this.bdd.getReadableDatabase();
		this.curs1 = dbr.rawQuery("select idExo from Lien where idSean="+idSeance, null);
	    this.curs1.moveToFirst();
	    Cursor curs2 = dbr.rawQuery("select image, nom from Exercices where idExercice="+this.curs1.getInt(0), null);
	    curs2.moveToFirst();
	    image.setImageDrawable(this.getResources().getDrawable(Integer.parseInt(curs2.getString(0))));
	    nomExo.setText(curs2.getString(1));
	    curs2.close();
	    
	    //Recupere les valeurs pass� en parametre et dans les champs
	    this.nbrExoInitiale = Integer.valueOf(getIntent().getStringExtra("nbr_exo"));
	    Integer nb_serie = Integer.valueOf(ser.getText().toString());
	    Integer nb_rep = Integer.valueOf(rep.getText().toString());

	    //Calcule le temps total de la s�ance (en secondes)
	    Integer secondeTotal = (((2 * nb_rep * nb_serie) + (30 * (nb_serie-1))) * this.nbrExoInitiale + 8 + (30 * (this.nbrExoInitiale-1)));
	    
	    //Initialise les variables pour le champ temps pour le decrementer
	    Integer minute = secondeTotal/60;
	    Integer secondeRest = secondeTotal - (minute*60);
	    
	    //initialise la duree initiale
	    this.dureeInitiale = ((2 * nb_rep ) * nb_serie * this.nbrExoInitiale + ((nb_serie-1) * 30) + 5);
	    
	    //Mise a jour du champ
		String [] heure = EntrainementActivity.this.mettreEnForme(minute.toString(), secondeRest.toString());
		dureeTexte.setText(heure[0]+":"+heure[1]);
	    
	    //Decrement de 1 le nbr d'exercice a faire
		this.nbrExoInitiale--;
	    
	    //Thread qui gere le temps et la decrementation des repetitions
		this.threadHorloge = new Horloge();
		this.threadHorloge.start();
		
		//Thread qui gere la gestion des fenetres
		this.threadGestionnaire = new Gestionnaire();
		this.threadGestionnaire.start();
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
				this.threadHorloge.interrupt();
				finish();
				return true;
			case R.id.menu_apropos:
				Intent intent2 = new Intent(EntrainementActivity.this,AproposActivity.class);
				startActivity(intent2);
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1) {
			if(resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");
				Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show();
			}
			if(resultCode == RESULT_CANCELED) {
				this.threadHorloge.interrupt();
				finish();
			}
			//Recupere le champ serie et le decremente
			EditText ser = (EditText) findViewById(R.id.editTextSerie);
			ser.setText(String.valueOf(Integer.valueOf(ser.getText().toString())-1));
			//Si il n'y a plus de serie
			if(this.nbrExoInitiale > 0 && ser.getText().toString().equals("0")) {
				//Recuperation du prochain exercice
					SQLiteDatabase dbr = this.bdd.getReadableDatabase();
					EntrainementActivity.this.nbrExoInitiale--;
				    this.curs1.moveToNext();
				    Cursor curs2 = dbr.rawQuery("select image, nom from Exercices where idExercice="+curs1.getInt(0), null);
				    curs2.moveToFirst();
			    //Modification de l'image
					ImageView image = (ImageView)this.findViewById(R.id.imageView1);
				    image.setImageDrawable(this.getResources().getDrawable(Integer.parseInt(curs2.getString(0))));
			    //Modification du champ type exo
				    TextView nomExo = (TextView) findViewById(R.id.typeExo);
				    nomExo.setText(curs2.getString(1));
				    curs2.close();
			    //Modification du champ serie
				    ser.setText(EntrainementActivity.this.nbrSerieInitiale.toString());
			    //Modification du champ repetition
				    EditText rep = (EditText) findViewById(R.id.editTextRep);
				    rep.setText(EntrainementActivity.this.nbrRepetitionsInitiale.toString());
			} else {
			    //Modification du champ repetition
				    EditText rep = (EditText) findViewById(R.id.editTextRep);
				    rep.setText(EntrainementActivity.this.nbrRepetitionsInitiale.toString());
			}
			//Relance un thread pour decrementer les repetitions
				this.threadGestionnaire = new Gestionnaire();
				this.threadGestionnaire.start();
		}
	}
	
	private class Horloge extends Thread {
		public void run() {
			TextView t = (TextView) findViewById(R.id.duree);
			while(!t.getText().toString().equals("00:01")) {
				//Methode qui permet d'eviter la famine sous Windows
				Thread.yield();
				//Met en pause le thread pour simuler une seconde
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
				//
				EntrainementActivity.this.runOnUiThread(new Runnable() {
		            public void run() {
		            	//Recuperation des champs
		            	TextView champDuree = (TextView) findViewById(R.id.duree);
		            	TextView champRep = (TextView) findViewById(R.id.editTextRep);
		            	//Recuperation des minutes et des secondes
		        		String [] duree = champDuree.getText().toString().split(":");
						Integer minute = Integer.valueOf(duree[0]);
						Integer seconde = Integer.valueOf(duree[1]);
						Integer nb_rep = Integer.valueOf(champRep.getText().toString());
						//Si 2 secondes sont pass�, on decremente les repetitions (1 repetition = 2 sec)
		            	if(nb_rep > 0 && (EntrainementActivity.this.dureeInitiale - (minute*60)+seconde)%2 == 0 ) {
		            		champRep.setText(String.valueOf(nb_rep-1));
		            	}
		            	//Mise a jour des minutes quand il n'y a plus de secondes
						if(seconde <= 0) {
							minute--;
							seconde = 59;
						} else {
							seconde--;
						}
						//Mise a jour du champ
						String [] heure = EntrainementActivity.this.mettreEnForme(minute.toString(), seconde.toString());
						champDuree.setText(heure[0]+":"+heure[1]);
		            }
				});
			}
			//Affiche un toast pour signaler que l'entrainement est fini
			EntrainementActivity.this.runOnUiThread(new Runnable() {
	            public void run() {
	            	EntrainementActivity.this.curs1.close();
	    			Toast.makeText(EntrainementActivity.this, "Good Job !!", Toast.LENGTH_SHORT).show();
	            }
			});
			finish();
		}
		
	}
	
	private String[] mettreEnForme(String minutes, String secondes) {
		String[] tab = new String[2];
		if(minutes.length() < 2) {
			tab[0] = minutes = "0"+minutes;
		} else {
			tab[0] = minutes;
		}
		if(secondes.length() < 2) {
			tab[1] = secondes = "0"+secondes;
		} else {
			tab[1] = secondes;
		}
		return tab;
	}
	
	private class Gestionnaire extends Thread {
		public void run() {
			TextView champRep = (TextView) findViewById(R.id.editTextRep);
			while(!champRep.getText().toString().equals("0")) {
				try {
					//On l'endort pendant une seconde
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//S'il reste une serie a faire
			EditText ser = (EditText) findViewById(R.id.editTextSerie);
			if(EntrainementActivity.this.nbrExoInitiale > 0 || !ser.getText().toString().equals("1")) {
				EntrainementActivity.this.runOnUiThread(new Runnable() {
		            public void run() {
		            	Intent intent = new Intent(EntrainementActivity.this,PauseActivity.class);
		            	startActivityForResult(intent, 1);
		            }
				});
			}
		}
	}
}
