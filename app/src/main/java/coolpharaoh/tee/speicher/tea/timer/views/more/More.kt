package coolpharaoh.tee.speicher.tea.timer.views.more

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coolpharaoh.tee.speicher.tea.timer.BuildConfig
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.views.contact.Contact
import coolpharaoh.tee.speicher.tea.timer.views.export_import.ExportImport
import coolpharaoh.tee.speicher.tea.timer.views.software.Software
import coolpharaoh.tee.speicher.tea.timer.views.statistics.Statistics
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerItem
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerViewAdapter
import java.util.Objects

class More : AppCompatActivity(), RecyclerViewAdapter.OnClickListener {
    private enum class ListItems {
        CONTACT, RATING, STATISTICS, EXPORT_IMPORT, SOFTWARE, PRIVACY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more)
        defineToolbarAsActionbar()
        enableAndShowBackButton()

        configureAndShowListView()
        displayVersion()
    }

    private fun defineToolbarAsActionbar() {
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        val mToolbarCustomTitle = findViewById<TextView>(R.id.tool_bar_title)
        mToolbarCustomTitle.setText(R.string.more_heading)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
    }

    private fun enableAndShowBackButton() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureAndShowListView() {
        val moreList = generateListItems()

        val adapter = RecyclerViewAdapter(R.layout.list_single_layout_more, moreList, this)

        val recyclerViewMore = findViewById<RecyclerView>(R.id.recycler_view_more)
        recyclerViewMore.addItemDecoration(DividerItemDecoration(recyclerViewMore.context,
            DividerItemDecoration.VERTICAL))
        recyclerViewMore.layoutManager = LinearLayoutManager(this)
        recyclerViewMore.adapter = adapter
    }

    private fun generateListItems(): List<RecyclerItem> {
        //write into listView
        val moreList: MutableList<RecyclerItem> = ArrayList()
        val itemContact = RecyclerItem(getString(R.string.more_contact_heading),
            getString(R.string.more_contact_description))
        moreList.add(itemContact)
        val itemRating = RecyclerItem(getString(R.string.more_rating_heading),
            getString(R.string.more_rating_description))
        moreList.add(itemRating)
        val itemStatistics = RecyclerItem(getString(R.string.more_statistics_heading),
            getString(R.string.more_statistics_description))
        moreList.add(itemStatistics)
        val itemExportImport = RecyclerItem(getString(R.string.more_export_import_heading),
            getString(R.string.more_export_import_description))
        moreList.add(itemExportImport)
        val itemSoftware = RecyclerItem(getString(R.string.more_software_heading),
            getString(R.string.more_software_description))
        moreList.add(itemSoftware)
        val itemPrivacy = RecyclerItem(getString(R.string.more_privacy_heading),
            getString(R.string.more_privacy_description))
        moreList.add(itemPrivacy)
        return moreList
    }

    private fun displayVersion() {
        val textViewVersion = findViewById<TextView>(R.id.text_view_more_version)
        val version = BuildConfig.VERSION_NAME
        textViewVersion.text = resources.getString(R.string.more_version, version)
    }

    override fun onRecyclerItemClick(position: Int) {
        when (ListItems.entries[position]) {
            ListItems.CONTACT -> navigateToContact()
            ListItems.RATING -> navigateToStore()
            ListItems.STATISTICS -> navigateToStatistics()
            ListItems.EXPORT_IMPORT -> navigateToExportImport()
            ListItems.SOFTWARE -> navigateToSoftware()
            ListItems.PRIVACY -> navigateToPrivacyPolicy()
        }
    }

    private fun navigateToContact() {
        val contactScreen = Intent(this@More, Contact::class.java)
        startActivity(contactScreen)
    }

    private fun navigateToStore() {
        val appPackageName = packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")))
        } catch (activityNotFoundException: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }

    private fun navigateToStatistics() {
        val statisticsScreen = Intent(this@More, Statistics::class.java)
        startActivity(statisticsScreen)
    }

    private fun navigateToExportImport() {
        val importExportScreen = Intent(this@More, ExportImport::class.java)
        startActivity(importExportScreen)
    }

    private fun navigateToSoftware() {
        val softwareScreen = Intent(this@More, Software::class.java)
        startActivity(softwareScreen)
    }

    private fun navigateToPrivacyPolicy() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.more_privacy_policy_url)))
        startActivity(intent)
    }
}