package info.acidflow.stickyheader.widget;

import android.graphics.Canvas;
import android.graphics.Region;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class StickyHeaderItemDecoration extends RecyclerView.ItemDecoration {

    private LinearLayoutManager mLayoutManager;
    private int mBackgroundColor;
    private IHeaderProvider mHeaderProvider;
    private ViewGroup mStickyViewParent;
    private int mCurrentHeaderPosition;
    private int mPreviousHeaderPosition = -1;
    private int mFirstVisiblePos;
    private int mNextHeaderOffset;
    private boolean isCurrentHeaderPushed;
    private View mCurrentHeaderView;
    private View mFirstVisibleView;
    private View mNextHeaderView;

    public StickyHeaderItemDecoration( LinearLayoutManager layoutManager, int color,
                                       IHeaderProvider headerProvider, ViewGroup stickyHolder ) {
        super( );
        mLayoutManager = layoutManager;
        mBackgroundColor = color;
        mHeaderProvider = headerProvider;
        mStickyViewParent = stickyHolder;
    }

    @Override
    public void onDrawOver( Canvas c, RecyclerView parent, RecyclerView.State state ) {
        super.onDrawOver( c, parent, state );

        mFirstVisiblePos = findFirstVisibleItemPosition( );
        mNextHeaderOffset = getNextHeaderOffset( parent.getAdapter( ).getItemCount( ),
                mFirstVisiblePos
        );
        isCurrentHeaderPushed = isCurrentHeaderPushed( mFirstVisiblePos, mNextHeaderOffset );

        if ( !isHeaderUpdateRequired( ) ) {
            return;
        }

        mCurrentHeaderPosition = mHeaderProvider.getHeaderForPosition( mFirstVisiblePos );
        mFirstVisibleView = mLayoutManager.findViewByPosition( mFirstVisiblePos );
        mCurrentHeaderView = getHeaderViewForPosition( parent, mFirstVisiblePos );
        mNextHeaderView = mLayoutManager.findViewByPosition( mFirstVisiblePos + mNextHeaderOffset );

        if ( isHeader( mFirstVisiblePos ) ) {
            // Overriding the first header draw in the adapter to avoid drawing on top of it
            overrideCanvasBackground( c, mCurrentHeaderView, getFirstVisibleViewTranslation( ) );
        }

        if ( isCurrentHeaderPushed ) {
            pushAwayStickyHeader( c );
            pushAwayStickyView( );
            // Overriding the second header draw in the adapter to avoid drawing on top of it
            overrideCanvasBackground( c, mNextHeaderView, getNextHeaderTranslation( ) );
            drawNextHeader( c );
        } else {
            resetStickyHeaderTranslation( );
        }

        if ( shouldUpdateStickyHeaderContent( ) ) {
            updateStickyHeaderContent( mCurrentHeaderView );
        }
        mPreviousHeaderPosition = mCurrentHeaderPosition;
    }

    private void drawNextHeader( Canvas c ) {
        c.save( );
        c.translate( 0, getNextHeaderTranslation( ) );
        mNextHeaderView.draw( c );
        c.restore( );
    }

    private void updateStickyHeaderContent( View v ) {
        mStickyViewParent.removeAllViews( );
        mStickyViewParent.addView( v );
    }

    private boolean shouldUpdateStickyHeaderContent( ) {
        return mPreviousHeaderPosition != mCurrentHeaderPosition;
    }

    private void resetStickyHeaderTranslation( ) {
        mStickyViewParent.setTranslationY( 0 );
    }

    private void pushAwayStickyHeader( Canvas c ) {
        c.save( );
        c.translate( 0, getCurrentHeaderTranslation( ) );
        c.restore( );
    }

    private void pushAwayStickyView( ) {
        mStickyViewParent.setTranslationY( getCurrentHeaderTranslation( ) );
    }

    private boolean isHeaderUpdateRequired( ) {
        return isHeader( mFirstVisiblePos ) || isCurrentHeaderPushed;
    }

    private void overrideCanvasBackground( Canvas c, View headerView, float translationY ) {
        c.save( );
        c.translate( 0, translationY );
        c.clipRect( 0, 0, headerView.getMeasuredWidth( ), headerView.getMeasuredHeight( ),
                Region.Op.INTERSECT
        );
        c.drawColor( mBackgroundColor );
        c.restore( );
    }

    private int getNextHeaderOffset( int adapterSize, int firstVisiblePos ) {
        int offset = RecyclerView.NO_POSITION;
        for ( int i = 1, childCount = mLayoutManager.getChildCount( );
              firstVisiblePos + i < adapterSize && i <= childCount; ++i ) {
            if ( mHeaderProvider.isHeader( firstVisiblePos + i ) ) {
                offset = i;
                break;
            }
        }
        return offset;
    }

    private boolean isHeader( int position ) {
        return mHeaderProvider.isHeader( position );
    }

    private View getHeaderViewForPosition( RecyclerView rv, int position ) {
        // Create the View corresponding the header
        RecyclerView.ViewHolder headerHolder = mHeaderProvider.onCreateHeaderViewHolder( rv );
        mHeaderProvider.onBindHeaderViewHolder(
                headerHolder, mHeaderProvider.getHeaderForPosition( position )
        );

        // Measuring it
        View header = headerHolder.itemView;
        int widthSpec = ViewGroup.MeasureSpec.makeMeasureSpec( rv.getWidth( ),
                View.MeasureSpec.EXACTLY
        );
        int heightSpec = View.MeasureSpec.makeMeasureSpec( rv.getHeight( ),
                View.MeasureSpec.UNSPECIFIED
        );

        int childWidth = ViewGroup.getChildMeasureSpec( widthSpec,
                rv.getPaddingLeft( ) + rv.getPaddingRight( ), header.getLayoutParams( ).width
        );
        int childHeight = ViewGroup.getChildMeasureSpec( heightSpec,
                rv.getPaddingTop( ) + rv.getPaddingBottom( ), header.getLayoutParams( ).height
        );
        header.measure( childWidth, childHeight );
        header.layout( 0, 0, header.getMeasuredWidth( ), header.getMeasuredHeight( ) );
        return header;
    }

    private int findFirstVisibleItemPosition( ) {
        return mLayoutManager.findFirstVisibleItemPosition( );
    }

    private boolean isCurrentHeaderPushed( int firstPos, int offset ) {
        View nextHeader = mLayoutManager.findViewByPosition( firstPos + offset );
        return nextHeader != null && nextHeader.getTop( ) <= mStickyViewParent.getHeight( );
    }

    private float getCurrentHeaderTranslation( ) {
        return mNextHeaderView.getTop( ) - mStickyViewParent.getHeight( );
    }

    private float getNextHeaderTranslation( ) {
        return mNextHeaderView.getTop( );
    }

    private float getFirstVisibleViewTranslation( ) {
        return mFirstVisibleView.getTop( );
    }
}
