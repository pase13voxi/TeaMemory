package coolpharaoh.tee.speicher.tea.timer.views.information;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerItem;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class Information extends AppCompatActivity implements DetailRecyclerViewAdapter.OnClickListener {

    private static final String DATE_FORMAT = "dd MMMM yyyy";
    private static final String TEA_ID_EXTRA = "teaId";

    private static boolean noPicture = true;

    private InformationViewModel informationViewModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        final long teaId = this.getIntent().getLongExtra(TEA_ID_EXTRA, 0);
        informationViewModel = new InformationViewModel(teaId, getApplication());

        fillTexViewTeaName();
        fillTexViewTeaVariety();
        fillRatingBar();
        showDetailsList();
        fillLastUsed();
        fillCounter();
        fillNotes();

        final RatingBar ratingBar = findViewById(R.id.rating_bar_information);
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, b) -> updateTeaRating(rating));

        final ImageButton buttonAddDetail = findViewById(R.id.button_information_add_detail);
        buttonAddDetail.setOnClickListener(v -> addDetail());

        final FloatingActionButton buttonCamera = findViewById(R.id.button_information_camera);
        buttonCamera.setOnClickListener(v -> makeImage());
    }

    private void defineToolbarAsActionbar() {
        final Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (noPicture) {
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_primary));
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void fillTexViewTeaName() {
        final TextView texViewTeaName = findViewById(R.id.text_view_information_tea_name);
        if (noPicture) {
            texViewTeaName.setTextColor(ContextCompat.getColor(this, R.color.text_black));
        }
        texViewTeaName.setText(informationViewModel.getTeaName());
    }

    private void fillTexViewTeaVariety() {
        final TextView texViewTeaVariety = findViewById(R.id.text_view_information_tea_variety);
        texViewTeaVariety.setText(informationViewModel.getVarietyAsText());
    }

    private void fillRatingBar() {
        final RatingBar ratingBar = findViewById(R.id.rating_bar_information);
        ratingBar.setRating(informationViewModel.getTeaRating());
    }

    private void showDetailsList() {
        final RecyclerView recyclerViewDetails = findViewById(R.id.recycler_view_information_details);
        recyclerViewDetails.addItemDecoration(new DividerItemDecoration(recyclerViewDetails.getContext(), DividerItemDecoration.VERTICAL));

        informationViewModel.getDetails().observe(this, details -> {
            final List<RecyclerItem> detailsList = new ArrayList<>();
            for (final Note note : details) {
                detailsList.add(new RecyclerItem(note.getHeader(), note.getDescription()));
            }
            final DetailRecyclerViewAdapter adapter = new DetailRecyclerViewAdapter(detailsList, this);
            recyclerViewDetails.setAdapter(adapter);
            recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));
        });
    }

    private void fillLastUsed() {
        final TextView textViewLastUsed = findViewById(R.id.text_view_information_last_used);
        final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        final String date = formatter.format(informationViewModel.getDate());
        textViewLastUsed.setText(getString(R.string.information_counter_last_used, date));
    }

    private void fillCounter() {
        final TextView textViewToday = findViewById(R.id.text_view_information_counter_today);
        final TextView textViewWeek = findViewById(R.id.text_view_information_counter_week);
        final TextView textViewMonth = findViewById(R.id.text_view_information_counter_month);
        final TextView textViewOverall = findViewById(R.id.text_view_information_counter_overall);

        final Counter counter = informationViewModel.getCounter();
        textViewToday.setText(String.valueOf(counter.getDay()));
        textViewWeek.setText(String.valueOf(counter.getWeek()));
        textViewMonth.setText(String.valueOf(counter.getMonth()));
        textViewOverall.setText(String.valueOf(counter.getOverall()));
    }

    private void fillNotes() {
        final EditText editTextNotes = findViewById(R.id.edit_text_information_notes);
        final Note note = informationViewModel.getNotes();
        editTextNotes.setText(note.getDescription());
    }

    private void updateTeaRating(final float rating) {
        informationViewModel.updateTeaRating((int) rating);
    }

    private void addDetail() {
        final ViewGroup parent = findViewById(R.id.information_parent);

        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_add_edit_information, parent, false);

        final EditText editTextHeading = dialogLayout.findViewById(R.id.edit_text_information_dialog_add_edit_header);
        final EditText editTextDescription = dialogLayout.findViewById(R.id.edit_text_information_dialog_add_edit_description);

        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.information_add_detail_dialog_heading)
                .setView(dialogLayout)
                .setNegativeButton(R.string.information_edit_detail_dialog_negative, null)
                .setPositiveButton(R.string.information_edit_detail_dialog_positive,
                        (dialogInterface, i) ->
                                storeDetail(editTextHeading, editTextDescription))
                .show();
    }

    private void storeDetail(final EditText editTextHeading, final EditText editTextDescription) {
        final String heading = editTextHeading.getText().toString();
        final String description = editTextDescription.getText().toString();
        if (!heading.trim().isEmpty() && !description.trim().isEmpty()) {
            informationViewModel.addDetail(editTextHeading.getText().toString(),
                    editTextDescription.getText().toString());
        }
    }

    private void makeImage() {

        noPicture = !noPicture;

        final ImageView imageViewImage = findViewById(R.id.image_view_information_image);
        final Toolbar toolbar = findViewById(R.id.tool_bar);
        final TextView texViewTeaName = findViewById(R.id.text_view_information_tea_name);

        if (noPicture) {
            imageViewImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.whole_cup));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_primary));
            texViewTeaName.setTextColor(ContextCompat.getColor(this, R.color.text_black));
        } else {
            imageViewImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.tree));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
            texViewTeaName.setTextColor(ContextCompat.getColor(this, R.color.text_white));
        }
    }

    @Override
    public void onRecyclerItemClick(final Button buttonOptions, final int position) {
        final PopupMenu popup = new PopupMenu(getApplication(), buttonOptions);
        popup.inflate(R.menu.menu_information_details);

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_information_details_edit) {
                editDetail(position);
                return true;
            } else if (item.getItemId() == R.id.action_information_details_delete) {
                informationViewModel.deleteDetail(position);
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void editDetail(final int position) {
        final ViewGroup parent = findViewById(R.id.information_parent);

        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_add_edit_information, parent, false);

        final Note detail = informationViewModel.getDetail(position);

        final EditText editTextHeading = dialogLayout.findViewById(R.id.edit_text_information_dialog_add_edit_header);
        editTextHeading.setText(detail.getHeader());
        final EditText editTextDescription = dialogLayout.findViewById(R.id.edit_text_information_dialog_add_edit_description);
        editTextDescription.setText(detail.getDescription());

        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.information_edit_detail_dialog_heading)
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

        final EditText editTextNotes = findViewById(R.id.edit_text_information_notes);
        informationViewModel.updateNotes(editTextNotes.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_information, menu);

        fillInStock(menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void fillInStock(final Menu menu) {
        final MenuItem item = menu.findItem(R.id.action_information_in_stock);
        if (informationViewModel.isInStock()) {
            item.setIcon(R.drawable.home_white);
        } else {
            item.setIcon(R.drawable.home_white_empty);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_information_in_stock) {
            if (informationViewModel.isInStock()) {
                item.setIcon(R.drawable.home_white_empty);
                informationViewModel.updateTeaInStock(false);
            } else {
                item.setIcon(R.drawable.home_white);
                informationViewModel.updateTeaInStock(true);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}