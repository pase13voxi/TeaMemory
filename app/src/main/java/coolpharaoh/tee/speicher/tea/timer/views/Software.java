package coolpharaoh.tee.speicher.tea.timer.views;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.listadapter.ListRowItem;
import coolpharaoh.tee.speicher.tea.timer.views.listadapter.SoftwareListAdapter;

public class Software extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView toolbarCustomTitle = findViewById(R.id.toolbar_title);
        toolbarCustomTitle.setText(R.string.software_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<ListRowItem> softwareList = new ArrayList<>();
        ListRowItem itemPicker = new ListRowItem(getResources().getString(R.string.software_colorpicker_heading), getResources().getString(R.string.software_colorpicker_description));
        softwareList.add(itemPicker);
        ListRowItem itemTooltips = new ListRowItem(getResources().getString(R.string.software_tooltip_heading), getResources().getString(R.string.software_tooltip_description));
        softwareList.add(itemTooltips);
        ListRowItem itemStatistic = new ListRowItem(getResources().getString(R.string.software_statistic_heading), getResources().getString(R.string.software_statistic_description));
        softwareList.add(itemStatistic);

        //Liste mit Adapter verknüpfen
        SoftwareListAdapter adapter = new SoftwareListAdapter(this, softwareList);
        //Adapter dem Listview hinzufügen
        ListView listViewAbout = findViewById(R.id.listview_software);
        listViewAbout.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
