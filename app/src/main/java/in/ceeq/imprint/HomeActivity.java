package in.ceeq.imprint;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.crashlytics.android.Crashlytics;

import in.ceeq.imprint.helpers.PermissionUtils;
import in.ceeq.imprint.helpers.PreferenceUtils;
import io.fabric.sdk.android.Fabric;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_app_list);

        initToolbar(findViewById(R.id.toolbar), getString(R.string.app_name));
        setStatusBarColor();
        setupPager();

        requestPermissions();
    }

    private void requestPermissions() {

        if (!PermissionUtils.hasPermission(this, Manifest.permission.USE_FINGERPRINT)) {
            PermissionUtils.handleFingerPrintPermissionDenied(this);
        }

        if (!PermissionUtils.hasPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            PermissionUtils.showOverlayPermissionDialog(this, "Allow to draw!");
        }

        if (!PreferenceUtils.newInstance(this).getBooleanPrefs(PreferenceUtils.IS_ACCESSIBILITY_SERVICE_RUNNING)) {
            PermissionUtils.showAccessibilityServicePermissionDialog(this, "Allow to accessibility!");
        }

        // startService(new Intent(this, EventService.class));
    }

    private void setupPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        homePagerAdapter.addFragment(AppsFragment.newInstance(),
                getString(R.string.tab_title_all));
        homePagerAdapter.addFragment(AppsFragment.newInstance(),
                getString(R.string.tab_title_secured));
        viewPager.setAdapter(homePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
