package info.acidflow.stickyheaders.sample.ui.activities;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import info.acidflow.stickyheader.widget.DefaultHeaderSpanSizeProvider;
import info.acidflow.stickyheader.widget.StickyHeaderItemDecoration;
import info.acidflow.stickyheader.widget.StickyHeaderSpanSizeLookup;
import info.acidflow.stickyheaders.R;
import info.acidflow.stickyheaders.sample.model.Header;
import info.acidflow.stickyheaders.sample.model.Item;
import info.acidflow.stickyheaders.sample.ui.adapter.SampleAdapter;
import info.acidflow.stickyheaders.sample.ui.adapter.delegate.HeaderItemAdapterDelegate;
import info.acidflow.stickyheaders.sample.ui.adapter.delegate.TextItemAdapterDelegate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        List< Object > data = generateItems( );

        TextItemAdapterDelegate itemDelegate = new TextItemAdapterDelegate( );
        HeaderItemAdapterDelegate headerDelegate = new HeaderItemAdapterDelegate( data );
        SampleAdapter< Object > adapter = new SampleAdapter<>( data );
        adapter.addDelegate( itemDelegate );
        adapter.addDelegate( headerDelegate );

        LinearLayoutManager manager = new GridLayoutManager( this, 3 );
        ( ( GridLayoutManager ) manager ).setSpanSizeLookup(
                new StickyHeaderSpanSizeLookup( new DefaultHeaderSpanSizeProvider(
                        ( GridLayoutManager ) manager ), headerDelegate )
        );

        RecyclerView rv = ( RecyclerView ) findViewById( R.id.recyclerView );
        ViewGroup sticky = ( ViewGroup ) findViewById( R.id.sticky );
        rv.setLayoutManager( manager );
        rv.addItemDecoration( new StickyHeaderItemDecoration( manager, headerDelegate,
                sticky, getThemeColorAttribute( this, android.R.attr.windowBackground ) ) );
        rv.setAdapter( adapter );
    }

    @NonNull
    private List< Object > generateItems( ) {
        final List< Object > data = new ArrayList<>( 1000 );
        for ( int i = 0; i < 1000; ++i ) {
            String text = String.valueOf( i );
            data.add( i % 10 == 0 ? new Header( text ) : new Item( text ) );
        }
        return data;
    }

    private static int getThemeColorAttribute( Context ctx, int attr ) {
        TypedValue typedValue = new TypedValue( );
        Resources.Theme theme = ctx.getTheme( );
        theme.resolveAttribute( attr, typedValue, true );
        return typedValue.data;
    }
}
