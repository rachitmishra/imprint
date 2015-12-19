package in.ceeq.imprint.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

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

    public static String getFormattedTitle(String str) {
        if (!TextUtils.isEmpty(str)) {
            StringBuilder ret = new StringBuilder();
            if (str.contains("-")) {
                String[] words = str.trim().split("-");
                for (int i = 0; i < words.length; i++) {
                    if (!TextUtils.isEmpty(words[i])) {
                        ret.append(Character.toUpperCase(words[i].charAt(0)));
                        ret.append(words[i].substring(1));
                        if (i < words.length - 1) {
                            ret.append(' ');
                        }
                    }
                }
            } else {
                ret.append(Character.toUpperCase(str.charAt(0)));
                ret.append(str.substring(1));
            }
            return ret.toString();
        }
        return str;
    }

    /**
     * source http://stackoverflow.com/a/16108347
     * @param original Original String
     * @return
     */
    public static ArrayList<String> splitString (String original)
    {
        ArrayList<String> splitted = new ArrayList<String>();
        int skipCommas = 0;
        String s = "";
        for ( char c : original.toCharArray() )
        {
            if ( c==',' && skipCommas == 0)
            {
                splitted.add (s);
                s="";
            } else {
                if ( c=='(' )
                    skipCommas++;
                if ( c==')' )
                    skipCommas--;
                s+= c;
            }
        }
        splitted.add(s);
        return splitted;
    }

    public static boolean isEmptyList(ArrayList arrayList) {
        return (arrayList == null || arrayList.size() <= 0);
    }

    public static boolean isEmptyList(List arrayList) {
        return (arrayList == null || arrayList.size() <= 0);
    }

}
