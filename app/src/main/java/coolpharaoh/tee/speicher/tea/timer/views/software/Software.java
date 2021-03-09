package coolpharaoh.tee.speicher.tea.timer.views.software;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ListRowItem;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class Software extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        configureAndShowListView();
    }

    private void defineToolbarAsActionbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView toolbarCustomTitle = findViewById(R.id.toolbar_title);
        toolbarCustomTitle.setText(R.string.software_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureAndShowListView() {
        List<ListRowItem> softwareList = generateListItems();

        SoftwareListAdapter adapter = new SoftwareListAdapter(this, softwareList);

        ListView listViewAbout = findViewById(R.id.listview_software);
        listViewAbout.setAdapter(adapter);
    }

    private List<ListRowItem> generateListItems() {
        List<ListRowItem> softwareList = new ArrayList<>();
        ListRowItem itemPicker = new ListRowItem(getResources().getString(R.string.software_colorpicker_heading), getResources().getString(R.string.software_colorpicker_description));
        softwareList.add(itemPicker);
        ListRowItem itemTooltips = new ListRowItem(getResources().getString(R.string.software_tooltip_heading), getResources().getString(R.string.software_tooltip_description));
        softwareList.add(itemTooltips);
        ListRowItem itemStatistic = new ListRowItem(getResources().getString(R.string.software_statistic_heading), getResources().getString(R.string.software_statistic_description));
        softwareList.add(itemStatistic);
        return softwareList;
    }
}
