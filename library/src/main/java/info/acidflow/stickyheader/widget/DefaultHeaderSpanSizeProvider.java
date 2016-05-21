package info.acidflow.stickyheader.widget;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by paul on 21/05/16.
 */
public class DefaultHeaderSpanSizeProvider implements IHeaderSpanSizeProvider {

    private final int mSpanSize;

    public DefaultHeaderSpanSizeProvider( GridLayoutManager layoutManager ) {
        mSpanSize = layoutManager.getSpanCount();
    }

    @Override
    public int getSpanCount( ) {
        return mSpanSize;
    }
}
