package jerry.shen.plugin;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MainService extends Service{
    
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final int INTERVAL_TIME = 4000;
    private String LATITUDE = "0";
    private String LONGITUDE = "0";
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
        Log.i("MainService", "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
        Log.i("MainService", "onStartCommand");
        LATITUDE = intent.getStringExtra("latitude");
        LONGITUDE = intent.getStringExtra("longitude");
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
                loc.getLatitude();
                loc.getLongitude();
                Log.i("MainService", String.valueOf(loc.getLatitude()));
                Log.i("MainService", String.valueOf(loc.getLongitude()));
                Log.i("MainService", LATITUDE);
                Log.i("MainService", LONGITUDE);
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