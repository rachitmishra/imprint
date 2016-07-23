package in.ceeq.imprint.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import in.ceeq.imprint.PermissionRationaleDialogFragment;
import in.ceeq.imprint.R;

public class PermissionUtils {

    // Request code to open application settings screen
    public static final int REQUEST_CODE_APP_PERMISSION_SCREEN = 100;

    // Request code for requesting permission to read external storage
    public static final int REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE = 103;

    public static final int REQUEST_CODE_PERMISSION_USE_FINGERPRINT = 104;

    public static final int REQUEST_CODE_PERMISSION_DRAW_OVERLAY = 105;

    public static final int REQUEST_CODE_PERMISSION_ACCESSIBILITY_SETTINGS = 105;

    /**
     * Check if the application has permission
     *
     * @param context        Context/Activity
     * @param permissionCode Permission code
     * @return true/false based on permission granted or denied
     */
    public static boolean hasPermission(Context context, String permissionCode) {
        return ContextCompat.checkSelfPermission(context, permissionCode) == PackageManager
                .PERMISSION_GRANTED;
    }


    /**
     * Handle permission to read external storage not granted by the user
     * Request the permission again or show a Snackbar disabling the feature
     *
     * @param activity Context/Activity
     */
    public static void handleExternalStoragePermissionDenied(final Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(activity.findViewById(android.R.id.content), R.string.permission_error_read_external_storage,
                    Snackbar.LENGTH_LONG).setAction(R.string.label_allow, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPermissionSettingsScreen(activity, REQUEST_CODE_APP_PERMISSION_SCREEN);
                }
            }).setActionTextColor(ContextCompat.getColor(activity, R.color.yellow_tan)).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE);
        }
    }

    /**
     * Handle permission to read external storage not granted by the user
     * Request the permission again or show a Snackbar disabling the feature
     *
     * @param activity Context/Activity
     */
    public static void handleFingerPrintPermissionDenied(final Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.USE_FINGERPRINT)) {
            Snackbar.make(activity.findViewById(android.R.id.content), R.string.permission_error_fingerprint,
                    Snackbar.LENGTH_LONG).setAction(R.string.label_allow, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPermissionSettingsScreen(activity, REQUEST_CODE_APP_PERMISSION_SCREEN);
                }
            }).setActionTextColor(ContextCompat.getColor(activity, R.color.yellow_tan)).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.USE_FINGERPRINT}, REQUEST_CODE_PERMISSION_USE_FINGERPRINT);
        }
    }


    /**
     * Open application settings page
     *
     * @param activity    Context/Activity
     * @param requestCode Request code
     */
    public static void openPermissionSettingsScreen(Activity activity, int requestCode) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" +
                activity.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(myAppSettings, requestCode);
    }

    private void showPermissionDialog(AppCompatActivity activity, String title, String message,
                                      DialogInterface.OnClickListener positiveClickListner) {

        PermissionRationaleDialogFragment.show(activity.getSupportFragmentManager(), null);
    }

    private static void requestPermissionFromActivity(final FragmentActivity fragmentActivity,
                                                      final String permission) {
//        ActivityCompat.requestPermissions(fragmentActivity,
//                new String[]{permission}, getRequestCodeByPermission(permission));
    }

    private static void requestPermissionFromFragment(final Fragment fragment, final String permission) {
        if(fragment != null) {
            fragment.requestPermissions(new String[]{permission}, getRequestCodeByPermission(permission));
        }
    }

    private static void openAccessibilitySettingScreen(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS,
                                          Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, REQUEST_CODE_PERMISSION_ACCESSIBILITY_SETTINGS);
    }

    private static void openOverlayDrawSettingScreen(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                          Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, REQUEST_CODE_PERMISSION_DRAW_OVERLAY);

    }
}
