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
        showDetailsList();
        fillNotes();

        ImageButton buttonAddDetail = findViewById(R.id.buttonAddDetail);
        buttonAddDetail.setOnClickListener(v -> addDetail());
    }

    private void defineToolbarAsActionbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void fillToolbarTitle() {
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(informationViewModel.getTeaName());
    }

    private void showDetailsList() {
        RecyclerView recyclerViewDetails = findViewById(R.id.recycler_view_details);
        recyclerViewDetails.addItemDecoration(new DividerItemDecoration(recyclerViewDetails.getContext(), DividerItemDecoration.VERTICAL));

        informationViewModel.getDetails().observe(this, details -> {
            List<ListRowItem> detailsList = new ArrayList<>();
            for (Note note : details) {
                final ListRowItem item = new ListRowItem(note.getHeader(), note.getDescription());
                detailsList.add(item);
            }
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(detailsList, this);
            recyclerViewDetails.setAdapter(adapter);
            recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));
        });
    }

    private void fillNotes() {
        EditText editTextNotes = findViewById(R.id.editTextNotes);
        Note note = informationViewModel.getNotes();
        editTextNotes.setText(note.getDescription());
    }

    private void addDetail() {
        ViewGroup parent = findViewById(R.id.information_parent);

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_add_edit_information, parent, false);

        final EditText editTextHeading = dialogLayout.findViewById(R.id.editTextDialogAddEditHeader);
        final EditText editTextDescription = dialogLayout.findViewById(R.id.editTextDialogAddEditDescription);

        new AlertDialog.Builder(this).setTitle(R.string.information_add_detail_dialog_heading)
                .setView(dialogLayout)
                .setNegativeButton(R.string.information_edit_detail_dialog_negative, null)
                .setPositiveButton(R.string.information_edit_detail_dialog_positive,
                        (dialogInterface, i) ->
                                informationViewModel.addDetail(editTextHeading.getText().toString(),
                                        editTextDescription.getText().toString()))
                .show();
    }

    @Override
    public void onOptionsRecyclerItemClick(Button buttonOptions, int position) {
        PopupMenu popup = new PopupMenu(getApplication(), buttonOptions);
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
        ViewGroup parent = findViewById(R.id.information_parent);

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_add_edit_information, parent, false);

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

        EditText editTextNotes = findViewById(R.id.editTextNotes);
        informationViewModel.updateNotes(editTextNotes.getText().toString());
    }
}