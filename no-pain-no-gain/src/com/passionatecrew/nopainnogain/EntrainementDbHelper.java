package com.passionatecrew.nopainnogain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EntrainementDbHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "entrainement.db";

	public final String SQL_CREATE_SEANCE = "CREATE TABLE Seances (idSeance INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, objectif TEXT);";
	public final String SQL_CREATE_EXO = "CREATE TABLE Exercices (idExercice INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, nom TEXT, image TEXT);";
	public final String SQL_CREATE_LIEN = "CREATE TABLE Lien (idSean INTEGER, idExo INTEGER, FOREIGN KEY(idSean) REFERENCES Seances(idSeance), FOREIGN KEY(idExo) REFERENCES Exercices(idExercice), PRIMARY KEY(idSean, idExo))";
	public final String SQL_DELETE_SEANCE = "DROP TABLE IF EXISTS Seances;";
	public final String SQL_DELETE_EXO = "DROP TABLE IF EXISTS Exercices;";
	public final String SQL_DELETE_LIEN = "DROP TABLE IF EXISTS Lien;";
	
	public EntrainementDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_SEANCE);
		db.execSQL(SQL_CREATE_EXO);
		db.execSQL(SQL_CREATE_LIEN);
		this.remplissageTableExercices(db);
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_SEANCE);
		db.execSQL(SQL_DELETE_EXO);
		db.execSQL(SQL_DELETE_LIEN);
		onCreate(db);
		
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db,oldVersion,newVersion);
	}
	
	public void remplissageTableExercices(SQLiteDatabase db) {
		//Remplissage table Exercices Jambes
			db.execSQL("insert into Exercices(type, nom, image) values('Jambes', 'Squat', '"+String.valueOf(R.drawable.squat)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Jambes', 'Chaise', '"+String.valueOf(R.drawable.chaise)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Jambes', 'Flexions', '"+String.valueOf(R.drawable.flexion)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Jambes', 'Fentes', '"+String.valueOf(R.drawable.fente)+"')");
		//Remplissage table Exercices Bras
			db.execSQL("insert into Exercices(type, nom, image) values('Bras', 'Dips', '"+String.valueOf(R.drawable.dips)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Bras', 'Tractions', '"+String.valueOf(R.drawable.tractions)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Bras', 'Pompes', '"+String.valueOf(R.drawable.pompes)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Bras', 'Alt�res', '"+String.valueOf(R.drawable.altere)+"')");
		//Remplissage table Exercices Epaules
			db.execSQL("insert into Exercices(type, nom, image) values('Epaules', 'D�velopp� militaire', '"+String.valueOf(R.drawable.developpemilitaire)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Epaules', 'D�velopp� nuque', '"+String.valueOf(R.drawable.developpenuque)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Epaules', 'Tirage menton', '"+String.valueOf(R.drawable.tiragementon)+"')");
		//Remplissage table Exercices Dos
			db.execSQL("insert into Exercices(type, nom, image) values('Dos', 'Rowing barre', '"+String.valueOf(R.drawable.rowingbarre)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Dos', 'Rowing un bras', '"+String.valueOf(R.drawable.rowingbras)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Dos', 'Tirage nuque', '"+String.valueOf(R.drawable.tiragenuque)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Dos', 'Pull over', '"+String.valueOf(R.drawable.pullover)+"')");
		//Remplissage table Exercices Dos
			db.execSQL("insert into Exercices(type, nom, image) values('Torse', 'Gainage', '"+String.valueOf(R.drawable.gainage)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Torse', 'D�velopp� couch�', '"+String.valueOf(R.drawable.developpecouche)+"')");
			db.execSQL("insert into Exercices(type, nom, image) values('Torse', 'Papillon', '"+String.valueOf(R.drawable.papillon)+"')");
	}

}
