package info.acidflow.stickyheaders.sample.ui.adapter.delegate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.acidflow.stickyheader.widget.IHeaderProvider;
import info.acidflow.stickyheaders.R;
import info.acidflow.stickyheaders.sample.model.Header;

public class HeaderItemAdapterDelegate implements
        IAdapterDelegate< HeaderItemAdapterDelegate.HeaderViewHolder, Header >,
        IHeaderProvider< HeaderItemAdapterDelegate.HeaderViewHolder > {

    private List< Object > mItems;

    public HeaderItemAdapterDelegate( List< Object > items ) {
        super( );
        mItems = items;
    }

    @Override
    public HeaderViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View v = LayoutInflater.from( parent.getContext( ) ).inflate(
                R.layout.list_header, parent, false
        );
        return new HeaderViewHolder( v );
    }

    @Override
    public void onBindViewHolder( final HeaderViewHolder holder, final Header item ) {
        holder.text.setText( item.text );
        holder.itemView.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                Toast.makeText( v.getContext( ), String.valueOf( holder.getAdapterPosition( ) ),
                        Toast.LENGTH_SHORT
                ).show( );
            }
        } );
    }

    @Override
    public Class< Header > getItemClass( ) {
        return Header.class;
    }

    @Override
    public int getViewType( ) {
        return getClass( ).hashCode( );
    }

    @Override
    public int getHeaderForPosition( int position ) {
        if ( position < 0 || position >= mItems.size( ) ) {
            return RecyclerView.NO_POSITION;
        }

        if ( mItems.get( position ) instanceof Header ) {
            return position;
        }

        for ( int i = position - 1; i >= 0; --i ) {
            if ( mItems.get( i ) instanceof Header ) {
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    @Override
    public boolean isHeader( int position ) {
        return mItems.get( position ) instanceof Header;
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder( ViewGroup parent ) {
        return onCreateViewHolder( parent, getViewType( ) );
    }

    @Override
    public void onBindHeaderViewHolder( HeaderViewHolder holder, final int position ) {
        onBindViewHolder( holder, ( Header ) mItems.get( position ) );
        holder.itemView.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                Toast.makeText( v.getContext( ), String.valueOf( position ),
                        Toast.LENGTH_SHORT
                ).show( );
            }
        } );
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public HeaderViewHolder( View itemView ) {
            super( itemView );
            text = ( TextView ) itemView.findViewById( R.id.text );
        }
    }
}
