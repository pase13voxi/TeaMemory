package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import static android.os.Build.VERSION_CODES.Q;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;
import coolpharaoh.tee.speicher.tea.timer.core.tea.ColorConversation;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea;
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController;
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageControllerFactory;

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
        if (item.getCategory() != null) {
            ((ViewHolderCategory) holder).bindData(item);
        } else {
            ((ViewHolderTea) holder).bindData(item);
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return recyclerItems.get(position).getCategory() != null ? 1 : 0;
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
        textViewHeading.setText(recyclerItems.get(headerPosition).getCategory());
    }

    @Override
    public boolean isHeader(final int itemPosition) {
        boolean isHeader = false;
        if (itemPosition < recyclerItems.size()) {
            isHeader = recyclerItems.get(itemPosition).getCategory() != null;
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
        private final ImageView image;
        private final TextView imageText;
        private final TextView header;
        private final TextView description;
        private final ImageView inStock;
        private final OnClickListener onClickListener;
        private final ImageController imageController;
        private RecyclerItemOverview item;

        public ViewHolderTea(final View view, final OnClickListener onClickListener) {
            super(view);

            this.view = view;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            image = view.findViewById(R.id.image_view_recycler_view_image);
            imageText = view.findViewById(R.id.text_view_recycler_view_image);
            header = view.findViewById(R.id.text_view_recycler_view_heading);
            description = view.findViewById(R.id.text_view_recycler_view_description);
            inStock = view.findViewById(R.id.button_overview_in_stock);

            this.onClickListener = onClickListener;
            imageController = ImageControllerFactory.getImageController(view.getContext());
        }

        void bindData(final RecyclerItemOverview item) {
            this.item = item;

            fillRoundImage();
            fillTeaName();
            fillVariety();
            showInStock();
        }

        private void fillRoundImage() {
            resetImage();

            final Uri imageUri = getImageUri();
            if (imageUri != null) {
                setImage(imageUri);
            } else {
                setImageText();
            }
        }

        private void resetImage() {
            Glide.with(view.getContext()).clear(image);
            image.setTag(null);
            imageText.setVisibility(View.INVISIBLE);
        }

        @Nullable
        private Uri getImageUri() {
            Uri imageUri = null;
            if (CurrentSdk.getSdkVersion() >= Q) {
                imageUri = imageController.getImageUriByTeaId(item.getTeaId());
            }
            return imageUri;
        }

        private void setImage(final Uri imageUri) {
            Glide.with(view.getContext())
                    .load(imageUri)
                    .signature(new ObjectKey(imageController.getLastModified(imageUri)))
                    .override(100, 100)
                    .centerCrop()
                    .into(image);
            image.setTag(imageUri.toString());
        }

        private void setImageText() {
            image.setBackgroundColor(item.getColor());
            imageText.setVisibility(View.VISIBLE);
            imageText.setText(item.getImageText());
            imageText.setTextColor(ColorConversation.discoverForegroundColor(item.getColor()));
        }

        private void fillTeaName() {
            header.setText(item.getTeaName());
        }

        private void fillVariety() {
            if (item.getVariety().equals("")) {
                description.setText("-");
            } else {
                description.setText(item.getVariety());
            }
        }

        private void showInStock() {
            inStock.setVisibility(isInStock(item));
        }

        private int isInStock(final RecyclerItemOverview item) {
            return item.getFavorite() ? View.VISIBLE : View.GONE;
        }

        @Override
        public void onClick(final View view) {
            if (item.getTeaId() != null) {
                onClickListener.onRecyclerItemClick(item.getTeaId());
            } else {
                Log.e(LOG_TAG, "Recycler item does not contain tea id");
            }
        }

        @Override
        public boolean onLongClick(final View view) {
            if (item.getTeaId() != null) {
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
