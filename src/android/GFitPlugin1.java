import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import android.util.Log;
import android.provider.Settings;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GFitPlugin extends CordovaPlugin {

    private static final int REQUEST_OAUTH = 1;

    /**
    *  Track whether an authorization activity is stacking over the current activity, i.e. when
    *  a known auth error is being resolved, such as showing the account chooser or presenting a
    *  consent dialog. This avoids common duplications as might happen on screen rotations, etc.
    */
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;

    private GoogleApiClient mClient = null;

    public static final String TAG = "Google Fit Plugin";

    /**
    * Constructor.
    */
    public GFitPlugin() {}

        /**
        * Sets the context of the Command. This can then be used to do things like
        * get file paths associated with the Activity.
        *
        * @param cordova The context of the main Activity.
        * @param webView The CordovaWebView Cordova is running in.
        */
        public void initialize(CordovaInterface cordova, CordovaWebView webView) {
            super.initialize(cordova, webView);
            Log.v(TAG,"Init GFitPlugin");
        }

        public DataReadResult execute(final String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

            final int duration = Toast.LENGTH_SHORT;
            // Shows a toast
            Log.v(TAG,"Google Fit Plugin received: "+ action);

            // cordova.getActivity().runOnUiThread(new Runnable() {
            //     public void run() {
            //         Toast toast = Toast.makeText(cordova.getActivity().getApplicationContext(), action, duration);
            //         toast.show();
            //     }
            // });

            return fetchHistoryData();
        }

        private DataReadResult fetchHistoryData() {
            // Setting a start and end date using a range of 1 week before this moment.
            String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSZ";
            Calendar cal = Calendar.getInstance();
            Date now = new Date();
            cal.setTime(now);
            long endTime = cal.getTimeInMillis();
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            long startTime = cal.getTimeInMillis();

            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
            Log.i(TAG, "Range End: " + dateFormat.format(endTime));

            DataReadRequest readRequest = new DataReadRequest.Builder()
            // The data request can specify multiple data types to return, effectively
            // combining multiple data queries into one call.
            // In this example, it's very unlikely that the request is for several hundred
            // datapoints each consisting of a few steps and a timestamp.  The more likely
            // scenario is wanting to see how many steps were walked per day, for 7 days.
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            // Analogous to a "Group By" in SQL, defines how data should be aggregated.
            // bucketByTime allows for a time span, whereas bucketBySession would allow
            // bucketing by "sessions", which would need to be defined in code.
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build();
            // Invoke the History API to fetch the data with the query and await the result of
            // the read request.
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);
            return dataReadResult;
        }

}
