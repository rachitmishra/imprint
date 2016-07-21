package in.ceeq.imprint;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import in.ceeq.imprint.helpers.DeviceUtils;


public class BaseActivity extends AppCompatActivity {

    public void initToolbar(View toolbar, String title) {
        setSupportActionBar((Toolbar) toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(Boolean.TRUE);
        setTitle("");
        TextView titleTv = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleTv.setText(title);
    }

    public void setStatusBarColor() {
        // set status bar color
        final int color = ResourcesCompat.getColor(getResources(), R.color.colorAccent, null);
        if (DeviceUtils.hasLollipop()) {
            final Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
            window.setNavigationBarColor(color);
        }
    }
}
