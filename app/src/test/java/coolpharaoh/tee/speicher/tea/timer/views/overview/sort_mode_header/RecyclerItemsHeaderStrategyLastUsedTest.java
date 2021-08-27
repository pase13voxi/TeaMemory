package coolpharaoh.tee.speicher.tea.timer.views.overview.sort_mode_header;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.overview.RecyclerItemOverview;

@RunWith(MockitoJUnitRunner.class)
public class RecyclerItemsHeaderStrategyLastUsedTest {
    public static final String CURRENT_DATE = "2020-08-19T10:15:30Z";
    private static final String[] VARIETIES = {"Black tea", "Green tea", "Yellow tea", "White tea",
            "Oolong tea", "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};
    private static final String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    @Mock
    Application application;
    @Mock
    Resources resources;
    @Mock
    DateUtility dateUtility;

    @Before
    public void setUp() throws Exception {
        when(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(VARIETIES);
        when(resources.getStringArray(R.array.overview_sort_last_used_month)).thenReturn(MONTH_NAMES);
        when(application.getResources()).thenReturn(resources);
        when(application.getString(R.string.overview_sort_last_used_this_week)).thenReturn("This week");
        when(application.getString(R.string.overview_sort_last_used_this_month)).thenReturn("This month");

        when(dateUtility.getDate()).thenReturn(Date.from(getFixedDate()));
        CurrentDate.setFixedDate(dateUtility);
    }

    private Instant getFixedDate() {
        Clock clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"));
        return Instant.now(clock);
    }

    @Test
    public void generateRecyclerItemsHeader() {
        final ArrayList<Tea> teas = createTeas();

        final RecyclerItemsHeaderStrategy recyclerItemsHeader = new RecyclerItemsHeaderStrategyLastUsed(application);
        final List<RecyclerItemOverview> recyclerItems = recyclerItemsHeader.generateFrom(teas);

        assertThat(recyclerItems)
                .extracting(
                        RecyclerItemOverview::getTeaId,
                        RecyclerItemOverview::getTeaName,
                        RecyclerItemOverview::getVariety,
                        RecyclerItemOverview::getCategory
                ).contains(
                tuple(null, null, null, "- This week -"),
                tuple(teas.get(0).getId(), teas.get(0).getName(), teas.get(0).getVariety(), null),
                tuple(null, null, null, "- This month -"),
                tuple(teas.get(1).getId(), teas.get(1).getName(), teas.get(1).getVariety(), null),
                tuple(null, null, null, "- June -"),
                tuple(teas.get(2).getId(), teas.get(2).getName(), teas.get(2).getVariety(), null),
                tuple(null, null, null, "- 2019 -"),
                tuple(teas.get(3).getId(), teas.get(3).getName(), teas.get(3).getVariety(), null)
        );
    }

    private ArrayList<Tea> createTeas() {
        List<Date> dates = generateDifferentDates();

        final ArrayList<Tea> teas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            final Tea tea = new Tea();
            tea.setId((long) i);
            tea.setName("TEA" + i + 1);
            tea.setVariety("VARIETY" + i + 1);
            tea.setDate(dates.get(i));
            teas.add(tea);
        }
        return teas;
    }

    private List<Date> generateDifferentDates() {
        final Instant now = getFixedDate();

        final ArrayList<Date> dates = new ArrayList<>();

        final Date thisWeek = Date.from(now);
        dates.add(thisWeek);
        final Date thisMonth = Date.from(now.minus(Duration.ofDays(8)));
        dates.add(thisMonth);
        final Date thisYear = Date.from(now.minus(Duration.ofDays(50)));
        dates.add(thisYear);
        final Date lastYear = Date.from(now.minus(Duration.ofDays(500)));
        dates.add(lastYear);

        return dates;
    }
}