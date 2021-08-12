package coolpharaoh.tee.speicher.tea.timer.views.software;

import android.os.Bundle;
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
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerItem;
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerViewAdapter;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class Software extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        configureAndShowListView();
    }

    private void defineToolbarAsActionbar() {
        final Toolbar toolbar = findViewById(R.id.tool_bar);
        final TextView toolbarCustomTitle = findViewById(R.id.tool_bar_title);
        toolbarCustomTitle.setText(R.string.software_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureAndShowListView() {
        final List<RecyclerItem> softwareList = generateListItems();

        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(R.layout.list_single_layout_software, softwareList,
                position -> { /*this functionality is not needed, but needs to be override*/ });

        final RecyclerView recyclerViewDetails = findViewById(R.id.recycler_view_software);
        recyclerViewDetails.addItemDecoration(new DividerItemDecoration(recyclerViewDetails.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDetails.setAdapter(adapter);
    }

    private List<RecyclerItem> generateListItems() {
        final List<RecyclerItem> softwareList = new ArrayList<>();
        final RecyclerItem itemPicker = new RecyclerItem(getString(R.string.software_colorpicker_heading), getString(R.string.software_colorpicker_description));
        softwareList.add(itemPicker);
        final RecyclerItem itemStatistic = new RecyclerItem(getString(R.string.software_statistic_heading), getString(R.string.software_statistic_description));
        softwareList.add(itemStatistic);
        return softwareList;
    }
}
