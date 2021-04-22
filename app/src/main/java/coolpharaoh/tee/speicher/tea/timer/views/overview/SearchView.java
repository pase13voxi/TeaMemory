package coolpharaoh.tee.speicher.tea.timer.views.overview;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import coolpharaoh.tee.speicher.tea.timer.R;

class SearchView {

    private SearchView() {
    }

    static void configureSearchView(Menu menu, OverviewViewModel overviewViewModel) {
        MenuItem searchItem = menu.findItem(R.id.action_overview_search);

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchOpenedOrClosed(menu, overviewViewModel, searchItem, searchView);

        textChanged(overviewViewModel, searchView);
    }

    private static void searchOpenedOrClosed(Menu menu, OverviewViewModel overviewViewModel, MenuItem searchItem, androidx.appcompat.widget.SearchView searchView) {
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewDetachedFromWindow(View view) {
                setItemsVisibility(menu, searchItem, true);
                overviewViewModel.refreshTeas();
            }

            @Override
            public void onViewAttachedToWindow(View view) {
                setItemsVisibility(menu, searchItem, false);
            }
        });
    }

    private static void textChanged(OverviewViewModel overviewViewModel, androidx.appcompat.widget.SearchView searchView) {
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchString) {
                overviewViewModel.visualizeTeasBySearchString(searchString);
                return false;
            }


        });
    }

    private static void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }
}
