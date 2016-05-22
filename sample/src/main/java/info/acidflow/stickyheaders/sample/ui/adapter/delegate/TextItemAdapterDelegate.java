package info.acidflow.stickyheaders.sample.ui.adapter.delegate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import info.acidflow.stickyheaders.R;
import info.acidflow.stickyheaders.sample.model.Item;

public class TextItemAdapterDelegate implements IAdapterDelegate< TextItemAdapterDelegate.TextViewHolder, Item > {

    @Override
    public TextViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View v = LayoutInflater.from( parent.getContext( ) ).inflate(
                R.layout.list_item, parent, false
        );
        return new TextViewHolder( v );
    }

    @Override
    public void onBindViewHolder( final TextViewHolder holder, final Item item ) {
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
    public Class< Item > getItemClass( ) {
        return Item.class;
    }

    @Override
    public int getViewType( ) {
        return getClass( ).hashCode( );
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public TextViewHolder( View itemView ) {
            super( itemView );
            text = ( TextView ) itemView.findViewById( R.id.text );
        }
    }
}
