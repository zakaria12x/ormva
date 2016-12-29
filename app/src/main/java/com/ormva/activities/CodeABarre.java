package com.ormva.activities;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.derniertest.R;
import com.example.derniertest.R.layout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ormva.pojo.Immobilisation;
import com.ormva.pojo.Service;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CodeABarre extends Activity  {
	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
	public void scanBar(View v) {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			/*showDialog(AndroidBarcodeQrExample.this, "No Scanner Found",
					"Download a scanner code activity?", "Yes", "No").show();
		*/}}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				
				/*Toast toast = Toast.makeText(getApplicationContext(), "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
				toast.show();*/
				
				ws_imoCode(contents);
		        
			}
		}
	}
	 void ws_imoCode(String code){
			AsyncHttpClient client = new AsyncHttpClient();
	        client.get(getResources().getString(R.string.addressIP_Port)+"/testserver"
	        		+ "/immobar/getimmobar?code="+code ,new AsyncHttpResponseHandler() {
	            
	            @Override
	            public void onSuccess(String response) {
	                
	                
	                try {  
	                        JSONObject obj = new JSONObject(response);
	                       
	                        if(obj.getBoolean("status")){
	                        	String str = obj.getString("immobilisation");
	                        	   
	        						String[] listeChamps = str.split(",");
	        						
	        						int id = Integer.parseInt(listeChamps[0]);
	        						
	        						String designation = listeChamps[1].toString();
	        					
	        						String code = listeChamps[2].toString();
	        						
	        						String date_ac = listeChamps[3].toString();
	        						
	        						String date_mis = listeChamps[4].toString();
	        						
	        						String commentaire = listeChamps[5].toString();
	        						if(commentaire.equals("vide")){
	        							commentaire = "aucun commentaire";
	    							}else{
	    								
	    							}
	        						
	        						Immobilisation immob = new Immobilisation(id,designation,code,date_ac,date_mis,commentaire);
	        						
	        						Toast.makeText(getApplicationContext(), immob.getDesignation(), Toast.LENGTH_LONG).show();                      
	    	                        
	        					
	        						Intent intent = new Intent(getApplicationContext(), RatingActivity.class);
	        						intent.putExtra("immob", immob);
	        						startActivity(intent);
	                        }else{
	                        	Toast.makeText(getApplicationContext(), obj.getString("immobilisation"), Toast.LENGTH_LONG).show();                      
	                        }
	                } catch (JSONException e) {
	                   
	                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
	                    e.printStackTrace();

	                }
	            }
	            @Override
	            public void onFailure(int statusCode, Throwable error,String content) {
	               
	 
	                
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_code_abarre);
		checkLogin();
		
	}
	public void checkLogin(){
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 				
		boolean isLogin = pref.getBoolean("islogin", false);
		if(!isLogin){
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
	        startActivity(i);
		}
	}


}
