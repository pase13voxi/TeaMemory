package coolpharaoh.tee.speicher.tea.timer.views.overview

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.appcompat.widget.SearchView
import coolpharaoh.tee.speicher.tea.timer.R

internal object SearchView {

    @JvmStatic
    fun configureSearchView(menu: Menu, overviewViewModel: OverviewViewModel) {
        val searchItem = menu.findItem(R.id.action_overview_search)

        val searchView = searchItem.actionView as SearchView

        searchOpenedOrClosed(menu, overviewViewModel, searchItem, searchView)

        textChanged(overviewViewModel, searchView)
    }

    private fun searchOpenedOrClosed(menu: Menu, overviewViewModel: OverviewViewModel,
                                     searchItem: MenuItem, searchView: SearchView) {
        searchView.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {

            override fun onViewDetachedFromWindow(view: View) {
                setItemsVisibility(menu, searchItem, true)
                overviewViewModel.refreshTeas()
            }

            override fun onViewAttachedToWindow(view: View) {
                setItemsVisibility(menu, searchItem, false)
            }
        })
    }

    private fun textChanged(overviewViewModel: OverviewViewModel, searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(searchString: String): Boolean {
                overviewViewModel.visualizeTeasBySearchString(searchString)
                return false
            }
        })
    }

    private fun setItemsVisibility(menu: Menu, exception: MenuItem, visible: Boolean) {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item !== exception) item.isVisible = visible
        }
    }
}