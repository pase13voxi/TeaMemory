package coolpharaoh.tee.speicher.tea.timer.views.overview

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.TeaMemory
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.sdkVersion
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.views.description.UpdateDescription
import coolpharaoh.tee.speicher.tea.timer.views.more.More
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.NewTea
import coolpharaoh.tee.speicher.tea.timer.views.overview.SearchView.configureSearchView
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.RecyclerItemOverview
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.RecyclerItemsHeaderStrategyFactory.getStrategy
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.RecyclerViewAdapterOverview
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.StickyHeaderItemDecoration
import coolpharaoh.tee.speicher.tea.timer.views.settings.Settings
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageControllerFactory.getImageController
import java.util.Objects

// This class has 9 Parent because of AppCompatActivity
class Overview : AppCompatActivity(), RecyclerViewAdapterOverview.OnClickListener {

    private val teaListData: MutableList<RecyclerItemOverview> = ArrayList()

    private var overviewViewModel: OverviewViewModel? = null
    private var teaListAdapter: RecyclerViewAdapterOverview? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        defineToolbarAsActionbar()

        overviewViewModel = OverviewViewModel(application)

        initializeTeaList()
        initializeNewTeaButton()
        initializeRandomTeaButton()
        showUpdateDialogOnStart()
    }

    private fun defineToolbarAsActionbar() {
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        val toolbarCustomTitle = findViewById<TextView>(R.id.tool_bar_title)
        toolbarCustomTitle.setPadding(40, 0, 0, 0)
        toolbarCustomTitle.setText(R.string.app_name)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.title = null
    }

    private fun initializeTeaList() {
        val recyclerViewTeaList = findViewById<RecyclerView>(R.id.recycler_view_overview_tea_list)
        recyclerViewTeaList.layoutManager = LinearLayoutManager(this)
        teaListAdapter = RecyclerViewAdapterOverview(teaListData, this)
        recyclerViewTeaList.adapter = teaListAdapter

        bindTeaListWithTeaAdapterAndObserve(recyclerViewTeaList)
    }

    private fun bindTeaListWithTeaAdapterAndObserve(teaList: RecyclerView) {
        overviewViewModel!!.getTeas().observe(this) { teas: List<Tea> ->
            val recyclerItemsHeader = getStrategy(overviewViewModel!!.sortWithHeader, application)
            teaListData.clear()
            teaListData.addAll(recyclerItemsHeader.generateFrom(teas))

            teaListAdapter!!.notifyDataSetChanged()

            updateStickyHeaderOnRecyclerView(teaList, teaListAdapter)
        }
    }

    private fun updateStickyHeaderOnRecyclerView(teaList: RecyclerView, adapter: RecyclerViewAdapterOverview?) {
        if (overviewViewModel!!.sortWithHeader != -1) {
            teaList.addItemDecoration(StickyHeaderItemDecoration(adapter!!))
        }

        if (teaList.itemDecorationCount > 1) {
            teaList.removeItemDecorationAt(1)
        }

        if (teaList.itemDecorationCount > 0 && overviewViewModel!!.sortWithHeader == -1) {
            teaList.removeItemDecorationAt(0)
        }
    }

    override fun onRecyclerItemClick(teaId: Long) {
        navigateToShowTea(teaId)
    }

    private fun navigateToShowTea(teaId: Long) {
        val showTeaScreen = Intent(this@Overview, ShowTea::class.java)
        showTeaScreen.putExtra("teaId", teaId)
        startActivity(showTeaScreen)
    }

    override fun onRecyclerItemLongClick(itemView: View?, teaId: Long) {
        val popup = PopupMenu(application, itemView, Gravity.END)
        popup.inflate(R.menu.menu_overview_tea_list)

        val tea = overviewViewModel!!.getTeaBy(teaId)
        val inStockTitle = if (tea!!.inStock) R.string.overview_tea_list_menu_not_in_stock else R.string.overview_tea_list_menu_in_stock
        popup.menu.findItem(R.id.action_overview_tea_list_in_stock).setTitle(inStockTitle)

        popup.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.action_overview_tea_list_in_stock) {
                updateTeaInStock(teaId, !tea.inStock)
                return@setOnMenuItemClickListener true
            } else if (item.itemId == R.id.action_overview_tea_list_edit) {
                navigateToNewOrEditTea(teaId)
                return@setOnMenuItemClickListener true
            } else if (item.itemId == R.id.action_overview_tea_list_delete) {
                removeTeaDialog(teaId)
                return@setOnMenuItemClickListener true
            }
            false
        }
        popup.show()
    }

    private fun updateTeaInStock(teaId: Long, inStock: Boolean) {
        overviewViewModel!!.updateInStockOfTea(teaId, inStock)
    }

    private fun removeTeaDialog(teaId: Long) {
        val tea = overviewViewModel!!.getTeaBy(teaId)
        AlertDialog.Builder(this, R.style.dialog_theme)
            .setTitle(getString(R.string.overview_dialog_delete_tea_title, tea!!.name))
            .setMessage(R.string.overview_dialog_delete_tea_message)
            .setPositiveButton(R.string.overview_dialog_delete_tea_positive) { dialogInterface: DialogInterface?, i: Int -> removeTeaByTeaId(teaId) }
            .setNegativeButton(R.string.overview_dialog_delete_tea_negative, null)
            .show()
    }

    private fun removeTeaByTeaId(teaId: Long) {
        overviewViewModel!!.deleteTea(teaId.toInt().toLong())

        if (sdkVersion >= VERSION_CODES.Q) {
            val imageController = getImageController(this)
            imageController.removeImageByTeaId(teaId)
        }
    }

    private fun initializeNewTeaButton() {
        val newTea = findViewById<FloatingActionButton>(R.id.floating_button_overview_new_tea)
        newTea.setOnClickListener { v: View? -> navigateToNewOrEditTea(null) }
    }

    private fun navigateToNewOrEditTea(teaId: Long?) {
        val newTeaScreen = Intent(this@Overview, NewTea::class.java)
        if (teaId != null) {
            newTeaScreen.putExtra("teaId", teaId)
        }
        startActivity(newTeaScreen)
    }

    private fun initializeRandomTeaButton() {
        val randomChoice = findViewById<FloatingActionButton>(R.id.floating_button_overview_random_choice)
        randomChoice.setOnClickListener { v: View? -> dialogRandomChoice() }
    }

    private fun dialogRandomChoice() {
        val randomChoiceDialog = RandomChoiceDialog(
            overviewViewModel!!,
            getImageController(this)
        )
        randomChoiceDialog.show(supportFragmentManager, RandomChoiceDialog.TAG)
    }

    private fun showUpdateDialogOnStart() {
        val application = application as TeaMemory
        if (application != null && !application.overviewDialogsShown) {
            application.overviewDialogsShown = true
            showUpdateDialog()
        }
    }

    private fun showUpdateDialog() {
        if (overviewViewModel!!.isOverviewUpdateAlert) {
            AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.overview_dialog_update_header)
                .setMessage(R.string.overview_dialog_update_description)
                .setPositiveButton(R.string.overview_dialog_update_positive) { dialog: DialogInterface?, which: Int -> navigateToUpdateWindow() }
                .setNeutralButton(R.string.overview_dialog_update_neutral, null)
                .setNegativeButton(R.string.overview_dialog_update_negative) { dialog: DialogInterface?, which: Int -> overviewViewModel!!.isOverviewUpdateAlert = false }
                .show()
        }
    }

    private fun navigateToUpdateWindow() {
        overviewViewModel!!.isOverviewUpdateAlert = false
        val intent = Intent(this@Overview, UpdateDescription::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_overview, menu)

        configureSearchView(menu, overviewViewModel!!)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_overview_settings) {
            navigateToSettings()
        } else if (id == R.id.action_overview_more) {
            navigateToMore()
        } else if (id == R.id.action_overview_sort) {
            dialogSortOption()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun navigateToSettings() {
        val settingScreen = Intent(this@Overview, Settings::class.java)
        startActivity(settingScreen)
    }

    private fun navigateToMore() {
        val moreScreen = Intent(this@Overview, More::class.java)
        startActivity(moreScreen)
    }

    private fun dialogSortOption() {
        val recyclerViewConfigurationDialog = RecyclerViewConfigurationDialog(overviewViewModel!!)
        recyclerViewConfigurationDialog.show(supportFragmentManager, RecyclerViewConfigurationDialog.TAG)
    }

    public override fun onResume() {
        super.onResume()
        overviewViewModel!!.refreshTeas()
    }
}