package coolpharaoh.tee.speicher.tea.timer.views;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

    private boolean firstView = true;

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

        final HorizontalBar horizontal = findViewById(R.id.statistic_chart);
        horizontal.init(this).hasAnimation(true).addAll(getItems(statisticsViewModel.getStatisticsOverall())).build();


        Spinner spinnerCategory = findViewById(R.id.spinner_category);

        ArrayAdapter<CharSequence> spinnerCategoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.statistics_category, R.layout.spinner_item_sort);

        spinnerCategoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_sort);
        spinnerCategory.setAdapter(spinnerCategoryAdapter);

        //sortierung hat sich verändert
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (!firstView) {
                            horizontal.removeAll();
                            horizontal.addAll(getItems(statisticsViewModel.getStatisticsOverall()));
                        } else {
                            firstView = false;
                        }
                        break;
                    case 1:
                        horizontal.removeAll();
                        horizontal.addAll(getItems(statisticsViewModel.getStatisticsMonth()));
                        break;
                    case 2:
                        horizontal.removeAll();
                        horizontal.addAll(getItems(statisticsViewModel.getStatisticsWeek()));
                        break;
                    case 3:
                        horizontal.removeAll();
                        horizontal.addAll(getItems(statisticsViewModel.getStatisticsDay()));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private List<BarItem> getItems(List<StatisticsPOJO> statistics) {
        List<BarItem> items = new ArrayList<>();

        for (StatisticsPOJO statistic : statistics) {
            items.add(new BarItem(statistic.teaname, (double) statistic.counter, statistic.teacolor, ColorConversation.discoverForegroundColor(statistic.teacolor)));
        }

        return items;
    }
}
