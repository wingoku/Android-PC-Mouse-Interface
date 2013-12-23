package com.umer.npproject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.R.xml;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.EditText;

public class SendTrackpadXYAxis extends Activity{


	Socket socket;
	
	int portNum;
	float xAccelerator, yAccelerator;
	String ipAddress;
	
	static OutputStream output;
	
	String axis;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Mousepad mousepad = new Mousepad(this);
		
		setContentView(mousepad);
		
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		
		Bundle bundle = getIntent().getExtras();
		
		
		String port_Num = bundle.getString("PORT_NUM");
		String x_Accelerator = bundle.getString("X_MULTIPLIER");
		String y_Accelerator = bundle.getString("Y_MULTIPLIER");
		
		if((!x_Accelerator.isEmpty() || x_Accelerator != null))
			xAccelerator = Float.parseFloat(x_Accelerator);
		else
			xAccelerator = 1.5f; // default acceleration value
			
		if (!y_Accelerator.isEmpty() || y_Accelerator!=null)
			yAccelerator = Float.parseFloat(y_Accelerator);
		else
			yAccelerator = 1.2f;// default acceleration value
		
		portNum = Integer.parseInt(port_Num);

		Thread thread1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				connectAsClient(portNum);
			}
			
			
		});
		
		thread1.start();

	}


	

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		try {
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	
	public void connectAsClient(int portNum)
	{
		try
		{
			socket.close();
			
		}
		catch(Exception e)
		{
			Log.e("Closing Socket", e.toString());
		}
		
		try 
		{
			socket = null;

			
			Log.e("Data sending", "creating socket "+ portNum );
			
			ServerSocket serverSocket = new ServerSocket(portNum);
			
			socket = serverSocket.accept();
			
			
			Log.e("Umer socket", socket.toString());
			
			output = socket.getOutputStream();
			
			
			Log.e("Data sending", "socket created");
			
		} catch (Exception e) {

			Log.e("Socket opening", e.toString());
			
		} 
	}
	
	public void sendData(final float xAxis, final float yAxis)
	{
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
			
				try {
					Log.e("Data sending","writing to oputu stream");
					
					String data = "x "+ xAxis * xAccelerator + " y " + yAxis * yAccelerator;
					
					Log.e("Data sending", data);
					
					output.write(data.getBytes());
					
					output.flush(); 
					Log.e("Data sending", "data sent");
					
					}
				catch (IOException e) {
					
					Log.e("Data sending", e.toString());
					
				}
			}
		});
		
		thread.start();
				
	}
	
	

public class Mousepad extends SurfaceView{


	SurfaceHolder surfaceHolder;
	
	Context context;

	
	float xAxis, yAxis;
	
	Paint paint;

	
	final static float Touch_Circle_Radius = 15.0f;
	
	//float canvasHeight = -1;
	
	
	
	public Mousepad(Context con) {
		super(con);

		surfaceHolder = getHolder(); 
		context = con;
		
		paint = new Paint();
		
		paint.setColor(Color.GRAY);
		
		paint.setTextSize(18.0f);
	
		
	}	


	@Override
	public boolean onTouchEvent(MotionEvent event) {


		xAxis = event.getRawX();
		
		yAxis = event.getRawY();
		
		yAxis-=100; 
		sendData(xAxis, yAxis);
		
		
		switch(event.getAction())
		{
		
		case MotionEvent.ACTION_DOWN:
		
			showTouches();
			
			return true; 
			
		
		case MotionEvent.ACTION_UP:
			
			cleanCanvas();
			
			return true;
			
			
			
		case MotionEvent.ACTION_MOVE:

			
			showTouches();
			
			return true;
		
		}
				
		return false;
	}

	

	private void showTouches()
	{
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				if(surfaceHolder.getSurface().isValid())
				{					
					Canvas canvas = surfaceHolder.lockCanvas();
					
					canvas.drawColor(Color.BLACK);
					
					canvas.drawText("xAxis: "+ xAxis + " yAxis: "+ yAxis, 0, 20 , paint); 
					canvas.drawCircle(xAxis, yAxis, Touch_Circle_Radius, paint);

					
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
				
			}
		});
		
		thread.start();
		
	}


	
	private void cleanCanvas()
	{

		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				Canvas canvas = surfaceHolder.lockCanvas();
				
				canvas.drawColor(Color.BLACK);
				
				canvas.drawText("xAxis: "+ xAxis + " yAxis: "+ yAxis, 0, 20 , paint);
				
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		});
		
		thread.start();
		
	}   
	 

}
	

}
