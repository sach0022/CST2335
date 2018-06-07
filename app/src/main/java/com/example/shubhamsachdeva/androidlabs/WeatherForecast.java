package com.example.shubhamsachdeva.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends Activity {
    ProgressBar progressBar;


    TextView currentTemp ;
    TextView minTemp ;
    TextView windSpeed;
    TextView maxTemp ;
    ImageView weatherImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

         progressBar = (ProgressBar) findViewById(R.id.bar);
        progressBar.setVisibility(View.VISIBLE);

        currentTemp = (TextView) findViewById(R.id.current);
        minTemp = (TextView) findViewById(R.id.miniTemp);
        maxTemp = (TextView) findViewById(R.id.maxTemp);
       weatherImage = (ImageView) findViewById(R.id.weath);
windSpeed=(TextView)findViewById(R.id.windSpeed);
ForecastQuery weather = new ForecastQuery();
weather.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
    }


public class ForecastQuery extends AsyncTask<String  , Integer, String> {

    String cTemp;
    String minimumT;
    String maximumT;
    String wind;
    String weatherImg;
    HttpURLConnection urlConnection;
    private Bitmap bitmap;
    @Override
    protected String doInBackground(String... strings) {
        try {
            for(String siteUrl: strings) {
                URL url = new URL(siteUrl);

                 urlConnection = (HttpURLConnection) url.openConnection();
                InputStream iStream = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp= factory.newPullParser();
                xpp.setInput( iStream  , "UTF-8");

//start reading:
                int type;
                //While you're not at the end of the document:
                while((type = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    //Are you currently at a Start Tag?
                    if(xpp.getEventType() == xpp.START_TAG)
                    {
                        //Is it the "AMessage" tag?
                        if(xpp.getName().equals("temperature") )
                        {
                             cTemp =xpp.getAttributeValue(null, "value");
                            Log.i("Current Temperature:" , cTemp );
                            publishProgress(25);
                            minimumT =xpp.getAttributeValue(null, "min");
                            Log.i("Minimum Temperature:" , minimumT);
                            publishProgress(50);
                            maximumT =xpp.getAttributeValue(null, "max");
                            Log.i("Maximum Temperature:" , maximumT);
                            publishProgress(75);
                        }
                        //Is it the Weather tag?
                        else if(xpp.getName().equals("speed") )
                        {
//                            FileOutputStream of = openFileOutput("XMLData", Context.MODE_PRIVATE);
                            wind =xpp.getAttributeValue(null, "value");
                            Log.i("Wind Speed: " , wind );
                        }
                        else if(xpp.getName().equals("weather")){
                            weatherImg = xpp.getAttributeValue(null, "icon");
                            String iconFile = weatherImg+".png";
                            if(fileExist(iconFile)){
                                FileInputStream fis = null;
                                try {    fis = openFileInput(iconFile);   }
                                catch (FileNotFoundException e) {    e.printStackTrace();  }
                                bitmap = BitmapFactory.decodeStream(fis);

                            } else {
                                URL iconUrl = new URL("https://openweathermap.org/img/w/" + weatherImg + ".png");
                                bitmap  = HttpUtils.getImage(iconUrl);
                                FileOutputStream outputStream = openFileOutput( weatherImg + ".png", Context.MODE_PRIVATE);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();


                            }
                            publishProgress(100);
                        }
                    }
                    // Go to the next XML event
                    xpp.next();
                }
            }


        }catch (Exception mfe)
        {
            Log.e("Error", mfe.getMessage());
        }
        //Send a string to the GUI thread through onPostExecute
        return "Finished";

    }

    //when GUI is ready, update the objects
    public void onProgressUpdate(Integer... data)
    {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(data[0]);
    }
    public boolean fileExist(String name){
        File file = getBaseContext().getFileStreamPath(name);
        return file.exists();
    }
    //gui thread
    public void onPostExecute(String result)
    {
        currentTemp.setText(currentTemp.getText()+cTemp);
        minTemp.setText(minTemp.getText()+ minimumT);
        maxTemp.setText(maxTemp.getText()+maximumT);
        windSpeed.setText(windSpeed.getText()+wind);
        weatherImage.setImageBitmap(bitmap);
        progressBar.setVisibility(View.INVISIBLE);

    }
}

}

class HttpUtils {
    public static Bitmap getImage(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public static Bitmap getImage(String urlString) {
        try {
            URL url = new URL(urlString);
            return getImage(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
