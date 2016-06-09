package cm.ratan.agecalcipro;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AgecalciActivity extends Activity {
    /** Called when the activity is first created. */
	
	int ageyear;
	EditText et_cur;
	EditText et_birth;
	TextView tv_cur;
	TextView tv_birth;
	Button btn_calc;
	CustomDateTimePicker custom;
	CustomDateTimePicker custom1;
	private int day;
	private int year;
	private int hour;
	private int min;
	private int sec;
	private int day_b;
	private int year_b;
	private int hour_b;
	private int min_b;
	private int sec_b;
	private String monthName_b;
	private String am_pm_b;
	private int monthnum;
	private int monthnum_b;
	private String monthName;
	private String am_pm;
	Thread t,t_birth;
	boolean lock_cur=true;
	boolean lock_bir=true;
	TextView age;
	Thread ageThread;
	boolean lock_click=true;
	LinearLayout agelayout;
	LinearLayout.LayoutParams ageprops;
	Thread layoutthread;
	Button btnreset;
	Thread revertslider;
	TextView fut_birth1,fut_birth2;
	LinearLayout futlayout;
	LinearLayout.LayoutParams futlayoutpam;
	Thread futlayslide,revertfutslide;
	int screenwidth;
	
	int y,mo,d,h,m,s;
	
	
	boolean singleuse=true;
	
	@Override
	protected void onStart() 
	{
		super.onStart();
		
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
       // ActionBar actionbar=(AgecalciActivity.this).getActionBar();
	//	actionbar.hide();
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.custom_title);
        tv_cur=(TextView)findViewById(R.id.current);
        tv_birth=(TextView)findViewById(R.id.birth);
        
        
        
        et_cur=(EditText)findViewById(R.id.et_cur);
        et_birth=(EditText)findViewById(R.id.et_birth);
        
        
        et_cur.setKeyListener(null);
        et_birth.setKeyListener(null);
        
        age=(TextView)findViewById(R.id.textView1);
        fut_birth1=(TextView)findViewById(R.id.futurebirth1);
        fut_birth2=(TextView)findViewById(R.id.futurebirth2);
        agelayout=(LinearLayout)findViewById(R.id.agelayout);
        agelayout.setVisibility(LinearLayout.INVISIBLE);
        futlayout=(LinearLayout)findViewById(R.id.linearLayout5);
        ageprops=(LinearLayout.LayoutParams)agelayout.getLayoutParams();
        futlayoutpam=(LinearLayout.LayoutParams)futlayout.getLayoutParams();
        futlayout.setVisibility(LinearLayout.INVISIBLE);
        
        Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/deftone stylus.ttf");
        tv_cur.setTypeface(tf);
        tv_birth.setTypeface(tf);
        btnreset=(Button)findViewById(R.id.btnreset);
        LinearLayout ll1=(LinearLayout)findViewById(R.id.linearLayout1);
        screenwidth=ll1.getWidth();
        Display disp=getWindowManager().getDefaultDisplay();
        screenwidth=disp.getWidth()-17;
        
         
        
        
        btnreset.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) 
			{
				et_cur.setEnabled(true);
				et_birth.setEnabled(true);
				et_birth.setText("");
				revertslider=new Thread(new revertslider(),"revertslider");
				revertslider.start();
				revertfutslide=new Thread(new revertfutslide(),"revertfutureslider");
				revertfutslide.start();
				lock_cur=true;
				//layoutthread.stop();
				//ageThread.stop();
				
				
			}
		});
        
        btn_calc=(Button)findViewById(R.id.button1);
        
        btn_calc.setOnClickListener(new OnClickListener() 
        {
			
			public void onClick(View arg0) 
			{
				if(et_cur.isEnabled()&&et_birth.isEnabled())
				 validate();
				if(!lock_click)
				{
				lock_cur=false;
				lock_bir=false;
				
				
				
			
				et_cur.setEnabled(false);
				et_birth.setEnabled(false);
				calculate();
				calculatef();
				lock_click=true;
				
				}
				
				
			}
		});
        
        
        
        
        
        
        
        
         
        Calendar c=Calendar.getInstance();
        	day = c.get(Calendar.DAY_OF_MONTH);
       
        	monthnum=c.get(Calendar.MONTH);
        	
        	 Calendar calendar = Calendar.getInstance();
        	    calendar.set(Calendar.MONTH, c.get(Calendar.MONTH));

        	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
        	    simpleDateFormat.setCalendar(calendar);
        	    monthName = simpleDateFormat.format(calendar.getTime());

        	    am_pm = (calendar.get(Calendar.AM_PM) == (Calendar.AM)) ? "AM"
        	            : "PM";
        	    
        	
        	year=c.get(Calendar.YEAR);
        	hour=c.get(Calendar.HOUR);
        	min=c.get(Calendar.MINUTE);
        	sec=c.get(Calendar.SECOND);
        	
        	
        	if(hour>12)
        		hour-=12;
        	if(hour==0)
        		hour=12;
        	
        
        et_cur.setText(day
        + "/" +monthName+ "/" + year
         +", " + hour + ":" + min+":"+sec
        + " " + am_pm); 
        
        
        
        et_cur.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View arg0, MotionEvent arg1) 
			{
				
				custom1.showDialog();
				// TODO Auto-generated method stub
				return false;
			}
		});
        
        et_birth.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View arg0, MotionEvent arg1) {
				
				custom.showDialog();
				// TODO Auto-generated method stub
				return false;
			}
		});
        
        
        custom1=new CustomDateTimePicker(this, new CustomDateTimePicker.ICustomDateTimeListener() 
        {
			
			public void onSet(Dialog dialog, Calendar calendarSelected,
					Date dateSelected, int year1, String monthFullName,
					String monthShortName, int monthNumber, int date,
					String weekDayFullName, String weekDayShortName, int hour24,
					int hour12, int min1, int sec, String AM_PM) 
			{
				et_cur.setText(calendarSelected
                        .get(Calendar.DAY_OF_MONTH)
                + "/" +monthFullName+ "/" + custom1.pad(year1)
                + ", " + custom1.pad(hour12) + ":" + custom1.pad(min1)+":00"
                + " " + AM_PM);
				
				
				
				day=calendarSelected.get(Calendar.DAY_OF_MONTH);
				year=year1;
				hour=hour12;
				min=min1;
				sec=0;
				monthName=monthFullName;
				am_pm=AM_PM;
				monthnum=monthNumber;
				
			}
			
			public void onCancel() 
			{
				
				
			}
		});
        
      
        
       
       
        custom=new CustomDateTimePicker(this, new CustomDateTimePicker.ICustomDateTimeListener() 
        {
			
			public void onSet(Dialog dialog, Calendar calendarSelected,
					Date dateSelected, int year, String monthFullName,
					String monthShortName, int monthNumber, int date,
					String weekDayFullName, String weekDayShortName, int hour24,
					int hour12, int min, int sec, String AM_PM) 
			{
				et_birth.setText(calendarSelected
                        .get(Calendar.DAY_OF_MONTH)
                + "/" +monthFullName+ "/" + custom.pad(year)
                + ", " + custom.pad(hour12) + ":" + custom.pad(min)+":00"
                + " " + AM_PM);
				day_b=calendarSelected.get(Calendar.DAY_OF_MONTH);
				year_b=year;
				hour_b=hour12;
				min_b=min;
				sec_b=0;
				monthName_b=monthFullName;
				am_pm_b=AM_PM;
				monthnum_b=monthNumber;
			//	t_birth.start();
				
			}
			
			public void onCancel() 
			{
				
				
			}
		});
        
       t=new Thread(new et_curtimer(),"mycurrentthread");
       t.start();
      // t_birth=new Thread(new et_birthtimer(),"mybirththread");
      // layoutthread =new Thread(new ageslide() ,"ageslider");
       //revertslider=new Thread(new revertslider(),"revertslider");
      // futlayslide=new Thread(new futslide(),"futureslider");
   //  revertfutslide=new Thread(new revertfutslide(),"revertfutureslider");
       
        
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	
    	menu.add("About");
    	menu.add("Exit");
    	// TODO Auto-generated method stub
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	String s = item.getTitle().toString();

		if (s.equals("About")) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("About");
			// builder.setCancelable(true);
			builder.setMessage("This App is developed by Ratan Apps ,All it gui design and component is registered to Ratan Apps");
			/*
			 * builder.setPositiveButton("OK", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * public void onClick(DialogInterface arg0, int arg1) {
			 * arg0.cancel();
			 * 
			 * } });
			 */
			AlertDialog dlg = builder.create();
			dlg.show();
			// Toast.makeText(this,"dialog is opening ",Toast.LENGTH_LONG).show();

		}

		 else if (s.equals("Exit")) {
			finish();
		}
    	// TODO Auto-generated method stub
    	return super.onOptionsItemSelected(item);
    }
    void validate()
    {
    	int error=0;
    	if(et_birth.getText().toString().equals(""))
    	{
    		error++;
    		AlertDialog.Builder builder=new AlertDialog.Builder(this);
    		builder.setTitle("Error !!!");
    		builder.setMessage("Please Fill Date of Birth");
    		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) 
				{
					arg0.cancel();
					
				}
			});
    		AlertDialog alert=builder.create();
    		alert.show();
    	}
    	else if(year_b>year)
    	{
    		error++;
    		AlertDialog.Builder builder=new AlertDialog.Builder(this);
    		builder.setTitle("Error !!!");
    		builder.setMessage("Date of Birth should be LESS than Current Date");
    		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) 
				{
					arg0.cancel();
					
				}
			});
    		AlertDialog alert=builder.create();
    		alert.show();
    	}
    	else if(year_b==year)
    	{
    		
    		if(monthnum_b>monthnum)
    		{
    			error++;
    			showdialog();
    		}
    		else if(monthnum_b==monthnum)
    		{
    			if(day_b>day)
    			{
    				error++;
    				showdialog();
    			}
    			
    			else if(day_b==day)
    			{
    				if(hour_b>hour)
    				{
    					error++;
    					showdialog();
    				}
    				else if(hour_b==hour)
    				{
    					if(min_b>min)
    					{
    						error++;
    						showdialog();
    					}
    					else if(min_b==min)
    					{
    						if(sec_b==sec)
    						{
    							error++;
    							AlertDialog.Builder builder=new AlertDialog.Builder(this);
    							builder.setTitle("Error !!!");
    							builder.setMessage("Date of Birth should not be equal to Current Date");
    							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    								
    								public void onClick(DialogInterface arg0, int arg1) 
    								{
    									arg0.cancel();
    									
    								}
    							});
    							AlertDialog alert=builder.create();
    							alert.show();
    						}
    					}
    				}
    			}
    			
    		}
    	}
    	
    	if(error==0)
    	{
    		lock_click=false;
    	}
    }
    void showdialog()
    {
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("Error !!!");
		builder.setMessage("Date of Birth should be LESS than Current Date");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface arg0, int arg1) 
			{
				arg0.cancel();
				
			}
		});
		AlertDialog alert=builder.create();
		alert.show();
    }
    
    /*
    class et_birthtimer implements Runnable
    {
    	Handler h=new Handler();
		public void run() 
		{
			
			while(lock_bir)
			{
				
				
				if(sec_b==60)
				{
					min_b++;
					sec_b=0;
				}
				if(min_b==60)
				{
					hour_b++;
					min_b=0;
				}
				if(hour_b==13)
				{
					if(am_pm_b.equals("PM"))
					{
					hour_b=1;
					am_pm_b="AM";
					day_b++;
					}
					else
					{
						hour_b=1;
						am_pm_b="PM";
						
					}
					
				}
				
				
				
				
			h.post(new Runnable() {
				
				public void run() 
				{
					 et_birth.setText(day_b
						        + "/" +monthName_b+ "/" + year_b
						         +", " + custom.pad(hour_b) + ":" + custom.pad(min_b)+":"+custom.pad(sec_b)
						        + " " + am_pm_b);
					
				}
			});
			
			sec_b++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	     	}
			
			
		}
    	
    }    */
    class et_curtimer implements Runnable
    {
    	Handler h=new Handler();
		public void run() 
		{
			while(true)
			{
				
				sec++;
				if(sec==60)
				{
					min++;
					sec=0;
				}
				if(min==60)
				{
					hour++;
					min=0;
				}
				if(hour==12)
				{
					if(am_pm.equals("PM"))
					{
						am_pm="AM";
						day++;
					}
					else if(am_pm.equals("AM"))
					{
						am_pm="PM";
					}
				}
				if(hour==13)
				{
					hour=1;
					
				}
				
				
				
				
				
			h.post(new Runnable() {
				
				public void run() 
				{
					if(lock_cur)
					{
						
					 et_cur.setText(day
						        + "/" +monthName+ "/" + year
						         +", " +custom.pad(hour)+ ":" +custom.pad(min)+":"+custom.pad(sec)
						        + " " + am_pm);
					}
					
				}
			});
			
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	     	}
			
		}
    	
    }
    
    
    int d1,m1,y1,h1,min1,s1;
    int d2,m2,y2,h2,min2,s2;
    void convertcalculatable()
    {
    	d2=day;
    	m2=monthnum+1;
    	y2=year;
    	h2=hour;
    	if(am_pm.equals("AM")&&h2==12)
    		h2=0;
    	else if(am_pm.equals("PM")&&h2!=12)
    		h2+=12;
    	
    	min2=min;
    	s2=sec;
    	d1=day_b;
    	m1=monthnum_b+1;
    	y1=year_b;
    	h1=hour_b;
    	if(am_pm_b.equals("AM")&&h1==12)
    		h1=0;
    	else if(am_pm_b.equals("PM")&&h1!=12)
    		h1+=12;
    	
    	min1=min_b;
    	s1=sec_b;
     	
    	
    }
    
    int df1,mf1,yf1,df2,mf2,yf2;
    void convertcalculatablef()
    {
    	df2=day_b;
    	mf2=monthnum_b+1;
    	if(monthnum_b<monthnum||((monthnum_b==monthnum)&&(day_b<day)))
    	    yf2=year+1;
    	else if(monthnum_b>monthnum||((monthnum_b==monthnum)&&(day_b>day)))
    		yf2=year;
    	else 
    		yf2=year;
    	
    		
    	df1=day;
    	mf1=monthnum+1;
    	yf1=year;
    	//System.out.println("*******************The df2="+df2+" mf2="+mf2+" yf2="+yf2);
    	//System.out.println("*******************The df21="+df1+" mf1="+mf1+" yf1="+yf1);
    }
    
    void calculatef()
    {
    	boolean passed=true;
    	convertcalculatablef();
    	if((yf2==yf1)&&(mf2==mf1)&&(df2==df1))
		{
			fut_birth1.setText("Happy birthday to you");
			fut_birth2.setText("& Heartiest congratulation for your "+ageyear+"'s birthday");
			passed=false;
		}
    	if(df2<df1)
		{
			if(mf2==1||mf2==3||mf2==5||mf2==7||mf2==8||mf2==10||mf2==12)
			{
				//System.out.println("Entering bro inside");
				mf2--;
				df2+=31;
			}
			else if(mf2==4||mf2==6||mf2==9||mf2==11)
			{
				mf2--;
				df2+=30;
			}
			else if(mf2==2)
			{
				if(yf2%4000==0&&yf2%4!=0)
				{
					mf2--;
					df2+=28;
				}
				else if(yf2%4==0)
				{
					mf2--;
					df2+=29;
				}
			}
			
		}
		if(mf2<mf1)
		{
			yf2--;
			mf2+=12;
		}
			
		int year=yf2-yf1;
		int month=mf2-mf1;
		int day=df2-df1;
		
		
		if(passed)
		{
			ageyear=ageyear+1;
		fut_birth1.setText("You will "+ageyear+" after");
		fut_birth2.setText(year+" years,"+month+" months,"+day+" days");
		}
		futlayout.setVisibility(LinearLayout.VISIBLE);
		futlayslide=new Thread(new futslide(),"futureslider");
		futlayslide.start();
		
		
    }
    
    void calculate()
    {
    	convertcalculatable();
    	
    		if(s2<s1)
    		{
    			min2--;
    			s2+=60;
    		}
    		
    		if(min2<min1)
    		{
    			h2--;
    			min2+=60;
    		}
    		
    		if(h2<h1)
    		{
    			d2--;
    			h2+=24;
    		}
    		
    		if(d2<d1)
    		{
    			if(m2==1||m2==3||m2==5||m2==7||m2==8||m2==10||m2==12)
    			{
    				//System.out.println("Entering bro inside");
    				m2--;
    				d2+=31;
    			}
    			else if(m2==4||m2==6||m2==9||m2==11)
    			{
    				m2--;
    				d2+=30;
    			}
    			else if(m2==2)
    			{
    				if(y2%4000==0&&y2%4!=0)
    				{
    					m2--;
    					d2+=28;
    				}
    				else if(y2%4==0)
    				{
    					m2--;
    					d2+=29;
    				}
    			}
    			
    		}
    		if(m2<m1)
    		{
    			y2--;
    			m2+=12;
    		}
    			
    		//y,mo,d,h,m,s
    		 y=y2-y1;
    		 mo=m2-m1;
    		 d=d2-d1;
    		 h=h2-h1;
    		m=min2-min1;
    		s=s2-s1;
    		
    		
    		ageyear=y;
    		System.out.println("*****************************The value of h2 and h1 is "+h2+" "+h1+" ");
    		age.setText(y+":"+mo+":"+d+":::"+h+":"+m+":"+s);
    		ageThread=new Thread(new agetimer(),"agethread");
    		agelayout.setVisibility(LinearLayout.VISIBLE);
    		
    		
    		
    		if(singleuse)
    		{
    		ageThread.start();
    		singleuse=false;
    		}
    		layoutthread =new Thread(new ageslide() ,"ageslider");
    		layoutthread.start();
    	
    }
    
    class agetimer implements Runnable
    {
    	
    	Handler h1=new Handler();
    	/*agetimer(int year,int month,int day,int hour,int minute,int second)
    	{
    		y=year;
    		mo=month;
    		d=day;
    		h=hour;
    		m=minute;
    		s=second;
    	}*/
		public void run() 
		{
			
			while(true)
			{
				
				s++;
				if(s==60)
				{
					m+=1;
					s=0;
				}
				
				if(m==60)
				{
					h++;
					m=0;
				}
				
				if(h==24)
				{
					d++;
			     	h=0;
				}
				
				
				
				h1.post(new Runnable() 
				{
					
					public void run() 
					{
						System.out.println(d);
						age.setText(y+":"+custom.pad(mo)+":"+custom.pad(d)+"  "+custom.pad(h)+":"+custom.pad(m)+":"+custom.pad(s));
						
					}
				});
			
			
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
		
		
    	
    }
    class ageslide implements Runnable
	{
		int mar_left=305;
		Handler h_age=new Handler();
		public void run() 
		{
			while(mar_left!=0)
			{
				h_age.post(new Runnable() 
				{
					
					public void run() 
					{
						ageprops.setMargins(mar_left,10,0,0);
						agelayout.setLayoutParams(ageprops);
					//	futlayoutpam.setMargins(mar_left,10,0,0);
					//	futlayout.setLayoutParams(futlayoutpam);
						
					}
				});
				mar_left--;
				System.out.println(" executing thread "+mar_left);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
	}
    
    class revertslider implements Runnable
    {
    	int mar_left=0;
	Handler h_age=new Handler();
	public void run() 
	{
		while(mar_left!=305)
		{
			h_age.post(new Runnable() 
			{
				
				public void run() 
				{
					ageprops.setMargins(mar_left,10,0,0);
					agelayout.setLayoutParams(ageprops);
				//	futlayoutpam.setMargins(mar_left,10,0,0);
				//	futlayout.setLayoutParams(futlayoutpam);
					
				}
			});
			mar_left++;
			System.out.println(" executing thread "+mar_left);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
		if(mar_left==305)
		{
			h_age.post(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					agelayout.setVisibility(LinearLayout.INVISIBLE);	
					//futlayout.setVisibility(LinearLayout.INVISIBLE);
				}
			});
			
		}
		
		
	}
    	
    }
    
    class futslide implements Runnable
    {
    	int mar_width=0;
		Handler h_age=new Handler();
		public void run() 
		{
			while(mar_width!=screenwidth)
			{
				h_age.post(new Runnable() 
				{
					
					public void run() 
					{
						
						futlayoutpam=new LinearLayout.LayoutParams(mar_width, 105);
						futlayoutpam.setMargins(0, 20, 0,10);
				        
						futlayout.setLayoutParams(futlayoutpam);
						
					}
				});
				mar_width++;
				//System.out.println(" executing thread "+mar_left);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
    	
    }
    
    class revertfutslide implements Runnable
    {

    	int mar_width=screenwidth;
	    Handler h_age=new Handler();
	    
	public void run() 
	{
		while(mar_width!=0)
		{
			h_age.post(new Runnable() 
			{
				
				public void run() 
				{
					futlayoutpam=new LinearLayout.LayoutParams(mar_width, 105);
					futlayoutpam.setMargins(0, 20, 0,10);
			       
					futlayout.setLayoutParams(futlayoutpam);
				    //	futlayoutpam.setMargins(mar_left,10,0,0);
				   //	futlayout.setLayoutParams(futlayoutpam);
					
				}
			});
			mar_width--;
			//System.out.println(" executing thread "+mar_left);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
		if(mar_width==0)
		{
			h_age.post(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					agelayout.setVisibility(LinearLayout.INVISIBLE);	
					//futlayout.setVisibility(LinearLayout.INVISIBLE);
					
				}
			});
			
		}
		
		
	}
    	
    	
    }
    
    
    
    
    
    
}