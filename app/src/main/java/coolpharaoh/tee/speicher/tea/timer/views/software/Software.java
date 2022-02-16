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
        final RecyclerItem itemColorPicker = new RecyclerItem(getString(R.string.software_color_picker_heading), getString(R.string.software_color_picker_description));
        softwareList.add(itemColorPicker);
        final RecyclerItem itemChart = new RecyclerItem(getString(R.string.software_chart_heading), getString(R.string.software_chart_description));
        softwareList.add(itemChart);
        final RecyclerItem itemGlide = new RecyclerItem(getString(R.string.software_glide_heading), getString(R.string.software_glide_description));
        softwareList.add(itemGlide);
        final RecyclerItem itemJUnit = new RecyclerItem(getString(R.string.software_junit5_heading), getString(R.string.software_junit5_description));
        softwareList.add(itemJUnit);
        final RecyclerItem itemGson = new RecyclerItem(getString(R.string.software_gson_heading), getString(R.string.software_gson_description));
        softwareList.add(itemGson);
        return softwareList;
    }
}
