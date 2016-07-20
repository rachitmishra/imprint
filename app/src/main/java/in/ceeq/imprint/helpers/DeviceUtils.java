package in.ceeq.imprint.helpers;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;


public class DeviceUtils {

    private final String PREFERENCE_DEVICE_SIZE = "X-DEVICE-SIZE";
    private PreferenceUtils mPreferenceUtils;

    public DeviceUtils(Context context) {
        mPreferenceUtils = PreferenceUtils.newInstance(context);
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasJellybeanMr2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasMoreHeap() {
        return Runtime.getRuntime().maxMemory() > 20971520;
    }

    public String getDeviceResolution() {
        return mPreferenceUtils.getStringPrefs(PREFERENCE_DEVICE_SIZE);
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public final class Size {
        public static final String SMALL = "small";
        public static final String MEDIUM = "medium";
        public static final String LARGE = "large";
        public static final String XLARGE = "xlarge";
    }
}
