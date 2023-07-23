package coolpharaoh.tee.speicher.tea.timer.views.statistics

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import coolpharaoh.tee.speicher.tea.timer.R
import java.util.Objects

// This class has 9 Parent because of AppCompatActivity
class Statistics : AppCompatActivity() {
    private var horizontalBarGraph: HorizontalBarGraph? = null
    private var statisticsViewModel: StatisticsViewModel? = null
    private var checkedItem = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        defineToolbarAsActionbar()
        enableAndShowBackButton()
        statisticsViewModel = StatisticsViewModel(application)
        horizontalBarGraph =
            HorizontalBarGraph(findViewById(R.id.horizontal_graph_statistics), this)
        horizontalBarGraph!!.display(statisticsViewModel!!.statisticsWeek)
    }

    private fun defineToolbarAsActionbar() {
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        val mToolbarCustomTitle = findViewById<TextView>(R.id.tool_bar_title)
        mToolbarCustomTitle.setText(R.string.statistics_heading)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.title = null
    }

    private fun enableAndShowBackButton() {
        Objects.requireNonNull(supportActionBar)?.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_statistics, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_statistics_period) {
            dialogSortOption()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogSortOption() {
        val items = resources.getStringArray(R.array.statistics_category)

        // Creating and Building the Dialog
        val builder = AlertDialog.Builder(this, R.style.dialog_theme)
        builder.setIcon(R.drawable.statistics_black)
        builder.setTitle(R.string.statistics_dialog_title)
        builder.setSingleChoiceItems(items, checkedItem) { dialog: DialogInterface, item: Int ->
            checkedItem = item
            when (checkedItem) {
                0 -> {
                    horizontalBarGraph!!.reset()
                    horizontalBarGraph!!.display(statisticsViewModel!!.statisticsWeek)
                }

                1 -> {
                    horizontalBarGraph!!.reset()
                    horizontalBarGraph!!.display(statisticsViewModel!!.statisticsMonth)
                }

                2 -> {
                    horizontalBarGraph!!.reset()
                    horizontalBarGraph!!.display(statisticsViewModel!!.statisticsYear)
                }

                3 -> {
                    horizontalBarGraph!!.reset()
                    horizontalBarGraph!!.display(statisticsViewModel!!.statisticsOverall)
                }

                else -> {}
            }
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.statistics_dialog_negative, null)
        builder.create().show()
    }
}