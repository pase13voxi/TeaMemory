package coolpharaoh.tee.speicher.tea.timer.views.show_tea;


import static org.apache.commons.lang3.StringUtils.rightPad;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TimeConverter;
import coolpharaoh.tee.speicher.tea.timer.views.description.ShowTeaDescription;
import coolpharaoh.tee.speicher.tea.timer.views.information.Information;
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.NewTea;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer.SharedTimerPreferences;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer.TimerController;
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind.DisplayAmountKindFactory;
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind.DisplayAmountKindStrategy;
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit.DisplayTemperatureUnitFactory;
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit.DisplayTemperatureUnitStrategy;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class ShowTea extends AppCompatActivity {
    private static final String LOG_TAG = ShowTea.class.getSimpleName();
    public static final String EXTRA_TEA_ID = "teaId";

    private TextView textViewInfusionIndex;
    private ImageButton buttonNextInfusion;
    private TextView textViewTemperature;
    private Spinner spinnerMinutes;
    private Spinner spinnerSeconds;
    private TextView textViewTimer;
    private ImageButton buttonTemperature;
    private ImageButton buttonInfo;
    private ImageView imageViewFill;
    private ImageView imageViewSteam;
    private Button buttonStartTimer;
    private ImageView imageViewCup;
    private TextView textViewDoublePoint;
    private TextView textViewSeconds;
    private TextView textViewMinutes;
    private ImageButton buttonInfusionIndex;
    private TextView textViewName;
    private TextView textViewVariety;
    private TextView textViewAmount;
    private ImageButton buttonCalculateAmount;

    private ShowTeaViewModel showTeaViewModel;
    private boolean infoShown = false;
    //animation
    private long maxMilliSec;
    private int percent;

    private TimerController foregroundTimer;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            updateClock(intent);
        }

        private void updateClock(final Intent intent) {
            if (intent.getExtras() != null) {
                final long millis = intent.getLongExtra("countdown", 0);
                final boolean ready = intent.getBooleanExtra("ready", false);
                if (!infoShown && showTeaViewModel.isAnimation()) {
                    updateImage(millis);
                }
                if (ready) {
                    textViewTimer.setText(R.string.show_tea_tea_ready);
                    if (!infoShown && showTeaViewModel.isAnimation()) {
                        imageViewFill.setImageResource(R.drawable.cup_fill100pr);
                        imageViewSteam.setVisibility((View.VISIBLE));
                    }
                } else {
                    final String ms = String.format(Locale.getDefault(), "%02d : %02d",
                            TimeUnit.MILLISECONDS.toMinutes(millis),
                            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                    textViewTimer.setText(ms);
                }
            }
        }

        private void updateImage(final long milliSec) {
            final int percentTmp = 100 - ((int) (((float) milliSec / (float) maxMilliSec) * 100));
            if (percentTmp > percent) {
                percent = percentTmp;

                final Context context = getApplicationContext();
                final String imageName = String.format("cup_fill%spr", percent);
                final int imageId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                imageViewFill.setImageResource(imageId);
                imageViewFill.setTag(imageId);
            }
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tea);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        declareViewElements();

        setFieldsTransparent();

        initializeSpinnerWithBigCharacters();

        initializeInformationWindow();

        foregroundTimer = new TimerController(getApplication(), new SharedTimerPreferences(getApplication()));

        buttonStartTimer = findViewById(R.id.button_show_tea_start_timer);
        buttonStartTimer.setOnClickListener(v -> startOrResetTimer());

        buttonInfusionIndex.setOnClickListener(v -> showDialogChangeInfusion());

        buttonNextInfusion.setOnClickListener(v -> displayNextInfusion());

        buttonTemperature.setOnClickListener(v -> switchToCoolingPeriod());

        buttonInfo.setOnClickListener(v -> showDialogCoolingPeriod());

        buttonCalculateAmount.setOnClickListener(view -> decideToShowDialogAmount());

    }

    private void defineToolbarAsActionbar() {
        final Toolbar toolbar = findViewById(R.id.tool_bar);
        final TextView toolbarTitle = findViewById(R.id.tool_bar_title);
        toolbarTitle.setText(R.string.show_tea_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);

        toolbarTitle.setOnClickListener(v -> navigateToDetailInformation());
    }

    private void navigateToDetailInformation() {
        final Intent informationScreen = new Intent(ShowTea.this, Information.class);
        informationScreen.putExtra(EXTRA_TEA_ID, showTeaViewModel.getTeaId());
        // Intent starten und zur zweiten Activity wechseln
        startActivity(informationScreen);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void declareViewElements() {
        buttonInfusionIndex = findViewById(R.id.show_tea_tool_bar_infusion_index);
        textViewInfusionIndex = findViewById(R.id.show_tea_tool_bar_text_infusion_index);
        buttonNextInfusion = findViewById(R.id.show_tea_tool_bar_next_infusion);
        textViewName = findViewById(R.id.text_view_show_tea_name);
        textViewVariety = findViewById(R.id.text_view_show_tea_variety);
        textViewTemperature = findViewById(R.id.text_view_show_tea_temperature);
        buttonInfo = findViewById(R.id.button_show_tea_info);
        buttonTemperature = findViewById(R.id.button_show_tea_temperature);
        textViewAmount = findViewById(R.id.text_view_show_tea_amount);
        buttonCalculateAmount = findViewById(R.id.button_show_tea_calculate_amount);
        spinnerMinutes = findViewById(R.id.spinner_show_tea_minutes);
        spinnerSeconds = findViewById(R.id.spinner_show_tea_seconds);
        textViewMinutes = findViewById(R.id.text_view_show_tea_minutes);
        textViewSeconds = findViewById(R.id.text_view_show_tea_seconds);
        textViewDoublePoint = findViewById(R.id.text_view_show_tea_double_point);
        textViewTimer = findViewById(R.id.text_view_show_tea_timer);
        imageViewCup = findViewById(R.id.image_view_show_tea_cup);
        imageViewFill = findViewById(R.id.image_view_show_tea_fill);
        imageViewSteam = findViewById(R.id.image_view_show_tea_steam);
    }

    private void setFieldsTransparent() {
        final int alpha = 130;
        textViewName.getBackground().mutate().setAlpha(alpha);
        textViewVariety.getBackground().mutate().setAlpha(alpha);
        textViewDoublePoint.getBackground().mutate().setAlpha(alpha);
        textViewTimer.getBackground().mutate().setAlpha(alpha);
    }

    private void initializeSpinnerWithBigCharacters() {
        final ArrayAdapter<CharSequence> spinnerTimeAdapter = ArrayAdapter.createFromResource(
                this, R.array.show_tea_items_timer, R.layout.spinner_item);
        spinnerTimeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerMinutes.setAdapter(spinnerTimeAdapter);
        spinnerSeconds.setAdapter(spinnerTimeAdapter);
    }

    private void initializeInformationWindow() {
        final long teaId = this.getIntent().getLongExtra(EXTRA_TEA_ID, 0);
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

    private void showTeaInformation(final long teaId) {
        showTeaViewModel = new ShowTeaViewModel(teaId, getApplication());

        if (showTeaViewModel.teaExists()) {
            fillInformationFields();

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

        spinnerMinutes.setSelection(showTeaViewModel.getTime().getMinutes());
        spinnerSeconds.setSelection(showTeaViewModel.getTime().getSeconds());
    }

    private void fillTemperatureWithUnit() {
        final DisplayTemperatureUnitStrategy displayTemperatureUnitStrategy = DisplayTemperatureUnitFactory.get(showTeaViewModel.getTemperatureUnit(), getApplication());

        textViewTemperature.setText(displayTemperatureUnitStrategy.getTextIdShowTea(showTeaViewModel.getTemperature()));
    }

    private void fillAmountWithUnit() {
        final ImageButton imageButtonAmount = findViewById(R.id.button_show_tea_calculate_amount);
        final DisplayAmountKindStrategy displayAmountKindStrategy = DisplayAmountKindFactory.get(showTeaViewModel.getAmountKind(), getApplication());

        textViewAmount.setText(rightPad(displayAmountKindStrategy.getTextShowTea(showTeaViewModel.getAmount()), 10));
        imageButtonAmount.setImageResource(displayAmountKindStrategy.getImageResourceIdShowTea());
    }

    private void decideToShowInfusionBar() {
        if (showTeaViewModel.getInfusionSize() == 1) {
            textViewInfusionIndex.setVisibility(View.GONE);
            buttonInfusionIndex.setVisibility(View.GONE);
            buttonNextInfusion.setVisibility(View.GONE);
        }
    }

    private void decideToDisplayDescription() {
        if (showTeaViewModel.isShowTeaAlert()) {
            dialogShowTeaDescription();
        }
    }

    private void dialogShowTeaDescription() {
        final ViewGroup parent = findViewById(R.id.show_tea_parent);

        final LayoutInflater inflater = getLayoutInflater();
        final View alertLayoutDialogDescription = inflater.inflate(R.layout.dialog_showtea_description, parent, false);
        final CheckBox donNotShowAgain = alertLayoutDialogDescription.findViewById(R.id.check_box_show_tea_dialog_description);

        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setView(alertLayoutDialogDescription)
                .setTitle(R.string.show_tea_dialog_description_header)
                .setNegativeButton(R.string.show_tea_dialog_description_cancel, (dialog, which) -> disableDescription(donNotShowAgain))
                .setPositiveButton(R.string.show_tea_dialog_description_show, (dialog, which) -> navigateToShowTeaDescription(donNotShowAgain))
                .show();
    }

    private void disableDescription(final CheckBox donNotShowAgain) {
        if (donNotShowAgain.isChecked()) {
            showTeaViewModel.setShowTeaAlert(false);
        }
    }

    private void navigateToShowTeaDescription(final CheckBox donNotShowAgain) {
        disableDescription(donNotShowAgain);
        final Intent intent = new Intent(ShowTea.this, ShowTeaDescription.class);
        startActivity(intent);
    }

    private void decideToDisplayContinueNextInfusionDialog() {
        if (showTeaViewModel.getNextInfusion() != 0 && showTeaViewModel.getInfusionSize() != 1) {
            displayContinueNextInfusionDialog();
        }
    }

    private void displayContinueNextInfusionDialog() {
        final int lastInfusion = showTeaViewModel.getNextInfusion();
        final int nextInfusion = showTeaViewModel.getNextInfusion() + 1;
        //Infomationen anzeigen
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
        builder.setTitle(R.string.show_tea_dialog_following_infusion_header);
        builder.setMessage(getResources().getString(R.string.show_tea_dialog_following_infusion_description, lastInfusion, nextInfusion));
        builder.setPositiveButton(R.string.show_tea_dialog_following_infusion_yes, (dialog, which) -> continueNextInfusion());
        builder.setNegativeButton(R.string.show_tea_dialog_following_infusion_no, (dialog, which) -> showTeaViewModel.resetNextInfusion());
        builder.show();
    }

    private void continueNextInfusion() {
        showTeaViewModel.setInfusionIndex(showTeaViewModel.getNextInfusion());
        infusionIndexChanged();
    }

    private void infusionIndexChanged() {
        final DisplayTemperatureUnitStrategy displayTemperatureUnitStrategy = DisplayTemperatureUnitFactory.get(showTeaViewModel.getTemperatureUnit(), getApplication());

        textViewTemperature.setText(displayTemperatureUnitStrategy.getTextIdShowTea(showTeaViewModel.getTemperature()));

        spinnerMinutes.setSelection(showTeaViewModel.getTime().getMinutes());
        spinnerSeconds.setSelection(showTeaViewModel.getTime().getSeconds());
        textViewInfusionIndex.setText(getResources().getString(R.string.show_tea_break_count_point, (showTeaViewModel.getInfusionIndex() + 1)));

        nextInfusionEnable();

        buttonInfo.setVisibility(View.INVISIBLE);
        infoShown = false;
    }

    private void nextInfusionEnable() {
        buttonNextInfusion.setEnabled(showTeaViewModel.getInfusionIndex() != showTeaViewModel.getInfusionSize() - 1);
    }

    private void startOrResetTimer() {
        if (getResources().getString(R.string.show_tea_timer_start).contentEquals(buttonStartTimer.getText())) {
            startTimer();
        } else if (getResources().getString(R.string.show_tea_timer_reset).contentEquals(buttonStartTimer.getText())) {
            resetTimer();
        }
    }

    private void startTimer() {
        askNotificationPermission();

        buttonStartTimer.setText(R.string.show_tea_timer_reset);

        collectDrinkingBehaviorInformation();

        disableInfusionBarAndCooldownSwitch();

        hideTimeInputAndVisualizeTimerDisplay();

        visualizeTeaCup();

        calculateInfusionTimeAndStartTimer();
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Snackbar.make(findViewById(R.id.show_tea_parent), R.string.show_tea_snack_bar_notification_description, Snackbar.LENGTH_LONG)
                            .setAction(R.string.show_tea_snack_bar_notification_button, v -> {
                                final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                final Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }).show();
                }
            });

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void collectDrinkingBehaviorInformation() {
        if (!infoShown) showTeaViewModel.countCounter();
        showTeaViewModel.setCurrentDate();
        showTeaViewModel.updateNextInfusion();
    }

    private void disableInfusionBarAndCooldownSwitch() {
        buttonTemperature.setEnabled(false);
        buttonInfusionIndex.setEnabled(false);
        buttonNextInfusion.setEnabled(false);
    }

    private void hideTimeInputAndVisualizeTimerDisplay() {
        setVisibilityTimeInput(View.INVISIBLE);
        textViewTimer.setVisibility((View.VISIBLE));
    }

    private void setVisibilityTimeInput(final int visibility) {
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
        final int min = Integer.parseInt(spinnerMinutes.getSelectedItem().toString());
        final int sec = Integer.parseInt(spinnerSeconds.getSelectedItem().toString());
        final long millisec = TimeUnit.MINUTES.toMillis(min) + TimeUnit.SECONDS.toMillis(sec);

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
        buttonStartTimer.setText(R.string.show_tea_timer_start);

        enableInfusionBarAndCooldownSwitch();

        visualizeTimerDisplayAndHideTimeInput();

        hideAndResetTeaCup();

        foregroundTimer.reset();

        askForRatingAfterTheCounterHasBeenUsed();
    }

    private void enableInfusionBarAndCooldownSwitch() {
        buttonTemperature.setEnabled(true);
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
            imageViewFill.setImageResource(R.drawable.cup_fill0pr);
            imageViewSteam.setVisibility((View.INVISIBLE));
            //für animation zurücksetzen
            imageViewCup.setImageResource(R.drawable.cup);
            percent = 0;
        }
    }

    private void askForRatingAfterTheCounterHasBeenUsed() {
        if (showTeaViewModel.getOverallCounter() % 3 == 0) {
            askForRating();
        }
    }

    private void askForRating() {
        final ReviewManager reviewManager = ReviewManagerFactory.create(this);
        final Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                final ReviewInfo reviewInfo = task.getResult();

                final Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                });
            }
        });
    }

    private void showDialogChangeInfusion() {
        final int tmpSize = showTeaViewModel.getInfusionSize();
        final String[] items = new String[tmpSize];
        for (int i = 0; i < tmpSize; i++) {
            items[i] = getResources().getString(R.string.show_tea_dialog_infusion_count_description, i + 1);
        }

        //Get CheckedItem
        final int checkedItem = showTeaViewModel.getInfusionIndex();

        // Creating and Building the Dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
        builder.setIcon(R.drawable.infusion_black);
        builder.setTitle(R.string.show_tea_dialog_infusion_count_title);
        builder.setSingleChoiceItems(items, checkedItem, (dialog, item) -> {
            showTeaViewModel.setInfusionIndex(item);
            infusionIndexChanged();
            dialog.dismiss();
        });
        builder.setNegativeButton(R.string.show_tea_dialog_infusion_count_negative, null);
        builder.create().show();
    }

    private void displayNextInfusion() {
        showTeaViewModel.incrementInfusionIndex();
        infusionIndexChanged();
    }

    private void switchToCoolingPeriod() {
        if (!infoShown) {

            final TimeConverter cooldowntime = showTeaViewModel.getCoolDownTime();
            if (cooldowntime != null && cooldowntime.getTime() != null) {
                buttonInfo.setVisibility(View.VISIBLE);
                infoShown = true;

                spinnerMinutes.setSelection(cooldowntime.getMinutes());
                spinnerSeconds.setSelection(cooldowntime.getSeconds());
            } else {
                Toast.makeText(getApplication(), R.string.show_tea_cool_down_time_not_found, Toast.LENGTH_LONG).show();
            }
        } else {
            buttonInfo.setVisibility(View.INVISIBLE);
            infoShown = false;
            spinnerMinutes.setSelection(showTeaViewModel.getTime().getMinutes());
            spinnerSeconds.setSelection(showTeaViewModel.getTime().getSeconds());
        }
    }

    private void showDialogCoolingPeriod() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
        builder.setTitle(R.string.show_tea_cool_down_time_header);
        builder.setMessage(R.string.show_tea_cool_down_time_description);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void decideToShowDialogAmount() {
        if (showTeaViewModel.getAmount() == -500 || showTeaViewModel.getAmount() == 0) {
            Toast.makeText(getApplication(), R.string.show_tea_amount_not_found, Toast.LENGTH_LONG).show();
        } else {
            showDialogAmount();
        }
    }

    private void showDialogAmount() {
        final ViewGroup parent = findViewById(R.id.show_tea_parent);

        final LayoutInflater inflater = getLayoutInflater();
        final View alertLayoutDialogAmount = inflater.inflate(R.layout.dialog_amount, parent, false);
        final SeekBar seekBarAmountPerAmount = alertLayoutDialogAmount.findViewById(R.id.seek_bar_show_tea_amount_per_amount);
        final TextView textViewAmountPerAmount = alertLayoutDialogAmount.findViewById(R.id.text_view_show_tea_show_amount_per_amount);
        // 10 for 1 liter
        fillAmountPerAmount(10, textViewAmountPerAmount);

        seekBarAmountPerAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int value, final boolean b) {
                fillAmountPerAmount(value, textViewAmountPerAmount);
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
                // this functionality is not needed, but needs to be override
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                // this functionality is not needed, but needs to be override
            }
        });

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.dialog_theme);
        dialogBuilder.setView(alertLayoutDialogAmount);
        dialogBuilder.setTitle(R.string.show_tea_dialog_amount);
        dialogBuilder.setIcon(DisplayAmountKindFactory.get(showTeaViewModel.getAmountKind(), getApplication()).getImageResourceIdShowTea());
        dialogBuilder.setPositiveButton(R.string.show_tea_dialog_amount_ok, null);
        dialogBuilder.show();
    }

    private void fillAmountPerAmount(final int value, final TextView textViewAmountPerAmount) {
        final float liter = (float) value / 10;
        final float amountPerLiter = (float) showTeaViewModel.getAmount() * liter;

        final DisplayAmountKindStrategy displayAmountKindStrategy = DisplayAmountKindFactory.get(showTeaViewModel.getAmountKind(), getApplication());
        textViewAmountPerAmount.setText(displayAmountKindStrategy.getTextCalculatorShowTea(amountPerLiter, liter));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_tea, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_show_tea_edit) {
            return navigateToEditTea();
        } else if (id == R.id.action_show_tea_information) {
            navigateToDetailInformation();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean navigateToEditTea() {
        final Intent newTeaScreen = new Intent(ShowTea.this, NewTea.class);
        newTeaScreen.putExtra(EXTRA_TEA_ID, showTeaViewModel.getTeaId());
        newTeaScreen.putExtra("showTea", true);
        // Intent starten und zur zweiten Activity wechseln
        startActivity(newTeaScreen);
        finish();
        return true;
    }
}
