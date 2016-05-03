package info.acidflow.pinnedheadergrid;

import android.graphics.Canvas;
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
        boolean isCurrentHeaderPushed = isCurrentHeaderPushed(firstVisiblePos, nextHeaderOffset);

        View headerView = getHeaderViewForPosition(parent, firstVisiblePos);
        View firstVisibleView = mLayoutManager.findViewByPosition(firstVisiblePos);
        View nextHeaderView = mLayoutManager.findViewByPosition(firstVisiblePos + nextHeaderOffset);

        if (isHeader(firstVisiblePos)) {
            // Overriding the first header draw in the adapter to avoid drawing on top of it
            overrideCanvasBackground(c, headerView, firstVisibleView.getTop());
        }

        if (isCurrentHeaderPushed) {
            c.save();
            c.translate(0, getCurrentHeaderTranslation(nextHeaderView));
            mStickyViewParent.setTranslationY(getCurrentHeaderTranslation(nextHeaderView));
        } else {
            mStickyViewParent.setTranslationY(0);
        }

        mStickyViewParent.removeAllViews();
        mStickyViewParent.addView(headerView);

        if (isCurrentHeaderPushed) {
            c.restore();
        }
        if (isCurrentHeaderPushed) {
            headerView = getHeaderViewForPosition(parent, firstVisiblePos + nextHeaderOffset);
            // Overriding the second header draw in the adapter to avoid drawing on top of it
            overrideCanvasBackground(c, headerView, getNextHeaderTranslation(nextHeaderView));
            c.save();
            c.translate(0, getNextHeaderTranslation(nextHeaderView));
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
        for (int i = 1, childCount = mLayoutManager.getChildCount(); i <= childCount; ++i) {
            if (mHeaderProvider.isHeader(firstVisiblePos + i)) {
                offset = i;
                break;
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
        View header = headerHolder.itemView;
        int widthSpec = ViewGroup.MeasureSpec.makeMeasureSpec(rv.getWidth(),
                View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(rv.getHeight(),
                View.MeasureSpec.UNSPECIFIED);

        int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                rv.getPaddingLeft() + rv.getPaddingRight(), header.getLayoutParams().width);
        int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                rv.getPaddingTop() + rv.getPaddingBottom(), header.getLayoutParams().height);
        header.measure(childWidth, childHeight);
        header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
        return header;
    }

    private int findFirstVisibleItemPosition() {
        return mLayoutManager.findFirstVisibleItemPosition();
    }

    private boolean isCurrentHeaderPushed(int firstPos, int offset) {
        return mLayoutManager.findViewByPosition(firstPos + offset).getTop() <=
                mStickyViewParent.getHeight();
    }

    private float getCurrentHeaderTranslation(View nextHeaderView) {
        return nextHeaderView.getTop() - mStickyViewParent.getHeight();
    }

    private float getNextHeaderTranslation(View nextHeaderView) {
        return nextHeaderView.getTop();
    }
}
