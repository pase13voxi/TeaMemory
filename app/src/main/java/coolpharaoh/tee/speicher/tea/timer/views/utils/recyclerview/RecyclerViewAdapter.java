package coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final int viewId;
    private final List<ListRowItem> listRowItems;
    private final OnClickListener onClickListener;

    public RecyclerViewAdapter(final int resource, final List<ListRowItem> listRowItems, final OnClickListener onClickListener) {
        this.viewId = resource;
        this.listRowItems = listRowItems;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);

        final View settingsView = inflater.inflate(viewId, parent, false);

        return new ViewHolder(settingsView, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ListRowItem detail = listRowItems.get(position);

        final TextView header = holder.header;
        header.setText(detail.getHeading());
        final TextView description = holder.description;
        description.setText(detail.getDescription());
    }

    @Override
    public int getItemCount() {
        return listRowItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView header;
        private final TextView description;
        final OnClickListener onClickListener;

        public ViewHolder(final View itemView, final OnClickListener onClickListener) {
            super(itemView);

            header = itemView.findViewById(R.id.text_view_recycler_view_heading);
            description = itemView.findViewById(R.id.text_view_recycler_view_description);
            itemView.setOnClickListener(this);

            this.onClickListener = onClickListener;
        }


        @Override
        public void onClick(final View view) {
            onClickListener.onOptionsRecyclerItemClick(getAdapterPosition());
        }
    }

    public interface OnClickListener {
        void onOptionsRecyclerItemClick(int position);
    }
}
