package in.ceeq.imprint;


import android.app.KeyguardManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.ceeq.imprint.adapters.AppListRecyclerAdapter;
import in.ceeq.imprint.adapters.BaseRecyclerAdapter;
import in.ceeq.imprint.entity.App;
import in.ceeq.imprint.helpers.PreferenceUtils;

public class AppsFragment extends Fragment implements BaseRecyclerAdapter.OnViewAppListener {

    private KeyguardManager mKeyguardManager;
    private FingerprintManager mFingerprintManager;
    private FingerprintAuthDialogFragment mFragment;
    private CipherKeyHelper mCipherKeyHelper;

    public AppsFragment() {
    }

    public static AppsFragment newInstance() {
        AppsFragment fragment = new AppsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_list, container, false);

        ArrayList<App> applicationList = getApplicationList();
        RecyclerView applicationRecyclerView =
                (RecyclerView) view.findViewById(R.id.recycler_view_application_list);
        applicationRecyclerView.setHasFixedSize(Boolean.TRUE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        applicationRecyclerView.setLayoutManager(linearLayoutManager);
        BaseRecyclerAdapter mBaseRecyclerAdapter =
                new AppListRecyclerAdapter(getContext(), applicationList, this);
        applicationRecyclerView.setAdapter(mBaseRecyclerAdapter);

        mCipherKeyHelper = CipherKeyHelper.newInstance();
        initFingerPrintAuth();
        return view;
    }

    private void initFingerPrintAuth() {
        mFragment = new FingerprintAuthDialogFragment();
        mFingerprintManager = getContext().getSystemService(FingerprintManager.class);
        mKeyguardManager = getContext().getSystemService(KeyguardManager.class);

        mCipherKeyHelper.init();
    }

    @Override
    public void onViewApp(final App app) {
        if (!mKeyguardManager.isKeyguardSecure()) {
            // Show a message that the user hasn't set up a fingerprint or lock screen.
            Snackbar.make(getView(),
                    "Secure lock screen hasn't set up.\n"
                            + "Go to 'Settings -> Security -> Fingerprint' to set up a fingerprint",
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        //noinspection ResourceType
        if (!mFingerprintManager.hasEnrolledFingerprints()) {
            // This happens when no fingerprints are registered.
            Snackbar.make(getView(),
                    "Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint",
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        mCipherKeyHelper.createKey();
        if (mCipherKeyHelper.initCipher()) {
            // Show the fingerprint dialog. The user has the option to use the fingerprint with
            // crypto, or you can fall back to using a server-side verified password.
            mFragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipherKeyHelper.getCipher()));
            boolean useFingerprintPreference =
                    PreferenceUtils.newInstance(getContext()).getBooleanPrefs(PreferenceUtils.USE_FINGERPRINT);
            if (useFingerprintPreference) {
                mFragment.setStage(
                        FingerprintAuthDialogFragment.Stage.FINGERPRINT);
            } else {
                mFragment.setStage(
                        FingerprintAuthDialogFragment.Stage.PASSWORD);
            }
            mFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment_tag");
        } else {
            // This happens if the lock screen has been disabled or or a fingerprint got
            // enrolled. Thus show the dialog to authenticate with their password first
            // and ask the user if they want to authenticate with fingerprints in the
            // future
            mFragment.setCryptoObject(
                    new FingerprintManager.CryptoObject(mCipherKeyHelper.getCipher()));
            mFragment.setStage(
                    FingerprintAuthDialogFragment.Stage.NEW_FINGERPRINT_ENROLLED);
            mFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment_tag");
        }
    }

    private ArrayList<App> getApplicationList() {
        ArrayList<App> applicationList = new ArrayList<>();
        final PackageManager pm = getContext().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo : packages) {
            if (pm.getLaunchIntentForPackage(applicationInfo.packageName) != null) {
                // apps with launcher intent
                if ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    // updated system apps

                } else if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    // system apps

                } else {
                    // user installed apps
                    App app = new App();
                    app.name = applicationInfo.loadLabel(pm).toString();
                    app.logo = applicationInfo.loadIcon(pm);
                    applicationList.add(app);
                }
            }
        }

        return applicationList;
    }

    public void onPurchased(boolean withFingerprint) {
        if (withFingerprint) {
            // If the user has authenticated with fingerprint, verify that using cryptography and
            // then show the confirmation message.
            showConfirmation(mCipherKeyHelper.tryEncrypt());
        } else {
            // Authentication happened with backup password. Just show the confirmation message.
            showConfirmation(null);
        }
    }

    // Show confirmation, if fingerprint was used show crypto information.
    private void showConfirmation(byte[] encrypted) {
//		findViewById(R.id.confirmation_message).setVisibility(View.VISIBLE);
//		if (encrypted != null) {
//			TextView v = (TextView) findViewById(R.id.encrypted_message);
//			v.setVisibility(View.VISIBLE);
//			v.setText(Base64.encodeToString(encrypted, 0 /* flags */));
//		}
    }
}
