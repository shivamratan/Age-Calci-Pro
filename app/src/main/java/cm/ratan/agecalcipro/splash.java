package cm.ratan.agecalcipro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;


public class splash extends Activity implements Runnable
{
	ProgressBar progress;
	Thread t;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		progress=(ProgressBar)findViewById(R.id.progressBar1);
		progress.getIndeterminateDrawable().setColorFilter(0xffff0000,android.graphics.PorterDuff.Mode.MULTIPLY);
		TextView tv=(TextView)findViewById(R.id.loading);
		Typeface tf=Typeface.createFromAsset(getAssets(), "fonts/deftone stylus.ttf");
		tv.setTypeface(tf);
		
		t=new Thread(this);
		t.start();
		
	}
	public void run() 
	{
		for(int i=0;i<=2;i++)
		{
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Intent i=new Intent(this,AgecalciActivity.class);
		startActivity(i);
		
		
	}
	
	
	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
}
