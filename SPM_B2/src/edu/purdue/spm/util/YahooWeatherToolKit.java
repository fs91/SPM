package edu.purdue.spm.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class YahooWeatherToolKit {

	public static class MyWeather{
		public String description;
		public String city;
		public String region;
		public String country;

		public String windChill;
		public String windDirection;
		public String windSpeed;

		public String sunrise;
		public String sunset;

		public String conditiontext;
		public String conditiondate;
		public String temperature;


		public String imgURL;

		@Override
		public String toString() {
			return "MyWeather [description=" + description + ", city=" + city
					+ ", region=" + region + ", country=" + country
					+ ", windChill=" + windChill + ", windDirection="
					+ windDirection + ", windSpeed=" + windSpeed + ", sunrise="
					+ sunrise + ", sunset=" + sunset + ", conditiontext="
					+ conditiontext + ", conditiondate=" + conditiondate
					+ ", temperature=" + temperature + ", imgURL=" + imgURL
					+ "]";
		}
	}



	private static class ContactServerTask extends AsyncTask<Object, Void, Object> {

		String woeid;
		private ContactServerTask(String woeid){
			super();
			this.woeid = woeid;
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = null;
			try{

				result = QueryYahooWeather(woeid);
			}catch(Exception e){
				return e.getMessage();
			}
			return result;
		}
	}


	public static String obtainWeatherInfo(String woeid) {

		String weatherString = "";
		try {
			weatherString = (String) new ContactServerTask(woeid).execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document weatherDoc = convertStringToDocument(weatherString);

		MyWeather weatherResult = parseWeather(weatherDoc);
		return weatherResult.toString();
	}

	public static MyWeather obtainWeatherObj(String woeid) {

		String weatherString = "";
		try {
			weatherString = (String) new ContactServerTask(woeid).execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document weatherDoc = convertStringToDocument(weatherString);

		MyWeather weatherResult = parseWeather(weatherDoc);
		return weatherResult;
	}


	private static MyWeather parseWeather(Document srcDoc){

		MyWeather myWeather = new MyWeather();

		//<description>Yahoo! Weather for New York, NY</description>
		myWeather.description = srcDoc.getElementsByTagName("description").item(0).getTextContent();

		//<yweather:location city="New York" region="NY" country="United States"/>
		Node locationNode = srcDoc.getElementsByTagName("yweather:location").item(0);
		myWeather.city = locationNode.getAttributes().getNamedItem("city").getNodeValue().toString();
		myWeather.region = locationNode.getAttributes().getNamedItem("region").getNodeValue().toString();
		myWeather.country = locationNode.getAttributes().getNamedItem("country").getNodeValue().toString();

		//<yweather:wind chill="60" direction="0" speed="0"/>
		Node windNode = srcDoc.getElementsByTagName("yweather:wind").item(0);
		myWeather.windChill = windNode.getAttributes().getNamedItem("chill").getNodeValue().toString();
		myWeather.windDirection = windNode.getAttributes().getNamedItem("direction").getNodeValue().toString();
		myWeather.windSpeed = windNode.getAttributes().getNamedItem("speed").getNodeValue().toString();

		//<yweather:astronomy sunrise="6:52 am" sunset="7:10 pm"/>
		Node astronomyNode = srcDoc.getElementsByTagName("yweather:astronomy").item(0);
		myWeather.sunrise = astronomyNode.getAttributes().getNamedItem("sunrise").getNodeValue().toString();
		myWeather.sunset = astronomyNode.getAttributes().getNamedItem("sunset").getNodeValue().toString();

		//<yweather:condition text="Fair" code="33" temp="60" date="Fri, 23 Mar 2012 8:49 pm EDT"/>
		Node conditionNode = srcDoc.getElementsByTagName("yweather:condition").item(0);
		myWeather.conditiontext = conditionNode.getAttributes().getNamedItem("text").getNodeValue().toString();
		myWeather.conditiondate = conditionNode.getAttributes().getNamedItem("date").getNodeValue().toString();
		myWeather.temperature = conditionNode.getAttributes().getNamedItem("temp").getNodeValue().toString();

		Node imgURLNode = srcDoc.getElementsByTagName("description").item(1);
		myWeather.imgURL = (imgURLNode.getTextContent().split(">")[0]).split("\"")[1];
		return myWeather;
	}

	private static Document convertStringToDocument(String src){
		Document dest = null;

		DocumentBuilderFactory dbFactory =
				DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try {
			parser = dbFactory.newDocumentBuilder();
			dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dest;
	}

	private static String QueryYahooWeather(String woeid){
		Log.i("MSG", "Query with woeid = "+woeid);
		String qResult = "";
		String queryString = "http://weather.yahooapis.com/forecastrss?w="+woeid;
		Log.i("MSG", "Query URL is "+queryString);
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(queryString);

		try {
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

			if (httpEntity != null){
				InputStream inputStream = httpEntity.getContent();
				Reader in = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(in);
				StringBuilder stringBuilder = new StringBuilder();

				String stringReadLine = null;

				while ((stringReadLine = bufferedreader.readLine()) != null) {
					stringBuilder.append(stringReadLine + "\n");	
				}

				qResult = stringBuilder.toString();	
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return qResult;
	}
}