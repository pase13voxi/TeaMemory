package coolpharaoh.tee.speicher.tea.timer.views.new_tea;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit.CELSIUS;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit.FAHRENHEIT;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.TEA_SPOON;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.YELLOW_TEA;

import android.app.Application;
import android.content.res.Resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.TaskExecutorExtension;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;

@ExtendWith({MockitoExtension.class, TaskExecutorExtension.class})
class NewTeaViewModelTest {
    private static final String TIME_1 = "05:45";
    private static final String[] VARIETY_TEAS = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
            "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};
    private NewTeaViewModel newTeaViewModelEmpty;
    private NewTeaViewModel newTeaViewModelFilled;

    @Mock
    Application application;
    @Mock
    Resources resources;
    @Mock
    TeaRepository teaRepository;
    @Mock
    InfusionRepository infusionRepository;
    @Mock
    SharedSettings sharedSettings;

    private static final long TEA_ID_FILLED = 1L;
    private Tea tea;

    @BeforeEach
    void setUp() {
        newTeaViewModelEmpty = new NewTeaViewModel(null, application, teaRepository,
                infusionRepository, sharedSettings);
        mockStoredTea();
        newTeaViewModelFilled = new NewTeaViewModel(TEA_ID_FILLED, application, teaRepository,
                infusionRepository, sharedSettings);
    }

    @Test
    void getTeaId() {
        assertThat(newTeaViewModelFilled.getTeaId()).isEqualTo(tea.getId());
    }

    @Test
    void getName() {
        assertThat(newTeaViewModelFilled.getName()).isEqualTo(tea.getName());
    }

    @Test
    void getVarietyAsText() {
        mockStringResource();

        assertThat(newTeaViewModelFilled.getVarietyAsText())
                .isEqualTo(Variety.convertStoredVarietyToText(tea.getVariety(), application));
    }

    @Test
    void getVariety() {
        assertThat(newTeaViewModelFilled.getVariety())
                .isEqualTo(Variety.fromStoredText(tea.getVariety()));
    }

    @Test
    void setVariety() {
        mockStringResource();

        newTeaViewModelEmpty.setVariety("VARIETY");

        assertThat(newTeaViewModelEmpty.getVarietyAsText()).isEqualTo("VARIETY");
    }

    @Test
    void setAmountAndExpectAmountAndAmountKind() {
        newTeaViewModelEmpty.setAmount(5.5, TEA_SPOON);

        assertThat(newTeaViewModelEmpty.getAmount()).isEqualTo(5.5);
        assertThat(newTeaViewModelEmpty.getAmountKind()).isEqualTo(TEA_SPOON);
    }

    @Test
    void setColorAndExpectColor() {
        newTeaViewModelEmpty.setColor(1234);

        assertThat(newTeaViewModelEmpty.getColor()).isEqualTo(1234);
    }

    @Test
    void setTemperatureCelsiusAndExpectTemperature() {
        when(sharedSettings.getTemperatureUnit()).thenReturn(CELSIUS);

        newTeaViewModelEmpty.setInfusionTemperature(90);

        assertThat(newTeaViewModelEmpty.getInfusionTemperature()).isEqualTo(90);
        assertThat(newTeaViewModelEmpty.getTemperatureUnit()).isEqualTo(CELSIUS);
    }

    @Test
    void setTemperatureCelsiusAndExpectFahrenheitTemperature() {
        when(sharedSettings.getTemperatureUnit()).thenReturn(CELSIUS);

        newTeaViewModelEmpty.setInfusionTemperature(90);

        when(sharedSettings.getTemperatureUnit()).thenReturn(FAHRENHEIT);
        assertThat(newTeaViewModelEmpty.getInfusionTemperature()).isEqualTo(194);
        assertThat(newTeaViewModelEmpty.getTemperatureUnit()).isEqualTo(FAHRENHEIT);
    }

    @Test
    void setTemperatureFahrenheitAndExpectTemperature() {
        when(sharedSettings.getTemperatureUnit()).thenReturn(FAHRENHEIT);

        newTeaViewModelEmpty.setInfusionTemperature(194);

        assertThat(newTeaViewModelEmpty.getInfusionTemperature()).isEqualTo(194);
        assertThat(newTeaViewModelEmpty.getTemperatureUnit()).isEqualTo(FAHRENHEIT);
    }

    @Test
    void setTemperatureFahrenheitAndExpectCelsiusTemperature() {
        when(sharedSettings.getTemperatureUnit()).thenReturn(FAHRENHEIT);

        newTeaViewModelEmpty.setInfusionTemperature(194);

        when(sharedSettings.getTemperatureUnit()).thenReturn(CELSIUS);
        assertThat(newTeaViewModelEmpty.getInfusionTemperature()).isEqualTo(90);
        assertThat(newTeaViewModelEmpty.getTemperatureUnit()).isEqualTo(CELSIUS);
    }

    @Test
    void setTemperatureFahrenheitAndExpectStoredTemperatureFahrenheitAndTemperature() {
        when(sharedSettings.getTemperatureUnit()).thenReturn(CELSIUS);

        newTeaViewModelEmpty.setInfusionTemperature(90);

        assertThat(newTeaViewModelEmpty.getInfusionTemperature()).isEqualTo(90);
        assertThat(newTeaViewModelEmpty.getTemperatureUnit()).isEqualTo(CELSIUS);
    }

    @Test
    void setCoolDownTimeAndExpectCoolDownTime() {
        newTeaViewModelEmpty.setInfusionCoolDownTime(TIME_1);

        assertThat(newTeaViewModelEmpty.getInfusionCoolDownTime()).isEqualTo(TIME_1);
    }

    @Test
    void resetCoolDownTimeAndExpectNull() {
        newTeaViewModelEmpty.resetInfusionCoolDownTime();

        assertThat(newTeaViewModelEmpty.getInfusionCoolDownTime()).isNull();
    }

    @Test
    void setTimeAndExpectTime() {
        newTeaViewModelEmpty.setInfusionTime(TIME_1);

        assertThat(newTeaViewModelEmpty.getInfusionTime()).isEqualTo(TIME_1);
    }

    @Test
    void addInfusion() {
        when(sharedSettings.getTemperatureUnit()).thenReturn(CELSIUS);
        assertThat(newTeaViewModelEmpty.getInfusionSize()).isEqualTo(1);

        newTeaViewModelEmpty.addInfusion();

        assertThat(newTeaViewModelEmpty.dataChanges().getValue()).isEqualTo(1);
        assertThat(newTeaViewModelEmpty.getInfusionSize()).isEqualTo(2);
        assertThat(newTeaViewModelEmpty.getInfusionTime()).isNull();
        assertThat(newTeaViewModelEmpty.getInfusionCoolDownTime()).isNull();
        assertThat(newTeaViewModelEmpty.getInfusionTemperature()).isEqualTo(-500);
    }

    @Test
    void deleteInfusionAndExpectTwoInfusions() {
        assertThat(newTeaViewModelFilled.getInfusionSize()).isEqualTo(2);

        newTeaViewModelFilled.deleteInfusion();

        assertThat(newTeaViewModelFilled.getInfusionSize()).isEqualTo(1);
    }

    @Test
    void deleteFirstInfusionAndExpectIndexZero() {
        newTeaViewModelFilled.deleteInfusion();

        assertThat(newTeaViewModelFilled.dataChanges().getValue()).isZero();
    }

    @Test
    void deleteLastInfusionAndExpectIndexOne() {
        newTeaViewModelFilled.nextInfusion();

        newTeaViewModelFilled.deleteInfusion();

        assertThat(newTeaViewModelFilled.dataChanges().getValue()).isZero();
    }

    @Test
    void navigateToNextAndPreviousInfusion() {
        newTeaViewModelFilled.nextInfusion();
        assertThat(newTeaViewModelFilled.dataChanges().getValue()).isEqualTo(1);

        newTeaViewModelFilled.previousInfusion();

        assertThat(newTeaViewModelFilled.dataChanges().getValue()).isZero();
    }

    @Test
    void couldNotNavigateToNextInfusion() {
        newTeaViewModelEmpty.nextInfusion();

        assertThat(newTeaViewModelFilled.dataChanges().getValue()).isZero();
    }

    @Test
    void couldNotNavigateToPreviousInfusion() {
        newTeaViewModelEmpty.previousInfusion();

        assertThat(newTeaViewModelEmpty.dataChanges().getValue()).isZero();
    }

    @Test
    void saveTeaAndExpectNewTea() {
        newTeaViewModelEmpty.saveTea("name");

        final ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).insertTea(captor.capture());
        final Tea savedTea = captor.getValue();
        assertThat(savedTea.getName()).isEqualTo("name");
        verify(infusionRepository).insertInfusion(any());
    }

    @Test
    void saveTeaAndExpectEditedExistingTea() {
        newTeaViewModelFilled.saveTea("name");

        final ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea(captor.capture());
        final Tea savedTea = captor.getValue();
        assertThat(savedTea.getName()).isEqualTo("name");
        verify(infusionRepository).deleteInfusionsByTeaId(TEA_ID_FILLED);
        verify(infusionRepository, times(2)).insertInfusion(any());
    }

    private void mockStoredTea() {
        final Date today = Date.from(Instant.now());
        tea = new Tea("TEA", YELLOW_TEA.getCode(), 3, TEA_SPOON.getText(), 5, 0, today);
        tea.setId(TEA_ID_FILLED);
        when(teaRepository.getTeaById(TEA_ID_FILLED)).thenReturn(tea);

        final List<Infusion> infusions = new ArrayList<>();
        final Infusion infusion1 = new Infusion(TEA_ID_FILLED, 0, "2", "0:30", 5, 5);
        infusions.add(infusion1);
        final Infusion infusion2 = new Infusion(TEA_ID_FILLED, 1, "4", "1", 50, 100);
        infusions.add(infusion2);
        when(infusionRepository.getInfusionsByTeaId(TEA_ID_FILLED)).thenReturn(infusions);
    }

    private void mockStringResource() {
        when(application.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(VARIETY_TEAS);
    }
}
