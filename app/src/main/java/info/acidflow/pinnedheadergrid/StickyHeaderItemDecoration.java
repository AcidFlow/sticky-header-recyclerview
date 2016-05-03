package info.acidflow.pinnedheadergrid;

import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by paul on 29/04/16.
 */
public class StickyHeaderItemDecoration extends RecyclerView.ItemDecoration {

    private LinearLayoutManager mLayoutManager;
    private int mBackgroundColor;
    private IHeaderProvider mHeaderProvider;
    private ViewGroup mStickyViewParent;

    public StickyHeaderItemDecoration(LinearLayoutManager layoutManager, int color,
                                      IHeaderProvider headerProvider, ViewGroup stickyHolder) {
        super();
        mLayoutManager = layoutManager;
        mBackgroundColor = color;
        mHeaderProvider = headerProvider;
        mStickyViewParent = stickyHolder;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int firstVisiblePos = findFirstVisibleItemPosition();
        int nextHeaderOffset = getNextHeaderOffset(firstVisiblePos);
        boolean nextViewIsHeader = nextHeaderOffset != RecyclerView.NO_POSITION;

        View headerView = getHeaderViewForPosition(parent, firstVisiblePos);
        View firstVisibleView = mLayoutManager.findViewByPosition(firstVisiblePos);

        if (isHeader(firstVisiblePos)) {
            overrideCanvasBackground(c, headerView, firstVisibleView.getTop());
        }

        if (nextViewIsHeader) {
            c.save();
            c.translate(0, firstVisibleView.getTop());
            mStickyViewParent.setTranslationY(firstVisibleView.getTop());
        } else {
            mStickyViewParent.setTranslationY(0);
        }

        mStickyViewParent.removeAllViews();
        mStickyViewParent.addView(headerView);

        if (nextViewIsHeader) {
            c.restore();
        }
        if (nextViewIsHeader) {
            headerView = getHeaderViewForPosition(parent, firstVisiblePos + nextHeaderOffset);
            overrideCanvasBackground(c, headerView, firstVisibleView.getBottom());
            c.save();
            c.translate(0, firstVisibleView.getBottom());
            headerView.draw(c);
            c.restore();
        }
    }

    private void overrideCanvasBackground(Canvas c, View headerView, float translationY) {
        c.save();
        c.translate(0, translationY);
        c.clipRect(0, 0, headerView.getMeasuredWidth(), headerView.getMeasuredHeight(),
                Region.Op.INTERSECT);
        c.drawColor(mBackgroundColor);
        c.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    private int getNextHeaderOffset(int firstVisiblePos) {
        int offset = RecyclerView.NO_POSITION;
        for (int i = 1, spanCount = getSpanCount(); i <= spanCount; ++i) {
            if (mHeaderProvider.isHeader(firstVisiblePos + i)) {
                offset = i;
            }
        }
        return offset;
    }

    private boolean isHeader(int position) {
        return mHeaderProvider.isHeader(position);
    }

    private View getHeaderViewForPosition(RecyclerView rv, int position) {
        RecyclerView.ViewHolder headerHolder = mHeaderProvider.onCreateHeaderViewHolder(rv);
        mHeaderProvider.onBindHeaderViewHolder(
                headerHolder, mHeaderProvider.getHeaderForPosition(position)
        );
        headerHolder.itemView.measure(
                View.MeasureSpec.makeMeasureSpec(rv.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(rv.getHeight(), View.MeasureSpec.UNSPECIFIED)
        );
        headerHolder.itemView.layout(0, 0, headerHolder.itemView.getMeasuredWidth(),
                headerHolder.itemView.getMeasuredHeight()
        );
        return headerHolder.itemView;
    }

    private int findFirstVisibleItemPosition() {
        return mLayoutManager.findFirstVisibleItemPosition();
    }

    private int getSpanCount() {
        if (mLayoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) mLayoutManager).getSpanCount();
        }
        return 1;
    }
}
