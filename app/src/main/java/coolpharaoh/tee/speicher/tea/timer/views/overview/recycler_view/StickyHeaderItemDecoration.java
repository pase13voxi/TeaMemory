package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class StickyHeaderItemDecoration extends RecyclerView.ItemDecoration {

    private final StickyHeaderInterface listener;
    private int stickyHeaderHeight;

    public StickyHeaderItemDecoration(final StickyHeaderInterface listener) {
        this.listener = listener;
    }

    @Override
    public void onDrawOver(final Canvas canvas, final RecyclerView parent, final RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        final int topChildPosition = getTopChildPosition(parent);
        if (topChildPosition != RecyclerView.NO_POSITION) {
            drawOverHeader(canvas, parent, topChildPosition);
        }
    }

    private int getTopChildPosition(final RecyclerView parent) {
        int topChildPosition = RecyclerView.NO_POSITION;

        final View topChild = parent.getChildAt(0);
        if (topChild != null) {
            topChildPosition = parent.getChildAdapterPosition(topChild);
        }

        return topChildPosition;
    }

    private void drawOverHeader(final Canvas canvas, final RecyclerView parent, final int topChildPosition) {
        final int headerPosition = listener.getHeaderPositionForItem(topChildPosition);
        final View currentHeader = getHeaderViewForItem(headerPosition, parent);
        fixLayoutSize(parent, currentHeader);
        final int contactPoint = currentHeader.getBottom();
        final View childInContact = getChildInContact(parent, contactPoint, headerPosition);

        if (isChildInContactAHeader(parent, childInContact)) {
            moveHeader(canvas, currentHeader, childInContact);
        } else {
            drawHeader(canvas, currentHeader);
        }
    }

    private View getHeaderViewForItem(final int headerPosition, final RecyclerView parent) {
        final int layoutResId = listener.getHeaderLayout(headerPosition);
        final View header = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        listener.bindHeaderData(header, headerPosition);
        return header;
    }

    private void fixLayoutSize(final ViewGroup parent, final View view) {

        final int parentWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        final int parentHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

        final int childWidthSpec = ViewGroup.getChildMeasureSpec(parentWidthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
        final int childHeightSpec = ViewGroup.getChildMeasureSpec(parentHeightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

        view.measure(childWidthSpec, childHeightSpec);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        stickyHeaderHeight = view.getMeasuredHeight();
    }

    private View getChildInContact(final RecyclerView parent, final int contactPoint, final int currentHeaderPosition) {
        View childInContact = null;
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (isChildAndStickyHeaderInContact(parent, contactPoint, currentHeaderPosition, i)) {
                childInContact = parent.getChildAt(i);
                break;
            }
        }
        return childInContact;
    }

    private boolean isChildAndStickyHeaderInContact(final RecyclerView parent, final int contactPoint, final int currentHeaderPosition, final int index) {
        final View child = parent.getChildAt(index);

        final int childBottomPosition = getChildBottomPosition(parent, currentHeaderPosition, index);
        final int childTopPosition = child.getTop();

        return childBottomPosition > contactPoint && childTopPosition <= contactPoint;
    }

    private int getChildBottomPosition(final RecyclerView parent, final int currentHeaderPosition, final int index) {
        final int heightTolerance = getHeightTolerance(parent, currentHeaderPosition, index);
        final View child = parent.getChildAt(index);

        final int childBottomPosition;
        if (child.getTop() > 0) {
            childBottomPosition = child.getBottom() + heightTolerance;
        } else {
            childBottomPosition = child.getBottom();
        }
        return childBottomPosition;
    }

    private int getHeightTolerance(final RecyclerView parent, final int currentHeaderPosition, final int index) {
        final View child = parent.getChildAt(index);

        int heightTolerance = 0;
        if (currentHeaderPosition != index) {
            final boolean isChildHeader = listener.isHeader(parent.getChildAdapterPosition(child));
            if (isChildHeader) {
                heightTolerance = stickyHeaderHeight - child.getHeight();
            }
        }
        return heightTolerance;
    }

    private boolean isChildInContactAHeader(final RecyclerView parent, final View childInContact) {
        return childInContact != null && listener.isHeader(parent.getChildAdapterPosition(childInContact));
    }

    private void moveHeader(final Canvas canvas, final View currentHeader, final View nextHeader) {
        canvas.save();
        canvas.translate(0, (nextHeader.getTop() - currentHeader.getHeight()));
        currentHeader.draw(canvas);
        canvas.restore();
    }

    private void drawHeader(final Canvas canvas, final View header) {
        canvas.save();
        canvas.translate(0, 0);
        header.draw(canvas);
        canvas.restore();
    }

    public interface StickyHeaderInterface {

        int getHeaderPositionForItem(final int itemPosition);

        int getHeaderLayout(final int headerPosition);

        void bindHeaderData(final View header, final int headerPosition);

        boolean isHeader(final int itemPosition);
    }
}
