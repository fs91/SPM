package edu.purdue.spm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import edu.purdue.spm.Login;


public class Portal {

	public static Socket requestSocket;
	public static InputStream inStream;
	public static Scanner in;
	public static OutputStream outStream;
	public static PrintWriter out;
	public static String message;
	public static String messageBack;
	public static boolean serverConnected = false;

	public static AlertDialog.Builder alert;
	public static ProgressDialog progressDialog;
	public static Context context;


	public static class connectServer extends AsyncTask<Void, Void, Void>
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

	//A method for sending message to server
	public static void sendMessage(String msg) {
		//progressDialog = ProgressDialog.show(Login.this, "", "Please wait...");
		Log.i("TRACK", "out is:"+out);
		//out = new PrintWriter(outStream, true);
		out.println(msg);
		System.out.println("client>" + msg);
		//progressDialog.dismiss();
	}


	public static void sendMessageGivenContext(Context context, String message){
		try {
			Portal.message = message;
			Portal.sendMessage(Portal.message);
			Portal.ReadMessages temp = new Portal.ReadMessages();
			temp.setContext(context);
			temp.execute().get();
			
			Log.i("wocao", Portal.messageBack);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static class ReadMessages extends AsyncTask<Void, Void, Void>
	{
		Context context;

		public void setContext(Context con){
			context = con;			
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			Log.i("TRACK", "context is:"+context);
			progressDialog = ProgressDialog.show(context, "", "Please wait...");
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


}
