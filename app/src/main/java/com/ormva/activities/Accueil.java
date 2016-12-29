package com.ormva.activities;

import com.example.derniertest.R;
import com.example.derniertest.R.layout;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class Accueil extends TabActivity implements OnLongClickListener{
	RelativeLayout view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);
		
		final TabHost tabHost = getTabHost();
        
        
        TabSpec inv_spec = tabHost.newTabSpec("INV");
        inv_spec.setIndicator("Inventaire", getResources().getDrawable(R.drawable.icon));
        Intent inv_Intent = new Intent(this, HomeActivity.class);
        inv_spec.setContent(inv_Intent);
        
        
        TabSpec code_spec = tabHost.newTabSpec("code");
        code_spec.setIndicator("Code a bare", getResources().getDrawable(R.drawable.icon));
        Intent code_Intent = new Intent(this, CodeABarre.class);
        code_spec.setContent(code_Intent);
        
        
        
        tabHost.addTab(inv_spec);
        tabHost.addTab(code_spec);
        
        
        
       /* tabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
            	for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
                }
            	
            		tabHost.getCurrentTabView().setBackgroundColor(Color.parseColor("#F29401"));
            }
        });*/
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}
}
