package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.tooltip.Tooltip;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.views.newtea.suggestions.Suggestions;
import coolpharaoh.tee.speicher.tea.timer.views.newtea.suggestions.SuggestionsFactory;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.ShowTea;


public class NewTea extends AppCompatActivity implements View.OnLongClickListener, Printer {

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

        initializeSpinnerWithBigCharacters();

        defineVarietyInput();

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
        mToolbarCustomTitle.setText(R.string.newtea_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeSpinnerWithBigCharacters() {
        Spinner spinnerTeaVariety = findViewById(R.id.spinnerTeaVariety);
        initializeSpinnerWithBigCharacters(R.array.variety_teas, R.layout.spinner_item_varietyoftea, R.layout.spinner_dropdown_item_varietyoftea, spinnerTeaVariety);
    }

    private void initializeSpinnerWithBigCharacters(final int texts, final int spinnerItemLayout, final int dropdownItemLayout, final Spinner spinner) {
        final ArrayAdapter<CharSequence> spinnerVarietyAdapter = ArrayAdapter.createFromResource(
                this, texts, spinnerItemLayout);

        spinnerVarietyAdapter.setDropDownViewResource(dropdownItemLayout);
        spinner.setAdapter(spinnerVarietyAdapter);
    }

    private void defineVarietyInput() {
        final Spinner spinnerTeaVariety = findViewById(R.id.spinnerTeaVariety);
        spinnerTeaVariety.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                varietyChanged(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //the method needs to be overwritten but shouldn't do anything
            }
        });

        final CheckBox checkboxTeaVariety = findViewById(R.id.checkBoxSelfInput);
        checkboxTeaVariety.setOnCheckedChangeListener((buttonView, isChecked) -> changeVarietyByHand(isChecked));
    }

    private void varietyChanged(int position) {
        variety = Variety.values()[position];

        buttonColorShape.setColorByVariety(variety.ordinal());

        if (Variety.OTHER.equals(variety)) {
            findViewById(R.id.checkBoxSelfInput).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.checkBoxSelfInput).setVisibility(View.INVISIBLE);
        }
    }

    private void changeVarietyByHand(final boolean isChecked) {
        if (isChecked) {
            findViewById(R.id.textViewTeaVariety).setVisibility(View.INVISIBLE);
            findViewById(R.id.spinnerTeaVariety).setVisibility(View.INVISIBLE);
            findViewById(R.id.editTextSelfInput).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.textViewTeaVariety).setVisibility(View.VISIBLE);
            findViewById(R.id.spinnerTeaVariety).setVisibility(View.VISIBLE);
            findViewById(R.id.editTextSelfInput).setVisibility(View.INVISIBLE);
        }
    }

    private void defineColorPicker() {
        final Button buttonColor = findViewById(R.id.buttonColor);
        buttonColorShape = new ButtonColorShape(buttonColor.getBackground(), getApplication());
        buttonColor.setOnClickListener(view -> createColorPicker());
        buttonColor.setOnLongClickListener(this);
    }

    private void createColorPicker() {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(NewTea.this, buttonColorShape.getColor());
        colorPickerDialog.setTitle(getResources().getString(R.string.newtea_color_dialog_title));
        colorPickerDialog.setOnColorChangedListener(color -> buttonColorShape.setColor(color));
        colorPickerDialog.show();
    }

    private void defineInfusionBar() {
        final ImageButton buttonPreviousInfusion = findViewById(R.id.buttonPreviousInfusion);
        buttonPreviousInfusion.setOnClickListener(v -> newTeaViewModel.previousInfusion());
        buttonPreviousInfusion.setOnLongClickListener(this);

        final ImageButton buttonNextInfusion = findViewById(R.id.buttonNextInfusion);
        buttonNextInfusion.setOnClickListener(v -> newTeaViewModel.nextInfusion());
        buttonNextInfusion.setOnLongClickListener(this);

        final ImageButton buttonDeleteInfusion = findViewById(R.id.buttonDeleteInfusion);
        buttonDeleteInfusion.setOnClickListener(v -> newTeaViewModel.deleteInfusion());
        buttonDeleteInfusion.setOnLongClickListener(this);

        final ImageButton buttonAddInfusion = findViewById(R.id.buttonAddInfusion);
        buttonAddInfusion.setOnClickListener(v -> newTeaViewModel.addInfusion());
        buttonAddInfusion.setOnLongClickListener(this);
    }

    private void defineInputPicker() {
        Button buttonAmountDialog = findViewById(R.id.buttonAmountDialog);
        buttonAmountDialog.setOnClickListener(view -> dialogAmountPicker());

        Button buttonTemperatureDialog = findViewById(R.id.buttonTemperatureDialog);
        buttonTemperatureDialog.setOnClickListener(view -> dialogTemperaturePicker());

        Button buttonCoolDownTimeDialog = findViewById(R.id.buttonCoolDownTimeDialog);
        buttonCoolDownTimeDialog.setOnClickListener(view -> dialogCoolDownTimePicker());

        Button buttonTimeDialog = findViewById(R.id.buttonTimeDialog);
        buttonTimeDialog.setOnClickListener(view -> dialogTimePicker());
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

        fillVarietyInputFields();

        buttonColorShape.setColor(newTeaViewModel.getColor());

        final EditText editTextName = findViewById(R.id.editTextName);
        editTextName.setText(newTeaViewModel.getName());
    }

    private void fillVarietyInputFields() {
        // get the right spinner id
        int spinnerId = -1;
        String[] spinnerElements = getResources().getStringArray(R.array.variety_codes);

        for (int i = 0; i < spinnerElements.length; i++) {
            if (spinnerElements[i].equals(newTeaViewModel.getVariety())) {
                spinnerId = i;
                break;
            }
        }

        final Spinner spinnerTeaVariety = findViewById(R.id.spinnerTeaVariety);
        final CheckBox checkboxTeaVariety = findViewById(R.id.checkBoxSelfInput);
        final EditText editTextTeaVariety = findViewById(R.id.editTextSelfInput);
        // if it is not a standard variety
        if (spinnerId == -1) {
            spinnerTeaVariety.setVisibility(View.INVISIBLE);
            spinnerTeaVariety.setSelection(spinnerElements.length - 1);
            findViewById(R.id.textViewTeaVariety).setVisibility(View.INVISIBLE);
            checkboxTeaVariety.setVisibility(View.VISIBLE);
            checkboxTeaVariety.setChecked(true);
            editTextTeaVariety.setVisibility(View.VISIBLE);
            editTextTeaVariety.setText(newTeaViewModel.getVariety());
        } else {
            spinnerTeaVariety.setSelection(spinnerId);
        }
    }

    private void onDataChanged() {
        bindAmountToButton();
        bindTemperatureToButton();
        bindCoolDownTimeToButton();
        showCoolDownTimeButton();
        bindTimeToButton();
        refreshInfusionBar();
    }

    private void bindAmountToButton() {
        final int amount = newTeaViewModel.getAmount();
        final String amountKind = newTeaViewModel.getAmountKind();
        final Button buttonAmount = findViewById(R.id.buttonAmountDialog);

        if (amount == -500) {
            buttonAmount.setText(R.string.newtea_button_amount_empty_text_ts);
        } else {
            if ("Gr".equals(amountKind)) {
                buttonAmount.setText(getString(R.string.newtea_button_amount_text_gr, amount));
            } else {
                buttonAmount.setText(getString(R.string.newtea_button_amount_text_ts, amount));
            }
        }
    }

    private void bindTemperatureToButton() {
        final int temperature = newTeaViewModel.getInfusionTemperature();
        final String temperatureUnit = newTeaViewModel.getTemperatureUnit();
        final Button buttonTemperature = findViewById(R.id.buttonTemperatureDialog);

        if (temperature == -500) {
            if ("Fahrenheit".equals(temperatureUnit)) {
                buttonTemperature.setText(R.string.newtea_button_temperature_empty_text_fahrenheit);
            } else {
                buttonTemperature.setText(R.string.newtea_button_temperature_empty_text_celsius);
            }
        } else {
            if ("Fahrenheit".equals(temperatureUnit)) {
                buttonTemperature.setText(getString(R.string.newtea_button_temperature_text_fahrenheit, temperature));
            } else {
                buttonTemperature.setText(getString(R.string.newtea_button_temperature_text_celsius, temperature));
            }
        }
    }

    private void bindCoolDownTimeToButton() {
        final String coolDownTime = newTeaViewModel.getInfusionCoolDownTime();
        final Button buttonCoolDownTime = findViewById(R.id.buttonCoolDownTimeDialog);
        if (coolDownTime == null) {
            buttonCoolDownTime.setText(R.string.newtea_button_cool_down_time_empty_text);
        } else {
            buttonCoolDownTime.setText(coolDownTime);
        }
    }

    private void showCoolDownTimeButton() {
        final int temperature = newTeaViewModel.getInfusionTemperature();
        final String temperatureUnit = newTeaViewModel.getTemperatureUnit();
        final Button buttonCoolDownTime = findViewById(R.id.buttonCoolDownTimeDialog);

        if (temperature == -500
                || (temperature == 100 && "Celsius".equals(temperatureUnit))
                || (temperature == 212 && "Fahrenheit".equals(temperatureUnit))) {
            buttonCoolDownTime.setVisibility(View.GONE);
            newTeaViewModel.resetInfusionCoolDownTime();
        } else {
            buttonCoolDownTime.setVisibility(View.VISIBLE);
        }
    }

    private void bindTimeToButton() {
        final String time = newTeaViewModel.getInfusionTime();
        final Button buttonTime = findViewById(R.id.buttonTimeDialog);
        if (time == null) {
            buttonTime.setText(R.string.newtea_button_time_empty_text);
        } else {
            buttonTime.setText(time);
        }
    }

    private void refreshInfusionBar() {
        final TextView textViewInfusion = findViewById(R.id.textViewCountInfusion);
        textViewInfusion.setText(getResources().getString(R.string.newtea_count_infusion, newTeaViewModel.getInfusionIndex() + 1));

        showDeleteInfusion();
        showAddInfusion();
        showPreviousInfusion();
        showNextInfusion();
    }

    private void showDeleteInfusion() {
        if (newTeaViewModel.getInfusionSize() > 1) {
            findViewById(R.id.buttonDeleteInfusion).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.buttonDeleteInfusion).setVisibility(View.GONE);
        }
    }

    private void showAddInfusion() {
        if (((newTeaViewModel.getInfusionIndex() + 1) == newTeaViewModel.getInfusionSize()) && (newTeaViewModel.getInfusionSize() < 20)) {
            findViewById(R.id.buttonAddInfusion).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.buttonAddInfusion).setVisibility(View.GONE);
        }
    }

    private void showPreviousInfusion() {
        final boolean enabled = newTeaViewModel.getInfusionIndex() != 0;
        findViewById(R.id.buttonPreviousInfusion).setEnabled(enabled);
    }

    private void showNextInfusion() {
        final boolean enabled = (newTeaViewModel.getInfusionIndex() + 1) != newTeaViewModel.getInfusionSize();
        findViewById(R.id.buttonNextInfusion).setEnabled(enabled);
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
        final EditText editTextName = findViewById(R.id.editTextName);

        String nameInput = editTextName.getText().toString();
        String varietyInput = getVarietyInput();

        if (inputIsValid(nameInput, varietyInput)) {
            createOrEditTea(varietyInput, nameInput);
            navigateToMainOrShowTea();
        }
    }

    private String getVarietyInput() {
        final CheckBox checkBoxTeaVariety = findViewById(R.id.checkBoxSelfInput);
        final EditText editTextTeaVariety = findViewById(R.id.editTextSelfInput);
        final Spinner spinnerTeaVariety = findViewById(R.id.spinnerTeaVariety);

        if (checkBoxTeaVariety.isChecked()) {
            return editTextTeaVariety.getText().toString();
        } else {
            return (String) spinnerTeaVariety.getSelectedItem();
        }
    }

    private boolean inputIsValid(final String nameInput, final String varietyInput) {
        return inputValidator.nameIsNotEmpty(nameInput)
                && inputValidator.nameIsValid(nameInput)
                && inputValidator.varietyIsValid(varietyInput);
    }

    private void createOrEditTea(final String teaVariety, final String name) {
        newTeaViewModel.saveTea(name, teaVariety, buttonColorShape.getColor());
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
    public boolean onLongClick(final View view) {
        if (view.getId() == R.id.buttonColor) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_choosecolor));
        } else if (view.getId() == R.id.buttonPreviousInfusion) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_arrowleft));
        } else if (view.getId() == R.id.buttonNextInfusion) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_arrowright));
        } else if (view.getId() == R.id.buttonAddInfusion) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_addinfusion));
        } else if (view.getId() == R.id.buttonDeleteInfusion) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_deleteinfusion));
        }
        return true;
    }

    private void showTooltip(final View v, final int gravity, final String text) {
        new Tooltip.Builder(v)
                .setText(text)
                .setTextColor(getResources().getColor(R.color.white))
                .setGravity(gravity)
                .setCornerRadius(8f)
                .setCancelable(true)
                .setDismissOnClick(true)
                .show();
    }

    @Override
    public void print(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
