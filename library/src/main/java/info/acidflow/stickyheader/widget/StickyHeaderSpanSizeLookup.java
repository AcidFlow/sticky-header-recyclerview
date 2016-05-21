package info.acidflow.stickyheader.widget;

import android.support.v7.widget.GridLayoutManager;

public class StickyHeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private IHeaderSpanSizeProvider mHeaderSpanProvider;
    private IHeaderProvider mHeaderProvider;
    private GridLayoutManager.SpanSizeLookup mDelegate;

    public StickyHeaderSpanSizeLookup( IHeaderSpanSizeProvider spanSizeProvider,
                                       IHeaderProvider headerProvider ) {
        mHeaderSpanProvider = spanSizeProvider;
        mHeaderProvider = headerProvider;
        mDelegate = new GridLayoutManager.DefaultSpanSizeLookup( );
    }

    public StickyHeaderSpanSizeLookup( IHeaderSpanSizeProvider spanSizeProvider,
                                       IHeaderProvider headerProvider,
                                       GridLayoutManager.SpanSizeLookup delegate ) {
        mHeaderSpanProvider = spanSizeProvider;
        mHeaderProvider = headerProvider;
        mDelegate = delegate;
    }

    @Override
    public int getSpanSize( int position ) {
        return mHeaderProvider.isHeader( position ) ?
                mHeaderSpanProvider.getSpanCount( ) : mDelegate.getSpanSize( position );
    }
}
