package in.ceeq.imprint.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import in.ceeq.imprint.entity.App;
import in.ceeq.imprint.helpers.Utils;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {

    public BaseRecyclerAdapter(final Context context,
                               final ArrayList<T> appArrayList,
                               final OnViewAppListener onViewAppListener) {
        mDataList = appArrayList;
        mContext = context;
        mOnViewAppListener = onViewAppListener;
    }

    public interface OnViewAppListener {
        public void onViewApp(App app);
    }
    /**
     * Generic data list
     */
    protected ArrayList<T> mDataList;
    protected OnViewAppListener mOnViewAppListener;
    protected Context mContext;

    @Override
    public int getItemCount() {
        return (!Utils.isEmptyList(mDataList)) ? mDataList.size() : 0;
    }

    public void swapDataSet(ArrayList<T> questionArrayList, boolean hasRefreshed) {
        if(hasRefreshed || mDataList.size() < 0) {
            mDataList = questionArrayList;
        } else {
            mDataList.addAll(questionArrayList);
        }
        notifyDataSetChanged();
    }
}
