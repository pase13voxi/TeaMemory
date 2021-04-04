package coolpharaoh.tee.speicher.tea.timer.views.about;

import android.content.Intent;
import android.net.Uri;
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

import coolpharaoh.tee.speicher.tea.timer.BuildConfig;
import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.contact.Contact;
import coolpharaoh.tee.speicher.tea.timer.views.software.Software;
import coolpharaoh.tee.speicher.tea.timer.views.statistics.Statistics;
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.ListRowItem;
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerViewAdapter;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class About extends AppCompatActivity implements RecyclerViewAdapter.OnClickListener {

    private enum ListItems {
        CONTACT, RATING, STATISTICS, SOFTWARE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        configureAndShowListView();
        displayVersion();
    }

    private void defineToolbarAsActionbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.about_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureAndShowListView() {
        List<ListRowItem> aboutList = generateListItems();

        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(R.layout.list_single_layout_about, aboutList, this);

        final RecyclerView recyclerViewDetails = findViewById(R.id.recycler_view_about);
        recyclerViewDetails.addItemDecoration(new DividerItemDecoration(recyclerViewDetails.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDetails.setAdapter(adapter);
    }

    private List<ListRowItem> generateListItems() {
        //write into listView
        List<ListRowItem> aboutList = new ArrayList<>();
        ListRowItem itemContact = new ListRowItem(getResources().getString(R.string.about_contact_heading), getResources().getString(R.string.about_contact_description));
        aboutList.add(itemContact);
        ListRowItem itemRating = new ListRowItem(getResources().getString(R.string.about_rating_heading), getResources().getString(R.string.about_rating_description));
        aboutList.add(itemRating);
        ListRowItem itemStatistics = new ListRowItem(getResources().getString(R.string.about_statistics_heading), getResources().getString(R.string.about_statistics_description));
        aboutList.add(itemStatistics);
        ListRowItem itemSoftware = new ListRowItem(getResources().getString(R.string.about_software_heading), getResources().getString(R.string.about_software_description));
        aboutList.add(itemSoftware);
        return aboutList;
    }

    private void displayVersion() {
        TextView textViewVersion = findViewById(R.id.textViewVersion);
        String version = BuildConfig.VERSION_NAME;
        textViewVersion.setText(getResources().getString(R.string.about_version, version));
    }

    @Override
    public void onOptionsRecyclerItemClick(final int position) {
        ListItems item = ListItems.values()[position];
        switch (item) {
            case CONTACT:
                navigateToContact();
                break;
            case RATING:
                navigateToStore();
                break;
            case STATISTICS:
                navigateToStatistics();
                break;
            case SOFTWARE:
                navigateToSoftware();
                break;
            default:
        }
    }

    private void navigateToContact() {
        Intent contactScreen = new Intent(About.this, Contact.class);
        startActivity(contactScreen);
    }

    private void navigateToStore() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void navigateToStatistics() {
        Intent statisticsScreen = new Intent(About.this, Statistics.class);
        startActivity(statisticsScreen);
    }

    private void navigateToSoftware() {
        Intent softwareScreen = new Intent(About.this, Software.class);
        startActivity(softwareScreen);
    }
}
