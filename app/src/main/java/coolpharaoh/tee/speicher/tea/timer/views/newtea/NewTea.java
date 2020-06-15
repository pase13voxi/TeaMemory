package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.tooltip.Tooltip;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.util.Objects;
import java.util.regex.Pattern;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.helper.HintConversation;
import coolpharaoh.tee.speicher.tea.timer.views.helper.TemperatureConversation;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.ShowTea;


public class NewTea extends AppCompatActivity implements View.OnLongClickListener {

    private enum Variety {
        BLACK_TEA, GREEN_TEA, YELLOW_TEA, WHITE_TEA, OOLONG_TEA, PU_ERH_TEA,
        HERBAL_TEA, FRUIT_TEA, ROOIBUS_TEA, OTHER
    }

    private Variety variety = Variety.BLACK_TEA;
    private String amountUnit = "Ts";


    private TextView textViewTeaSort;
    private Spinner spinnerTeaVariety;
    private CheckBox checkboxTeaSort;
    private EditText editTextTeaSort;
    private Button buttonColor;
    private ButtonColorShape buttonColorShape;
    private EditText editTextName;
    private EditText editTextTemperature;
    private Button buttonShowCoolDowntime;
    private EditText editTextCoolDownTime;
    private Button buttonAutofillCoolDownTime;
    private EditText editTextSteepingTime;
    private EditText editTextAmount;
    private Spinner spinnerAmount;
    private TextView textViewInfusion;
    private Button leftArrow;
    private Button rightArrow;
    private Button deleteInfusion;
    private Button addInfusion;

    private NewTeaViewModel newTeaViewModel;
    private long teaId;
    private boolean showTea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tea);
        hideKeyboardAtFirst();
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        declareViewElements();
        initializeSpinnerWithBigCharacters();

        initializeNewOrEditTea();

        spinnerTeaVariety.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                varietyChanged(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        checkboxTeaSort.setOnCheckedChangeListener((buttonView, isChecked) -> changeVarietyByHand(isChecked));

        buttonColor.setOnClickListener(view -> createColorPicker());
        buttonColor.setOnLongClickListener(this);

        spinnerAmount.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                amountUnitChanged(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        leftArrow.setOnClickListener(v -> navigateToPreviousInfusion());
        leftArrow.setOnLongClickListener(this);

        rightArrow.setOnClickListener(v -> navigateToNextInfusion());
        rightArrow.setOnLongClickListener(this);

        deleteInfusion.setOnClickListener(v -> deleteInfusion());
        deleteInfusion.setOnLongClickListener(this);

        addInfusion.setOnClickListener(v -> addInfusion());
        addInfusion.setOnLongClickListener(this);

        buttonShowCoolDowntime.setOnClickListener(v -> visualizeCoolDownTimeInput());
        buttonShowCoolDowntime.setOnLongClickListener(this);

        buttonAutofillCoolDownTime.setOnClickListener(v -> autofillCoolDownTime());
        buttonAutofillCoolDownTime.setOnLongClickListener(this);

        //showTea wird übergeben, falls die Navigation von showTea erfolgt
        showTea = this.getIntent().getBooleanExtra("showTea", false);
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

    private void declareViewElements() {
        textViewTeaSort = findViewById(R.id.textViewTeaVariety);
        spinnerTeaVariety = findViewById(R.id.spinnerTeaVariety);
        checkboxTeaSort = findViewById(R.id.checkBoxSelfInput);
        editTextTeaSort = findViewById(R.id.editTextSelfInput);
        buttonColor = findViewById(R.id.buttonColor);
        buttonColorShape = new ButtonColorShape(buttonColor.getBackground(), getApplicationContext());
        editTextName = findViewById(R.id.editTextName);
        editTextTemperature = findViewById(R.id.editTextTemperature);
        buttonShowCoolDowntime = findViewById(R.id.buttonShowCoolDownTime);
        editTextCoolDownTime = findViewById(R.id.editTextCoolDownTime);
        buttonAutofillCoolDownTime = findViewById(R.id.buttonAutofillCoolDownTime);
        editTextSteepingTime = findViewById(R.id.editTextTime);
        editTextAmount = findViewById(R.id.editTextAmount);
        spinnerAmount = findViewById(R.id.spinnerAmountUnit);
        textViewInfusion = findViewById(R.id.textViewCountInfusion);
        leftArrow = findViewById(R.id.buttonArrowLeft);
        rightArrow = findViewById(R.id.buttonArrowRight);
        deleteInfusion = findViewById(R.id.buttonDeleteInfusion);
        addInfusion = findViewById(R.id.buttonAddInfusion);
    }

    private void initializeSpinnerWithBigCharacters() {
        initializeSpinnerWithBigCharacters(R.array.variety_teas, R.layout.spinner_item_varietyoftea, R.layout.spinner_dropdown_item_varietyoftea, spinnerTeaVariety);

        initializeSpinnerWithBigCharacters(R.array.newtea_amount, R.layout.spinner_item_amount_unit, R.layout.spinner_dropdown_item_amount_unit, spinnerAmount);
    }

    private void initializeSpinnerWithBigCharacters(int texts, int spinnerItemLayout, int dropdownItemLayout, Spinner spinner) {
        ArrayAdapter<CharSequence> spinnerVarietyAdapter = ArrayAdapter.createFromResource(
                this, texts, spinnerItemLayout);

        spinnerVarietyAdapter.setDropDownViewResource(dropdownItemLayout);
        spinner.setAdapter(spinnerVarietyAdapter);
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
        newTeaViewModel = new NewTeaViewModel(TeaMemoryDatabase.getDatabaseInstance(getApplicationContext()), getApplicationContext());
    }

    private void initializeEditTea() {
        newTeaViewModel = new NewTeaViewModel(teaId, TeaMemoryDatabase.getDatabaseInstance(getApplicationContext()), getApplicationContext());

        fillVarietyInputFields();

        buttonColorShape.setColor(newTeaViewModel.getColor());

        editTextName.setText(newTeaViewModel.getName());

        fillAmountUnitInput();

        fillAmount();

        refreshInfusionBar();
    }

    private void fillVarietyInputFields() {
        //richtige SpinnerId bekommen
        int spinnerId = -1;
        String[] spinnerElements = getResources().getStringArray(R.array.variety_codes);

        for (int i = 0; i < spinnerElements.length; i++) {
            if (spinnerElements[i].equals(newTeaViewModel.getVariety())) {
                spinnerId = i;
                break;
            }
        }
        //Werte werden für Änderungen gefüllt
        //wenn Spinner manuell gefüllt wurde
        if (spinnerId == -1) {
            spinnerTeaVariety.setVisibility(View.INVISIBLE);
            spinnerTeaVariety.setSelection(spinnerElements.length - 1);
            textViewTeaSort.setVisibility(View.INVISIBLE);
            checkboxTeaSort.setVisibility(View.VISIBLE);
            checkboxTeaSort.setChecked(true);
            editTextTeaSort.setVisibility(View.VISIBLE);
            editTextTeaSort.setText(newTeaViewModel.getVariety());
        } else {
            spinnerTeaVariety.setSelection(spinnerId);
        }
    }

    private void fillAmountUnitInput() {
        amountUnit = newTeaViewModel.getAmountkind();
        if ("Ts".equals(amountUnit)) {
            spinnerAmount.setSelection(0);
        } else if ("Gr".equals(amountUnit)) {
            spinnerAmount.setSelection(1);
        }
    }

    private void fillAmount() {
        if (newTeaViewModel.getAmount() != -500) {
            editTextAmount.setText(String.valueOf(newTeaViewModel.getAmount()));
        }
    }

    private void refreshInfusionBar() {
        textViewInfusion.setText(getResources().getString(R.string.newtea_count_infusion, newTeaViewModel.getInfusionIndex() + 1, ". "));

        refreshInfusionBarInputFields();

        showDeleteInfusion();
        showAddInfusion();
        showPreviousInfusion();
        showNextInfusion();
    }

    private void refreshInfusionBarInputFields() {
        fillTemperatureInput();

        fillCoolDownTimeInput();

        fillInfusionTimeInput();
    }

    private void fillTemperatureInput() {
        if (newTeaViewModel.getInfusionTemperature() != -500) {
            editTextTemperature.setText(String.valueOf(newTeaViewModel.getInfusionTemperature()));
        } else {
            editTextTemperature.setText("");
        }
    }

    private void fillCoolDownTimeInput() {
        if (newTeaViewModel.getInfusionCooldowntime() != null) {
            editTextCoolDownTime.setText(newTeaViewModel.getInfusionCooldowntime());
        } else {
            editTextCoolDownTime.setText("");
        }
    }

    private void fillInfusionTimeInput() {
        if (newTeaViewModel.getInfusionTime() != null) {
            editTextSteepingTime.setText(newTeaViewModel.getInfusionTime());
        } else {
            editTextSteepingTime.setText("");
        }
    }

    private void showDeleteInfusion() {
        if (newTeaViewModel.getInfusionSize() > 1) {
            deleteInfusion.setVisibility(View.VISIBLE);
        } else {
            deleteInfusion.setVisibility(View.GONE);
        }
    }

    private void showAddInfusion() {
        if (((newTeaViewModel.getInfusionIndex() + 1) == newTeaViewModel.getInfusionSize()) && (newTeaViewModel.getInfusionSize() < 20)) {
            addInfusion.setVisibility(View.VISIBLE);
        } else {
            addInfusion.setVisibility(View.GONE);
        }
    }

    private void showPreviousInfusion() {
        if (newTeaViewModel.getInfusionIndex() == 0) {
            leftArrow.setEnabled(false);
        } else {
            leftArrow.setEnabled(true);
        }
    }

    private void showNextInfusion() {
        if ((newTeaViewModel.getInfusionIndex() + 1) == newTeaViewModel.getInfusionSize()) {
            rightArrow.setEnabled(false);
        } else {
            rightArrow.setEnabled(true);
        }
    }

    private void varietyChanged(int position) {
        variety = Variety.values()[position];

        buttonColorShape.setColorByVariety(variety.ordinal());

        if (variety.equals(Variety.OTHER)) {
            checkboxTeaSort.setVisibility(View.VISIBLE);
        } else {
            checkboxTeaSort.setVisibility(View.INVISIBLE);
        }
        sethints();
    }

    private void changeVarietyByHand(boolean isChecked) {
        if (isChecked) {
            textViewTeaSort.setVisibility(View.INVISIBLE);
            spinnerTeaVariety.setVisibility(View.INVISIBLE);
            editTextTeaSort.setVisibility(View.VISIBLE);
        } else {
            textViewTeaSort.setVisibility(View.VISIBLE);
            spinnerTeaVariety.setVisibility(View.VISIBLE);
            editTextTeaSort.setVisibility(View.INVISIBLE);
        }
    }

    private void createColorPicker() {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(NewTea.this, buttonColorShape.getColor());
        colorPickerDialog.setTitle(getResources().getString(R.string.newtea_color_dialog_title));
        colorPickerDialog.setOnColorChangedListener(color -> buttonColorShape.setColor(color));
        colorPickerDialog.show();
    }

    private void amountUnitChanged(int position) {
        if (position == 0) {
            amountUnit = "Ts";
        } else if (position == 1) {
            amountUnit = "Gr";
        }
        sethints();
    }

    private void navigateToPreviousInfusion() {
        if (changeInfusions()) {
            newTeaViewModel.previousInfusion();
            refreshInfusionBar();
        }
    }

    private void navigateToNextInfusion() {
        if (changeInfusions()) {
            newTeaViewModel.nextInfusion();
            refreshInfusionBar();
        }
    }

    private void deleteInfusion() {
        newTeaViewModel.deleteInfusion();
        refreshInfusionBar();
    }

    private void addInfusion() {
        if (changeInfusions()) {
            newTeaViewModel.addInfusion();
            refreshInfusionBar();
        }
    }

    private void visualizeCoolDownTimeInput() {
        if (editTextCoolDownTime.getVisibility() == View.VISIBLE) {
            buttonShowCoolDowntime.setBackground(getResources().getDrawable(R.drawable.button_arrowdown));
            editTextCoolDownTime.setVisibility(View.GONE);
            buttonAutofillCoolDownTime.setVisibility(View.GONE);
        } else {
            buttonShowCoolDowntime.setBackground(getResources().getDrawable(R.drawable.button_arrowup));
            editTextCoolDownTime.setVisibility(View.VISIBLE);
            buttonAutofillCoolDownTime.setVisibility(View.VISIBLE);
        }
    }

    private void autofillCoolDownTime() {
        //Ist die Temperatur nicht gesetzt, so ist sie -500
        int temperatureCelsius = -500;
        boolean temperatureValid = temperatureInputIsValid(editTextTemperature.getText().toString());
        if (temperatureValid && !editTextTemperature.getText().toString().equals("")) {
            temperatureCelsius = Integer.parseInt(editTextTemperature.getText().toString());
        }
        //Falls nötig in Celsius umwandeln
        if (newTeaViewModel.getTemperatureunit().equals("Fahrenheit")) {
            temperatureCelsius = TemperatureConversation.fahrenheitToCelsius(temperatureCelsius);
        }
        if (temperatureCelsius != -500 && temperatureCelsius != 100) {
            editTextCoolDownTime.setText(TemperatureConversation.celsiusToCoolDownTime(temperatureCelsius));
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_auto_cooldown_time, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_tea, menu);

        new Handler().post(() -> {
            View view = findViewById(R.id.action_done);

            if (view != null) {
                view.setOnLongClickListener(NewTea.this);
            }
        });

        return true;
    }

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
        String nameInput = editTextName.getText().toString();
        String varietyInput = getVarietyInput();
        String amountInput = editTextAmount.getText().toString();

        if (inputIsValid(nameInput, varietyInput, amountInput)) {
            createOrEditTea(varietyInput, nameInput, parseInteger(amountInput));
            navigateToMainOrShowTea();
        }
    }

    private String getVarietyInput() {
        if (checkboxTeaSort.isChecked()) {
            return editTextTeaSort.getText().toString();
        } else {
            return (String) spinnerTeaVariety.getSelectedItem();
        }
    }

    private boolean inputIsValid(String nameInput, String varietyInput, String amountInput) {
        return nameIsNotEmpty(nameInput)
                && nameIsValid(nameInput)
                && varietyIsValid(varietyInput)
                && amountIsValid(amountInput)
                && changeInfusions();
    }

    private boolean nameIsNotEmpty(String nameInput) {
        if (nameInput.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_no_name, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private boolean nameIsValid(String nameInput) {
        if (nameInput.length() > 300) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_name, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private boolean varietyIsValid(String varietyInput) {
        if (varietyInput.length() > 30) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_30Char, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private boolean amountIsValid(String amountInput) {
        if (amountInput.equals("")) {
            return true;
        } else if (amountInput.contains(".") || amountInput.length() > 3) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_amount, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private int parseInteger(String amountInput) {
        if (amountInput.equals("")) {
            return -500;
        } else {
            return Integer.parseInt(amountInput);
        }
    }

    private void createOrEditTea(String sortOfTea, String name, int amount) {
        if (teaId != 0) {
            newTeaViewModel.editTea(name, sortOfTea, amount, amountUnit, buttonColorShape.getColor());
        } else {
            newTeaViewModel.createNewTea(name, sortOfTea, amount, amountUnit, buttonColorShape.getColor());
        }
    }

    private void navigateToMainOrShowTea() {
        if (!showTea) {
            finish();
        } else {
            navigateToShowTeaActivity();
        }
    }

    private void navigateToShowTeaActivity() {
        Intent showteaScreen = new Intent(NewTea.this, ShowTea.class);
        showteaScreen.putExtra("teaId", newTeaViewModel.getTeaId());
        startActivity(showteaScreen);
        finish();
    }

    private void sethints() {
        //set Hint for variety
        editTextTemperature.setHint(HintConversation.getHintTemperature(variety.ordinal(), newTeaViewModel.getTemperatureunit(), getApplicationContext()));
        editTextAmount.setHint(HintConversation.getHintAmount(variety.ordinal(), amountUnit, getApplicationContext()));
        editTextSteepingTime.setHint(HintConversation.getHintTime(variety.ordinal(), getApplicationContext()));
    }

    private boolean changeInfusions() {
        String temperatureInput = editTextTemperature.getText().toString();
        String coolDownTimeInput = editTextCoolDownTime.getText().toString();
        String timeInput = editTextSteepingTime.getText().toString();

        if (infusionIsValid(temperatureInput, coolDownTimeInput, timeInput)) {
            newTeaViewModel.takeInfusionInformation(parseTextInput(timeInput), parseTextInput(coolDownTimeInput), parseInteger(temperatureInput));
            return true;
        }
        return false;
    }

    private boolean infusionIsValid(String temperatureInput, String coolDownTimeInput, String timeInput) {
        return temperatureIsValid(temperatureInput)
                && coolDownTimeIsValid(coolDownTimeInput)
                && steepingTimeIsValid(timeInput);
    }

    private boolean temperatureIsValid(String temperatureInput) {
        if (!temperatureInputIsValid(temperatureInput)) {
            if (newTeaViewModel.getTemperatureunit().equals("Celsius")) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_celsius, Toast.LENGTH_SHORT);
                toast.show();
            } else if (newTeaViewModel.getTemperatureunit().equals("Fahrenheit")) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_fahrenheit, Toast.LENGTH_SHORT);
                toast.show();
            }
            return false;
        }
        return true;
    }

    private boolean temperatureInputIsValid(String temperature) {
        boolean temperatureValid = true;
        if (!temperature.equals("")) {
            if (temperature.contains(".") || temperature.length() > 3) {
                temperatureValid = false;
            } else {
                int checktemperature = 0;
                if (newTeaViewModel.getTemperatureunit().equals("Celsius"))
                    checktemperature = Integer.parseInt(temperature);
                else if (newTeaViewModel.getTemperatureunit().equals("Fahrenheit"))
                    checktemperature = TemperatureConversation.fahrenheitToCelsius(Integer.parseInt(temperature));

                if (checktemperature > 100 || checktemperature < 0) {
                    temperatureValid = false;
                }
            }
        }
        return temperatureValid;
    }

    private boolean coolDownTimeIsValid(String coolDownTimeInput) {
        if (!timeIsValid(coolDownTimeInput)) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cooldown_time, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private boolean timeIsValid(String time) {
        boolean timeValid;

        timeValid = time.length() < 6;
        if (timeValid && !time.equals("")) {
            boolean formatMinutes = Pattern.matches("\\d\\d", time) || Pattern.matches("\\d", time);
            boolean formatSeconds = Pattern.matches("\\d\\d:\\d\\d", time) || Pattern.matches("\\d:\\d\\d", time);
            if (formatMinutes) {
                timeValid = Integer.parseInt(time) < 60;
            } else if (formatSeconds) {
                String[] split = time.split(":");
                timeValid = Integer.parseInt(split[0]) < 60 && Integer.parseInt(split[1]) < 60;
            } else {
                timeValid = false;
            }
        }
        return timeValid;
    }

    private boolean steepingTimeIsValid(String timeInput) {
        if (!timeIsValid(timeInput)) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_time_format, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private String parseTextInput(String textInput) {
        if (textInput.equals("")) {
            return null;
        }
        return textInput;
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.buttonColor) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_choosecolor));
        } else if (view.getId() == R.id.buttonArrowLeft) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_arrowleft));
        } else if (view.getId() == R.id.buttonArrowRight) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_arrowright));
        } else if (view.getId() == R.id.buttonAddInfusion) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_addinfusion));
        } else if (view.getId() == R.id.buttonDeleteInfusion) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_deleteinfusion));
        } else if (view.getId() == R.id.buttonShowCoolDownTime) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_showcooldowntime));
        } else if (view.getId() == R.id.buttonAutofillCoolDownTime) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_autofillcooldowntime));
        } else if (view.getId() == R.id.action_done) {
            showTooltip(view, Gravity.BOTTOM, getResources().getString(R.string.newtea_tooltip_done));
        }
        return true;
    }

    private void showTooltip(View v, int gravity, String text) {
        new Tooltip.Builder(v)
                .setText(text)
                .setTextColor(getResources().getColor(R.color.white))
                .setGravity(gravity)
                .setCornerRadius(8f)
                .setCancelable(true)
                .setDismissOnClick(true)
                .show();
    }
}
