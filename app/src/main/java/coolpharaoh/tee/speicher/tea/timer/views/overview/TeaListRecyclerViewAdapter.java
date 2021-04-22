package coolpharaoh.tee.speicher.tea.timer.views.overview;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.language.LanguageConversation;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;

public class TeaListRecyclerViewAdapter extends RecyclerView.Adapter<TeaListRecyclerViewAdapter.ViewHolder> {

    private final List<Tea> listRowItems;
    private final OnClickListener onClickListener;
    private final Application application;

    public TeaListRecyclerViewAdapter(final List<Tea> listRowItems, final OnClickListener onClickListener, Application application) {
        this.listRowItems = listRowItems;
        this.onClickListener = onClickListener;
        this.application = application;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);

        final View settingsView = inflater.inflate(R.layout.list_single_layout_tea, parent, false);

        return new ViewHolder(settingsView, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Tea tea = listRowItems.get(position);

        final TextView header = holder.header;
        header.setText(tea.getName());
        final TextView description = holder.description;
        if (tea.getVariety().equals("")) {
            description.setText(LanguageConversation.convertCodeToVariety("-", application));
        } else {
            description.setText(LanguageConversation.convertCodeToVariety(tea.getVariety(), application));
        }
    }

    @Override
    public int getItemCount() {
        return listRowItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final View item;
        private final TextView header;
        private final TextView description;
        final OnClickListener onClickListener;

        public ViewHolder(final View item, final OnClickListener onClickListener) {
            super(item);

            this.item = item;
            header = item.findViewById(R.id.text_view_recycler_view_heading);
            description = item.findViewById(R.id.text_view_recycler_view_description);
            item.setOnClickListener(this);
            item.setOnLongClickListener(this);

            this.onClickListener = onClickListener;
        }


        @Override
        public void onClick(final View view) {
            onClickListener.onRecyclerItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(final View view) {
            onClickListener.onRecyclerItemLongClick(item, getAdapterPosition());
            return false;
        }
    }

    public interface OnClickListener {
        void onRecyclerItemClick(int position);

        void onRecyclerItemLongClick(View itemView, int position);
    }
}
