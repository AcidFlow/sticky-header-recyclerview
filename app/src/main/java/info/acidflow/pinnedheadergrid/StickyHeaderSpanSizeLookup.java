package info.acidflow.pinnedheadergrid;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by paul on 29/04/16.
 */
public class StickyHeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private GridLayoutManager mLayoutManager;
    private IHeaderProvider mHeaderProvider;

    public StickyHeaderSpanSizeLookup(GridLayoutManager layoutManager, IHeaderProvider
            headerProvider) {
        mLayoutManager = layoutManager;
        mHeaderProvider = headerProvider;
    }

    @Override
    public int getSpanSize(int position) {
        return mHeaderProvider.isHeader(position) ? mLayoutManager.getSpanCount() : 1;
    }
}
