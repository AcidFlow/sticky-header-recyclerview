package info.acidflow.stickyheader.widget;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Contract a HeaderProvider.
 * @param <VH> the ViewHolder type for header items.
 */
public interface IHeaderProvider< VH extends RecyclerView.ViewHolder > {

    /**
     * Check is the item at the specified position is a header or not
     * @param position the position
     * @return true if it is a header, false otherwise
     */
    boolean isHeader( int position );

    /**
     * Get the header position for the item at the specified position.
     * @param position the item position
     * @return the position of the header corresponding to the section of the position.
     */
    int getHeaderForPosition( int position );

    VH onCreateHeaderViewHolder( ViewGroup parent );

    void onBindHeaderViewHolder( VH holder, int position );

}
