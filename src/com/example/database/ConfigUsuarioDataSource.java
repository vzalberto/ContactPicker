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

public class ConfigUsuarioDataSource {

	ContentValues cv;
	private DatabaseHelper dbHelper;
	SQLiteDatabase database;
	Context cont;
	
	 public ConfigUsuarioDataSource(Context context) {
		 
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
	public long insertarNuevaConfiguracion(int _idConfig,String _descripcion,String _valor){

		//String sqlConfigUno ="INSERT INTO "+ tablaConfiguraciones + "VALUES(1,'GPSRED','LA APP TRABAJARç SOBRE RED 0 , GPS 1',0 )";
		try{
			open();
			cv=new ContentValues();
			cv.put("IDCONFIG",_idConfig);
			cv.put("DESCRIPCION", _descripcion);
			cv.put("VALOR", _valor);
			long insertedId = database.insert(dbHelper.tablaConfiguraciones, null, cv);
			close();
			
			Toast.makeText(this.cont, "Insertado", Toast.LENGTH_LONG).show();
			return insertedId;
		}catch(Exception e){
			close();
			Log.d("DB EXCEPTION", "ERROR EN BASE DE DATOS SQLITE"+ e);
			return -1;
		}
	}
	
	public boolean borrarConfiguracion(int _idConfiguracion){

		try{
			open();
			database.delete(dbHelper.tablaConfiguraciones,"IDCONFIG=?", new String [] {String.valueOf(_idConfiguracion)});
			close();
			return true;
		}catch(Exception e){
			close();
			Log.d("DB EXCEPTION", "ERROR EN BASE DE DATOS SQLITE"+ e);
			return false;
		}
	}
	
	
	public boolean editarConfiguracion(int _idConfig, String _nuevoValor){
		
			try{
				open();
				cv = new ContentValues();
				cv.put("VALOR", _nuevoValor);
				database.update(dbHelper.tablaConfiguraciones, cv, "IDCONFIG=?", new String []{String.valueOf(_idConfig)});
				close();
				return true;
			}catch(Exception e){
				close();
				Log.d("DB EXCEPTION", "ERROR EN BASE DE DATOS SQLITE"+ e);
				return false;
			}
			
	}
	
	
	public List<ConfigUsuarioBean> obtenerConfiguraciones(){
		try{
		
			open();
			List<ConfigUsuarioBean> lsConfigUser = new ArrayList<ConfigUsuarioBean>();
			Cursor cursor = database.rawQuery("SELECT * FROM "+ dbHelper.tablaConfiguraciones, null); 
			 cursor.moveToFirst();
			    while (!cursor.isAfterLast()) {
			      ConfigUsuarioBean configUserItem = cursorToComment(cursor);
			      lsConfigUser.add(configUserItem);
			      cursor.moveToNext();
			    }
			    
			    Toast.makeText(this.cont, "Mostrar Lista", Toast.LENGTH_LONG).show();
			    // Make sure to close the cursor
			    cursor.close();
			    close();
			    return lsConfigUser;
			    
		}catch(Exception e){
			close();
			Log.d("DB EXCEPTION", "ERROR EN BASE DE DATOS SQLITE"+ e);
			return null;
		}
	}
	
	
	private ConfigUsuarioBean cursorToComment(Cursor cursor) {
	    ConfigUsuarioBean configBean = new ConfigUsuarioBean();
	    configBean.setIdConfiguracion(cursor.getInt(0));
	    configBean.setDescripcion(cursor.getString(1));
	    configBean.setValor(cursor.getString(2));
	    return configBean;
	  }
}
