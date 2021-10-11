package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea;

public class RecyclerViewAdapterOverview extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderItemDecoration.StickyHeaderInterface {
    private static final String LOG_TAG = ShowTea.class.getSimpleName();

    private final List<RecyclerItemOverview> recyclerItems;
    private final OnClickListener onClickListener;

    public RecyclerViewAdapterOverview(final List<RecyclerItemOverview> recyclerItems, final OnClickListener onClickListener) {
        this.recyclerItems = recyclerItems;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == 0) {
            final View recyclerView = inflater.inflate(R.layout.list_single_layout_tea, parent, false);
            return new ViewHolderTea(recyclerView, onClickListener);
        } else {
            final View recyclerView = inflater.inflate(R.layout.list_single_layout_tea_heading, parent, false);
            return new ViewHolderCategory(recyclerView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final RecyclerItemOverview item = recyclerItems.get(position);
        if (item.category != null) {
            ((ViewHolderCategory) holder).bindData(item);
        } else {
            ((ViewHolderTea) holder).bindData(item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return recyclerItems.get(position).category != null ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return recyclerItems.size();
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public int getHeaderLayout(final int headerPosition) {
        return R.layout.list_single_layout_tea_heading;
    }

    @Override
    public void bindHeaderData(final View header, final int headerPosition) {
        final TextView textViewHeading = header.findViewById(R.id.text_view_recycler_view_heading);
        textViewHeading.setText(recyclerItems.get(headerPosition).category);
    }

    @Override
    public boolean isHeader(final int itemPosition) {
        boolean isHeader = false;
        if (itemPosition < recyclerItems.size()) {
            isHeader = recyclerItems.get(itemPosition).category != null;
        }
        return isHeader;
    }

    static class ViewHolderCategory extends RecyclerView.ViewHolder {
        private final TextView header;

        public ViewHolderCategory(final View item) {
            super(item);

            header = item.findViewById(R.id.text_view_recycler_view_heading);
        }

        void bindData(final RecyclerItemOverview item) {
            header.setText(item.getCategory());
        }
    }

    static class ViewHolderTea extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final View view;
        private final TextView header;
        private final TextView description;
        private final ImageView inStock;
        private final OnClickListener onClickListener;
        private RecyclerItemOverview item;

        public ViewHolderTea(final View view, final OnClickListener onClickListener) {
            super(view);

            this.view = view;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            header = view.findViewById(R.id.text_view_recycler_view_heading);
            description = view.findViewById(R.id.text_view_recycler_view_description);
            inStock = view.findViewById(R.id.button_overview_in_stock);

            this.onClickListener = onClickListener;
        }

        void bindData(final RecyclerItemOverview item) {
            this.item = item;
            header.setText(item.getTeaName());
            if (item.getVariety().equals("")) {
                description.setText("-");
            } else {
                description.setText(item.getVariety());
            }
            inStock.setVisibility(isInStock(item));
        }

        private int isInStock(RecyclerItemOverview item) {
            return item.isFavorite() ? View.VISIBLE : View.GONE;
        }

        @Override
        public void onClick(final View view) {
            if (item.teaId != null) {
                onClickListener.onRecyclerItemClick(item.getTeaId());
            } else {
                Log.e(LOG_TAG, "Recycler item does not contain tea id");
            }
        }

        @Override
        public boolean onLongClick(final View view) {
            if (item.teaId != null) {
                onClickListener.onRecyclerItemLongClick(this.view, item.getTeaId());
            } else {
                Log.e(LOG_TAG, "Recycler item does not contain tea id");
            }
            return false;
        }
    }

    public interface OnClickListener {
        void onRecyclerItemClick(long teaId);

        void onRecyclerItemLongClick(View itemView, long teaId);
    }
}