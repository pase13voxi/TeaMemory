package coolpharaoh.tee.speicher.tea.timer.views.overview;

import static android.os.Build.VERSION_CODES.Q;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.convertStoredVarietyToText;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.RecyclerItemOverview;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea;
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController;

public class RandomChoiceDialog extends DialogFragment {
    public static final String TAG = "RandomChoiceDialog";

    private final OverviewViewModel overviewViewModel;
    private final ImageController imageController;
    private View dialogView;
    private RecyclerItemOverview randomChoiceItem;

    public RandomChoiceDialog(final OverviewViewModel overviewViewModel,
                              final ImageController imageController) {
        this.overviewViewModel = overviewViewModel;
        this.imageController = imageController;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstancesState) {
        final Activity activity = requireActivity();
        final ViewGroup parent = activity.findViewById(R.id.overview_parent);
        final LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_overview_random_choice, parent, false);

        refreshRandomChoice();

        final ImageButton buttonRefreshRandomChoice = dialogView.findViewById(R.id.button_random_choice_dialog_refresh);
        buttonRefreshRandomChoice.setOnClickListener(view -> refreshRandomChoice());

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.dialog_theme)
                .setTitle(R.string.overview_dialog_random_choice_title)
                .setView(dialogView)
                .setNegativeButton(R.string.overview_dialog_random_choice_negative, null);

        if (randomChoiceItem != null) {
            builder.setPositiveButton(R.string.overview_dialog_random_choice_positive, (dialogInterface, i) -> navigateToRandomTea());
        }

        return builder.create();
    }

    private void refreshRandomChoice() {
        final Tea randomTea = overviewViewModel.getRandomTeaInStock();
        if (randomTea != null) {
            randomChoiceItem = generateRecyclerViewItem(randomTea);

            fillRandomChoice();
        } else {
            noRandomChoiceAvailable();
        }
    }

    private RecyclerItemOverview generateRecyclerViewItem(final Tea tea) {
        final String variety = convertStoredVarietyToText(tea.getVariety(), getActivity().getApplication());
        return new RecyclerItemOverview(null, tea.getId(), tea.getName(),
                variety, tea.getColor(), tea.isInStock());
    }

    private void fillRandomChoice() {
        final TextView textViewTeaName = dialogView.findViewById(R.id.text_view_random_choice_dialog_tea_name);
        textViewTeaName.setText(randomChoiceItem.getTeaName());

        final TextView textViewTeaVariety = dialogView.findViewById(R.id.text_view_random_choice_dialog_variety);
        textViewTeaVariety.setText(randomChoiceItem.getVariety());

        updateImage();
    }

    private void updateImage() {
        final ImageView imageViewImage = dialogView.findViewById(R.id.image_view_random_tea_choice_image);
        imageViewImage.setImageURI(null);
        imageViewImage.setTag(null);

        final TextView textViewImageText = dialogView.findViewById(R.id.text_view_random_choice_dialog_image);

        Uri imageUri = null;
        if (CurrentSdk.getSdkVersion() >= Q) {
            imageUri = imageController.getImageUriByTeaId(randomChoiceItem.getTeaId());
        }

        if (imageUri != null) {
            fillImage(imageViewImage, textViewImageText, imageUri);
        } else {
            fillImageText(imageViewImage, textViewImageText);
        }
    }

    private void fillImage(final ImageView imageViewImage, final TextView textViewImageText, final Uri imageUri) {
        imageViewImage.setImageURI(imageUri);
        imageViewImage.setTag(imageUri.toString());
        textViewImageText.setVisibility(View.INVISIBLE);
    }

    private void fillImageText(final ImageView imageViewImage, final TextView textViewImageText) {
        imageViewImage.setBackgroundColor(randomChoiceItem.getColor());
        textViewImageText.setVisibility(View.VISIBLE);
        textViewImageText.setText(randomChoiceItem.getImageText());
    }

    private void noRandomChoiceAvailable() {
        final TextView textViewNoTea = dialogView.findViewById(R.id.text_view_random_choice_no_tea);
        textViewNoTea.setVisibility(View.VISIBLE);

        final RelativeLayout layoutTeaAvailable = dialogView.findViewById(R.id.layout_random_choice_tea_available);
        layoutTeaAvailable.setVisibility(View.GONE);

        final TextView textViewHint = dialogView.findViewById(R.id.text_view_random_choice_hint);
        textViewHint.setVisibility(View.GONE);
    }

    private void navigateToRandomTea() {
        final Intent showTeaScreen = new Intent(dialogView.getContext(), ShowTea.class);
        showTeaScreen.putExtra("teaId", randomChoiceItem.getTeaId());
        startActivity(showTeaScreen);
    }
}
