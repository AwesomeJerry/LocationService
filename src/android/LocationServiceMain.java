package jerry.shen.plugin;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationServiceMain extends Service{
    
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final int INTERVAL_TIME = 4000;
    private String LATITUDE = "0";
    private String LONGITUDE = "0";
    private String REGID = "";
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
        Log.i("LocationServiceMain", "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
        Log.i("LocationServiceMain", "onStartCommand");
        LATITUDE = intent.getStringExtra("latitude");
        LONGITUDE = intent.getStringExtra("longitude");
        REGID = intent.getStringExtra("regid");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, INTERVAL_TIME, 0, listener);
        } catch (Exception e) {
            
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL_TIME, 0, listener);
        } catch (Exception e) {
            
        }
        
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
        Log.i("LocationServiceMain", "onDestroy");
        locationManager.removeUpdates(listener);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
    
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
        // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }



/** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
          return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(final Location loc)
        {
            Log.i("**************************************", "Location changed");
            if(isBetterLocation(loc, previousBestLocation)) {
                Double curLat = loc.getLatitude();
                Double curLng = loc.getLongitude();
                Log.i("LocationServiceMain", String.valueOf(loc.getLatitude()));
                Log.i("LocationServiceMain", String.valueOf(loc.getLongitude()));
                Log.i("LocationServiceMain", LATITUDE);
                Log.i("LocationServiceMain", LONGITUDE);
                Double lat = Double.parseDouble(LATITUDE);
                Double lng = Double.parseDouble(LONGITUDE);
                if(Math.abs(curLat - lat) < 0.0001 && Math.abs(curLng - lng) < 0.0001) {
                    Log.i("LocationServiceMain", "You are there!");
                    new makeRequest().execute();
                }
            }
        }

        public void onProviderDisabled(String provider)
        {

        }

        public void onProviderEnabled(String provider)
        {

        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }
    
    private class makeRequest extends AsyncTask<URL, Integer, Long> {
    	
        protected Long doInBackground(URL... urls) {
            HttpClient httpclient =  new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://140.119.19.45/gashatrip/notification/sendNotification.php");

            // Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("regid", REGID));

            //Execute and get the response.
            try {
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                if (entity != null) {       
                    try {
                        // do something useful
                    } finally {
                        try {
                            InputStream instream = entity.getContent();
                            instream.close();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }

}