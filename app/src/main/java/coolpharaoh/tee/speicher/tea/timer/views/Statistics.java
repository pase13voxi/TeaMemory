package coolpharaoh.tee.speicher.tea.timer.views;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.felix.horizontalbargraph.HorizontalBar;
import br.com.felix.horizontalbargraph.model.BarItem;
import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.datatransfer.pojo.StatisticsPOJO;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.StatisticsViewModel;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.ColorConversation;

public class Statistics extends AppCompatActivity {

    private StatisticsViewModel statisticsViewModel;

    private HorizontalBar horizontal;

    private int checkedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        //Toolbar als ActionBar festlegen
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.statistics_heading);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        statisticsViewModel = new StatisticsViewModel(TeaMemoryDatabase.getDatabaseInstance(getApplicationContext()));

        horizontal = findViewById(R.id.statistic_chart);
        horizontal.init(this).hasAnimation(true).addAll(getItems(statisticsViewModel.getStatisticsOverall())).build();

        Button buttonPeriod = findViewById(R.id.toolbar_statistics);
        buttonPeriod.setOnClickListener(view -> dialogSortOption());
    }

    private void dialogSortOption() {
        final String[] items = getResources().getStringArray(R.array.statistics_category);

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
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
        horizontal.removeAll();
        switch (choice) {
            case 0:
                horizontal.addAll(getItems(statisticsViewModel.getStatisticsOverall()));
                break;
            case 1:
                horizontal.addAll(getItems(statisticsViewModel.getStatisticsMonth()));
                break;
            case 2:
                horizontal.addAll(getItems(statisticsViewModel.getStatisticsWeek()));
                break;
            case 3:
                horizontal.addAll(getItems(statisticsViewModel.getStatisticsDay()));
                break;
            default:
                break;
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
