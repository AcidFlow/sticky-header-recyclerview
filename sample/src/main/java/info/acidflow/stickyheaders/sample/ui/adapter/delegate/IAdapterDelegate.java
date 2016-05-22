package info.acidflow.stickyheaders.sample.ui.adapter.delegate;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface IAdapterDelegate< VH extends RecyclerView.ViewHolder, T > {

    Class< T > getItemClass( );

    int getViewType( );

    VH onCreateViewHolder( ViewGroup parent, int viewType );

    void onBindViewHolder( VH holder, T item );

}
