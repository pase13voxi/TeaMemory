package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StickyHeaderItemDecoration extends RecyclerView.ItemDecoration {

    private final StickyHeaderInterface listener;
    private int stickyHeaderHeight;

    public StickyHeaderItemDecoration(@NonNull final StickyHeaderInterface listener) {
        this.listener = listener;
    }

    @Override
    public void onDrawOver(@NonNull final Canvas canvas, @NonNull final RecyclerView parent, @NonNull final RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        final int topChildPosition = getTopChildPosition(parent);
        if (topChildPosition != -1) {
            drawOverHeader(canvas, parent, topChildPosition);
        }
    }

    private void drawOverHeader(@NonNull Canvas canvas, @NonNull RecyclerView parent, int topChildPosition) {
        final int headerPos = listener.getHeaderPositionForItem(topChildPosition);
        final View currentHeader = getHeaderViewForItem(headerPos, parent);
        fixLayoutSize(parent, currentHeader);
        final int contactPoint = currentHeader.getBottom();
        final View childInContact = getChildInContact(parent, contactPoint, headerPos);

        if (childInContact != null && listener.isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeader(canvas, currentHeader, childInContact);
        } else {
            drawHeader(canvas, currentHeader);
        }
    }

    private int getTopChildPosition(@NonNull final RecyclerView parent) {
        int topChildPosition = -1;

        final View topChild = parent.getChildAt(0);
        if (topChild != null) {
            topChildPosition = parent.getChildAdapterPosition(topChild);
        }

        return topChildPosition;
    }

    private View getHeaderViewForItem(final int headerPosition, final RecyclerView parent) {
        final int layoutResId = listener.getHeaderLayout(headerPosition);
        final View header = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        listener.bindHeaderData(header, headerPosition);
        return header;
    }

    private void drawHeader(final Canvas canvas, final View header) {
        canvas.save();
        canvas.translate(0, 0);
        header.draw(canvas);
        canvas.restore();
    }

    private void moveHeader(final Canvas canvas, final View currentHeader, final View nextHeader) {
        canvas.save();
        canvas.translate(0, (nextHeader.getTop() - currentHeader.getHeight()));
        currentHeader.draw(canvas);
        canvas.restore();
    }

    private View getChildInContact(final RecyclerView parent, final int contactPoint, final int currentHeaderPos) {
        View childInContact = null;
        for (int i = 0; i < parent.getChildCount(); i++) {
            int heightTolerance = 0;
            final View child = parent.getChildAt(i);

            //measure height tolerance with child if child is another header
            if (currentHeaderPos != i) {
                final boolean isChildHeader = listener.isHeader(parent.getChildAdapterPosition(child));
                if (isChildHeader) {
                    heightTolerance = stickyHeaderHeight - child.getHeight();
                }
            }

            //add heightTolerance if child top be in display area
            final int childBottomPosition;
            if (child.getTop() > 0) {
                childBottomPosition = child.getBottom() + heightTolerance;
            } else {
                childBottomPosition = child.getBottom();
            }

            if (childBottomPosition > contactPoint) {
                if (child.getTop() <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child;
                    break;
                }
            }
        }
        return childInContact;
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

    public interface StickyHeaderInterface {

        int getHeaderPositionForItem(final int itemPosition);

        int getHeaderLayout(final int headerPosition);

        void bindHeaderData(final View header, final int headerPosition);

        boolean isHeader(final int itemPosition);
    }
}
