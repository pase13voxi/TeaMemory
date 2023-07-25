package coolpharaoh.tee.speicher.tea.timer.views.software

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerItem
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerViewAdapter
import java.util.Objects

class Software : AppCompatActivity(), RecyclerViewAdapter.OnClickListener  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_software)
        defineToolbarAsActionbar()
        enableAndShowBackButton()

        configureAndShowListView()
    }

    private fun defineToolbarAsActionbar() {
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        val toolbarCustomTitle = findViewById<TextView>(R.id.tool_bar_title)
        toolbarCustomTitle.setText(R.string.software_heading)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.title = null
    }

    private fun enableAndShowBackButton() {
        Objects.requireNonNull(supportActionBar)?.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureAndShowListView() {
        val softwareList = generateListItems()

        val adapter = RecyclerViewAdapter(R.layout.list_single_layout_software, softwareList, this)

                val recyclerViewDetails = findViewById<RecyclerView>(R.id.recycler_view_software)
                recyclerViewDetails.addItemDecoration(
                    DividerItemDecoration
                        (recyclerViewDetails.context, DividerItemDecoration.VERTICAL)
                )
                recyclerViewDetails.layoutManager = LinearLayoutManager(this)
                recyclerViewDetails.adapter = adapter
            }

    private fun generateListItems(): List<RecyclerItem> {
        val softwareList: MutableList<RecyclerItem> = ArrayList()
        val itemColorPicker = RecyclerItem(getString(R.string.software_color_picker_heading), getString(R.string.software_color_picker_description))
        softwareList.add(itemColorPicker)
        val itemChart = RecyclerItem(getString(R.string.software_chart_heading), getString(R.string.software_chart_description))
        softwareList.add(itemChart)
        val itemGlide = RecyclerItem(getString(R.string.software_glide_heading), getString(R.string.software_glide_description))
        softwareList.add(itemGlide)
        val itemJUnit = RecyclerItem(getString(R.string.software_junit5_heading), getString(R.string.software_junit5_description))
        softwareList.add(itemJUnit)
        val itemGson = RecyclerItem(getString(R.string.software_gson_heading), getString(R.string.software_gson_description))
        softwareList.add(itemGson)
        return softwareList
    }

    override fun onRecyclerItemClick(position: Int) {
        /*this functionality is not needed, but needs to be overwritten*/
    }
}