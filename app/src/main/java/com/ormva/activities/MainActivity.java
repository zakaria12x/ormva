package com.ormva.activities;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.derniertest.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	// Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object
    EditText emailET;
    // Passwprd Edit View Object
    EditText pwdET;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		checkLogin();
		errorMsg = (TextView)findViewById(R.id.login_error);
        // Find Email Edit View control by ID
        emailET = (EditText)findViewById(R.id.loginEmail);
        // Find Password Edit View control by ID
        pwdET = (EditText)findViewById(R.id.loginPassword);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        Button btn_log = (Button)findViewById(R.id.btnLogin);
        
        btn_log.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
		        String email = emailET.getText().toString();
		        
		        String password = pwdET.getText().toString();
		        
		        RequestParams params = new RequestParams();
		        
		        if(Utility.isNotNull(email) && Utility.isNotNull(password)){
		            
		            if(Utility.validate(email)){
		                
		                params.put("login", email);
		                
		                params.put("pwd", password);
		                
		                invokeWS(params);
		                //navigatetoHomeActivity();
		            } 

		            else{
		                //Toast.makeText(getApplicationContext(), "login non valide", Toast.LENGTH_LONG).show();
		                errorMsg.setText("login non valide !");
		            }
		        } else{
		            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank"
		            		, Toast.LENGTH_LONG).show();
		        }
				
			}
		});
 
	}
	public void invokeWS(RequestParams params){
        
         prgDialog.show();
         
         AsyncHttpClient client = new AsyncHttpClient();
         client.get(getResources().getString(R.string.addressIP_Port)+"/testserver/login/dologin",params 
        		 ,new AsyncHttpResponseHandler() {
             
             @Override
             public void onSuccess(String response) {
                 
                 prgDialog.hide();
                 try {
                         
                         JSONObject obj = new JSONObject(response);
                         
                         if(obj.getBoolean("status")){
                        	Toast.makeText(getApplicationContext(), "Vous avez authentifi� avec succ�e ",
                        			Toast.LENGTH_LONG).show();
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 
     						Editor editor = pref.edit();
     						editor.putBoolean("islogin", true); 
     						editor.commit();
                             navigatetoHomeActivity();
                         }
                         
                         else{
                             errorMsg.setText("login ou mot de passe incorrect");
                         }
                 } catch (JSONException e) {
                    
                     Toast.makeText(getApplicationContext(), "la reponse Json invalide", Toast.LENGTH_LONG).show();
                     e.printStackTrace();
 
                 }
             }
             // When the response returned by REST has Http response code other than '200'
             @Override
             public void onFailure(int statusCode, Throwable error,String content) {
                
                 prgDialog.hide();
                 
                 if(statusCode == 404){
                     Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                 } 
                 // When Http response code is '500'
                 else if(statusCode == 500){
                     Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                 } 
                 // When Http response code other than 404, 500
                 else{
                     Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                 }
             }
         });
    }
	
	public void navigatetoHomeActivity(){
        Intent homeIntent = new Intent(getApplicationContext(),Accueil.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
	public void checkLogin(){
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 				
		boolean isLogin = pref.getBoolean("islogin", false);
		if(isLogin){
			navigatetoHomeActivity();
		}
	}
	
}
