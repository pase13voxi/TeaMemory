package coolpharaoh.tee.speicher.tea.timer.views.more;

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
import coolpharaoh.tee.speicher.tea.timer.views.export_import.ExportImport;
import coolpharaoh.tee.speicher.tea.timer.views.software.Software;
import coolpharaoh.tee.speicher.tea.timer.views.statistics.Statistics;
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerItem;
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerViewAdapter;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class More extends AppCompatActivity implements RecyclerViewAdapter.OnClickListener {

    private enum ListItems {
        CONTACT, RATING, STATISTICS, EXPORT_IMPORT, SOFTWARE, PRIVACY
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        configureAndShowListView();
        displayVersion();
    }

    private void defineToolbarAsActionbar() {
        final Toolbar toolbar = findViewById(R.id.tool_bar);
        final TextView mToolbarCustomTitle = findViewById(R.id.tool_bar_title);
        mToolbarCustomTitle.setText(R.string.more_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureAndShowListView() {
        final List<RecyclerItem> moreList = generateListItems();

        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(R.layout.list_single_layout_more, moreList, this);

        final RecyclerView recyclerViewMore = findViewById(R.id.recycler_view_more);
        recyclerViewMore.addItemDecoration(new DividerItemDecoration(recyclerViewMore.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewMore.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMore.setAdapter(adapter);
    }

    private List<RecyclerItem> generateListItems() {
        //write into listView
        final List<RecyclerItem> moreList = new ArrayList<>();
        final RecyclerItem itemContact = new RecyclerItem(getString(R.string.more_contact_heading), getString(R.string.more_contact_description));
        moreList.add(itemContact);
        final RecyclerItem itemRating = new RecyclerItem(getString(R.string.more_rating_heading), getString(R.string.more_rating_description));
        moreList.add(itemRating);
        final RecyclerItem itemStatistics = new RecyclerItem(getString(R.string.more_statistics_heading), getString(R.string.more_statistics_description));
        moreList.add(itemStatistics);
        final RecyclerItem itemExportImport = new RecyclerItem(getString(R.string.more_export_import_heading), getString(R.string.more_export_import_description));
        moreList.add(itemExportImport);
        final RecyclerItem itemSoftware = new RecyclerItem(getString(R.string.more_software_heading), getString(R.string.more_software_description));
        moreList.add(itemSoftware);
        final RecyclerItem itemPrivacy = new RecyclerItem(getString(R.string.more_privacy_heading), getString(R.string.more_privacy_description));
        moreList.add(itemPrivacy);
        return moreList;
    }

    private void displayVersion() {
        final TextView textViewVersion = findViewById(R.id.text_view_more_version);
        final String version = BuildConfig.VERSION_NAME;
        textViewVersion.setText(getResources().getString(R.string.more_version, version));
    }

    @Override
    public void onRecyclerItemClick(final int position) {
        final ListItems item = ListItems.values()[position];
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
            case EXPORT_IMPORT:
                navigateToExportImport();
                break;
            case SOFTWARE:
                navigateToSoftware();
                break;
            case PRIVACY:
                navigateToPrivacyPolicy();
                break;
            default:
        }
    }

    private void navigateToContact() {
        final Intent contactScreen = new Intent(More.this, Contact.class);
        startActivity(contactScreen);
    }

    private void navigateToStore() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (final android.content.ActivityNotFoundException activityNotFoundException) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void navigateToStatistics() {
        final Intent statisticsScreen = new Intent(More.this, Statistics.class);
        startActivity(statisticsScreen);
    }

    private void navigateToExportImport() {
        final Intent importExportScreen = new Intent(More.this, ExportImport.class);
        startActivity(importExportScreen);
    }

    private void navigateToSoftware() {
        final Intent softwareScreen = new Intent(More.this, Software.class);
        startActivity(softwareScreen);
    }

    private void navigateToPrivacyPolicy() {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.more_privacy_policy_url)));
        startActivity(intent);
    }
}
