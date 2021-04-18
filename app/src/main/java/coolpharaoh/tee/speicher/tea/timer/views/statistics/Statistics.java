package coolpharaoh.tee.speicher.tea.timer.views.statistics;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.felix.horizontalbargraph.HorizontalBar;
import br.com.felix.horizontalbargraph.model.BarItem;
import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.tea.ColorConversation;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.StatisticsPOJO;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class Statistics extends AppCompatActivity {

    private StatisticsViewModel statisticsViewModel;

    private HorizontalBar horizontalBar;

    private int checkedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        statisticsViewModel = new StatisticsViewModel(getApplication());

        initHorizontalBar();
    }

    private void defineToolbarAsActionbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.tool_bar_title);
        mToolbarCustomTitle.setText(R.string.statistics_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initHorizontalBar() {
        horizontalBar = findViewById(R.id.statistic_chart);
        horizontalBar.init(this).hasAnimation(true).addAll(getItems(statisticsViewModel.getStatisticsOverall())).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_statistics, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_statistics_period) {
            dialogSortOption();
        }

        return super.onOptionsItemSelected(item);
    }

    private void dialogSortOption() {
        final String[] items = getResources().getStringArray(R.array.statistics_category);

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
        builder.setIcon(R.drawable.statistics_black);
        builder.setTitle(R.string.statistics_dialog_title);
        builder.setSingleChoiceItems(items, checkedItem, (dialog, item) -> {
            checkedItem = item;
            changePeriod(item);
            dialog.dismiss();
        });
        builder.setNegativeButton(R.string.statistics_dialog_negative, null);
        builder.create().show();
    }

    private void changePeriod(int choice) {
        horizontalBar.removeAll();
        switch (choice) {
            case 0:
                horizontalBar.addAll(getItems(statisticsViewModel.getStatisticsOverall()));
                break;
            case 1:
                horizontalBar.addAll(getItems(statisticsViewModel.getStatisticsMonth()));
                break;
            case 2:
                horizontalBar.addAll(getItems(statisticsViewModel.getStatisticsWeek()));
                break;
            case 3:
                horizontalBar.addAll(getItems(statisticsViewModel.getStatisticsDay()));
                break;
            default:
        }
    }

    private List<BarItem> getItems(List<StatisticsPOJO> statistics) {
        List<BarItem> items = new ArrayList<>();

        for (StatisticsPOJO statistic : statistics) {
            items.add(new BarItem(statistic.teaname, (double) statistic.counter, statistic.teacolor, ColorConversation.discoverForegroundColor(statistic.teacolor)));
        }

        return items;
    }
}
