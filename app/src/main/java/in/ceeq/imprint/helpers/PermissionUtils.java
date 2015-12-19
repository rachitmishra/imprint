package in.ceeq.imprint.helpers;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import in.ceeq.imprint.R;

public class PermissionUtils {

    // Request code to open application settings screen
    public static final int REQUEST_CODE_APP_PERMISSION_SCREEN = 100;

    // Request code for requesting permission to get location
    public static final int REQUEST_CODE_PERMISSION_LOCATION = 102;

    // Request code for requesting permission to read external storage
    public static final int REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE = 103;

    // Request code for requesting permission to receive SMS
    public static final int REQUEST_CODE_PERMISSION_RECEIVE_SMS = 104;

    public static final int REQUEST_CODE_PERMISSION_USE_FINGERPRINT = 104;

    /**
     * Check if the application has permission
     *
     * @param context Context/Activity
     * @param permissionCode Permission code
     *
     * @return true/false based on permission granted or denied
     */
    public static boolean hasPermission(Context context, String permissionCode) {
        return ContextCompat.checkSelfPermission(context, permissionCode) == PackageManager
                                                                                     .PERMISSION_GRANTED;
    }

    /**
     * Handle permission to get location not granted by the user
     * Request the permission again or show a Snackbar disabling the feature
     *
     * @param activity Context/Activity
     */
    public static void handleLocationPermissionDenied(final Activity activity) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(activity.findViewById(android.R.id.content), R.string.permission_error_location,
                    Snackbar.LENGTH_LONG).setAction(R.string.enable, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPermissionSettingsScreen(activity, REQUEST_CODE_APP_PERMISSION_SCREEN);
                }
            }).setActionTextColor(ContextCompat.getColor(activity, R.color.yellow_500)).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION   },
                    REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    /**
     * Handle permission to receive SMS not granted by the user
     * Request the permission again or show a Snackbar disabling the feature
     *
     * @param activity Context/Activity
     */
    public static void handleReceiveSmsPermissionDenied(final Activity activity) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECEIVE_SMS)) {
            Snackbar.make(activity.findViewById(android.R.id.content), R.string.permission_error_receive_message,
                    Snackbar.LENGTH_LONG).setAction(R.string.enable, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPermissionSettingsScreen(activity, REQUEST_CODE_APP_PERMISSION_SCREEN);
                }
            }).setActionTextColor(ContextCompat.getColor(activity, R.color.yellow_500)).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[] {  Manifest.permission.RECEIVE_SMS},
                    REQUEST_CODE_PERMISSION_RECEIVE_SMS);
        }
    }

    /**
     * Handle permission to read external storage not granted by the user
     * Request the permission again or show a Snackbar disabling the feature
     *
     * @param activity Context/Activity
     */
    public static void handleExternalStoragePermissionDenied(final Activity activity) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(activity.findViewById(android.R.id.content), R.string.permission_error_read_external_storage,
                    Snackbar.LENGTH_LONG).setAction(R.string.label_allow, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPermissionSettingsScreen(activity, REQUEST_CODE_APP_PERMISSION_SCREEN);
                }
            }).setActionTextColor(ContextCompat.getColor(activity, R.color.yellow_500)).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE);
        }
    }

    /**
     * Handle permission to read external storage not granted by the user
     * Request the permission again or show a Snackbar disabling the feature
     *
     * @param activity Context/Activity
     */
    public static void handleFingerPrintPermissionDenied(final Activity activity) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.USE_FINGERPRINT)) {
            Snackbar.make(activity.findViewById(android.R.id.content), R.string.permission_error_fingerprint,
                    Snackbar.LENGTH_LONG).setAction(R.string.label_allow, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPermissionSettingsScreen(activity, REQUEST_CODE_APP_PERMISSION_SCREEN);
                }
            }).setActionTextColor(ContextCompat.getColor(activity, R.color.yellow_500)).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.USE_FINGERPRINT }, REQUEST_CODE_PERMISSION_USE_FINGERPRINT);
        }
    }


    /**
     * Open application settings page
     *
     * @param activity Context/Activity
     * @param requestCode Request code
     */
    public static void openPermissionSettingsScreen(Activity activity, int requestCode) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" +
                                                                                                          activity.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(myAppSettings, requestCode);
    }
}
