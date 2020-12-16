package coolpharaoh.tee.speicher.tea.timer.views.information;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ListRowItem;

public class Information extends AppCompatActivity implements RecyclerViewAdapter.OnClickListener {

    private InformationViewModel informationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        long teaId = this.getIntent().getLongExtra("teaId", 0);
        informationViewModel = new InformationViewModel(teaId, getApplication());

        fillToolbarTitle();
        fillRatingBar();
        showDetailsList();
        fillNotes();

        final RatingBar ratingBar = findViewById(R.id.information_rating_bar);
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, b) -> updateTeaRating(rating));

        final ImageButton buttonAddDetail = findViewById(R.id.buttonAddDetail);
        buttonAddDetail.setOnClickListener(v -> addDetail());
    }

    private void defineToolbarAsActionbar() {
        final Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void fillToolbarTitle() {
        final TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(informationViewModel.getTeaName());
    }

    private void fillRatingBar() {
        final RatingBar ratingBar = findViewById(R.id.information_rating_bar);
        ratingBar.setRating(informationViewModel.getTeaRating());
    }

    private void showDetailsList() {
        final RecyclerView recyclerViewDetails = findViewById(R.id.recycler_view_details);
        recyclerViewDetails.addItemDecoration(new DividerItemDecoration(recyclerViewDetails.getContext(), DividerItemDecoration.VERTICAL));

        informationViewModel.getDetails().observe(this, details -> {
            final List<ListRowItem> detailsList = new ArrayList<>();
            for (final Note note : details) {
                detailsList.add(new ListRowItem(note.getHeader(), note.getDescription()));
            }
            final RecyclerViewAdapter adapter = new RecyclerViewAdapter(detailsList, this);
            recyclerViewDetails.setAdapter(adapter);
            recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));
        });
    }

    private void fillNotes() {
        final EditText editTextNotes = findViewById(R.id.editTextNotes);
        final Note note = informationViewModel.getNotes();
        editTextNotes.setText(note.getDescription());
    }

    private void addDetail() {
        final ViewGroup parent = findViewById(R.id.information_parent);

        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_add_edit_information, parent, false);

        final EditText editTextHeading = dialogLayout.findViewById(R.id.editTextDialogAddEditHeader);
        final EditText editTextDescription = dialogLayout.findViewById(R.id.editTextDialogAddEditDescription);

        new AlertDialog.Builder(this).setTitle(R.string.information_add_detail_dialog_heading)
                .setView(dialogLayout)
                .setNegativeButton(R.string.information_edit_detail_dialog_negative, null)
                .setPositiveButton(R.string.information_edit_detail_dialog_positive,
                        (dialogInterface, i) ->
                                storeDetail(editTextHeading, editTextDescription))
                .show();
    }

    private void updateTeaRating(final float rating) {
        informationViewModel.updateTeaRating((int) rating);
    }

    private void storeDetail(final EditText editTextHeading, final EditText editTextDescription) {
        final String heading = editTextHeading.getText().toString();
        final String description = editTextDescription.getText().toString();
        if (!heading.trim().isEmpty() && !description.trim().isEmpty()) {
            informationViewModel.addDetail(editTextHeading.getText().toString(),
                    editTextDescription.getText().toString());
        }
    }

    @Override
    public void onOptionsRecyclerItemClick(Button buttonOptions, int position) {
        final PopupMenu popup = new PopupMenu(getApplication(), buttonOptions);
        popup.inflate(R.menu.menu_information_options);

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_information_option_edit) {
                editDetail(position);
                return true;
            } else if (item.getItemId() == R.id.action_information_option_delete) {
                informationViewModel.deleteDetail(position);
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void editDetail(int position) {
        final ViewGroup parent = findViewById(R.id.information_parent);

        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_add_edit_information, parent, false);

        final Note detail = informationViewModel.getDetail(position);

        final EditText editTextHeading = dialogLayout.findViewById(R.id.editTextDialogAddEditHeader);
        editTextHeading.setText(detail.getHeader());
        final EditText editTextDescription = dialogLayout.findViewById(R.id.editTextDialogAddEditDescription);
        editTextDescription.setText(detail.getDescription());

        new AlertDialog.Builder(this).setTitle(R.string.information_edit_detail_dialog_heading)
                .setView(dialogLayout)
                .setNegativeButton(R.string.information_edit_detail_dialog_negative, null)
                .setPositiveButton(R.string.information_edit_detail_dialog_positive,
                        (dialogInterface, i) ->
                                informationViewModel.updateDetail(position, editTextHeading.getText().toString(),
                                        editTextDescription.getText().toString()))
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        final EditText editTextNotes = findViewById(R.id.editTextNotes);
        informationViewModel.updateNotes(editTextNotes.getText().toString());
    }
}