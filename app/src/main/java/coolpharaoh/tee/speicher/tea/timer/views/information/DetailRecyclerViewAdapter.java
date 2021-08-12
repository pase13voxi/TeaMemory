package coolpharaoh.tee.speicher.tea.timer.views.information;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerItem;

/**
 * Created by paseb on 03.11.2016.
 */

class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder> {

    private final List<RecyclerItem> listRowItems;
    private final OnClickListener onClickListener;

    public DetailRecyclerViewAdapter(List<RecyclerItem> listRowItems, OnClickListener onClickListener) {
        this.listRowItems = listRowItems;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View detailsView = inflater.inflate(R.layout.list_single_layout_details, parent, false);

        return new ViewHolder(detailsView, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerItem detail = listRowItems.get(position);

        TextView header = holder.header;
        header.setText(detail.getHeading());
        TextView description = holder.description;
        description.setText(detail.getDescription());
    }

    @Override
    public int getItemCount() {
        return listRowItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView header;
        private final TextView description;
        OnClickListener onClickListener;

        public ViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView);

            header = itemView.findViewById(R.id.text_view_list_details_heading);
            description = itemView.findViewById(R.id.text_view_list_details_description);
            itemView.findViewById(R.id.button_detail_options).setOnClickListener(this);

            this.onClickListener = onClickListener;
        }


        @Override
        public void onClick(View view) {
            final Button buttonOptions = view.findViewById(R.id.button_detail_options);
            onClickListener.onRecyclerItemClick(buttonOptions, getAdapterPosition());
        }
    }

    public interface OnClickListener {
        void onRecyclerItemClick(Button buttonOptions, int position);
    }
}
