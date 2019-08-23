package coolpharaoh.tee.speicher.tea.timer.views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.felix.horizontalbargraph.HorizontalBar;
import br.com.felix.horizontalbargraph.model.BarItem;
import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Coloring;
import coolpharaoh.tee.speicher.tea.timer.pojos.StatisticsPOJO;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.StatisticsViewModel;

public class Statistics extends AppCompatActivity {

    private StatisticsViewModel mStatisticsViewModel;

    private TextView mToolbarCustomTitle;
    private Spinner spinnerCategory;
    private boolean firstView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        //Toolbar als ActionBar festlegen
        Toolbar toolbar = findViewById(R.id.tool_bar);
        mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.statistics_heading);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStatisticsViewModel = new StatisticsViewModel(getApplicationContext());

        final HorizontalBar horizontal = findViewById(R.id.statistic_chart);
        horizontal.init(this).hasAnimation(true).addAll(getItems(mStatisticsViewModel.getStatisticsOverall())).build();


        spinnerCategory = findViewById(R.id.spinner_category);

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
                            horizontal.addAll(getItems(mStatisticsViewModel.getStatisticsOverall()));
                        } else {
                            firstView = false;
                        }
                        break;
                    case 1:
                        horizontal.removeAll();
                        horizontal.addAll(getItems(mStatisticsViewModel.getStatisticsMonth()));
                        break;
                    case 2:
                        horizontal.removeAll();
                        horizontal.addAll(getItems(mStatisticsViewModel.getStatisticsWeek()));
                        break;
                    case 3:
                        horizontal.removeAll();
                        horizontal.addAll(getItems(mStatisticsViewModel.getStatisticsDay()));
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
            items.add(new BarItem(statistic.teaname, (double) statistic.counter, statistic.teacolor, Coloring.discoverForegroundColor(statistic.teacolor)));
        }

        return items;
    }
}
