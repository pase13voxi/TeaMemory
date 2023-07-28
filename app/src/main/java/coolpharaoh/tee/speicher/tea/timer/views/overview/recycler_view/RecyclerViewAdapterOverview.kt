package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import android.net.Uri
import android.os.Build.VERSION_CODES
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.sdkVersion
import coolpharaoh.tee.speicher.tea.timer.core.tea.ColorConversation.discoverForegroundColor
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.StickyHeaderItemDecoration.StickyHeaderInterface
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageControllerFactory.getImageController

class RecyclerViewAdapterOverview(private val recyclerItems: List<RecyclerItemOverview>,
                                  private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>(), StickyHeaderInterface {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        return if (viewType == 0) {
            val recyclerView = inflater.inflate(R.layout.list_single_layout_tea, parent, false)
            ViewHolderTea(recyclerView, onClickListener)
        } else {
            val recyclerView =
                inflater.inflate(R.layout.list_single_layout_tea_heading, parent, false)
            ViewHolderCategory(recyclerView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = recyclerItems[position]
        if (item.category != null) {
            (holder as ViewHolderCategory).bindData(item)
        } else {
            (holder as ViewHolderTea).bindData(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (recyclerItems[position].category != null) 1 else 0
    }

    override fun getItemCount(): Int {
        return recyclerItems.size
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var itemPosition = itemPosition
        var headerPosition = 0
        do {
            if (isHeader(itemPosition)) {
                headerPosition = itemPosition
                break
            }
            itemPosition -= 1
        } while (itemPosition >= 0)
        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.list_single_layout_tea_heading
    }

    override fun bindHeaderData(header: View, headerPosition: Int) {
        val textViewHeading = header.findViewById<TextView>(R.id.text_view_recycler_view_heading)
        textViewHeading.text = recyclerItems[headerPosition].category
    }

    override fun isHeader(itemPosition: Int): Boolean {
        var isHeader = false
        if (itemPosition < recyclerItems.size) {
            isHeader = recyclerItems[itemPosition].category != null
        }
        return isHeader
    }

    internal class ViewHolderCategory(item: View) : RecyclerView.ViewHolder(item) {
        private val header: TextView

        init {
            header = item.findViewById(R.id.text_view_recycler_view_heading)
        }

        fun bindData(item: RecyclerItemOverview) {
            header.text = item.category
        }
    }

    internal class ViewHolderTea(private val view: View, onClickListener: OnClickListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener, OnLongClickListener {

        private val image: ImageView
        private val imageText: TextView
        private val header: TextView
        private val description: TextView
        private val inStock: ImageView
        private val onClickListener: OnClickListener
        private val imageController: ImageController
        private var item: RecyclerItemOverview? = null

        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
            image = view.findViewById(R.id.image_view_recycler_view_image)
            imageText = view.findViewById(R.id.text_view_recycler_view_image)
            header = view.findViewById(R.id.text_view_recycler_view_heading)
            description = view.findViewById(R.id.text_view_recycler_view_description)
            inStock = view.findViewById(R.id.button_overview_in_stock)
            this.onClickListener = onClickListener
            imageController = getImageController(view.context)
        }

        fun bindData(item: RecyclerItemOverview?) {
            this.item = item

            fillRoundImage()
            fillTeaName()
            fillVariety()
            showInStock()
        }

        private fun fillRoundImage() {
            resetImage()

            val imageUri = imageUri
            if (imageUri != null) {
                setImage(imageUri)
            } else {
                setImageText()
            }
        }

        private fun resetImage() {
            Glide.with(view.context).clear(image)
            image.tag = null
            imageText.visibility = View.INVISIBLE
        }

        private val imageUri: Uri?
            get() {
                var imageUri: Uri? = null
                if (sdkVersion >= VERSION_CODES.Q) {
                    imageUri = imageController.getImageUriByTeaId(item!!.teaId!!)
                }
                return imageUri
            }

        private fun setImage(imageUri: Uri) {
            Glide.with(view.context)
                .load(imageUri)
                .signature(ObjectKey(imageController.getLastModified(imageUri)))
                .override(100, 100)
                .centerCrop()
                .into(image)
            image.tag = imageUri.toString()
        }

        private fun setImageText() {
            image.setBackgroundColor(item!!.color!!)
            imageText.visibility = View.VISIBLE
            imageText.text = item!!.imageText
            imageText.setTextColor(discoverForegroundColor(item!!.color!!))
        }

        private fun fillTeaName() {
            header.text = item!!.teaName
        }

        private fun fillVariety() {
            if (item!!.variety == "") {
                description.text = "-"
            } else {
                description.text = item!!.variety
            }
        }

        private fun showInStock() {
            inStock.visibility = isInStock(item)
        }

        private fun isInStock(item: RecyclerItemOverview?): Int {
            return if (item!!.favorite) View.VISIBLE else View.GONE
        }

        override fun onClick(view: View) {
            if (item!!.teaId != null) {
                onClickListener.onRecyclerItemClick(item!!.teaId!!)
            } else {
                Log.e(LOG_TAG, "Recycler item does not contain tea id")
            }
        }

        override fun onLongClick(view: View): Boolean {
            if (item!!.teaId != null) {
                onClickListener.onRecyclerItemLongClick(this.view, item!!.teaId!!)
            } else {
                Log.e(LOG_TAG, "Recycler item does not contain tea id")
            }
            return false
        }
    }

    interface OnClickListener {
        fun onRecyclerItemClick(teaId: Long)
        fun onRecyclerItemLongClick(itemView: View?, teaId: Long)
    }

    companion object {
        private val LOG_TAG = ShowTea::class.java.simpleName
    }
}