/* ========================================================================
 * Author: Umer Farooq 
 * Website: www.wingoku.com
 *
 * DISTRIBUTION OF CODE WITH CREDITS TO ORIGINAL AUTHOR
 *
 *
 * Copyright 2013 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ======================================================================== */

package com.umer.npproject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StartingPoint extends Activity{


	Button setBut, setIpPort;
	EditText port, xMultiplier, yMultiplier;
	Socket socket;
	
	String ipAddress;
	
	static OutputStream output;
	
	String axis;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_starting_point);
		
		port = (EditText) findViewById(R.id.portEditText);
		xMultiplier = (EditText) findViewById(R.id.xMultiplier);
		yMultiplier = (EditText) findViewById(R.id.yMultiplier);
		
		setBut = (Button) findViewById(R.id.setPort);
		
		setBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
				
				try
				{
					String portNum = port.getText().toString();
					String x_Multiplier = xMultiplier.getText().toString();
					String y_Mutiplier = yMultiplier.getText().toString();
					
					if(wifiManager.isWifiEnabled() && (!portNum.isEmpty() || portNum != null))
					{
						Bundle bundle = new Bundle();
						
						bundle.putString("PORT_NUM", portNum);
						bundle.putString("X_MULTIPLIER", x_Multiplier);
						bundle.putString("Y_MULTIPLIER", y_Mutiplier);
						
						Intent intent = new Intent(StartingPoint.this, SendTrackpadXYAxis.class);
						
						intent.putExtras(bundle);
						
						startActivity(intent);
					}
					else
						Toast.makeText(StartingPoint.this, "Port Field is empty or Wifi is turned off", Toast.LENGTH_SHORT).show();
				}
				catch(Exception e)
				{
					Log.e("Umer MousePad", e.toString());
				}
			}
		});
	}

	

}
