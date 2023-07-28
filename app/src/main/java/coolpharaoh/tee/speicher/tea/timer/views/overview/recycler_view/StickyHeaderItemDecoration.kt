package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class StickyHeaderItemDecoration(private val listener: StickyHeaderInterface) : ItemDecoration() {

    private var stickyHeaderHeight = 0

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        val topChildPosition = getTopChildPosition(parent)
        if (topChildPosition != RecyclerView.NO_POSITION) {
            drawOverHeader(canvas, parent, topChildPosition)
        }
    }

    private fun getTopChildPosition(parent: RecyclerView): Int {
        var topChildPosition = RecyclerView.NO_POSITION

        val topChild = parent.getChildAt(0)
        if (topChild != null) {
            topChildPosition = parent.getChildAdapterPosition(topChild)
        }

        return topChildPosition
    }

    private fun drawOverHeader(canvas: Canvas, parent: RecyclerView, topChildPosition: Int) {
        val headerPosition = listener.getHeaderPositionForItem(topChildPosition)
        val currentHeader = getHeaderViewForItem(headerPosition, parent)
        fixLayoutSize(parent, currentHeader)
        val contactPoint = currentHeader.bottom
        val childInContact = getChildInContact(parent, contactPoint, headerPosition)

        if (isChildInContactAHeader(parent, childInContact)) {
            moveHeader(canvas, currentHeader, childInContact)
        } else {
            drawHeader(canvas, currentHeader)
        }
    }

    private fun getHeaderViewForItem(headerPosition: Int, parent: RecyclerView): View {
        val layoutResId = listener.getHeaderLayout(headerPosition)
        val header = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        listener.bindHeaderData(header, headerPosition)
        return header
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {

        val parentWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val parentHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        val childWidthSpec = ViewGroup.getChildMeasureSpec(parentWidthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(parentHeightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)

        view.measure(childWidthSpec, childHeightSpec)

        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        stickyHeaderHeight = view.measuredHeight
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int,
                                  currentHeaderPosition: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            if (isChildAndStickyHeaderInContact(parent, contactPoint, currentHeaderPosition, i)) {
                childInContact = parent.getChildAt(i)
                break
            }
        }
        return childInContact
    }

    private fun isChildAndStickyHeaderInContact(parent: RecyclerView, contactPoint: Int,
                                                currentHeaderPosition: Int, index: Int): Boolean {
        val child = parent.getChildAt(index)

        val childBottomPosition = getChildBottomPosition(parent, currentHeaderPosition, index)
        val childTopPosition = child.top

        return childBottomPosition > contactPoint && childTopPosition <= contactPoint
    }

    private fun getChildBottomPosition(parent: RecyclerView, currentHeaderPosition: Int,
                                       index: Int): Int {
        val heightTolerance = getHeightTolerance(parent, currentHeaderPosition, index)
        val child = parent.getChildAt(index)

        val childBottomPosition: Int
        childBottomPosition = if (child.top > 0) {
            child.bottom + heightTolerance
        } else {
            child.bottom
        }
        return childBottomPosition
    }

    private fun getHeightTolerance(parent: RecyclerView, currentHeaderPosition: Int,
                                   index: Int): Int {
        val child = parent.getChildAt(index)

        var heightTolerance = 0
        if (currentHeaderPosition != index) {
            val isChildHeader = listener.isHeader(parent.getChildAdapterPosition(child))
            if (isChildHeader) {
                heightTolerance = stickyHeaderHeight - child.height
            }
        }
        return heightTolerance
    }

    private fun isChildInContactAHeader(parent: RecyclerView, childInContact: View?): Boolean {
        return childInContact != null && listener.isHeader(parent.getChildAdapterPosition(childInContact))
    }

    private fun moveHeader(canvas: Canvas, currentHeader: View, nextHeader: View?) {
        canvas.save()
        canvas.translate(0f, (nextHeader!!.top - currentHeader.height).toFloat())
        currentHeader.draw(canvas)
        canvas.restore()
    }

    private fun drawHeader(canvas: Canvas, header: View) {
        canvas.save()
        canvas.translate(0f, 0f)
        header.draw(canvas)
        canvas.restore()
    }

    interface StickyHeaderInterface {
        fun getHeaderPositionForItem(itemPosition: Int): Int
        fun getHeaderLayout(headerPosition: Int): Int
        fun bindHeaderData(header: View, headerPosition: Int)
        fun isHeader(itemPosition: Int): Boolean
    }
}