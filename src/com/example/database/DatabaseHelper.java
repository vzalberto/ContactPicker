package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHelper extends SQLiteOpenHelper {
	
	

	String tablaConfiguraciones  ="CONFIG_USUARIO_TABLE"; 
	String tablaContactos = "CONTACTOS_NOTIFICACION";
	static final String dbName="guarurappDB";

	 public DatabaseHelper(Context context) {
		  	super(context, dbName, null,1);
	 }
	 
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sqlCreateTablaConfigUsuario = "CREATE TABLE "+ tablaConfiguraciones +"(IDCONFIG INTEGER PRIMARY KEY UNIQUE, DESCRIPCION TEXT, VALOR TEXT)";
		db.execSQL(sqlCreateTablaConfigUsuario);
		
		String sqlCreateTablaContactos = "CREATE TABLE "+ tablaContactos +"(IDCONTACTO INTEGER PRIMARY KEY AUTOINCREMENT,NOMBRE TEXT, NUMERO TEXT UNIQUE, NOTIFICACION_SMS INTEGER, NOTIFICACION_APP INTEGER, IDAPPCONTACTO TEXT)";
		db.execSQL(sqlCreateTablaContactos);
	
	}

	
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	

	
	
	
	
	


}
