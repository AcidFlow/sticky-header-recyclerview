package info.acidflow.stickyheader.widget;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface IHeaderProvider< VH extends RecyclerView.ViewHolder > {

    boolean isHeader( int position );

    int getHeaderForPosition( int position );

    VH onCreateHeaderViewHolder( ViewGroup parent );

    void onBindHeaderViewHolder( VH holder, int position );

}
