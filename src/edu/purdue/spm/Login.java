package edu.purdue.spm;

import static edu.purdue.spm.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static edu.purdue.spm.CommonUtilities.EXTRA_MESSAGE;
import static edu.purdue.spm.CommonUtilities.SENDER_ID;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

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

import com.google.android.gcm.GCMRegistrar;

import edu.purdue.spm.R;
import edu.purdue.spm.util.Portal;

public class Login extends Activity {

	// parameters for connecting to server


	

	//Buttons for login.xml
	TextView tv;
	EditText account;
	EditText password;
	Button loginBtn;
	Button registerBtn;
	RelativeLayout ui;
	

	Intent intentAfterLogin;

	//GCM parameters
	AsyncTask<Void, Void, Void> mRegisterTask;
	public static String GCMaccount = "";
	public static String GCMpassword = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		/*intentAfterLogin = new Intent(this, ViewPagerFragmentActivity.class);
		startActivity(intentAfterLogin);
		System.exit(0);
		*/
		

		
		unregisterGCM();
		//Alert builder
		Portal.alert = new AlertDialog.Builder(this);

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
					GCMaccount = account.getText().toString();
					GCMpassword = password.getText().toString();
					registerGCM();
					startActivity(intentAfterLogin);
				//	System.exit(0);
				} else {
					Toast.makeText(getApplicationContext(), Portal.messageBack, Toast.LENGTH_LONG).show();
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





	public boolean RegisterRequest(String account, String password) {
		try {
			Portal.context = this.getApplicationContext();
			Portal.message = "Register:;:"+account+":;:"+password+":;:Lucas:;:Lin:;:8888888888";
			Portal.sendMessage(Portal.message);
			new Portal.ReadMessages().execute().get();
			Toast.makeText(getApplicationContext(), Portal.messageBack, Toast.LENGTH_LONG).show();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return false;
	}

	protected boolean LoginRequest(String account, String password) {
		try {
			Portal.message = "Login:;:"+account+":;:"+password;
			Portal.sendMessage(Portal.message);
			
			
			Portal.ReadMessages tmp = new Portal.ReadMessages();
			tmp.setContext(Login.this);
			tmp.execute().get();
			
			
			Toast.makeText(getApplicationContext(), Portal.messageBack, Toast.LENGTH_LONG).show();
			if(Portal.messageBack.equals("Login success")){
				return true;
			}else{
				return false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return false;
	}



	public void connectServer(){
		Portal.progressDialog = ProgressDialog.show(Login.this, "", "Loading...");

		//Checking the Internet availability
		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			Portal.alert.setTitle("Internet Connection error.");
			Portal.alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					System.exit(0);
				}
			});
			Portal.alert.show();
		}else{
			new Portal.connectServer().execute();
		}
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
		System.out.println("regID: "+regId);
		
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
	
	
	public void unregisterGCM(){
		// Get GCM registration id
		System.out.println("Unregistering GCM");
		final String regId = GCMRegistrar.getRegistrationId(this);
		System.out.println("regID: "+regId);
		final Context context = this;
		mRegisterTask = new AsyncTask<Void, Void, Void>() {
	
			@Override
			protected Void doInBackground(Void... params) {
				// Register on our server
				// On server creates a new user
				ServerUtilities.unregister(context, regId);
				return null;
			}
	
			@Override
			protected void onPostExecute(Void result) {
				mRegisterTask = null;
			}
	
		};
		mRegisterTask.execute(null, null, null);
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