package coolpharaoh.tee.speicher.tea.timer.views.overview;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import coolpharaoh.tee.speicher.tea.timer.R;

class SearchView {

    private SearchView() {
    }

    static void configureSearchView(final Menu menu, final OverviewViewModel overviewViewModel) {
        final MenuItem searchItem = menu.findItem(R.id.action_overview_search);

        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchOpenedOrClosed(menu, overviewViewModel, searchItem, searchView);

        textChanged(overviewViewModel, searchView);
    }

    private static void searchOpenedOrClosed(final Menu menu, final OverviewViewModel overviewViewModel,
                                             final MenuItem searchItem, final androidx.appcompat.widget.SearchView searchView) {
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewDetachedFromWindow(final View view) {
                setItemsVisibility(menu, searchItem, true);
                overviewViewModel.refreshTeas();
            }

            @Override
            public void onViewAttachedToWindow(final View view) {
                setItemsVisibility(menu, searchItem, false);
            }
        });
    }

    private static void textChanged(final OverviewViewModel overviewViewModel, final androidx.appcompat.widget.SearchView searchView) {
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String searchString) {
                overviewViewModel.visualizeTeasBySearchString(searchString);
                return false;
            }


        });
    }

    private static void setItemsVisibility(final Menu menu, final MenuItem exception, final boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            final MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }
}
