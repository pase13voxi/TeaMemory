package coolpharaoh.tee.speicher.tea.timer.views.show_tea;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit.CELSIUS;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit.FAHRENHEIT;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.TEA_SPOON;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.OOLONG_TEA;

import android.app.Application;
import android.content.res.Resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TimeConverter;
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

@ExtendWith(MockitoExtension.class)
class ShowTeaViewModelTest {
    public static final String CURRENT_DATE = "2020-08-19T10:15:30Z";
    private ShowTeaViewModel showTeaViewModel;

    @Mock
    Application application;
    @Mock
    Resources resources;
    @Mock
    TeaRepository teaRepository;
    @Mock
    InfusionRepository infusionRepository;
    @Mock
    CounterRepository counterRepository;
    @Mock
    SharedSettings sharedSettings;
    @Mock
    DateUtility dateUtility;

    private static final long TEA_ID = 1L;

    @BeforeEach
    void setUp() {
        showTeaViewModel = new ShowTeaViewModel(TEA_ID, application, teaRepository, infusionRepository,
                counterRepository, sharedSettings);
    }

    @Test
    void teaExist() {
        final Tea tea = new Tea();
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        assertThat(showTeaViewModel.teaExists()).isTrue();
    }

    @Test
    void teaDoesNotExist() {
        assertThat(showTeaViewModel.teaExists()).isFalse();
    }

    @Test
    void getTeaId() {
        final long teaIdBefore = 1L;

        final Tea tea = new Tea();
        tea.setId(teaIdBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final long teaIdAfter = showTeaViewModel.getTeaId();

        assertThat(teaIdAfter).isEqualTo(teaIdBefore);
    }

    @Test
    void getName() {
        final String teaNameBefore = "TEA";

        final Tea tea = new Tea();
        tea.setName(teaNameBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final String teaNameAfter = showTeaViewModel.getName();

        assertThat(teaNameAfter).isEqualTo(teaNameBefore);
    }

    @Test
    void getVariety() {
        final String[] varietyTeas = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};

        final Tea tea = new Tea();
        tea.setVariety(OOLONG_TEA.getCode());
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        when(application.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(varietyTeas);

        final String varietyAfter = showTeaViewModel.getVariety();

        assertThat(varietyAfter).isEqualTo(varietyTeas[OOLONG_TEA.getChoice()]);
    }

    @Test
    void getUnknownVariety() {
        final String[] varietyTeas = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};

        final String varietyBefore = "VARIETY";

        final Tea tea = new Tea();
        tea.setVariety(varietyBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        when(application.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(varietyTeas);

        final String varietyAfter = showTeaViewModel.getVariety();

        assertThat(varietyAfter).isEqualTo(varietyBefore);
    }

    @Test
    void getEmptyVariety() {
        final String varietyBefore = "";

        final Tea tea = new Tea();
        tea.setVariety(varietyBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final String varietyAfter = showTeaViewModel.getVariety();

        assertThat(varietyAfter).isEqualTo("-");
    }

    @Test
    void getAmount() {
        final double amountBefore = 1;

        final Tea tea = new Tea();
        tea.setAmount(amountBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final double amountAfter = showTeaViewModel.getAmount();

        assertThat(amountAfter).isEqualTo(amountBefore);
    }

    @Test
    void getAmountKind() {
        final Tea tea = new Tea();
        tea.setAmountKind(TEA_SPOON.getText());
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final AmountKind amountKind = showTeaViewModel.getAmountKind();

        assertThat(amountKind).isEqualTo(TEA_SPOON);
    }

    @Test
    void getColor() {
        final int colorBefore = 1;

        final Tea tea = new Tea();
        tea.setColor(colorBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final int colorAfter = showTeaViewModel.getColor();

        assertThat(colorAfter).isEqualTo(colorBefore);
    }

    @Test
    void setCurrentDate() {
        final Date fixedDate = mockFixedDate();
        final Tea teaBefore = new Tea();
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(teaBefore);

        showTeaViewModel.setCurrentDate();

        final ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea((captor.capture()));
        final Tea teaAfter = captor.getValue();

        assertThat(teaAfter.getDate()).isEqualTo(fixedDate);
    }

    @Test
    void getNextInfusion() {
        final int nextInfusionBefore = 0;

        final Tea tea = new Tea();
        tea.setNextInfusion(nextInfusionBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final int nextInfusionAfter = showTeaViewModel.getNextInfusion();

        assertThat(nextInfusionAfter).isEqualTo(nextInfusionBefore);
    }

    @Test
    void updateNextInfusion() {
        final Tea tea = new Tea();
        tea.setNextInfusion(0);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        when(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(Arrays.asList(new Infusion(), new Infusion()));

        showTeaViewModel.updateNextInfusion();

        final ArgumentCaptor<Tea> teaCaptor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea((teaCaptor.capture()));
        final Tea updatedTea = teaCaptor.getValue();

        assertThat(updatedTea.getNextInfusion()).isEqualTo(1);
    }

    @Test
    void resetNextInfusion() {
        final Tea tea = new Tea();
        tea.setNextInfusion(2);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        showTeaViewModel.resetNextInfusion();

        final ArgumentCaptor<Tea> teaCaptor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea((teaCaptor.capture()));
        final Tea updatedTea = teaCaptor.getValue();

        assertThat(updatedTea.getNextInfusion()).isZero();
    }

    @Test
    void updateLastInfusionBiggerOrEqual() {
        final Tea tea = new Tea();
        tea.setNextInfusion(0);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        when(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(Collections.singletonList(new Infusion()));

        showTeaViewModel.updateNextInfusion();

        final ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea((captor.capture()));
        final Tea lastInfusionAfter = captor.getValue();

        assertThat(lastInfusionAfter.getNextInfusion()).isZero();
    }

    @Test
    void navigateBetweenInfusions() {
        when(sharedSettings.getTemperatureUnit()).thenReturn(CELSIUS);

        final List<Infusion> infusions = new ArrayList<>();
        final Infusion infusion1 = new Infusion(1L, 1, "1", "2", 1, 32);
        infusions.add(infusion1);

        final Infusion infusion2 = new Infusion(1L, 2, "2:30", "5:30", 2, 33);
        infusions.add(infusion2);

        when(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(infusions);

        assertThat(showTeaViewModel.getInfusionSize()).isEqualTo(infusions.size());
        assertThat(showTeaViewModel.getInfusionIndex()).isZero();

        final TimeConverter time1 = showTeaViewModel.getTime();
        final TimeConverter cooldownTime1 = showTeaViewModel.getCoolDownTime();
        final int temperature1 = showTeaViewModel.getTemperature();

        assertThat(time1.getTime()).isEqualTo(infusions.get(0).getTime());
        assertThat(time1.getMinutes()).isEqualTo(1);
        assertThat(time1.getSeconds()).isZero();
        assertThat(cooldownTime1.getTime()).isEqualTo(infusions.get(0).getCoolDownTime());
        assertThat(cooldownTime1.getMinutes()).isEqualTo(2);
        assertThat(cooldownTime1.getSeconds()).isZero();
        assertThat(temperature1).isEqualTo(infusions.get(0).getTemperatureCelsius());

        showTeaViewModel.incrementInfusionIndex();
        assertThat(showTeaViewModel.getInfusionIndex()).isEqualTo(1);

        final TimeConverter time2 = showTeaViewModel.getTime();
        final TimeConverter coolDownTime2 = showTeaViewModel.getCoolDownTime();
        final int temperature2 = showTeaViewModel.getTemperature();

        assertThat(time2.getTime()).isEqualTo(infusions.get(1).getTime());
        assertThat(time2.getMinutes()).isEqualTo(2);
        assertThat(time2.getSeconds()).isEqualTo(30);
        assertThat(coolDownTime2.getTime()).isEqualTo(infusions.get(1).getCoolDownTime());
        assertThat(coolDownTime2.getMinutes()).isEqualTo(5);
        assertThat(coolDownTime2.getSeconds()).isEqualTo(30);
        assertThat(temperature2).isEqualTo(infusions.get(1).getTemperatureCelsius());

        when(sharedSettings.getTemperatureUnit()).thenReturn(FAHRENHEIT);
        showTeaViewModel.setInfusionIndex(0);
        assertThat(showTeaViewModel.getInfusionIndex()).isZero();

        final int temperature3 = showTeaViewModel.getTemperature();
        assertThat(temperature3).isEqualTo(infusions.get(0).getTemperatureFahrenheit());
    }

    @Test
    void getEmptyTime() {
        final List<Infusion> infusions = new ArrayList<>();
        final Infusion infusion1 = new Infusion(1L, 1, null, null, 1, 1);
        infusions.add(infusion1);

        when(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(infusions);

        final TimeConverter timeAfter = showTeaViewModel.getTime();

        assertThat(timeAfter.getTime()).isNull();
        assertThat(timeAfter.getMinutes()).isZero();
        assertThat(timeAfter.getSeconds()).isZero();
    }

    @Test
    void countCounter() {
        final Date currentDate = mockFixedDate();
        final Counter counterBefore = new Counter(1L, 1, 1, 1, 1, currentDate, currentDate, currentDate);
        when(counterRepository.getCounterByTeaId(TEA_ID)).thenReturn(counterBefore);

        showTeaViewModel.countCounter();

        final ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterRepository).updateCounter((captor.capture()));
        final Counter counterAfter = captor.getValue();

        assertThat(counterAfter)
                .extracting(Counter::getDay, Counter::getWeek, Counter::getMonth, Counter::getOverall)
                .containsExactly(2, 2, 2, 2L);
    }

    @Test
    void countCounterAndCounterIsNull() {
        showTeaViewModel.countCounter();

        final ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterRepository).updateCounter((captor.capture()));
        final Counter counterAfter = captor.getValue();

        assertThat(counterAfter)
                .extracting(Counter::getDay, Counter::getWeek, Counter::getMonth, Counter::getOverall)
                .containsExactly(1, 1, 1, 1L);
    }

    @Test
    void getOverallCounter() {
        final Date currentDate = Date.from(Instant.now());
        final Counter counterBefore = new Counter(1L, 1, 1, 1, 1, currentDate, currentDate, currentDate);
        when(counterRepository.getCounterByTeaId(TEA_ID)).thenReturn(counterBefore);

        final long overallCounter = showTeaViewModel.getOverallCounter();

        assertThat(overallCounter).isEqualTo(1L);
    }

    @Test
    void isAnimation() {
        final boolean animation = true;

        when(sharedSettings.isAnimation()).thenReturn(animation);

        assertThat(showTeaViewModel.isAnimation()).isEqualTo(animation);
    }

    @Test
    void isShowTeaAlert() {
        final boolean showTeaAlert = true;

        when(sharedSettings.isShowTeaAlert()).thenReturn(showTeaAlert);

        assertThat(showTeaViewModel.isShowTeaAlert()).isEqualTo(showTeaAlert);
    }

    @Test
    void setShowTeaAlert() {
        final boolean showTeaAlert = true;

        showTeaViewModel.setShowTeaAlert(showTeaAlert);

        verify(sharedSettings).setShowTeaAlert(showTeaAlert);
    }

    private Date mockFixedDate() {
        final Clock clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"));
        final Instant now = Instant.now(clock);
        final Date fixedDate = Date.from(now);

        CurrentDate.setFixedDate(dateUtility);
        when(dateUtility.getDate()).thenReturn(fixedDate);
        return fixedDate;
    }
}