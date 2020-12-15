package coolpharaoh.tee.speicher.tea.timer.views.showtea;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.views.information.Information;
import coolpharaoh.tee.speicher.tea.timer.views.newtea.NewTea;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.countdowntimer.SharedTimerPreferences;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.countdowntimer.TimerController;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ListRowItem;

public class ShowTea extends AppCompatActivity implements View.OnLongClickListener {
    private static final String LOG_TAG = ShowTea.class.getSimpleName();

    private TextView textViewInfusionIndex;
    private Button buttonNextInfusion;
    private TextView textViewTemperature;
    private Spinner spinnerMinutes;
    private Spinner spinnerSeconds;
    private TextView textViewTimer;
    private Button buttonExchange;
    private Button buttonInfo;
    private ImageView imageViewFill;
    private ImageView imageViewSteam;
    private Button buttonStartTimer;
    private ImageView imageViewCup;
    private TextView textViewDoublePoint;
    private TextView textViewSeconds;
    private TextView textViewMinutes;
    private Button buttonInfusionIndex;
    private TextView textViewName;
    private TextView textViewVariety;
    private TextView textViewAmount;
    private Button buttonCalculateAmount;

    private ShowTeaViewModel showTeaViewModel;
    private boolean infoShown = false;
    //animation
    private long maxMilliSec;
    private int percent;

    private TimerController foregroundTimer;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateClock(intent);
        }

        private void updateClock(Intent intent) {
            if (intent.getExtras() != null) {
                long millis = intent.getLongExtra("countdown", 0);
                boolean ready = intent.getBooleanExtra("ready", false);
                if (!infoShown && showTeaViewModel.isAnimation()) {
                    updateImage(millis);
                }
                if (ready) {
                    textViewTimer.setText(R.string.showtea_tea_ready);
                    if (!infoShown && showTeaViewModel.isAnimation()) {
                        imageViewFill.setImageResource(R.drawable.fill100pr);
                        imageViewSteam.setVisibility((View.VISIBLE));
                    }
                } else {
                    String ms = String.format(Locale.getDefault(), "%02d : %02d",
                            TimeUnit.MILLISECONDS.toMinutes(millis),
                            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                    textViewTimer.setText(ms);
                }
            }
        }

        private void updateImage(long millisec) {
            int percentTmp = 100 - ((int) (((float) millisec / (float) maxMilliSec) * 100));
            if (percentTmp > percent) {
                percent = percentTmp;

                Context context = getApplicationContext();
                String imageName = String.format("fill%spr", percent);
                int imageId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                imageViewFill.setImageResource(imageId);
                imageViewFill.setTag(imageId);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tea);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        declareViewElements();

        setFieldsTransparent();

        initializeSpinnerWithBigCharacters();

        initializeInformationWindow();

        foregroundTimer = new TimerController(getApplication(), new SharedTimerPreferences(getApplication()));

        buttonStartTimer = findViewById(R.id.buttonStartTimer);
        buttonStartTimer.setOnClickListener(v -> startOrResetTimer());

        buttonInfusionIndex.setOnClickListener(v -> showDialogChangeInfusion());
        buttonInfusionIndex.setOnLongClickListener(this);

        buttonNextInfusion.setOnClickListener(v -> displayNextInfusion());
        buttonNextInfusion.setOnLongClickListener(this);

        buttonExchange.setOnClickListener(v -> switchToCoolingPeriod());
        buttonExchange.setOnLongClickListener(this);

        buttonInfo.setOnClickListener(v -> showDialogCoolingPeriod());

        buttonCalculateAmount.setOnClickListener(view -> showDialogAmount());
        buttonCalculateAmount.setOnLongClickListener(this);
    }

    private void defineToolbarAsActionbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.showtea_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);

        toolbarTitle.setOnClickListener(v -> navigateToDetailInformation());
    }

    private void navigateToDetailInformation() {
        Intent informationScreen = new Intent(ShowTea.this, Information.class);
        informationScreen.putExtra("teaId", showTeaViewModel.getTeaId());
        // Intent starten und zur zweiten Activity wechseln
        startActivity(informationScreen);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void declareViewElements() {
        buttonInfusionIndex = findViewById(R.id.toolbar_infusionindex);
        textViewInfusionIndex = findViewById(R.id.toolbar_text_infusionindex);
        buttonNextInfusion = findViewById(R.id.toolbar_nextinfusion);
        textViewName = findViewById(R.id.textViewName);
        textViewVariety = findViewById(R.id.textViewVariety);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        buttonInfo = findViewById(R.id.buttonInfo);
        buttonExchange = findViewById(R.id.buttonExchange);
        textViewAmount = findViewById(R.id.textViewAmount);
        buttonCalculateAmount = findViewById(R.id.buttonCalculateAmount);
        spinnerMinutes = findViewById(R.id.spinnerMinutes);
        spinnerSeconds = findViewById(R.id.spinnerSeconds);
        textViewMinutes = findViewById(R.id.textViewMinutes);
        textViewSeconds = findViewById(R.id.textViewSeconds);
        textViewDoublePoint = findViewById(R.id.textViewDoublePoint);
        textViewTimer = findViewById(R.id.textViewTimer);
        imageViewCup = findViewById(R.id.imageViewCup);
        imageViewFill = findViewById(R.id.imageViewFill);
        imageViewSteam = findViewById(R.id.imageViewSteam);
    }

    private void setFieldsTransparent() {
        int alpha = 130;
        textViewName.getBackground().setAlpha(alpha);
        textViewVariety.getBackground().setAlpha(alpha);
        textViewDoublePoint.getBackground().setAlpha(alpha);
        textViewTimer.getBackground().setAlpha(alpha);
    }

    private void initializeSpinnerWithBigCharacters() {
        ArrayAdapter<CharSequence> spinnerTimeAdapter = ArrayAdapter.createFromResource(
                this, R.array.itemsTimer, R.layout.spinner_item);
        spinnerTimeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerMinutes.setAdapter(spinnerTimeAdapter);
        spinnerSeconds.setAdapter(spinnerTimeAdapter);
    }

    private void initializeInformationWindow() {
        long teaId = this.getIntent().getLongExtra("teaId", 0);
        if (teaId == 0) {
            Log.e(LOG_TAG, "The tea id was not set before navigate to this Activity.");
            disableCompleteActivity();
        } else {
            showTeaInformation(teaId);
        }
    }

    private void disableCompleteActivity() {
        new NotExistingTeaDialog(this).show();
    }

    private void showTeaInformation(long teaId) {
        showTeaViewModel = new ShowTeaViewModel(teaId, getApplication());

        if (showTeaViewModel.teaExists()) {
            fillInformationFields();

            decideToShowCooldownTimeButton();

            decideToShowInfusionBar();

            decideToDisplayContinueNextInfusionDialog();

            decideToDisplayDescription();
        } else {
            Log.e(LOG_TAG, "The tea for the given tea id does not exist.");
            disableCompleteActivity();
        }
    }

    private void fillInformationFields() {
        textViewName.setText(showTeaViewModel.getName());
        textViewVariety.setText(showTeaViewModel.getVariety());

        fillTemperatureWithUnit();

        fillAmountWithUnit();

        spinnerMinutes.setSelection(showTeaViewModel.getTime().minutes);
        spinnerSeconds.setSelection(showTeaViewModel.getTime().seconds);
    }

    private void fillTemperatureWithUnit() {
        if (showTeaViewModel.getTemperature() != -500) {
            if (getResources().getString(R.string.fahrenheit).equals(showTeaViewModel.getTemperatureunit())) {
                textViewTemperature.setText(getResources().getString(R.string.showtea_display_fahrenheit, String.valueOf(showTeaViewModel.getTemperature())));
            } else {
                textViewTemperature.setText(getResources().getString(R.string.showtea_display_celsius, String.valueOf(showTeaViewModel.getTemperature())));
            }
        } else {
            if (getResources().getString(R.string.fahrenheit).equals(showTeaViewModel.getTemperatureunit())) {
                textViewTemperature.setText(getResources().getString(R.string.showtea_display_fahrenheit, "-"));
            } else {
                textViewTemperature.setText(getResources().getString(R.string.showtea_display_celsius, "-"));
            }
        }
    }

    private void fillAmountWithUnit() {
        if (showTeaViewModel.getAmount() != -500) {
            if ("Gr".equals(showTeaViewModel.getAmountKind())) {
                textViewAmount.setText(getResources().getString(R.string.showtea_display_gr, String.valueOf(showTeaViewModel.getAmount())));
            } else {
                textViewAmount.setText(getResources().getString(R.string.showtea_display_ts, String.valueOf(showTeaViewModel.getAmount())));
            }
        } else {
            buttonCalculateAmount.setEnabled(false);
            if ("Gr".equals(showTeaViewModel.getAmountKind())) {
                textViewAmount.setText(getResources().getString(R.string.showtea_display_gr, "-"));
            } else {
                textViewAmount.setText(getResources().getString(R.string.showtea_display_ts, "-"));
            }
        }
    }

    private void decideToShowCooldownTimeButton() {
        TimeHelper cooldowntime = showTeaViewModel.getCoolDownTime();
        if (cooldowntime != null && cooldowntime.time != null) {
            buttonExchange.setEnabled(true);
        }
    }

    private void decideToShowInfusionBar() {
        if (showTeaViewModel.getInfusionSize() == 1) {
            textViewInfusionIndex.setVisibility(View.GONE);
            buttonInfusionIndex.setVisibility(View.GONE);
            buttonNextInfusion.setVisibility(View.GONE);
        }
    }

    private void decideToDisplayDescription() {
        if (showTeaViewModel.isShowteaAlert()) {
            dialogShowTeaDescription();
        }
    }

    private void dialogShowTeaDescription() {
        ViewGroup parent = findViewById(R.id.showtea_parent);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogDescription = inflater.inflate(R.layout.dialog_showtea_description, parent, false);
        final CheckBox dontshowagain = alertLayoutDialogDescription.findViewById(R.id.checkboxDialogShowTeaDescription);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertLayoutDialogDescription);
        builder.setTitle(R.string.showtea_dialog_description_header);
        builder.setPositiveButton(R.string.showtea_dialog_description_ok, (dialog, which) -> {
            if (dontshowagain.isChecked()) {
                showTeaViewModel.setShowteaAlert(false);
            }
        });
        builder.show();
    }

    private void decideToDisplayContinueNextInfusionDialog() {
        if (showTeaViewModel.getNextInfusion() != 0 && showTeaViewModel.getInfusionSize() != 1) {
            displayContinueNextInfusionDialog();
        }
    }

    private void displayContinueNextInfusionDialog() {
        int lastInfusion = showTeaViewModel.getNextInfusion();
        int nextInfusion = showTeaViewModel.getNextInfusion() + 1;
        //Infomationen anzeigen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.showtea_dialog_following_infusion_header);
        builder.setMessage(getResources().getString(R.string.showtea_dialog_following_infusion_description, lastInfusion, nextInfusion));
        builder.setPositiveButton(R.string.showtea_dialog_following_infusion_yes, (dialog, which) -> continueNextInfusion());
        builder.setNegativeButton(R.string.showtea_dialog_following_infusion_no, (dialog, which) -> showTeaViewModel.resetNextInfusion());
        builder.show();
    }

    private void continueNextInfusion() {
        showTeaViewModel.setInfusionIndex(showTeaViewModel.getNextInfusion());
        infusionIndexChanged();
    }

    private void infusionIndexChanged() {
        if (showTeaViewModel.getTemperature() != -500) {
            if (showTeaViewModel.getTemperatureunit().equals("Fahrenheit")) {
                textViewTemperature.setText(getResources().getString(R.string.showtea_display_fahrenheit, String.valueOf(showTeaViewModel.getTemperature())));
            } else {
                textViewTemperature.setText(getResources().getString(R.string.showtea_display_celsius, String.valueOf(showTeaViewModel.getTemperature())));
            }
        } else {
            if (showTeaViewModel.getTemperatureunit().equals("Fahrenheit")) {
                textViewTemperature.setText(getResources().getString(R.string.showtea_display_fahrenheit, "-"));
            } else {
                textViewTemperature.setText(getResources().getString(R.string.showtea_display_celsius, "-"));
            }
        }

        buttonExchange.setEnabled(showTeaViewModel.getCoolDownTime().time != null);

        spinnerMinutes.setSelection(showTeaViewModel.getTime().minutes);
        spinnerSeconds.setSelection(showTeaViewModel.getTime().seconds);
        textViewInfusionIndex.setText(getResources().getString(R.string.showtea_break_count_point, (showTeaViewModel.getInfusionIndex() + 1)));

        nextInfusionEnable();

        buttonInfo.setVisibility(View.INVISIBLE);
        infoShown = false;
    }

    private void nextInfusionEnable() {
        buttonNextInfusion.setEnabled(showTeaViewModel.getInfusionIndex() != showTeaViewModel.getInfusionSize() - 1);
    }

    private void startOrResetTimer() {
        if (getResources().getString(R.string.showtea_timer_start).contentEquals(buttonStartTimer.getText())) {
            startTimer();
        } else if (getResources().getString(R.string.showtea_timer_reset).contentEquals(buttonStartTimer.getText())) {
            resetTimer();
        }
    }

    private void startTimer() {
        buttonStartTimer.setText(R.string.showtea_timer_reset);

        collectDrinkingBehaviorInformation();

        disableInfusionBarAndCooldownSwitch();

        hideTimeInputAndVisualizeTimerDisplay();

        visualizeTeaCup();

        calculateInfusionTimeAndStartTimer();
    }

    private void collectDrinkingBehaviorInformation() {
        if (!infoShown) showTeaViewModel.countCounter();
        showTeaViewModel.setCurrentDate();
        showTeaViewModel.updateNextInfusion();
    }

    private void disableInfusionBarAndCooldownSwitch() {
        buttonExchange.setEnabled(false);
        buttonInfusionIndex.setEnabled(false);
        buttonNextInfusion.setEnabled(false);
    }

    private void hideTimeInputAndVisualizeTimerDisplay() {
        setVisibilityTimeInput(View.INVISIBLE);
        textViewTimer.setVisibility((View.VISIBLE));
    }

    private void setVisibilityTimeInput(int visibility) {
        spinnerMinutes.setVisibility(visibility);
        spinnerSeconds.setVisibility(visibility);
        textViewMinutes.setVisibility(visibility);
        textViewSeconds.setVisibility(visibility);
        textViewDoublePoint.setVisibility(visibility);
    }

    private void visualizeTeaCup() {
        if (!infoShown && showTeaViewModel.isAnimation()) {
            imageViewCup.setVisibility((View.VISIBLE));
            imageViewFill.setVisibility((View.VISIBLE));
            //Farbe des Inhalts der Tasse festlegen
            imageViewFill.setColorFilter(showTeaViewModel.getColor(), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void calculateInfusionTimeAndStartTimer() {
        int min = Integer.parseInt(spinnerMinutes.getSelectedItem().toString());
        int sec = Integer.parseInt(spinnerSeconds.getSelectedItem().toString());
        long millisec = TimeUnit.MINUTES.toMillis(min) + TimeUnit.SECONDS.toMillis(sec);

        maxMilliSec = millisec;

        foregroundTimer.startForegroundTimer(millisec, showTeaViewModel.getTeaId());
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(broadcastReceiver, new IntentFilter(TimerController.COUNTDOWN_BR));

        foregroundTimer.resumeForegroundTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        foregroundTimer.startBackgroundTimer();
    }

    @Override
    protected void onDestroy() {
        foregroundTimer.reset();
        unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }

    private void resetTimer() {
        buttonStartTimer.setText(R.string.showtea_timer_start);

        enableInfusionBarAndCooldownSwitch();

        visualizeTimerDisplayAndHideTimeInput();

        hideAndResetTeaCup();

        foregroundTimer.reset();
    }

    private void enableInfusionBarAndCooldownSwitch() {
        decideToShowCooldownTimeButton();
        buttonInfusionIndex.setEnabled(true);
        nextInfusionEnable();
    }

    private void visualizeTimerDisplayAndHideTimeInput() {
        setVisibilityTimeInput(View.VISIBLE);
        textViewTimer.setVisibility((View.INVISIBLE));
    }

    private void hideAndResetTeaCup() {
        if (!infoShown && showTeaViewModel.isAnimation()) {
            imageViewCup.setVisibility((View.INVISIBLE));
            imageViewFill.setVisibility((View.INVISIBLE));
            imageViewFill.setImageResource(R.drawable.fill0pr);
            imageViewSteam.setVisibility((View.INVISIBLE));
            //für animation zurücksetzen
            imageViewCup.setImageResource(R.drawable.cup_new);
            percent = 0;
        }
    }

    private void showDialogChangeInfusion() {
        int tmpSize = showTeaViewModel.getInfusionSize();
        String[] items = new String[tmpSize];
        for (int i = 0; i < tmpSize; i++) {
            items[i] = getResources().getString(R.string.showtea_dialog_infusion_count_desciption, i + 1);
        }

        //Get CheckedItem
        int checkedItem = showTeaViewModel.getInfusionIndex();

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
        builder.setIcon(R.drawable.infusion_black);
        builder.setTitle(R.string.showtea_dialog_infusion_count_title);
        builder.setSingleChoiceItems(items, checkedItem, (dialog, item) -> {
            showTeaViewModel.setInfusionIndex(item);
            infusionIndexChanged();
            dialog.dismiss();
        });
        builder.setNegativeButton(R.string.showtea_dialog_infusion_count_negative, null);
        builder.create().show();
    }

    private void displayNextInfusion() {
        showTeaViewModel.incrementInfusionIndex();
        infusionIndexChanged();
    }

    private void switchToCoolingPeriod() {
        if (!infoShown) {
            buttonInfo.setVisibility(View.VISIBLE);
            infoShown = true;
            spinnerMinutes.setSelection(showTeaViewModel.getCoolDownTime().minutes);
            spinnerSeconds.setSelection(showTeaViewModel.getCoolDownTime().seconds);
        } else {
            buttonInfo.setVisibility(View.INVISIBLE);
            infoShown = false;
            spinnerMinutes.setSelection(showTeaViewModel.getTime().minutes);
            spinnerSeconds.setSelection(showTeaViewModel.getTime().seconds);
        }
    }

    private void showDialogCoolingPeriod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.showtea_cooldown_header);
        builder.setMessage(R.string.showtea_cooldown_description);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void showDialogAmount() {
        ViewGroup parent = findViewById(R.id.showtea_parent);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogAmount = inflater.inflate(R.layout.dialog_amount, parent, false);
        final SeekBar seekBarAmountPerAmount = alertLayoutDialogAmount.findViewById(R.id.seekBarAmountPerAmount);
        final TextView textViewAmountPerAmount = alertLayoutDialogAmount.findViewById(R.id.textViewShowAmountPerAmount);
        // 10 for 1 liter
        fillAmountPerAmount(10, textViewAmountPerAmount);

        seekBarAmountPerAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                fillAmountPerAmount(value, textViewAmountPerAmount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // this functionality is not needed, but needs to be override
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // this functionality is not needed, but needs to be override
            }
        });

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertLayoutDialogAmount);
        adb.setTitle(R.string.showtea_dialog_amount);
        adb.setIcon(R.drawable.spoon);
        adb.setPositiveButton(R.string.showtea_dialog_amount_ok, null);
        adb.show();
    }

    private void fillAmountPerAmount(int value, TextView textViewAmountPerAmount) {
        float liter = (float) value / 10;
        float amountPerLiter = (float) showTeaViewModel.getAmount() * liter;
        if ("Gr".equals(showTeaViewModel.getAmountKind())) {
            textViewAmountPerAmount.setText(getResources().getString(R.string.showtea_dialog_amount_per_amount_gr, amountPerLiter, liter));
        } else {
            textViewAmountPerAmount.setText(getResources().getString(R.string.showtea_dialog_amount_per_amount_ts, amountPerLiter, liter));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_tea, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            return navigateToEditTea();
        } else if (id == R.id.action_counter) {
            dialogCounter();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean navigateToEditTea() {
        Intent newTeaScreen = new Intent(ShowTea.this, NewTea.class);
        newTeaScreen.putExtra("teaId", showTeaViewModel.getTeaId());
        newTeaScreen.putExtra("showTea", true);
        // Intent starten und zur zweiten Activity wechseln
        startActivity(newTeaScreen);
        finish();
        return true;
    }

    private void dialogCounter() {
        Counter counter = showTeaViewModel.getCounter();

        List<ListRowItem> counterList = new ArrayList<>();
        ListRowItem itemToday = new ListRowItem(getResources().getString(R.string.showtea_dialog_counter_day), String.valueOf(counter.getDay()));
        counterList.add(itemToday);
        ListRowItem itemWeek = new ListRowItem(getResources().getString(R.string.showtea_dialog_counter_week), String.valueOf(counter.getWeek()));
        counterList.add(itemWeek);
        ListRowItem itemMonth = new ListRowItem(getResources().getString(R.string.showtea_dialog_counter_month), String.valueOf(counter.getMonth()));
        counterList.add(itemMonth);
        ListRowItem itemAll = new ListRowItem(getResources().getString(R.string.showtea_dialog_counter_overall), String.valueOf(counter.getOverall()));
        counterList.add(itemAll);

        //Liste mit Adapter verknüpfen
        CounterListAdapter adapter = new CounterListAdapter(this, counterList);
        //Adapter dem Listview hinzufügen
        ListView listViewCounter = new ListView(this);
        listViewCounter.setAdapter(adapter);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(listViewCounter);
        adb.setTitle(R.string.showtea_action_counter);
        adb.setIcon(R.drawable.statistics_black);
        adb.setPositiveButton(R.string.showtea_dialog_counter_ok, null);
        adb.show();
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.buttonExchange) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.showtea_tooltip_exchange));
        } else if (view.getId() == R.id.buttonCalculateAmount) {
            showTooltip(view, Gravity.BOTTOM, getResources().getString(R.string.showtea_tooltip_calculateamount));
        } else if (view.getId() == R.id.toolbar_infusionindex) {
            showTooltip(view, Gravity.BOTTOM, getResources().getString(R.string.showtea_tooltip_infusion));
        } else if (view.getId() == R.id.toolbar_nextinfusion) {
            showTooltip(view, Gravity.BOTTOM, getResources().getString(R.string.showtea_tooltip_nextinfusion));
        }
        return true;
    }

    //creates a Tooltip
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
