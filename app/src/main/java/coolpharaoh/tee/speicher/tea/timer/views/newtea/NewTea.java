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
import coolpharaoh.tee.speicher.tea.timer.viewmodels.NewTeaViewModel;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.HintConversation;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.TemperatureConversation;
import coolpharaoh.tee.speicher.tea.timer.views.ShowTea;


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
        setDefaultTexts();

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
        textViewTeaSort = findViewById(R.id.textViewTeaSort);
        spinnerTeaVariety = findViewById(R.id.spinnerTeaSort);
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

    private void setDefaultTexts() {
        //TODO in UI definieren
        textViewTeaSort.setText(R.string.newtea_tea_variety);
        editTextName.setHint(getResources().getString(R.string.newtea_hint_name));
        spinnerTeaVariety.setPrompt(getResources().getString(R.string.newtea_tea_variety));
        checkboxTeaSort.setText(R.string.newtea_by_hand);
        editTextTeaSort.setHint(R.string.newtea_tea_variety);
        textViewInfusion.setText(getResources().getString(R.string.newtea_count_infusion, 1, ". "));
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

        fillTemperatureInput();

        fillCoolDownTimeInput();

        fillInfusionTimeInput();

        refreshInfusionConsole();
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
        if (newTeaViewModel.getAmount() != -500)
            editTextAmount.setText(String.valueOf(newTeaViewModel.getAmount()));
    }

    private void fillTemperatureInput() {
        if (newTeaViewModel.getInfusionTemperature() != -500) {
            editTextTemperature.setText(String.valueOf(newTeaViewModel.getInfusionTemperature()));
        }
    }

    private void fillCoolDownTimeInput() {
        if (newTeaViewModel.getInfusionCooldowntime() != null) {
            editTextCoolDownTime.setText(newTeaViewModel.getInfusionCooldowntime());
        }
    }

    private void fillInfusionTimeInput() {
        if (newTeaViewModel.getInfusionTime() != null) {
            editTextSteepingTime.setText(newTeaViewModel.getInfusionTime());
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
        if (changeInfusion()) {
            newTeaViewModel.previousInfusion();
            refreshInfusionConsole();
            refreshInfusionInformation();
        }
    }

    private void navigateToNextInfusion() {
        if (changeInfusion()) {
            newTeaViewModel.nextInfusion();
            refreshInfusionConsole();
            refreshInfusionInformation();
        }
    }

    private void deleteInfusion() {
        newTeaViewModel.deleteInfusion();
        refreshInfusionConsole();
        refreshInfusionInformation();
    }

    private void addInfusion() {
        if (changeInfusion()) {
            newTeaViewModel.addInfusion();
            refreshInfusionConsole();
            clearInfusionInformation();
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
        boolean temperatureValid = temperatureStringValid(editTextTemperature.getText().toString());
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
        } else if (showTea) {
            //Neues Intent anlegen
            Intent showteaScreen = new Intent(NewTea.this, ShowTea.class);
            showteaScreen.putExtra("teaId", newTeaViewModel.getTeaId());
            // Intent starten und zur zweiten Activity wechseln
            startActivity(showteaScreen);
            finish();
            return true;
        } else if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addTea() {
        //Der Name muss eingegeben werden
        if (!editTextName.getText().toString().equals("")) {
            //Attribute auslesen
            boolean sortValid = true;
            String sortOfTea;
            if (checkboxTeaSort.isChecked()) {
                sortOfTea = editTextTeaSort.getText().toString();
                sortValid = sortOfTea.length() <= 30;
            } else {
                sortOfTea = (String) spinnerTeaVariety.getSelectedItem();
            }
            //Ist der Name Valide
            String name = editTextName.getText().toString();
            boolean nameValid = nameValid(name);

            //Ist teelamass nicht gesetzt so ist es -500
            int amount = -500;
            boolean amountValid = amountValid(editTextAmount.getText().toString());
            if (amountValid && !editTextAmount.getText().toString().equals("")) {
                amount = Integer.parseInt(editTextAmount.getText().toString());
            }

            //Überprüfe ob alle Werte Valide sind
            if (!sortValid) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_30Char, Toast.LENGTH_SHORT);
                toast.show();
            } else if (!nameValid) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_name, Toast.LENGTH_SHORT);
                toast.show();
            } else if (!amountValid) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_amount, Toast.LENGTH_SHORT);
                toast.show();
            } else if (changeInfusion()) {

                if (teaId != 0) {
                    //Tee wird geändert
                    newTeaViewModel.editTea(name, sortOfTea, amount, amountUnit, buttonColorShape.getColor());
                } else {
                    //erstelle Tee
                    newTeaViewModel.createNewTea(name, sortOfTea, amount, amountUnit, buttonColorShape.getColor());
                }
                if (!showTea) {
                    //wechsel das Fenster
                    finish();
                } else {
                    //Neues Intent anlegen
                    Intent showteaScreen = new Intent(NewTea.this, ShowTea.class);
                    //find out teaAt by Name
                    showteaScreen.putExtra("teaId", newTeaViewModel.getTeaId());
                    // Intent starten und zur zweiten Activity wechseln
                    startActivity(showteaScreen);
                    finish();
                }
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_no_name, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean nameValid(String name) {
        return name.length() < 300;
    }

    private boolean temperatureStringValid(String temperature) {
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

    private boolean amountValid(String teelamass) {
        boolean amountValid = true;
        if (!teelamass.equals("")) {
            if (teelamass.contains(".") || teelamass.length() > 3) {
                amountValid = false;
            }
        }
        return amountValid;
    }

    private boolean timeValid(String time) {
        boolean timeValid;
        //ist die Zeit gesetzt so wird sie geprüft
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

    private void sethints() {
        //set Hint for variety
        editTextTemperature.setHint(HintConversation.getHintTemperature(variety.ordinal(), newTeaViewModel.getTemperatureunit(), getApplicationContext()));
        editTextAmount.setHint(HintConversation.getHintAmount(variety.ordinal(), amountUnit, getApplicationContext()));
        editTextSteepingTime.setHint(HintConversation.getHintTime(variety.ordinal(), getApplicationContext()));
    }

    private void refreshInfusionConsole() {
        //Show Delete or Not
        if (newTeaViewModel.getInfusionSize() > 1) {
            deleteInfusion.setVisibility(View.VISIBLE);
        } else {
            deleteInfusion.setVisibility(View.GONE);
        }
        //show Add or Not
        if (((newTeaViewModel.getInfusionIndex() + 1) == newTeaViewModel.getInfusionSize()) && (newTeaViewModel.getInfusionSize() < 20)) {
            addInfusion.setVisibility(View.VISIBLE);
        } else {
            addInfusion.setVisibility(View.GONE);
        }
        //enable Left
        if (newTeaViewModel.getInfusionIndex() == 0) {
            leftArrow.setEnabled(false);
        } else {
            leftArrow.setEnabled(true);
        }
        //enable Right
        if ((newTeaViewModel.getInfusionIndex() + 1) == newTeaViewModel.getInfusionSize()) {
            rightArrow.setEnabled(false);
        } else {
            rightArrow.setEnabled(true);
        }
        //show Text
        textViewInfusion.setText(getResources().getString(R.string.newtea_count_infusion, newTeaViewModel.getInfusionIndex() + 1, ". "));
    }

    private void refreshInfusionInformation() {
        clearInfusionInformation();

        fillTemperatureInput();

        fillCoolDownTimeInput();

        fillInfusionTimeInput();
    }

    private void clearInfusionInformation() {
        editTextTemperature.setText("");
        editTextCoolDownTime.setText("");
        editTextSteepingTime.setText("");
    }

    private boolean changeInfusion() {
        boolean works = true;

        //Ist die Temperatur nicht gesetzt, so ist sie -500
        int temperature = -500;
        boolean temperatureValid = temperatureStringValid(editTextTemperature.getText().toString());
        if (temperatureValid && !editTextTemperature.getText().toString().equals("")) {
            temperature = Integer.parseInt(editTextTemperature.getText().toString());
        }

        //Ist Zeit nicht gesetzt so ist sie null
        String coolDownTime = null;
        boolean coolDownTimeValid = timeValid(editTextCoolDownTime.getText().toString());
        if (coolDownTimeValid && !editTextCoolDownTime.getText().toString().equals("")) {
            coolDownTime = editTextCoolDownTime.getText().toString();
        }

        //Ist Zeit nicht gesetzt so ist sie null
        String time = null;
        boolean timeValid = timeValid(editTextSteepingTime.getText().toString());
        if (timeValid && !editTextSteepingTime.getText().toString().equals("")) {
            time = editTextSteepingTime.getText().toString();
        }

        if (!temperatureValid) {
            works = false;
            if (newTeaViewModel.getTemperatureunit().equals("Celsius")) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_celsius, Toast.LENGTH_SHORT);
                toast.show();
            } else if (newTeaViewModel.getTemperatureunit().equals("Fahrenheit")) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_fahrenheit, Toast.LENGTH_SHORT);
                toast.show();
            }
        } else if (!coolDownTimeValid) {
            works = false;
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cooldown_time, Toast.LENGTH_SHORT);
            toast.show();
        } else if (!timeValid) {
            works = false;
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_time_format, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            newTeaViewModel.takeInfusionInformation(time, coolDownTime, temperature);
        }
        return works;
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