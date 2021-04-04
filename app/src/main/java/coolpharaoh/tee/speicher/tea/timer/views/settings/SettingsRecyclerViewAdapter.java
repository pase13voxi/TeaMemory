package coolpharaoh.tee.speicher.tea.timer.views.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ListRowItem;

class SettingsRecyclerViewAdapter extends RecyclerView.Adapter<SettingsRecyclerViewAdapter.ViewHolder> {

    private final List<ListRowItem> listRowItems;
    private final OnClickListener onClickListener;

    public SettingsRecyclerViewAdapter(final List<ListRowItem> listRowItems, final OnClickListener onClickListener) {
        this.listRowItems = listRowItems;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);

        final View settingsView = inflater.inflate(R.layout.list_single_layout_setting, parent, false);

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

            header = itemView.findViewById(R.id.text_view_settings_recycler_view_heading);
            description = itemView.findViewById(R.id.text_view_settings_recycler_view_description);
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
