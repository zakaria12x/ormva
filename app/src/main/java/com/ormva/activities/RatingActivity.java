package com.ormva.activities;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.derniertest.R;
import com.example.derniertest.R.layout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ormva.pojo.Immobilisation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RatingActivity extends Activity implements OnLongClickListener{
	RelativeLayout view;
	int id = 0;
	TextView design;
	TextView date_ac;
	TextView date_mis;
	TextView txt_commentaire;
	Button btn_comment ;
	EditText txt_comment; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rating);
		view = ((RelativeLayout) findViewById(R.id.layout_bare));
		registerForContextMenu(view);
		
		checkLogin();
		txt_comment = (EditText) findViewById(R.id.txt_commentaire);
		txt_commentaire = (TextView) findViewById(R.id.txt_comment_db);
		btn_comment = (Button)findViewById(R.id.layout_home);
		Immobilisation immob = (Immobilisation)this.getIntent().getExtras().get("immob");
		id = immob.getId();
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 
		Editor editorAt = pref.edit();												
		editorAt.putInt("id_immob", id); 						 
		editorAt.commit();
		
		design = (TextView) findViewById(R.id.immo_design);
		date_ac = (TextView) findViewById(R.id.date_acquis);
		date_mis = (TextView) findViewById(R.id.date_mis);
		design.setText(immob.getDesignation());
		date_ac.setText("Date d'acquisition : " + immob.getDate_acquis());
		date_mis.setText("Date de mise en service : " + immob.getDate_mis());
		String commentaire = immob.getCommentaire();
		
			txt_commentaire.setText(immob.getCommentaire());
		
	     
		
		txt_commentaire.setText(immob.getCommentaire());
		btn_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String comment = txt_comment.getText().toString();
				if(comment.length()> 0){
					SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 				
				    int id_immob = pref.getInt("id_immob", 0);
				    //Toast.makeText(getApplicationContext(), "comment : " + id_immob, Toast.LENGTH_LONG).show();
	                
					commenter(id_immob,comment);
				}else{
					Toast.makeText(getApplicationContext(), "Veuillez saisir un comentaire puis valider", Toast.LENGTH_LONG).show();
	                
				}
			}
		});

		
	}
	 public void commenter(int id, String commentaire){
			AsyncHttpClient client = new AsyncHttpClient();
	        client.get(getResources().getString(R.string.addressIP_Port)+"/testserver/immo/commenter?id_immo="+id+"&comment="+commentaire ,new AsyncHttpResponseHandler() {
	            
	            @Override
	            public void onSuccess(String response) {
	                
	                
	                try {  
	                        JSONObject obj = new JSONObject(response);
	             
	                        if(obj.getBoolean("status")){
	                        	Intent intent = new Intent(getApplicationContext(), Accueil.class);
	            				startActivity(intent);
	                        } 
	                        
	                        else{
	                        	Toast.makeText(getApplicationContext(), "commentaire non ajouter", Toast.LENGTH_LONG).show();
	                        	
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
			menu.getItem(0).setTitle("Déconnexion");
			return super.onPrepareOptionsMenu(menu);
		}
}
