package info.acidflow.pinnedheadergrid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.acidflow.stickyheader.widget.IHeaderProvider;
import info.acidflow.stickyheader.widget.StickyHeaderItemDecoration;
import info.acidflow.stickyheader.widget.StickyHeaderSpanSizeLookup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        RecyclerView rv = ( RecyclerView ) findViewById( R.id.recyclerView );
        final List<String> numbers = new ArrayList<>( 100 );
        for(int i = 0; i < 100; ++i){
            String text = String.valueOf(i);
//            if(i % 10 == 0){
//                for(int j = 1, rand = new Random().nextInt(3); j < rand; ++j){
//                    text += "\n" + String.valueOf(i);
//                }
//            }
            numbers.add( text );
        }

        final DummyAdapter adapter = new DummyAdapter( numbers );
//*
        LinearLayoutManager manager = new GridLayoutManager( this, 8 );
        ((GridLayoutManager)manager).setSpanSizeLookup( new StickyHeaderSpanSizeLookup(
                (GridLayoutManager) manager, adapter ) );
                /*/
        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
                //*/
        rv.setLayoutManager( manager );
        rv.addItemDecoration( new StickyHeaderItemDecoration(manager, 0xffeeeeee,
                adapter, (ViewGroup) findViewById(R.id.sticky)) );
        rv.setAdapter( adapter );
        findViewById(R.id.add_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers.add(0, String.valueOf(numbers.size()));
                adapter.notifyItemInserted(0);
            }
        });

    }

    static class DummyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
            IHeaderProvider<HeaderViewHolder> {

        private static final int VIEW_TYPE_HEADER = 0;
        private static final int VIEW_TYPE_ITEM = 1;

        private List<String> mItems;

        public DummyAdapter( List< String > mItems ) {
            this.mItems = mItems;
        }

        @Override
        public int getItemViewType(int position) {
            if(isHeader(position)){
                return VIEW_TYPE_HEADER;
            }
            return VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case VIEW_TYPE_HEADER:
                    return onCreateHeaderViewHolder(parent);
                case VIEW_TYPE_ITEM:
                    return onCreateItemViewHolder(parent);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            switch (viewType){
                case VIEW_TYPE_HEADER:
                    onBindHeaderViewHolder((HeaderViewHolder) holder, position);
                    break;

                case VIEW_TYPE_ITEM:
                    onBindItemViewHolder((TextViewHolder) holder, position);
                    break;
            }
        }

        public TextViewHolder onCreateItemViewHolder( ViewGroup parent ) {
            View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.list_item, parent, false );
            return new TextViewHolder( v );
        }


        public void onBindItemViewHolder(TextViewHolder holder, final int position) {
            holder.text.setText( mItems.get( position ) );
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),String.valueOf( position ), Toast.LENGTH_SHORT).show();
                }
            });
        }


        public HeaderViewHolder onCreateHeaderViewHolder( ViewGroup parent ) {
            View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.list_header, parent, false );
            return new HeaderViewHolder( v );
        }

        public void onBindHeaderViewHolder(HeaderViewHolder holder, final int position) {
            holder.text.setText( mItems.get(position) );
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount( ) {
            return mItems.size();
        }

        @Override
        public int getHeaderForPosition(int position) {
            return position - (position % 10);
        }

        @Override
        public boolean isHeader( int position ) {
            return position % 10 == 0;
        }
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public TextViewHolder( View itemView ) {
            super( itemView );
            text = ( TextView ) itemView.findViewById( R.id.text );
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public HeaderViewHolder( View itemView ) {
            super( itemView );
            text = ( TextView ) itemView.findViewById( R.id.text );
        }
    }
}
