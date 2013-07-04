package com.example.contactpicker; 
  
import android.app.Activity; 
import android.content.Intent; 
import android.database.Cursor; 
import android.net.Uri; 
import android.os.Bundle; 
import android.provider.ContactsContract; 
import android.provider.ContactsContract.CommonDataKinds.Phone; 
import android.provider.ContactsContract.Contacts; 
import android.util.Log; 
import android.view.Menu; 
import android.view.View; 
import android.widget.Toast; 

import com.example.database.ContactosDataSource;
  
public class ContactPicker extends Activity { 
      
    private static final String DEBUG_TAG = "ContactPicker"; 
  
    @Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_contact_picker); 
    } 
  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { 
        // Inflate the menu; this adds items to the action bar if it is present. 
        getMenuInflater().inflate(R.menu.contact_picker, menu); 
        return true; 
    } 
      
      
    private static final int CONTACT_PICKER_RESULT = 1001;   
    public void doLaunchContactPicker(View view) {   
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,   
                Contacts.CONTENT_URI);   
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);   
    }   
      
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {   
        if (resultCode == RESULT_OK) {   
            switch (requestCode) {   
            case CONTACT_PICKER_RESULT:   
                // handle contact results  
                Cursor cursor = null;   
                String phone = "";  
                String name = ""; 

                 try {   
                     
                     Uri result = data.getData();   
                     Log.v(DEBUG_TAG, "Id: "+ result.toString());  
                                            
                     // Saca el id del contacto y lo convierte a entero 
                     String id = result.getLastPathSegment();                      
                     int _id = Integer.parseInt(id);
                     Toast toast = Toast.makeText(this, id, Toast.LENGTH_SHORT);         
                     toast.show(); 
                     
                     //Consulta teléfono 
                     cursor = getContentResolver().query(Phone.CONTENT_URI,   
                             null, Phone.CONTACT_ID + "=?", new String[] { id },   
                             null);  

                    if (cursor.moveToFirst()) {   
                         phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
                         Toast fon = Toast.makeText(this, phone, Toast.LENGTH_SHORT);
                         fon.show();
                         Log.v(DEBUG_TAG, "Phone: " + phone);  
                         } else {   
                         Log.w(DEBUG_TAG, "No results");   
                     }  

                    //Consulta nombre

                    if (cursor.moveToFirst()) {   
                         name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)); 
                         Toast nom = Toast.makeText(this, name, Toast.LENGTH_SHORT);
                         nom.show();
                         Log.v(DEBUG_TAG, "name: " + name);  
                         } else {   
                         Log.w(DEBUG_TAG, "No results");   
                     }  
                    
                    ContactosDataSource controlador = new ContactosDataSource(this); 
                    controlador.insertarNuevoContacto(_id, name, phone, 1, 0, id);
                    

                 } catch (Exception e) {   
                     Log.e(DEBUG_TAG, "Error al obtener datos :(", e); 
                 } finally {   
                     if (cursor != null) {   
                         cursor.close();   
                     }   
                 } 
                 
                break;   
            }   
        } else { 
            Log.w(DEBUG_TAG, "Warning: activity result not ok");   
        }   
    }   
      
  
} 