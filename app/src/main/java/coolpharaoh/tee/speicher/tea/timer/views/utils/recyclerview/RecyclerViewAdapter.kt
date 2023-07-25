package coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coolpharaoh.tee.speicher.tea.timer.R

class RecyclerViewAdapter(
    private val viewId: Int,
    private val listRowItems: List<RecyclerItem>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(viewId, parent, false)

        return ViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listRowItem = listRowItems[position]

        val header = holder.header
        header.text = listRowItem.heading
        val description = holder.description
        description.text = listRowItem.description
    }

    override fun getItemCount(): Int {
        return listRowItems.size
    }

    class ViewHolder(itemView: View, onClickListener: OnClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val header: TextView
        val description: TextView
        val onClickListener: OnClickListener

        init {
            header = itemView.findViewById(R.id.text_view_recycler_view_heading)
            description = itemView.findViewById(R.id.text_view_recycler_view_description)
            itemView.setOnClickListener(this)

            this.onClickListener = onClickListener
        }

        override fun onClick(view: View) {
            onClickListener.onRecyclerItemClick(adapterPosition)
        }
    }

    interface OnClickListener {
        fun onRecyclerItemClick(position: Int)
    }
}