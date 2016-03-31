package edu.purdue.spm.login;

import static edu.purdue.spm.login.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static edu.purdue.spm.login.CommonUtilities.EXTRA_MESSAGE;
import static edu.purdue.spm.login.CommonUtilities.SENDER_ID;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import com.google.android.gcm.GCMRegistrar;

import edu.purdue.spm.R;
import edu.purdue.spm.ViewPagerFragmentActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	
	// parameters for connecting to server
	Socket requestSocket;
	static InputStream inStream;
	static Scanner in;
	static OutputStream outStream;
	static PrintWriter out;
 	String message;
 	String messageBack;
	boolean serverConnected = false;
    
    AlertDialog.Builder alert;
    
    //Buttons for login.xml
	TextView tv;
	EditText account;
	EditText password;
	Button loginBtn;
	Button registerBtn;
	RelativeLayout ui;
	ProgressDialog progressDialog;
	
	Intent intentAfterLogin;
	
	//GCM parameters
    AsyncTask<Void, Void, Void> mRegisterTask;
    public static String GCMaccount = "Login";
    public static String GCMpassword = "123";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		//Alert builder
		alert = new AlertDialog.Builder(this);
		
		connectServer();
		
		intentAfterLogin = new Intent(this, ViewPagerFragmentActivity.class);
		
		//GCMRegistrar.unregister(this);
		
		ui = (RelativeLayout)findViewById(R.id.layout);
		tv = (TextView)findViewById(R.id.textView1);
    	account = (EditText)findViewById(R.id.editText1);
    	password = (EditText)findViewById(R.id.editText2);
    
    	loginBtn = (Button)findViewById(R.id.button1);
    	registerBtn = (Button)findViewById(R.id.button2);

    	
    	//Animation
		final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		tv.startAnimation(animationFadeIn);
		account.startAnimation(animationFadeIn);
		password.startAnimation(animationFadeIn);
		loginBtn.startAnimation(animationFadeIn);
		registerBtn.startAnimation(animationFadeIn);
		
		
		
		//When touch the screen outside the input box, hide the keyboard
	 	ui.setOnClickListener(new View.OnClickListener() {
    	    public void onClick(View v) {
    	    	InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	    	mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
    	    }
	 	 }); 
	 	
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		account.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
        		if(account.getText().toString().equals("Login")) {
        			account.setText("");	
        		}
            }
        });
		
		password.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
        		if(password.getText().toString().equals("Password")) {
        			password.setText("");	
        		}
            }
        });
		
		loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	//Validate input
            	/*if(!validateInputs(account.getText().toString(), password.getText().toString())){
            		return;
            	}*/
            	Log.i("TRACK", ""+account.getText().toString()+" "+password.getText().toString());
            	boolean loginsuccess = LoginRequest(account.getText().toString(),password.getText().toString());

            	if(loginsuccess) {
            		registerGCM();
                	startActivity(intentAfterLogin);
                	System.exit(0);
            	} else {
        	        Toast.makeText(getApplicationContext(), messageBack, Toast.LENGTH_LONG).show();
            	}
            	
            }
        });
		
		registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	RegisterRequest(account.getText().toString(),password.getText().toString());
            }
        });
	}
	

	public static final Pattern EMAIL_ADDRESS
    = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"
    );
	
	protected boolean validateInputs(String account, String password) {
		if(account.length()==0 || password.length()==0){
			Toast.makeText(getApplicationContext(), "Account or password can't be empty!", Toast.LENGTH_LONG).show();
			return false;
		} else {
			//both account and password are not empty
			Pattern pattern = Patterns.EMAIL_ADDRESS;
			if(account.equals("test")){
				return true;
			}
		    if(!pattern.matcher(account).matches()) {
				Toast.makeText(getApplicationContext(), "Invalid email address!", Toast.LENGTH_LONG).show();
				return false;
		    }
		}
		Toast.makeText(getApplicationContext(), "Pass!", Toast.LENGTH_LONG).show();

		return true;
	}

	
	
	protected boolean RegisterRequest(String account, String password) {
    	try {
    		message = "Register:;:"+account+":;:"+password+":;:Lucas:;:Lin:;:8888888888";
        	sendMessage(message);
			new readMessages().execute().get();
	        Toast.makeText(getApplicationContext(), messageBack, Toast.LENGTH_LONG).show();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}



	protected boolean LoginRequest(String account, String password) {
    	try {
    		message = "Login:;:"+account+":;:"+password;
        	sendMessage(message);
			new readMessages().execute().get();
	        Toast.makeText(getApplicationContext(), messageBack, Toast.LENGTH_LONG).show();
	        if(messageBack.equals("Login success")){
	        	return true;
	        }else{
	        	return false;
	        }
    	} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
	
	
    public void connectServer(){
		progressDialog = ProgressDialog.show(Login.this, "", "Loading...");

       	//Checking the Internet availability
    	ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
 
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
        	alert.setTitle("Internet Connection error.");
        	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int whichButton) {
        			System.exit(0);
        		}
        	});
        	alert.show();
        }else{
        	new connectServer().execute();
        }
    }

    
    private class connectServer extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPostExecute(Void result) {
    		progressDialog.dismiss();

            if(!serverConnected){
            	alert.setTitle("Connecting server error.");
            	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            		public void onClick(DialogInterface dialog, int whichButton) {
            			System.exit(0);
            		}
            	});
            	alert.show();
            }
        }

        @Override
        protected void onPreExecute() {
        	
        }

        @Override
        protected Void doInBackground(Void... params) {
			try {
				int port = 8089;
				requestSocket = new Socket("lore.cs.purdue.edu", port);
				System.out.println("Connected to lore.cs.purdue.edu:"+port);
				
				inStream = requestSocket.getInputStream();
				outStream = requestSocket.getOutputStream();
				in = new Scanner(inStream);
				out = new PrintWriter(outStream, true);
				serverConnected = true;
			} catch (UnknownHostException e) {
			} catch (IOException e) {
			} 
			return null;
        }
    }
    

    private class readMessages extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPostExecute(Void result) {
    		progressDialog.dismiss();
        	
    		
        }

        @Override
        protected void onPreExecute() {
        	progressDialog = ProgressDialog.show(Login.this, "", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
    		while(true){
    			messageBack = in.nextLine();
    			System.out.println("server>" + messageBack);
    			break;
    		}
    		return null;
        }
    }
    
    //A method for sending message to server
	public static void sendMessage(String msg) {
    	//progressDialog = ProgressDialog.show(Login.this, "", "Please wait...");

		out.println(msg);
		System.out.println("client>" + msg);
		//progressDialog.dismiss();
	}


	/*public static String readMessage(){
		String message = "";
		while(true){
			message = in.nextLine();
			System.out.println("server>" + message);
			break;
		}
		return message;
	}*/
	
	
	
	
	
	
	/*
	 * GCM
	 */
	public void registerGCM(){
		System.out.println("Registering GCM");
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
 
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
        
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));
        
        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
 
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
            	System.out.println("Already registered with GCM");
                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
 
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        ServerUtilities.register(context, GCMaccount, GCMpassword, regId);
                        return null;
                    }
 
                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }
 
                };
                mRegisterTask.execute(null, null, null);
            }
        }
		
	}
    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            //WakeLocker.acquire(getApplicationContext());
 
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
 
            // Showing received message
            System.out.println("newmessage:"+newMessage);

            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
 
            // Releasing wake lock
           // WakeLocker.release();
        }
    };
    
    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
    
    
	@Override
	protected void onPause() {
		super.onPause();
		account.clearAnimation();
		password.clearAnimation();
		loginBtn.clearAnimation();
		registerBtn.clearAnimation();
	}
}