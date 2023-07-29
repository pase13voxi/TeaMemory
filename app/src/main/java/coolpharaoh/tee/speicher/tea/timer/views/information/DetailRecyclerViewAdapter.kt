package coolpharaoh.tee.speicher.tea.timer.views.information

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerItem

internal class DetailRecyclerViewAdapter(private val listRowItems: List<RecyclerItem>, private val onClickListener: OnClickListener) : RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val detailsView = inflater.inflate(R.layout.list_single_layout_details, parent, false)

        return ViewHolder(detailsView, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detail = listRowItems[position]

        val header = holder.header
        header.text = detail.heading
        val description = holder.description
        description.text = detail.description
    }

    override fun getItemCount(): Int {
        return listRowItems.size
    }

    internal class ViewHolder(itemView: View, onClickListener: OnClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val header: TextView
        val description: TextView
        var onClickListener: OnClickListener

        init {
            header = itemView.findViewById(R.id.text_view_list_details_heading)
            description = itemView.findViewById(R.id.text_view_list_details_description)
            itemView.findViewById<View>(R.id.button_detail_options).setOnClickListener(this)

            this.onClickListener = onClickListener
        }

        override fun onClick(view: View) {
            val buttonOptions = view.findViewById<Button>(R.id.button_detail_options)
            onClickListener.onRecyclerItemClick(buttonOptions, adapterPosition)
        }
    }

    interface OnClickListener {
        fun onRecyclerItemClick(buttonOptions: Button, position: Int)
    }
}