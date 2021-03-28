package coolpharaoh.tee.speicher.tea.timer.views.newtea;

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

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.util.Arrays;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.views.newtea.suggestions.Suggestions;
import coolpharaoh.tee.speicher.tea.timer.views.newtea.suggestions.SuggestionsFactory;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.ShowTea;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class NewTea extends AppCompatActivity implements Printer {

    private static final String FAHRENHEIT = "Fahrenheit";

    private enum Variety {
        BLACK_TEA, GREEN_TEA, YELLOW_TEA, WHITE_TEA, OOLONG_TEA, PU_ERH_TEA,
        HERBAL_TEA, FRUIT_TEA, ROOIBUS_TEA, OTHER
    }

    private Variety variety = Variety.BLACK_TEA;
    private ButtonColorShape buttonColorShape;

    private NewTeaViewModel newTeaViewModel;
    private InputValidator inputValidator;
    private long teaId;
    private boolean showTea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.new_tea_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void defineColorPicker() {
        final Button buttonColor = findViewById(R.id.new_tea_button_color);
        buttonColorShape = new ButtonColorShape(buttonColor.getBackground(), getApplication());
        buttonColor.setOnClickListener(view -> createColorPicker());
    }

    private void createColorPicker() {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(NewTea.this, buttonColorShape.getColor());
        colorPickerDialog.setTitle(getResources().getString(R.string.new_tea_color_dialog_title));
        colorPickerDialog.setOnColorChangedListener(color -> newTeaViewModel.setColor(color));
        colorPickerDialog.show();
    }

    private void defineInfusionBar() {
        final ImageButton buttonPreviousInfusion = findViewById(R.id.new_tea_button_previous_infusion);
        buttonPreviousInfusion.setOnClickListener(v -> newTeaViewModel.previousInfusion());

        final ImageButton buttonNextInfusion = findViewById(R.id.new_tea_button_next_infusion);
        buttonNextInfusion.setOnClickListener(v -> newTeaViewModel.nextInfusion());

        final ImageButton buttonDeleteInfusion = findViewById(R.id.new_tea_button_delete_infusion);
        buttonDeleteInfusion.setOnClickListener(v -> newTeaViewModel.deleteInfusion());

        final ImageButton buttonAddInfusion = findViewById(R.id.new_tea_button_add_infusion);
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
        final String varietyText = newTeaViewModel.getVariety();
        final EditText editTextVariety = findViewById(R.id.edit_text_new_tea_variety);

        editTextVariety.setText(varietyText);

        final String[] varietyList = getResources().getStringArray(R.array.new_tea_variety_teas);
        final int varietyIndex = Arrays.asList(varietyList).indexOf(varietyText);
        if (varietyIndex == -1) {
            variety = Variety.OTHER;
        } else {
            variety = Variety.values()[varietyIndex];
        }
    }

    private void bindColorToInputField() {
        buttonColorShape.setColor(newTeaViewModel.getColor());
    }

    private void bindAmountToInputField() {
        final int amount = newTeaViewModel.getAmount();
        final String amountKind = newTeaViewModel.getAmountKind();
        final EditText editTextAmount = findViewById(R.id.edit_text_new_tea_amount);

        if (amount == -500) {
            editTextAmount.setText(R.string.new_tea_edit_text_amount_empty_text_ts);
        } else {
            if ("Gr".equals(amountKind)) {
                editTextAmount.setText(getString(R.string.new_tea_edit_text_amount_text_gr, amount));
            } else {
                editTextAmount.setText(getString(R.string.new_tea_edit_text_amount_text_ts, amount));
            }
        }
    }

    private void bindTemperatureToInputField() {
        final int temperature = newTeaViewModel.getInfusionTemperature();
        final String temperatureUnit = newTeaViewModel.getTemperatureUnit();
        final EditText editTextTemperature = findViewById(R.id.edit_text_new_tea_temperature);

        if (temperature == -500) {
            if (FAHRENHEIT.equals(temperatureUnit)) {
                editTextTemperature.setText(R.string.new_tea_edit_text_temperature_empty_text_fahrenheit);
            } else {
                editTextTemperature.setText(R.string.new_tea_edit_text_temperature_empty_text_celsius);
            }
        } else {
            if (FAHRENHEIT.equals(temperatureUnit)) {
                editTextTemperature.setText(getString(R.string.new_tea_edit_text_temperature_text_fahrenheit, temperature));
            } else {
                editTextTemperature.setText(getString(R.string.new_tea_edit_text_temperature_text_celsius, temperature));
            }
        }
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
        final String temperatureUnit = newTeaViewModel.getTemperatureUnit();
        final LinearLayout layoutCoolDownTime = findViewById(R.id.layout_new_tea_cool_down_time);

        if (temperature == -500
                || (temperature == 100 && "Celsius".equals(temperatureUnit))
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
        final TextView textViewInfusion = findViewById(R.id.new_tea_text_view_count_infusion);
        textViewInfusion.setText(getResources().getString(R.string.new_tea_count_infusion, newTeaViewModel.getInfusionIndex() + 1));

        showDeleteInfusion();
        showAddInfusion();
        showPreviousInfusion();
        showNextInfusion();
    }

    private void showDeleteInfusion() {
        if (newTeaViewModel.getInfusionSize() > 1) {
            findViewById(R.id.new_tea_button_delete_infusion).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.new_tea_button_delete_infusion).setVisibility(View.GONE);
        }
    }

    private void showAddInfusion() {
        if (((newTeaViewModel.getInfusionIndex() + 1) == newTeaViewModel.getInfusionSize()) && (newTeaViewModel.getInfusionSize() < 20)) {
            findViewById(R.id.new_tea_button_add_infusion).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.new_tea_button_add_infusion).setVisibility(View.GONE);
        }
    }

    private void showPreviousInfusion() {
        final boolean enabled = newTeaViewModel.getInfusionIndex() != 0;
        findViewById(R.id.new_tea_button_previous_infusion).setEnabled(enabled);
    }

    private void showNextInfusion() {
        final boolean enabled = (newTeaViewModel.getInfusionIndex() + 1) != newTeaViewModel.getInfusionSize();
        findViewById(R.id.new_tea_button_next_infusion).setEnabled(enabled);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_tea, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done) {
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

        String nameInput = editTextName.getText().toString();

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
        final Intent showteaScreen = new Intent(NewTea.this, ShowTea.class);
        showteaScreen.putExtra("teaId", newTeaViewModel.getTeaId());
        startActivity(showteaScreen);
        finish();
    }

    @Override
    public void print(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
