package in.ceeq.imprint.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.provider.Settings;
import android.view.accessibility.AccessibilityEvent;

import in.ceeq.imprint.helpers.DeviceUtils;
import in.ceeq.imprint.helpers.PreferenceUtils;


public class EventService extends AccessibilityService {

    private static final String TAG = "EventService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (event.getPackageName().equals("com.whatsapp")) {
                    if (DeviceUtils.hasMarshmallow()) {
                        if (Settings.canDrawOverlays(this)) {
                            startService(new Intent(this, LockService.class));
                        }
                    }
                }
        }
    }

    @Override
    public void onInterrupt() {

        PreferenceUtils.newInstance(this).set(PreferenceUtils.IS_ACCESSIBILITY_SERVICE_RUNNING, false);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);

        PreferenceUtils.newInstance(this).set(PreferenceUtils.IS_ACCESSIBILITY_SERVICE_RUNNING, true);
    }

}
