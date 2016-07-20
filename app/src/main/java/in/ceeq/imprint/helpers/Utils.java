package in.ceeq.imprint.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {

    public static final Pattern EMAIL_ADDRESS = Pattern
            .compile(
                    "^[a-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+(\\.[a-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+)" +
                            "*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2,})$");

   public static boolean isNetConnected(Context context) {
        if (context != null) {
            final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return !(networkInfo == null || !networkInfo.isConnectedOrConnecting());
        } else {
            return false;
        }
    }

    public static boolean isEmptyString(String value) {
        return !(value != null && !value.trim().isEmpty());
    }

    public static boolean isEmptyArrayString(String arrayString) {
        return !(arrayString != null && !arrayString.trim().isEmpty() && !arrayString.equals("[]") && !arrayString
                .equals("[ ]"));
    }

    public static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isEmptyMap(HashMap map) {
        return (map == null || map.size() <= 0);
    }

    public static boolean isEmptyList(ArrayList arrayList) {
        return (arrayList == null || arrayList.size() <= 0);
    }

    public static boolean isEmptyList(List arrayList) {
        return (arrayList == null || arrayList.size() <= 0);
    }

}
