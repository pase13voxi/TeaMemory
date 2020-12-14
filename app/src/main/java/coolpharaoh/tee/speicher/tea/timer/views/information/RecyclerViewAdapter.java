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
import coolpharaoh.tee.speicher.tea.timer.views.utils.ListRowItem;

/**
 * Created by paseb on 03.11.2016.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final List<ListRowItem> listRowItems;
    private final OnClickListener onClickListener;

    public RecyclerViewAdapter(List<ListRowItem> listRowItems, OnClickListener onClickListener) {
        this.listRowItems = listRowItems;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View detailsView = inflater.inflate(R.layout.detailslist_single_layout, parent, false);

        return new ViewHolder(detailsView, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListRowItem detail = listRowItems.get(position);

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

            header = itemView.findViewById(R.id.textViewListDetailsHeading);
            description = itemView.findViewById(R.id.textViewListDetailsDescription);
            itemView.findViewById(R.id.buttonDetailOptions).setOnClickListener(this);

            this.onClickListener = onClickListener;
        }


        @Override
        public void onClick(View view) {
            final Button buttonOptions = view.findViewById(R.id.buttonDetailOptions);
            onClickListener.onOptionsRecyclerItemClick(buttonOptions, getAdapterPosition());
        }
    }

    public interface OnClickListener {
        void onOptionsRecyclerItemClick(Button buttonOptions, int position);
    }
}
