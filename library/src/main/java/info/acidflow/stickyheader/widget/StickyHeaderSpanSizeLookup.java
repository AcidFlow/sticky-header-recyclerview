package info.acidflow.stickyheader.widget;

import android.support.v7.widget.GridLayoutManager;

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
