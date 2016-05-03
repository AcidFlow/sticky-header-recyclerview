package info.acidflow.pinnedheadergrid;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface IHeaderProvider<T extends RecyclerView.ViewHolder> {

    boolean isHeader(int position);

    int getHeaderForPosition(int position);

    T onCreateHeaderViewHolder( ViewGroup parent );

    void onBindHeaderViewHolder(T holder, int position);

}
