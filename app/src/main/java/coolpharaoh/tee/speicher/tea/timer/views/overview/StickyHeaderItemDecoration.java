package coolpharaoh.tee.speicher.tea.timer.views.overview;

import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class StickHeaderItemDecoration extends RecyclerView.ItemDecoration {

    private final StickyHeaderInterface listener;
    private int stickyHeaderHeight;

    public StickHeaderItemDecoration(@NonNull final StickyHeaderInterface listener) {
        this.listener = listener;
    }

    @Override
    public void onDrawOver(@NonNull final Canvas canvas, @NonNull final RecyclerView parent, @NonNull final RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        final View topChild = parent.getChildAt(0);
        if (topChild == null) {
            return;
        }

        final int topChildPosition = parent.getChildAdapterPosition(topChild);
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return;
        }

        final int headerPos = listener.getHeaderPositionForItem(topChildPosition);
        final View currentHeader = getHeaderViewForItem(headerPos, parent);
        fixLayoutSize(parent, currentHeader);
        final int contactPoint = currentHeader.getBottom();
        final View childInContact = getChildInContact(parent, contactPoint, headerPos);

        if (childInContact != null && listener.isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeader(canvas, currentHeader, childInContact);
            return;
        }

        drawHeader(canvas, currentHeader);
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


    /**
     * Properly measures and layouts the top sticky header.
     *
     * @param parent ViewGroup: RecyclerView in this case.
     */
    private void fixLayoutSize(final ViewGroup parent, final View view) {

        // Specs for parent (RecyclerView)
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

        // Specs for children (headers)
        final int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
        final int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

        view.measure(childWidthSpec, childHeightSpec);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        stickyHeaderHeight = view.getMeasuredHeight();
    }

    public interface StickyHeaderInterface {

        /**
         * This method gets called by {@link StickHeaderItemDecoration} to fetch the position of the header item in the adapter
         * that is used for (represents) item at specified position.
         *
         * @param itemPosition int. Adapter's position of the item for which to do the search of the position of the header item.
         * @return int. Position of the header item in the adapter.
         */
        int getHeaderPositionForItem(final int itemPosition);

        /**
         * This method gets called by {@link StickHeaderItemDecoration} to get layout resource id for the header item at specified adapter's position.
         *
         * @param headerPosition int. Position of the header item in the adapter.
         * @return int. Layout resource id.
         */
        int getHeaderLayout(final int headerPosition);

        /**
         * This method gets called by {@link StickHeaderItemDecoration} to setup the header View.
         *
         * @param header         View. Header to set the data on.
         * @param headerPosition int. Position of the header item in the adapter.
         */
        void bindHeaderData(final View header, final int headerPosition);

        /**
         * This method gets called by {@link StickHeaderItemDecoration} to verify whether the item represents a header.
         *
         * @param itemPosition int.
         * @return true, if item at the specified adapter's position represents a header.
         */
        boolean isHeader(final int itemPosition);
    }
}
