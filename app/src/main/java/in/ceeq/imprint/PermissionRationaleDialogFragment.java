package in.ceeq.imprint;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.ceeq.imprint.helpers.Utils;

public class PermissionRationaleDialogFragment extends DialogFragment implements View.OnClickListener {

    private String mPermission;
    public static final String BUNDLE_EXTRA_PERMISSION = "bundle_extra_permission";
    public static final String BUNDLE_EXTRA_TITLE = "bundle_extra_title";
    public static final String BUNDLE_EXTRA_MESSAGE = "bundle_extra_message";
    public static final String BUNDLE_EXTRA_IMAGE = "bundle_extra_image";
    private static final String TAG = "permission_rational_dialog_fragment";

    public static void show(FragmentManager fragmentManager, Bundle args) {
        PermissionRationaleDialogFragment permissionRationaleDialogFragment =
                new PermissionRationaleDialogFragment();
        permissionRationaleDialogFragment.setArguments(args);
        permissionRationaleDialogFragment.show(fragmentManager, TAG);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (args != null) {
            mPermission = args.getString(BUNDLE_EXTRA_PERMISSION);
        }

        if (Utils.isEmptyString(mPermission)) {
            throw new IllegalStateException();
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_permission_rationale, null);
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView titleTextView = (TextView) view.findViewById(R.id.permission_dialog_rational_tv_title);
        //titleTextView.setText(getPermissionRationalTitle(mPermission));
        final TextView messageTextView = (TextView) view.findViewById(R.id.permission_dialog_rational_tv_message);
       // messageTextView.setText(getString(getPermissionRationalMessage(mPermission)));
        view.findViewById(R.id.permission_dialog_rational_btn_dismiss).setOnClickListener(this);
        view.findViewById(R.id.permission_dialog_rational_btn_allow).setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.permission_dialog_rational_btn_dismiss:
                dismiss();
                break;
            case R.id.permission_dialog_rational_btn_allow:
                dismiss();
                break;
        }
    }

    public static final class Builder {
        private int mPageImage;
        private String mPageTitle;
        private String mPageMessage;

        public Builder setPageTitle(String pageTitle) {
            mPageTitle = pageTitle;
            return this;
        }

        public Builder setPageMessage(String pageMessage) {
            mPageMessage = pageMessage;
            return this;
        }

        public Builder setPageImage(int pageImage) {
            mPageImage = pageImage;
            return this;
        }

        public void show(AppCompatActivity activity) {
            Bundle extras = new Bundle();
            extras.putString(BUNDLE_EXTRA_TITLE, mPageTitle);
            extras.putString(BUNDLE_EXTRA_MESSAGE, mPageMessage);
            extras.putInt(BUNDLE_EXTRA_IMAGE, mPageImage);
            PermissionRationaleDialogFragment.show(activity.getSupportFragmentManager(), extras);
        }
    }
}
