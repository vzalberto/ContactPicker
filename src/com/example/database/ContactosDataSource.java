package com.example.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class ContactosDataSource {

	ContentValues cv;
	private DatabaseHelper dbHelper;
	SQLiteDatabase database;
	Context cont;
	
	 public ContactosDataSource(Context context) {
		 
		    dbHelper = new DatabaseHelper(context);
		    this.cont = context;
	 }
	 
	 public void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();	
	}

	 public void close() {
		    dbHelper.close();
	 }

	/*INSERTA UNA NUEVA CONFIGURACION EN LA TABLA CONFIGURACIONES, REGRESA EL ID INSERTADO*/
	public long insertarNuevoContacto(int _idContacto,String _nombre,String _numero, int _notificacionSMS,int _notificacionApp, String _idAppContacto){

		
		try{
			open();
			cv=new ContentValues();
			cv.put("IDCONTACTO",_idContacto);
			cv.put("NOMBRE", _nombre);
			cv.put("NUMERO", _numero);
			cv.put("NOTIFICACION_SMS", _notificacionSMS);
			cv.put("NOTIFICACION_APP", _notificacionApp);
			cv.put("IDAPPCONTACTO", _idAppContacto);

			long insertedId = database.insert(dbHelper.tablaContactos, null, cv);
			close();
			
			Toast.makeText(this.cont, "Contacto Insertado", Toast.LENGTH_LONG).show();
			return insertedId;
		}catch(Exception e){
			close();
			Log.d("DB EXCEPTION", "ERROR EN BASE DE DATOS SQLITE"+ e);
			return -1;
		}
	}
	
	public boolean borrarContacto(int _idContacto){

		try{
			open();
			database.delete(dbHelper.tablaContactos,"IDCONTACTO=?", new String [] {String.valueOf(_idContacto)});
			close();
			return true;
		}catch(Exception e){
			close();
			Log.d("DB EXCEPTION", "ERROR EN BASE DE DATOS SQLITE"+ e);
			return false;
		}
	}
	
	
	public boolean editarContacto(int _idContacto,String _nombre,String _numero){
		
			try{
				open();
				cv = new ContentValues();
				cv.put("NOMBRE", _nombre);
				cv.put("NUMERO", _numero);
				database.update(dbHelper.tablaContactos, cv, "IDCONTACTO=?", new String []{String.valueOf(_idContacto)});
				close();
				return true;
			}catch(Exception e){
				close();
				Log.d("DB EXCEPTION", "ERROR EN BASE DE DATOS SQLITE"+ e);
				return false;
			}
			
	}
	

	 /* METODOS PARA SETEO DE VALORES DE NOTIFICACIONES */ 

	
	// SETEAR A UN CONTACTO DEFINIDO QUE ENVIE POR SMS
	public boolean addNotificacionSMS(int _idContacto){
		try{
			open();
			cv = new ContentValues();
			cv.put("NOTIFICACION_SMS", "1");
			database.update(dbHelper.tablaContactos, cv, "IDCONTACTO=?", new String []{String.valueOf(_idContacto)});
			close();
			return true;
		}catch(Exception e){
			close();
			Log.d("DB EXCEPTION", "ERROR EN BASE DE DATOS SQLITE"+ e);
			return false;
		}
		
	}
	
	// SETEAR A UN CONTACTO DEFINIDO QUE ENVIE POR MEDIO DE LA APP, ES NECESARIO ESPECIFICAR
	// EL ID DE GUARURAPP DEL CONTACTO QUE SE ESTË ENLAZANDO
	public boolean addNotificacionAPP(int _idContacto, String _idAppContacto){
		try{
			open();
			cv = new ContentValues();
			cv.put("NOTIFICACION_APP", "1");
			cv.put("IDAPPCONTACTO",_idAppContacto);
			database.update(dbHelper.tablaContactos, cv, "IDCONTACTO=?", new String []{String.valueOf(_idContacto)});
			close();
			return true;
		}catch(Exception e){
			close();
			Log.d("DB EXCEPTION", "ERROR EN BASE DE DATOS SQLITE"+ e);
			return false;
		}
		
	}
	
	// SETEAR A UN CONTACTO DEFINIDO QUE <<NO>> ENVIE POR MEDIO DE LA APP
	public boolean removeNotificacionSMS(int _idContacto){
		try{
			open();
			cv = new ContentValues();
			cv.put("NOTIFICACION_SMS", "0");
			database.update(dbHelper.tablaContactos, cv, "IDCONTACTO=?", new String []{String.valueOf(_idContacto)});
			close();
			return true;
		}catch(Exception e){
			close();
			Log.d("DB EXCEPTION", "ERROR EN BASE DE DATOS SQLITE"+ e);
			return false;
		}
		
	}
	
	// SETEAR A UN CONTACTO DEFINIDO QUE <<NO>> ENVIE POR MEDIO DE LA APP
	public boolean removeNotificacionAPP(int _idContacto){
		try{
			open();
			cv = new ContentValues();
			cv.put("NOTIFICACION_APP", "0");
			database.update(dbHelper.tablaContactos, cv, "IDCONTACTO=?", new String []{String.valueOf(_idContacto)});
			close();
			return true;
		}catch(Exception e){
			close();
			Log.d("DB EXCEPTION", "ERROR EN BASE DE DATOS SQLITE"+ e);
			return false;
		}
		
	}
		
	
	public List<ContactosNotificacionBean> obtenerContactos(){
		try{
		
			open();
			List<ContactosNotificacionBean> lsContactosNotifica = new ArrayList<ContactosNotificacionBean>();
			Cursor cursor = database.rawQuery("SELECT * FROM "+ dbHelper.tablaContactos, null); 
			 cursor.moveToFirst();
			    while (!cursor.isAfterLast()) {
			    	ContactosNotificacionBean configUserItem = cursorToContactosBean(cursor);
			    	lsContactosNotifica.add(configUserItem);
			      cursor.moveToNext();
			    }
			    
			    Toast.makeText(this.cont, "Mostrar Lista", Toast.LENGTH_LONG).show();
			    // Make sure to close the cursor
			    cursor.close();
			    close();
			    return lsContactosNotifica;
			    
		}catch(Exception e){
			close();
			Log.d("DB EXCEPTION", "ERROR EN BASE DE DATOS SQLITE"+ e);
			return null;
		}
	}
	
	
	private ContactosNotificacionBean cursorToContactosBean(Cursor cursor) {
		//String sqlCreateTablaContactos = "CREATE TABLE "+ tablaContactos +"(IDCONTACTO INTEGER PRIMARY KEY AUTOINCREMENT,NOMBRE TEXT, NUMERO TEXT UNIQUE, NOTIFICACION_SMS INTEGER, NOTIFICACION_APP INTEGER, IDAPPCONTACTO TEXT)";
		ContactosNotificacionBean contactosBean = new ContactosNotificacionBean();
		contactosBean.setIdContacto(cursor.getInt(0));
		contactosBean.setNombre(cursor.getString(1));
		contactosBean.setNumero(cursor.getString(2));
		contactosBean.setNotificacionSMS(cursor.getInt(3));
		contactosBean.setNotificacionApp(cursor.getInt(4));
		contactosBean.setIdAppContacto(cursor.getString(5));
	    return contactosBean;
	  }
}
