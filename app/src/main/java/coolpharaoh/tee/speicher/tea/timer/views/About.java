package coolpharaoh.tee.speicher.tea.timer.views;

import android.content.Intent;
import android.net.Uri;
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
import coolpharaoh.tee.speicher.tea.timer.views.listadapter.AboutListAdapter;
import coolpharaoh.tee.speicher.tea.timer.views.listadapter.ListRowItem;

public class About extends AppCompatActivity {

    private enum ListItems {
        Contact, Rating, Statistics, Software
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //define toolbar as a actionbar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.about_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //write into listView
        List<ListRowItem> aboutList = new ArrayList<>();
        ListRowItem itemContact = new ListRowItem(getResources().getString(R.string.about_contact_heading),getResources().getString(R.string.about_contact_description));
        aboutList.add(itemContact);
        ListRowItem itemRating = new ListRowItem(getResources().getString(R.string.about_rating_heading), getResources().getString(R.string.about_rating_description));
        aboutList.add(itemRating);
        ListRowItem itemStatistics = new ListRowItem(getResources().getString(R.string.about_statistics_heading), getResources().getString(R.string.about_statistics_description));
        aboutList.add(itemStatistics);
        ListRowItem itemSoftware = new ListRowItem(getResources().getString(R.string.about_software_heading),getResources().getString(R.string.about_software_description));
        aboutList.add(itemSoftware);

        //bind list with adapter
        AboutListAdapter adapter = new AboutListAdapter(this, aboutList);
        //add adapter to listview
        ListView listViewAbout = findViewById(R.id.listview_about);
        listViewAbout.setAdapter(adapter);

        listViewAbout.setOnItemClickListener((parent, view, position, id) -> {
            ListItems item = ListItems.values()[position];
            switch(item){
                case Contact:
                    //create new intent
                    Intent contactScreen = new Intent(About.this, Contact.class);
                    //start intent and switch to the other activity
                    startActivity(contactScreen);
                    break;
                case Rating:
                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    break;
                case Statistics:
                    Intent statisticsScreen = new Intent(About.this, Statistics.class);
                    startActivity(statisticsScreen);
                    break;
                case Software:
                    Intent softwareScreen = new Intent(About.this, Software.class);
                    startActivity(softwareScreen);
                    break;
            }

        });
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
