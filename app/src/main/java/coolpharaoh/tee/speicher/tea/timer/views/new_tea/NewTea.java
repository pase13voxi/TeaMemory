package coolpharaoh.tee.speicher.tea.timer.views.new_tea;

import static coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit.CELSIUS;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit.FAHRENHEIT;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit;
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions.Suggestions;
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions.SuggestionsFactory;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea;
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind.DisplayAmountKindFactory;
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind.DisplayAmountKindStrategy;
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit.DisplayTemperatureUnitFactory;
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit.DisplayTemperatureUnitStrategy;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class NewTea extends AppCompatActivity implements Printer {

    private Variety variety;
    private ButtonColorShape buttonColorShape;

    private NewTeaViewModel newTeaViewModel;
    private InputValidator inputValidator;
    private long teaId;
    private boolean showTea;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tea);
        hideKeyboardAtFirst();
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        defineColorPicker();

        defineInfusionBar();

        defineInputPicker();

        initializeNewOrEditTea();

        newTeaViewModel.dataChanges().observe(this, index -> onDataChanged());

        //showTea wird übergeben, falls die Navigation von showTea erfolgt
        showTea = this.getIntent().getBooleanExtra("showTea", false);
        inputValidator = new InputValidator(getApplication(), this);
    }

    private void hideKeyboardAtFirst() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void defineToolbarAsActionbar() {
        final Toolbar toolbar = findViewById(R.id.tool_bar);
        final TextView mToolbarCustomTitle = findViewById(R.id.tool_bar_title);
        mToolbarCustomTitle.setText(R.string.new_tea_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void defineColorPicker() {
        final Button buttonColor = findViewById(R.id.button_new_tea_color);
        buttonColorShape = new ButtonColorShape(buttonColor.getBackground(), getApplication());
        buttonColor.setOnClickListener(view -> createColorPicker());
    }

    private void createColorPicker() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle(R.string.new_tea_color_dialog_title)
                .initialColor(buttonColorShape.getColor())
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .lightnessSliderOnly()
                .density(12)
                .setPositiveButton(R.string.new_tea_color_dialog_positive, (dialog, selectedColor, allColors) -> newTeaViewModel.setColor(selectedColor))
                .setNegativeButton(R.string.new_tea_color_dialog_negative, null)
                .build()
                .show();
    }

    private void defineInfusionBar() {
        final ImageButton buttonPreviousInfusion = findViewById(R.id.button_new_tea_previous_infusion);
        buttonPreviousInfusion.setOnClickListener(v -> newTeaViewModel.previousInfusion());

        final ImageButton buttonNextInfusion = findViewById(R.id.button_new_tea_next_infusion);
        buttonNextInfusion.setOnClickListener(v -> newTeaViewModel.nextInfusion());

        final ImageButton buttonDeleteInfusion = findViewById(R.id.button_new_tea_delete_infusion);
        buttonDeleteInfusion.setOnClickListener(v -> newTeaViewModel.deleteInfusion());

        final ImageButton buttonAddInfusion = findViewById(R.id.button_new_tea_add_infusion);
        buttonAddInfusion.setOnClickListener(v -> newTeaViewModel.addInfusion());
    }

    private void defineInputPicker() {
        final EditText editTextVarietyDialog = findViewById(R.id.edit_text_new_tea_variety);
        editTextVarietyDialog.setOnClickListener(view -> dialogVarietyPicker());

        final EditText editTextAmountDialog = findViewById(R.id.edit_text_new_tea_amount);
        editTextAmountDialog.setOnClickListener(view -> dialogAmountPicker());

        final EditText editTextTemperatureDialog = findViewById(R.id.edit_text_new_tea_temperature);
        editTextTemperatureDialog.setOnClickListener(view -> dialogTemperaturePicker());

        final EditText editTextCoolDownTimeDialog = findViewById(R.id.edit_text_new_tea_cool_down_time);
        editTextCoolDownTimeDialog.setOnClickListener(view -> dialogCoolDownTimePicker());

        final EditText editTextTimeDialog = findViewById(R.id.edit_text_new_tea_time);
        editTextTimeDialog.setOnClickListener(view -> dialogTimePicker());
    }

    private void dialogVarietyPicker() {
        new VarietyPickerDialog(newTeaViewModel)
                .show(getSupportFragmentManager(), VarietyPickerDialog.TAG);
    }

    private void dialogAmountPicker() {
        final Suggestions hints = SuggestionsFactory.getSuggestions(variety.ordinal(), getApplication());
        new AmountPickerDialog(hints, newTeaViewModel)
                .show(getSupportFragmentManager(), AmountPickerDialog.TAG);
    }

    private void dialogTemperaturePicker() {
        final Suggestions hints = SuggestionsFactory.getSuggestions(variety.ordinal(), getApplication());
        new TemperaturePickerDialog(hints, newTeaViewModel)
                .show(getSupportFragmentManager(), TemperaturePickerDialog.TAG);
    }

    private void dialogCoolDownTimePicker() {
        new CoolDownTimePickerDialog(newTeaViewModel)
                .show(getSupportFragmentManager(), CoolDownTimePickerDialog.TAG);

    }

    private void dialogTimePicker() {
        final Suggestions hints = SuggestionsFactory.getSuggestions(variety.ordinal(), getApplication());
        new TimePickerDialog(hints, newTeaViewModel)
                .show(getSupportFragmentManager(), TimePickerDialog.TAG);
    }

    private void initializeNewOrEditTea() {
        teaId = this.getIntent().getLongExtra("teaId", 0);
        if (teaId == 0) {
            initializeNewTea();
        } else {
            initializeEditTea();
        }
    }

    private void initializeNewTea() {
        newTeaViewModel = new NewTeaViewModel(getApplication());
    }

    private void initializeEditTea() {
        newTeaViewModel = new NewTeaViewModel(teaId, getApplication());

        final EditText editTextName = findViewById(R.id.edit_text_new_tea_name);
        editTextName.setText(newTeaViewModel.getName());
    }

    private void onDataChanged() {
        bindVarietyToInputField();
        bindColorToInputField();
        bindAmountToInputField();
        bindTemperatureToInputField();
        bindCoolDownTimeToInputField();
        showCoolDownTimeInput();
        bindTimeToInputField();
        refreshInfusionBar();
    }

    private void bindVarietyToInputField() {
        variety = newTeaViewModel.getVariety();

        final String varietyText = newTeaViewModel.getVarietyAsText();
        final EditText editTextVariety = findViewById(R.id.edit_text_new_tea_variety);

        editTextVariety.setText(varietyText);
    }

    private void bindColorToInputField() {
        buttonColorShape.setColor(newTeaViewModel.getColor());
    }

    private void bindAmountToInputField() {
        final EditText editTextAmount = findViewById(R.id.edit_text_new_tea_amount);

        final AmountKind amountKind = newTeaViewModel.getAmountKind();
        final DisplayAmountKindStrategy displayAmountKindStrategy = DisplayAmountKindFactory.get(amountKind, getApplication());

        final double amount = newTeaViewModel.getAmount();
        editTextAmount.setText(displayAmountKindStrategy.getTextNewTea(amount));

    }

    private void bindTemperatureToInputField() {
        final int temperature = newTeaViewModel.getInfusionTemperature();
        final TemperatureUnit temperatureUnit = newTeaViewModel.getTemperatureUnit();
        final DisplayTemperatureUnitStrategy displayTemperatureUnitStrategy = DisplayTemperatureUnitFactory.get(temperatureUnit, getApplication());

        final EditText editTextTemperature = findViewById(R.id.edit_text_new_tea_temperature);

        editTextTemperature.setText(displayTemperatureUnitStrategy.getTextNewTea(temperature));
    }

    private void bindCoolDownTimeToInputField() {
        final String coolDownTime = newTeaViewModel.getInfusionCoolDownTime();
        final EditText editTextCoolDownTime = findViewById(R.id.edit_text_new_tea_cool_down_time);
        if (coolDownTime == null) {
            editTextCoolDownTime.setText(R.string.new_tea_edit_text_cool_down_time_empty_text);
        } else {
            editTextCoolDownTime.setText(getString(R.string.new_tea_edit_text_cool_down_time_text, coolDownTime));
        }
    }

    private void showCoolDownTimeInput() {
        final int temperature = newTeaViewModel.getInfusionTemperature();
        final TemperatureUnit temperatureUnit = newTeaViewModel.getTemperatureUnit();
        final LinearLayout layoutCoolDownTime = findViewById(R.id.layout_new_tea_cool_down_time);

        if (temperature == -500
                || (temperature == 100 && CELSIUS.equals(temperatureUnit))
                || (temperature == 212 && FAHRENHEIT.equals(temperatureUnit))) {
            layoutCoolDownTime.setVisibility(View.GONE);
            newTeaViewModel.resetInfusionCoolDownTime();
        } else {
            layoutCoolDownTime.setVisibility(View.VISIBLE);
        }
    }

    private void bindTimeToInputField() {
        final String time = newTeaViewModel.getInfusionTime();
        final EditText editTextTime = findViewById(R.id.edit_text_new_tea_time);
        if (time == null) {
            editTextTime.setText(R.string.new_tea_edit_text_time_empty_text);
        } else {
            editTextTime.setText(getString(R.string.new_tea_edit_text_time_text, time));
        }
    }

    private void refreshInfusionBar() {
        final TextView textViewInfusion = findViewById(R.id.text_view_new_tea_count_infusion);
        textViewInfusion.setText(getResources().getString(R.string.new_tea_count_infusion, newTeaViewModel.getInfusionIndex() + 1));

        showDeleteInfusion();
        showAddInfusion();
        showPreviousInfusion();
        showNextInfusion();
    }

    private void showDeleteInfusion() {
        if (newTeaViewModel.getInfusionSize() > 1) {
            findViewById(R.id.button_new_tea_delete_infusion).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.button_new_tea_delete_infusion).setVisibility(View.GONE);
        }
    }

    private void showAddInfusion() {
        if (((newTeaViewModel.getInfusionIndex() + 1) == newTeaViewModel.getInfusionSize()) && (newTeaViewModel.getInfusionSize() < 20)) {
            findViewById(R.id.button_new_tea_add_infusion).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.button_new_tea_add_infusion).setVisibility(View.GONE);
        }
    }

    private void showPreviousInfusion() {
        final boolean enabled = newTeaViewModel.getInfusionIndex() != 0;
        findViewById(R.id.button_new_tea_previous_infusion).setEnabled(enabled);
    }

    private void showNextInfusion() {
        final boolean enabled = (newTeaViewModel.getInfusionIndex() + 1) != newTeaViewModel.getInfusionSize();
        findViewById(R.id.button_new_tea_next_infusion).setEnabled(enabled);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_tea, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_new_tea_done) {
            addTea();
        } else if (id == android.R.id.home) {
            if (showTea) {
                navigateToShowTeaActivity();
                return true;
            } else {
                NavUtils.navigateUpFromSameTask(this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addTea() {
        final EditText editTextName = findViewById(R.id.edit_text_new_tea_name);

        final String nameInput = editTextName.getText().toString();

        if (inputIsValid(nameInput)) {
            createOrEditTea(nameInput);
            navigateToMainOrShowTea();
        }
    }

    private boolean inputIsValid(final String nameInput) {
        return inputValidator.nameIsNotEmpty(nameInput)
                && inputValidator.nameIsValid(nameInput);
    }

    private void createOrEditTea(final String name) {
        newTeaViewModel.saveTea(name);
    }

    private void navigateToMainOrShowTea() {
        if (!showTea) {
            finish();
        } else {
            navigateToShowTeaActivity();
        }
    }

    private void navigateToShowTeaActivity() {
        final Intent showTeaScreen = new Intent(NewTea.this, ShowTea.class);
        showTeaScreen.putExtra("teaId", newTeaViewModel.getTeaId());
        startActivity(showTeaScreen);
        finish();
    }

    @Override
    public void print(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
