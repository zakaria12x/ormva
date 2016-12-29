package com.ormva.activities;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.derniertest.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ormva.pojo.Bureau;
import com.ormva.pojo.Immobilisation;
import com.ormva.pojo.Service;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnItemClickListener,OnLongClickListener{
	RelativeLayout view;
	private ArrayList<Service> listeServices = new ArrayList<Service>();
	private ArrayList<Bureau> listeBureaux = new ArrayList<Bureau>();
	private ArrayList<Immobilisation> listeImmobilisation = new ArrayList<Immobilisation>(); 
	
    ListView list_immob;
	Spinner mySpinner ;
	Spinner mySpinner_bur;
	int id_service = 0;
	int id_bureau = 0;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_home);
	    checkLogin();
	    view = ((RelativeLayout) findViewById(R.id.layout_home));
		registerForContextMenu(view);
		
	    mySpinner = (Spinner)findViewById(R.id.my_spinner);
	    
	    mySpinner_bur = (Spinner)findViewById(R.id.my_spinner_bur);
	    list_immob = (ListView) findViewById(android.R.id.list);
	    list_immob.setOnItemClickListener(this);
	    ws_service();
	    mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	    
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Service serv = (Service) parent.getSelectedItem();
				id_service = serv.getId();
				if(id_service > 0){
					listeImmobilisation.clear();
                	list_immob.setAdapter(new ArrayAdapter<Immobilisation>(getApplicationContext(), android.R.layout.simple_list_item_1,listeImmobilisation));
					ws_bureau(id_service);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	    mySpinner_bur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Bureau bur = (Bureau) parent.getSelectedItem();
				id_bureau = bur.getId();
				//Toast.makeText(getApplicationContext(), "" + id_bureau, Toast.LENGTH_LONG).show();
                
				if(id_bureau > 0){
					ws_liste_immob(id_bureau);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});

	    
	}
	
	
	 void ws_service(){
		AsyncHttpClient client = new AsyncHttpClient();
        client.get(getResources().getString(R.string.addressIP_Port)+"/testserver/serv/getserv" ,new AsyncHttpResponseHandler() {
            
            @Override
            public void onSuccess(String response) {
                
                
                try {  
                        JSONObject obj = new JSONObject(response);
             
                        if(obj.getBoolean("status")){
                        	String str = obj.getString("liste");
        					String[] listeligne = str.split(";");
        					
        					for(int i = 0 ; i < listeligne.length; i++ ){
        						String ligne = listeligne[i];
        						String[] listeChamps = ligne.split(",");
        						
        						int id = Integer.parseInt(listeChamps[0]);
        						String service = listeChamps[1].toString();
        						Service s = new Service(id,service);
        					
        						listeServices.add(s);
        										
        					}						
        					ArrayAdapter<Service> adapter = new ArrayAdapter<Service>(getApplicationContext(), R.layout.spinner_item, listeServices);
        					mySpinner.setAdapter(adapter);
                        } 
                        
                        else{
                        	Toast.makeText(getApplicationContext(), obj.getString("liste"), Toast.LENGTH_LONG).show();
                            
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
	 void ws_bureau(int id_service){
			AsyncHttpClient client = new AsyncHttpClient();
	        client.get(getResources().getString(R.string.addressIP_Port)+"/testserver/bur/getbureaux?idservice="+id_service ,new AsyncHttpResponseHandler() {
	            
	            @Override
	            public void onSuccess(String response) {
	                
	                
	                try {  
	                        JSONObject obj = new JSONObject(response);
	             
	                        if(obj.getBoolean("status")){
	                        	String str = obj.getString("liste");
	        					String[] listeligne = str.split(";");
	        					listeBureaux.clear();
	        					for(int i = 0 ; i < listeligne.length; i++ ){
	        						String ligne = listeligne[i];
	        						//String[] listeChamps = ligne.split(",");
	        						int id_bur= Integer.parseInt(listeligne[i]);
	        						//String service = listeChamps[1].toString();
	        						Bureau b = new Bureau(id_bur);
	        						listeBureaux.add(b);
	        										
	        					}						
	        					ArrayAdapter<Bureau> adapter = new ArrayAdapter<Bureau>(getApplicationContext(),R.layout.spinner_item , listeBureaux);
	        					mySpinner_bur.setAdapter(adapter);
	        						                        } 
	                        
	                        else{
	                        	listeBureaux.clear();
	                        	ArrayAdapter<Bureau> adapter = new ArrayAdapter<Bureau>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listeBureaux);
	        					mySpinner_bur.setAdapter(adapter);
	                        	//Toast.makeText(getApplicationContext(), obj.getString("liste"), Toast.LENGTH_LONG).show();
	                            
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
	 public void ws_liste_immob(int id_bureau){
			AsyncHttpClient client = new AsyncHttpClient();
	        client.get(getResources().getString(R.string.addressIP_Port)+"/testserver/immo/getimmo?idbureau="+id_bureau ,new AsyncHttpResponseHandler() {
	            
	            @Override
	            public void onSuccess(String response) {
	                
	                
	                try {  
	                        JSONObject obj = new JSONObject(response);
	             
	                        if(obj.getBoolean("status")){
	                        	String str = obj.getString("liste");
	        					String[] listeligne = str.split(";");
	        					listeImmobilisation.clear();
	        					for(int i = 0 ; i < listeligne.length; i++ ){
	        						String ligne = listeligne[i];
	        						String[] listeChamps = ligne.split(",");
	        						
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
	        					
	        						listeImmobilisation.add(immob);
	        										
	        					}						
	        					list_immob.setAdapter(new ArrayAdapter<Immobilisation>(getApplicationContext(),R.layout.spinner_item,listeImmobilisation){
	        			            @Override
	        			            public View getView(int position, View convertView, ViewGroup parent){
	        			                View view = super.getView(position,convertView,parent);
	        			                if(position %2 == 1)
	        			                {
	        			                    view.setBackgroundColor(Color.parseColor("#FFF2DE"));
	        			                }
	        			                else
	        			                {
	        			                    view.setBackgroundColor(Color.parseColor("#F2C781"));
	        			                }
	        			                return view;
	        			            }
	        					});
	        					//Toast.makeText(getApplicationContext(),listeImmobilisation.get(0).getDesignation() , Toast.LENGTH_LONG).show();
	                        	
	                        } 
	                        
	                        else{
	                        	//Toast.makeText(getApplicationContext(), obj.getString("liste"), Toast.LENGTH_LONG).show();
	                        	listeImmobilisation.clear();
	                        	list_immob.setAdapter(new ArrayAdapter<Immobilisation>(getApplicationContext(), android.R.layout.simple_list_item_1,listeImmobilisation));
	 	                       
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
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		 Immobilisation immob = (Immobilisation) list_immob.getAdapter().getItem(position);
				Intent intent = new Intent(this, RatingActivity.class);
				intent.putExtra("immob", immob);
				startActivity(intent);
		
		}
		public void checkLogin(){
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 				
			boolean isLogin = pref.getBoolean("islogin", false);
			if(!isLogin){
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
		        startActivity(i);
			}
		}
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) { 
			case R.id.desconnect:
				SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 
				Editor editor = pref.edit();
					editor.putBoolean("islogin", false); 
					editor.clear();
					editor.commit();
				
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				break;
			
			}
			return super.onOptionsItemSelected(item);
		}
		@Override
		public boolean onLongClick(View v) {
			v.showContextMenu();
			return false;
		}

		@Override
		public boolean onPrepareOptionsMenu(Menu menu) {
			menu.getItem(0).setTitle("D�connexion");
			return super.onPrepareOptionsMenu(menu);
		}
 }
