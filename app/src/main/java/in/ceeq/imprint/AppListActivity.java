package in.ceeq.imprint;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import in.ceeq.imprint.adapters.AppListRecyclerAdapter;
import in.ceeq.imprint.adapters.BaseRecyclerAdapter;
import in.ceeq.imprint.entity.App;
import in.ceeq.imprint.helpers.PermissionUtils;
import in.ceeq.imprint.helpers.PreferenceUtils;
import io.fabric.sdk.android.Fabric;

public class AppListActivity extends BaseAppActivity implements BaseRecyclerAdapter.OnViewAppListener{

	private KeyguardManager mKeyguardManager;
	private FingerprintManager mFingerprintManager;
	private FingerprintAuthenticationDialogFragment mFragment;
	private KeyStore mKeyStore;
	private KeyGenerator mKeyGenerator;
	private Cipher mCipher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_app_list);
	    initToolbar();
        ArrayList<App> applicationList = getApplicationList();
        RecyclerView applicationRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_application_list);
        applicationRecyclerView.setHasFixedSize(Boolean.TRUE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        applicationRecyclerView.setLayoutManager(linearLayoutManager);
        BaseRecyclerAdapter mBaseRecyclerAdapter = new AppListRecyclerAdapter(this, applicationList,this);
        applicationRecyclerView.setAdapter(mBaseRecyclerAdapter);
	    mFragment = new FingerprintAuthenticationDialogFragment();

	    mFingerprintManager = getSystemService(FingerprintManager.class);
	    mKeyguardManager = getSystemService(KeyguardManager.class);
	    try {
	    mKeyStore = KeyStore.getInstance("AndroidKeyStore");
	    mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

		    mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
					                              + KeyProperties.BLOCK_MODE_CBC + "/"
					                              + KeyProperties.ENCRYPTION_PADDING_PKCS7);
	    } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | KeyStoreException e) {
		    e.printStackTrace();
	    }

	    if(!PermissionUtils.hasPermission(this, Manifest.permission.USE_FINGERPRINT)) {
		    PermissionUtils.handleFingerPrintPermissionDenied(this);
	    }
    }

	private void initToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		setTitle("");
		TextView titleTv = (TextView) toolbar.findViewById(R.id.toolbar_title);
		titleTv.setText(getString(R.string.app_name));
		//setStatusBarColor(findViewById(R.id.statusBarBackground), ContextCompat.getColor(this, R.color.colorPrimary));
	}

    @Override
    public void onViewApp(final App app) {
	    if (!mKeyguardManager.isKeyguardSecure()) {
		    // Show a message that the user hasn't set up a fingerprint or lock screen.
		    Toast.makeText(this,
				    "Secure lock screen hasn't set up.\n"
						    + "Go to 'Settings -> Security -> Fingerprint' to set up a fingerprint",
				    Toast.LENGTH_LONG).show();
		    return;
	    }

	    //noinspection ResourceType
	    if (!mFingerprintManager.hasEnrolledFingerprints()) {
		    // This happens when no fingerprints are registered.
		    Toast.makeText(this,
				    "Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint",
				    Toast.LENGTH_LONG).show();
		    return;
	    }

	    createKey();
	    if (initCipher()) {

		    // Show the fingerprint dialog. The user has the option to use the fingerprint with
		    // crypto, or you can fall back to using a server-side verified password.
		    mFragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
		    boolean useFingerprintPreference = PreferenceUtils.getBooleanPrefs(this, PreferenceUtils.USE_FINGERPRINT);
		    if (useFingerprintPreference) {
			    mFragment.setStage(
					    FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);
		    } else {
			    mFragment.setStage(
					    FingerprintAuthenticationDialogFragment.Stage.PASSWORD);
		    }
		    mFragment.show(getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
	    } else {
		    // This happens if the lock screen has been disabled or or a fingerprint got
		    // enrolled. Thus show the dialog to authenticate with their password first
		    // and ask the user if they want to authenticate with fingerprints in the
		    // future
		    mFragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
		    mFragment.setStage(
				    FingerprintAuthenticationDialogFragment.Stage.NEW_FINGERPRINT_ENROLLED);
		    mFragment.show(getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
	    }
    }

	private ArrayList<App> getApplicationList() {
		ArrayList<App> applicationList = new ArrayList<>();
		final PackageManager pm = getPackageManager();
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo applicationInfo : packages) {
			if(pm.getLaunchIntentForPackage(applicationInfo.packageName) != null) {
				// apps with launcher intent
				if((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
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

	private static final String DIALOG_FRAGMENT_TAG = "myFragment";
	private static final String SECRET_MESSAGE = "Very secret message";
	/** Alias for our key in the Android Key Store */
	private static final String KEY_NAME = "imprint_app";

	/**
	 * Initialize the {@link Cipher} instance with the created key in the {@link #createKey()}
	 * method.
	 *
	 * @return {@code true} if initialization is successful, {@code false} if the lock screen has
	 * been disabled or reset after the key was generated, or if a fingerprint got enrolled after
	 * the key was generated.
	 */
	private boolean initCipher() {
		try {
			mKeyStore.load(null);
			SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME, null);
			mCipher.init(Cipher.ENCRYPT_MODE, key);
			return true;
		} catch (KeyPermanentlyInvalidatedException e) {
			return false;
		} catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
				         | NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException("Failed to init Cipher", e);
		}
	}

	public void onPurchased(boolean withFingerprint) {
		if (withFingerprint) {
			// If the user has authenticated with fingerprint, verify that using cryptography and
			// then show the confirmation message.
			tryEncrypt();
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

	/**
	 * Tries to encrypt some data with the generated key in {@link #createKey} which is
	 * only works if the user has just authenticated via fingerprint.
	 */
	private void tryEncrypt() {
		try {
			byte[] encrypted = mCipher.doFinal(SECRET_MESSAGE.getBytes());
			showConfirmation(encrypted);
		} catch (BadPaddingException | IllegalBlockSizeException e) {
			Toast.makeText(this, "Failed to encrypt the data with the generated key. "
					                     + "Retry the purchase", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Creates a symmetric key in the Android Key Store which can only be used after the user has
	 * authenticated with fingerprint.
	 */
	public void createKey() {
		// The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
		// for your flow. Use of keys is necessary if you need to know if the set of
		// enrolled fingerprints has changed.
		try {
			mKeyStore.load(null);
			// Set the alias of the entry in Android KeyStore where the key will appear
			// and the constrains (purposes) in the constructor of the Builder
			mKeyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
					                                                  KeyProperties.PURPOSE_ENCRYPT |
							                                                  KeyProperties.PURPOSE_DECRYPT)
					                   .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
					                   // Require the user to authenticate with a fingerprint to authorize every use
					                   // of the key
					                   .setUserAuthenticationRequired(true)
					                   .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
					                   .build());
			mKeyGenerator.generateKey();
		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
				         | CertificateException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
