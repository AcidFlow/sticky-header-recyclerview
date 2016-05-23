package info.acidflow.stickyheader.widget;

import android.support.v7.widget.GridLayoutManager;

/**
 * A Default implementation of IHeaderSpanSizeProvider, returning the span size from the layout
 * manager.
 */
public class DefaultHeaderSpanSizeProvider implements IHeaderSpanSizeProvider {

    private final int mSpanSize;

    /**
     * Construct a DefaultHeaderSpanSizeProvider returning the span size from the layout
     * manager.
     * @param layoutManager the GridLayoutManager
     */
    public DefaultHeaderSpanSizeProvider( GridLayoutManager layoutManager ) {
        mSpanSize = layoutManager.getSpanCount( );
    }

    @Override
    public int getSpanCount( ) {
        return mSpanSize;
    }
}
