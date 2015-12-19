package in.ceeq.imprint.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.ceeq.imprint.R;
import in.ceeq.imprint.entity.App;
import in.ceeq.imprint.helpers.Utils;

public class AppListRecyclerAdapter extends BaseRecyclerAdapter<App> {

    public AppListRecyclerAdapter(Context context, ArrayList<App> eventArrayList, OnViewAppListener
            onViewEventListener) {
        super(context, eventArrayList, onViewEventListener);
    }

    @Override
    public AppItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_app, parent, false);
        return new AppItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        AppItemViewHolder itemViewHolder = (AppItemViewHolder) viewHolder;
        App app = mDataList.get(position);
        if(app != null) {
            String questionSubject = app.name;
            if(!Utils.isEmptyString(questionSubject.trim())) {
                itemViewHolder.mAppName.setText(Html.fromHtml(questionSubject.trim()));
            }

            if(app.logo != null) {
                itemViewHolder.mAppLogo.setImageDrawable(app.logo);
            }
        }
    }

    private class AppItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mAppName;
        public View mAppContainer;
        public ImageView mAppLogo;

        public AppItemViewHolder(View itemView) {
            super(itemView);
            mAppName = (TextView) itemView.findViewById(R.id.textview_app_name);
            mAppLogo = (ImageView) itemView.findViewById(R.id.imageview_app_logo);
            mAppContainer = itemView.findViewById(R.id.layout_app_content);
            mAppContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnViewAppListener.onViewApp(mDataList.get(getLayoutPosition()));
        }
    }
}
