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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.net.Uri;
import android.R;

public class LocationServiceMain extends Service{
    
    private int TWO_MINUTES = 1000 * 60 * 2;
    private int CHECK_INTERVAL = 10000;
    private double ARRIVED_RANGE = 0.0003;
    private String ARRIVED_TITLE = "GashaTrip";
    private String ARRIVED_MESSAGE = "關心您";
    private String START_TITLE = "關心您";
    private String START_MESSAGE = "關心您";
    private String LATITUDE = "0";
    private String LONGITUDE = "0";
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    public String CLASSNAME = "";
    public String PACKAGENAME = "";
    public String REALNAME = "";
    Class<?> mainClass;

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
        START_TITLE = intent.getStringExtra("start_title");
        START_MESSAGE = intent.getStringExtra("start_message");
        ARRIVED_TITLE = intent.getStringExtra("arrived_title");
        ARRIVED_MESSAGE = intent.getStringExtra("arrived_message");
        CHECK_INTERVAL =  Integer.parseInt(intent.getStringExtra("check_interval"));
        ARRIVED_RANGE = Double.parseDouble(intent.getStringExtra("arrived_range"));
        LATITUDE = intent.getStringExtra("latitude");
        LONGITUDE = intent.getStringExtra("longitude");
        PACKAGENAME = intent.getStringExtra("package");
        CLASSNAME = intent.getStringExtra("class");
        REALNAME = PACKAGENAME + "." + CLASSNAME;
        try {
            mainClass = Class.forName(REALNAME);
        } catch (Exception e) {
            System.out.println(e);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener(this);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, CHECK_INTERVAL, 0, listener);
        } catch (Exception e) {
            
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, CHECK_INTERVAL, 0, listener);
        } catch (Exception e) {
            
        }
        int iconNo = getResources().getIdentifier("notiicon", "drawable", PACKAGENAME);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(iconNo)
                .setContentTitle(START_TITLE)
                .setContentText(START_MESSAGE);
        Intent resultIntent = new Intent(getApplicationContext(), mainClass);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(mainClass);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
        
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
    	Context callClass;
        public MyLocationListener(Context LocationServiceMain) {
			callClass = LocationServiceMain;
		}

		public void onLocationChanged(final Location loc)
        {
            Log.i("LocationServiceMain", "Location changed");
            if(isBetterLocation(loc, previousBestLocation)) {
                Double curLat = loc.getLatitude();
                Double curLng = loc.getLongitude();
                Log.i("LocationServiceMain", String.valueOf(loc.getLatitude()));
                Log.i("LocationServiceMain", String.valueOf(loc.getLongitude()));
                Log.i("LocationServiceMain", LATITUDE);
                Log.i("LocationServiceMain", LONGITUDE);
                Double lat = Double.parseDouble(LATITUDE);
                Double lng = Double.parseDouble(LONGITUDE);
                if(Math.abs(curLat - lat) < ARRIVED_RANGE && Math.abs(curLng - lng) < ARRIVED_RANGE) {
                    Log.i("LocationServiceMain", "You are there!");
                    int iconNo = getResources().getIdentifier("notiicon", "drawable", PACKAGENAME);
                    int soundNo = getResources().getIdentifier("dingdong", "raw", PACKAGENAME);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(iconNo)
                            .setContentTitle(ARRIVED_TITLE)
                            .setContentText(ARRIVED_MESSAGE)
                            .setSound(Uri.parse("android.resource://" + PACKAGENAME + "/raw/dingdong"));
                    Intent resultIntent = new Intent(getApplicationContext(), mainClass);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    stackBuilder.addParentStack(mainClass);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, mBuilder.build());
                    Intent intent = new Intent(callClass, LocationServiceMain.class);
                    stopService(intent);
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
}