package edu.purdue.spm.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class LoadBitmap {

//	Usage:
//	Bitmap bimage=  loadImageFromURL(bannerpath);
//	image.setImageBitmap(bimage);

	public static Bitmap loadImageFromURL(String url){
		try {
			return (Bitmap) new ContactServerTask(url).execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static Bitmap getBitmapFromURL(String src) {
		try {
			Log.e("src",src);
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			Log.e("Bitmap","returned");
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Exception",e.getMessage());
			return null;
		}
	}
	
	private static class ContactServerTask extends AsyncTask<Object, Void, Object> {

		String url;
		private ContactServerTask(String url){
			super();
			this.url = url;
		}
		
		@Override
		protected Bitmap doInBackground(Object... params) {
			Bitmap result = null;
			try{		
				result = getBitmapFromURL(url);
			}catch(Exception e){
				return null;
			}
			return result;
		}
	}
}
