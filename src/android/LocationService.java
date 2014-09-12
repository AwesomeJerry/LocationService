package jerry.shen.plugin;
 
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.content.Intent;

public class LocationService extends CordovaPlugin {
    public static final String ACTION_START_SERVICE = "startService";
    public static final String ACTION_STOP_SERVICE = "stopService";
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if (ACTION_START_SERVICE.equals(action)) { 
                JSONObject arg_object = args.getJSONObject(0);
                Intent intent = new Intent(this.cordova.getActivity(), MainService.class);
                this.cordova.getActivity().startService(intent);
                callbackContext.success();
                return true;
            } else if (ACTION_STOP_SERVICE.equals(action)) { 
                JSONObject arg_object = args.getJSONObject(0);
                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                this.cordova.getActivity().startActivity(intent);
                callbackContext.success();
                return true;
            }
            callbackContext.error("Invalid action");
            return false;
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        } 
    }
}