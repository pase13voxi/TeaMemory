package coolpharaoh.tee.speicher.tea.timer.views.show_tea

import android.Manifest
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.views.description.ShowTeaDescription
import coolpharaoh.tee.speicher.tea.timer.views.information.Information
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.NewTea
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer.SharedTimerPreferences
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer.TimerController
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind.DisplayAmountKindFactory
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit.DisplayTemperatureUnitFactory
import org.apache.commons.lang3.StringUtils
import java.util.Locale
import java.util.concurrent.TimeUnit

// This class has 9 Parent because of AppCompatActivity
class ShowTea : AppCompatActivity() {

    private var textViewInfusionIndex: TextView? = null
    private var buttonNextInfusion: ImageButton? = null
    private var textViewTemperature: TextView? = null
    private var spinnerMinutes: Spinner? = null
    private var spinnerSeconds: Spinner? = null
    private var textViewTimer: TextView? = null
    private var buttonTemperature: ImageButton? = null
    private var buttonInfo: ImageButton? = null
    private var imageViewFill: ImageView? = null
    private var imageViewSteam: ImageView? = null
    private var buttonStartTimer: Button? = null
    private var imageViewCup: ImageView? = null
    private var textViewDoublePoint: TextView? = null
    private var textViewSeconds: TextView? = null
    private var textViewMinutes: TextView? = null
    private var buttonInfusionIndex: ImageButton? = null
    private var textViewName: TextView? = null
    private var textViewVariety: TextView? = null
    private var textViewAmount: TextView? = null
    private var buttonCalculateAmount: ImageButton? = null

    private var showTeaViewModel: ShowTeaViewModel? = null
    private var infoShown = false

    //animation
    private var maxMilliSec: Long = 0
    private var percent = 0

    private var foregroundTimer: TimerController? = null

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            updateClock(intent)
        }

        private fun updateClock(intent: Intent) {
            if (intent.extras != null) {
                val millis = intent.getLongExtra("countdown", 0)
                val ready = intent.getBooleanExtra("ready", false)
                if (!infoShown && showTeaViewModel!!.isAnimation()) {
                    updateImage(millis)
                }
                if (ready) {
                    textViewTimer!!.setText(R.string.show_tea_tea_ready)
                    if (!infoShown && showTeaViewModel!!.isAnimation()) {
                        imageViewFill!!.setImageResource(R.drawable.cup_fill100pr)
                        imageViewSteam!!.visibility = View.VISIBLE
                    }
                } else {
                    val ms = String.format(
                        Locale.getDefault(), "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                    )
                    textViewTimer!!.text = ms
                }
            }
        }

        private fun updateImage(milliSec: Long) {
            val percentTmp = 100 - (milliSec.toFloat() / maxMilliSec.toFloat() * 100).toInt()
            if (percentTmp > percent) {
                percent = percentTmp
                val context = applicationContext
                val imageName = String.format("cup_fill%spr", percent)
                val imageId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
                imageViewFill!!.setImageResource(imageId)
                imageViewFill!!.tag = imageId
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_tea)
        defineToolbarAsActionbar()
        enableAndShowBackButton()

        declareViewElements()

        setFieldsTransparent()

        initializeSpinnerWithBigCharacters()

        initializeInformationWindow()

        foregroundTimer = TimerController(application, SharedTimerPreferences(application))

        buttonStartTimer = findViewById(R.id.button_show_tea_start_timer)
        buttonStartTimer!!.setOnClickListener { startOrResetTimer() }

        buttonInfusionIndex!!.setOnClickListener { showDialogChangeInfusion() }

        buttonNextInfusion!!.setOnClickListener { displayNextInfusion() }

        buttonTemperature!!.setOnClickListener { switchToCoolingPeriod() }

        buttonInfo!!.setOnClickListener { showDialogCoolingPeriod() }

        buttonCalculateAmount!!.setOnClickListener { decideToShowDialogAmount() }
    }

    private fun defineToolbarAsActionbar() {
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        val toolbarTitle = findViewById<TextView>(R.id.tool_bar_title)
        toolbarTitle.setText(R.string.show_tea_heading)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null

        toolbarTitle.setOnClickListener { navigateToDetailInformation() }
    }

    private fun navigateToDetailInformation() {
        val informationScreen = Intent(this@ShowTea, Information::class.java)
        informationScreen.putExtra(EXTRA_TEA_ID, showTeaViewModel!!.getTeaId())
        // Intent starten und zur zweiten Activity wechseln
        startActivity(informationScreen)
    }

    private fun enableAndShowBackButton() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun declareViewElements() {
        buttonInfusionIndex = findViewById(R.id.show_tea_tool_bar_infusion_index)
        textViewInfusionIndex = findViewById(R.id.show_tea_tool_bar_text_infusion_index)
        buttonNextInfusion = findViewById(R.id.show_tea_tool_bar_next_infusion)
        textViewName = findViewById(R.id.text_view_show_tea_name)
        textViewVariety = findViewById(R.id.text_view_show_tea_variety)
        textViewTemperature = findViewById(R.id.text_view_show_tea_temperature)
        buttonInfo = findViewById(R.id.button_show_tea_info)
        buttonTemperature = findViewById(R.id.button_show_tea_temperature)
        textViewAmount = findViewById(R.id.text_view_show_tea_amount)
        buttonCalculateAmount = findViewById(R.id.button_show_tea_calculate_amount)
        spinnerMinutes = findViewById(R.id.spinner_show_tea_minutes)
        spinnerSeconds = findViewById(R.id.spinner_show_tea_seconds)
        textViewMinutes = findViewById(R.id.text_view_show_tea_minutes)
        textViewSeconds = findViewById(R.id.text_view_show_tea_seconds)
        textViewDoublePoint = findViewById(R.id.text_view_show_tea_double_point)
        textViewTimer = findViewById(R.id.text_view_show_tea_timer)
        imageViewCup = findViewById(R.id.image_view_show_tea_cup)
        imageViewFill = findViewById(R.id.image_view_show_tea_fill)
        imageViewSteam = findViewById(R.id.image_view_show_tea_steam)
    }

    private fun setFieldsTransparent() {
        val alpha = 130
        textViewName!!.background.mutate().alpha = alpha
        textViewVariety!!.background.mutate().alpha = alpha
        textViewDoublePoint!!.background.mutate().alpha = alpha
        textViewTimer!!.background.mutate().alpha = alpha
    }

    private fun initializeSpinnerWithBigCharacters() {
        val spinnerTimeAdapter = ArrayAdapter.createFromResource(
            this, R.array.show_tea_items_timer, R.layout.spinner_item
        )
        spinnerTimeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerMinutes!!.adapter = spinnerTimeAdapter
        spinnerSeconds!!.adapter = spinnerTimeAdapter
    }

    private fun initializeInformationWindow() {
        val teaId = this.intent.getLongExtra(EXTRA_TEA_ID, 0)
        if (teaId == 0L) {
            Log.e(LOG_TAG, "The tea id was not set before navigate to this Activity.")
            disableCompleteActivity()
        } else {
            showTeaInformation(teaId)
        }
    }

    private fun disableCompleteActivity() {
        NotExistingTeaDialog(this).show()
    }

    private fun showTeaInformation(teaId: Long) {
        showTeaViewModel = ShowTeaViewModel(teaId, application)

        if (showTeaViewModel!!.teaExists()) {
            fillInformationFields()

            decideToShowInfusionBar()

            decideToDisplayContinueNextInfusionDialog()

            decideToDisplayDescription()
        } else {
            Log.e(LOG_TAG, "The tea for the given tea id does not exist.")
            disableCompleteActivity()
        }
    }

    private fun fillInformationFields() {
        textViewName!!.text = showTeaViewModel!!.name
        textViewVariety!!.text = showTeaViewModel!!.variety

        fillTemperatureWithUnit()

        fillAmountWithUnit()

        spinnerMinutes!!.setSelection(showTeaViewModel!!.time.minutes)
        spinnerSeconds!!.setSelection(showTeaViewModel!!.time.seconds)
    }

    private fun fillTemperatureWithUnit() {
        val displayTemperatureUnitStrategy = DisplayTemperatureUnitFactory[showTeaViewModel!!.getTemperatureUnit(), application]

        textViewTemperature!!.text = displayTemperatureUnitStrategy.getTextIdShowTea(showTeaViewModel!!.temperature)
    }

    private fun fillAmountWithUnit() {
        val imageButtonAmount = findViewById<ImageButton>(R.id.button_show_tea_calculate_amount)
        val displayAmountKindStrategy = DisplayAmountKindFactory[showTeaViewModel!!.amountKind, application]

        textViewAmount!!.text = StringUtils.rightPad(displayAmountKindStrategy.getTextShowTea(showTeaViewModel!!.amount), 10)
        imageButtonAmount.setImageResource(displayAmountKindStrategy.getImageResourceIdShowTea())
    }

    private fun decideToShowInfusionBar() {
        if (showTeaViewModel!!.getInfusionSize() == 1) {
            textViewInfusionIndex!!.visibility = View.GONE
            buttonInfusionIndex!!.visibility = View.GONE
            buttonNextInfusion!!.visibility = View.GONE
        }
    }

    private fun decideToDisplayDescription() {
        if (showTeaViewModel!!.isShowTeaAlert()) {
            dialogShowTeaDescription()
        }
    }

    private fun dialogShowTeaDescription() {
        val parent = findViewById<ViewGroup>(R.id.show_tea_parent)

        val inflater = layoutInflater
        val alertLayoutDialogDescription = inflater.inflate(R.layout.dialog_showtea_description, parent, false)
        val donNotShowAgain = alertLayoutDialogDescription.findViewById<CheckBox>(R.id.check_box_show_tea_dialog_description)

        AlertDialog.Builder(this, R.style.dialog_theme)
            .setView(alertLayoutDialogDescription)
            .setTitle(R.string.show_tea_dialog_description_header)
            .setNegativeButton(R.string.show_tea_dialog_description_cancel) { _, _ -> disableDescription(donNotShowAgain) }
            .setPositiveButton(R.string.show_tea_dialog_description_show) { _, _ -> navigateToShowTeaDescription(donNotShowAgain) }
            .show()
    }

    private fun disableDescription(donNotShowAgain: CheckBox) {
        if (donNotShowAgain.isChecked) {
            showTeaViewModel!!.setShowTeaAlert(false)
        }
    }

    private fun navigateToShowTeaDescription(donNotShowAgain: CheckBox) {
        disableDescription(donNotShowAgain)
        val intent = Intent(this@ShowTea, ShowTeaDescription::class.java)
        startActivity(intent)
    }

    private fun decideToDisplayContinueNextInfusionDialog() {
        if (showTeaViewModel!!.nextInfusion != 0 && showTeaViewModel!!.getInfusionSize() != 1) {
            displayContinueNextInfusionDialog()
        }
    }

    private fun displayContinueNextInfusionDialog() {
        val lastInfusion = showTeaViewModel!!.nextInfusion
        val nextInfusion = showTeaViewModel!!.nextInfusion + 1
        //Infomationen anzeigen
        val builder = AlertDialog.Builder(this, R.style.dialog_theme)
        builder.setTitle(R.string.show_tea_dialog_following_infusion_header)
        builder.setMessage(resources.getString(R.string.show_tea_dialog_following_infusion_description, lastInfusion, nextInfusion))
        builder.setPositiveButton(R.string.show_tea_dialog_following_infusion_yes) { _, _ -> continueNextInfusion() }
        builder.setNegativeButton(R.string.show_tea_dialog_following_infusion_no) { _, _ -> showTeaViewModel!!.resetNextInfusion() }
        builder.show()
    }

    private fun continueNextInfusion() {
        showTeaViewModel!!.infusionIndex = showTeaViewModel!!.nextInfusion
        infusionIndexChanged()
    }

    private fun infusionIndexChanged() {
        val displayTemperatureUnitStrategy = DisplayTemperatureUnitFactory[showTeaViewModel!!.getTemperatureUnit(), application]

        textViewTemperature!!.text = displayTemperatureUnitStrategy.getTextIdShowTea(showTeaViewModel!!.temperature)

        spinnerMinutes!!.setSelection(showTeaViewModel!!.time.minutes)
        spinnerSeconds!!.setSelection(showTeaViewModel!!.time.seconds)
        textViewInfusionIndex!!.text = resources.getString(R.string.show_tea_break_count_point, showTeaViewModel!!.infusionIndex + 1)

        nextInfusionEnable()

        buttonInfo!!.visibility = View.INVISIBLE
        infoShown = false
    }

    private fun nextInfusionEnable() {
        buttonNextInfusion!!.isEnabled = showTeaViewModel!!.infusionIndex != showTeaViewModel!!.getInfusionSize() - 1
    }

    private fun startOrResetTimer() {
        if (resources.getString(R.string.show_tea_timer_start).contentEquals(buttonStartTimer!!.text)) {
            startTimer()
        } else if (resources.getString(R.string.show_tea_timer_reset).contentEquals(buttonStartTimer!!.text)) {
            resetTimer()
        }
    }

    private fun startTimer() {
        askNotificationPermission()

        buttonStartTimer!!.setText(R.string.show_tea_timer_reset)

        collectDrinkingBehaviorInformation()

        disableInfusionBarAndCooldownSwitch()

        hideTimeInputAndVisualizeTimerDisplay()

        visualizeTeaCup()

        calculateInfusionTimeAndStartTimer()
    }

    private val requestPermissionLauncher = registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
        if (!isGranted) {
            Snackbar.make(findViewById(R.id.show_tea_parent), R.string.show_tea_snack_bar_notification_description, Snackbar.LENGTH_LONG)
                .setAction(R.string.show_tea_snack_bar_notification_button) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }.show()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(application, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun collectDrinkingBehaviorInformation() {
        if (!infoShown) showTeaViewModel!!.countCounter()
        showTeaViewModel!!.setCurrentDate()
        showTeaViewModel!!.updateNextInfusion()
    }

    private fun disableInfusionBarAndCooldownSwitch() {
        buttonTemperature!!.isEnabled = false
        buttonInfusionIndex!!.isEnabled = false
        buttonNextInfusion!!.isEnabled = false
    }

    private fun hideTimeInputAndVisualizeTimerDisplay() {
        setVisibilityTimeInput(View.INVISIBLE)
        textViewTimer!!.visibility = View.VISIBLE
    }

    private fun setVisibilityTimeInput(visibility: Int) {
        spinnerMinutes!!.visibility = visibility
        spinnerSeconds!!.visibility = visibility
        textViewMinutes!!.visibility = visibility
        textViewSeconds!!.visibility = visibility
        textViewDoublePoint!!.visibility = visibility
    }

    private fun visualizeTeaCup() {
        if (!infoShown && showTeaViewModel!!.isAnimation()) {
            imageViewCup!!.visibility = View.VISIBLE
            imageViewFill!!.visibility = View.VISIBLE
            //choose color of the cup content
            imageViewFill!!.setColorFilter(showTeaViewModel!!.color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun calculateInfusionTimeAndStartTimer() {
        val min = spinnerMinutes!!.selectedItem.toString().toInt()
        val sec = spinnerSeconds!!.selectedItem.toString().toInt()
        val millisec = TimeUnit.MINUTES.toMillis(min.toLong()) + TimeUnit.SECONDS.toMillis(sec.toLong())

        maxMilliSec = millisec

        foregroundTimer!!.startForegroundTimer(millisec, showTeaViewModel!!.getTeaId())
    }

    override fun onResume() {
        super.onResume()

        registerReceiver(broadcastReceiver, IntentFilter(TimerController.COUNTDOWN_BR))

        foregroundTimer!!.resumeForegroundTimer()
    }

    override fun onPause() {
        super.onPause()

        foregroundTimer!!.startBackgroundTimer()
    }

    override fun onDestroy() {
        foregroundTimer!!.reset()
        unregisterReceiver(broadcastReceiver)

        super.onDestroy()
    }

    private fun resetTimer() {
        buttonStartTimer!!.setText(R.string.show_tea_timer_start)

        enableInfusionBarAndCooldownSwitch()

        visualizeTimerDisplayAndHideTimeInput()

        hideAndResetTeaCup()

        foregroundTimer!!.reset()

        askForRatingAfterTheCounterHasBeenUsed()
    }

    private fun enableInfusionBarAndCooldownSwitch() {
        buttonTemperature!!.isEnabled = true
        buttonInfusionIndex!!.isEnabled = true
        nextInfusionEnable()
    }

    private fun visualizeTimerDisplayAndHideTimeInput() {
        setVisibilityTimeInput(View.VISIBLE)
        textViewTimer!!.visibility = View.INVISIBLE
    }

    private fun hideAndResetTeaCup() {
        if (!infoShown && showTeaViewModel!!.isAnimation()) {
            imageViewCup!!.visibility = View.INVISIBLE
            imageViewFill!!.visibility = View.INVISIBLE
            imageViewFill!!.setImageResource(R.drawable.cup_fill0pr)
            imageViewSteam!!.visibility = View.INVISIBLE
            //für animation zurücksetzen
            imageViewCup!!.setImageResource(R.drawable.cup)
            percent = 0
        }
    }

    private fun askForRatingAfterTheCounterHasBeenUsed() {
        if (showTeaViewModel!!.getOverallCounter() % 3 == 0L) {
            askForRating()
        }
    }

    private fun askForRating() {
        val reviewManager = ReviewManagerFactory.create(this)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task: Task<ReviewInfo?> ->
            if (task.isSuccessful) {
                val reviewInfo = task.result

                val flow = reviewManager.launchReviewFlow(this, reviewInfo!!)
                flow.addOnCompleteListener { }
            }
        }
    }

    private fun showDialogChangeInfusion() {
        val tmpSize = showTeaViewModel!!.getInfusionSize()
        val items = arrayOfNulls<String>(tmpSize)
        for (i in 0 until tmpSize) {
            items[i] = resources.getString(R.string.show_tea_dialog_infusion_count_description, i + 1)
        }

        //Get CheckedItem
        val checkedItem = showTeaViewModel!!.infusionIndex

        // Creating and Building the Dialog
        val builder = AlertDialog.Builder(this, R.style.dialog_theme)
        builder.setIcon(R.drawable.infusion_black)
        builder.setTitle(R.string.show_tea_dialog_infusion_count_title)
        builder.setSingleChoiceItems(items, checkedItem) { dialog: DialogInterface, item: Int ->
            showTeaViewModel!!.infusionIndex = item
            infusionIndexChanged()
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.show_tea_dialog_infusion_count_negative, null)
        builder.create().show()
    }

    private fun displayNextInfusion() {
        showTeaViewModel!!.incrementInfusionIndex()
        infusionIndexChanged()
    }

    private fun switchToCoolingPeriod() {
        if (!infoShown) {
            val cooldowntime = showTeaViewModel!!.coolDownTime
            if (cooldowntime.time != null) {
                buttonInfo!!.visibility = View.VISIBLE
                infoShown = true
                spinnerMinutes!!.setSelection(cooldowntime.minutes)
                spinnerSeconds!!.setSelection(cooldowntime.seconds)
            } else {
                Toast.makeText(application, R.string.show_tea_cool_down_time_not_found, Toast.LENGTH_LONG).show()
            }
        } else {
            buttonInfo!!.visibility = View.INVISIBLE
            infoShown = false
            spinnerMinutes!!.setSelection(showTeaViewModel!!.time.minutes)
            spinnerSeconds!!.setSelection(showTeaViewModel!!.time.seconds)
        }
    }

    private fun showDialogCoolingPeriod() {
        val builder = AlertDialog.Builder(this, R.style.dialog_theme)
        builder.setTitle(R.string.show_tea_cool_down_time_header)
        builder.setMessage(R.string.show_tea_cool_down_time_description)
        builder.setPositiveButton("OK", null)
        builder.show()
    }

    private fun decideToShowDialogAmount() {
        if (showTeaViewModel!!.amount == -500.0 || showTeaViewModel!!.amount == 0.0) {
            Toast.makeText(application, R.string.show_tea_amount_not_found, Toast.LENGTH_LONG).show()
        } else {
            showDialogAmount()
        }
    }

    private fun showDialogAmount() {
        val parent = findViewById<ViewGroup>(R.id.show_tea_parent)

        val inflater = layoutInflater
        val alertLayoutDialogAmount = inflater.inflate(R.layout.dialog_amount, parent, false)
        val seekBarAmountPerAmount = alertLayoutDialogAmount.findViewById<SeekBar>(R.id.seek_bar_show_tea_amount_per_amount)
        val textViewAmountPerAmount = alertLayoutDialogAmount.findViewById<TextView>(R.id.text_view_show_tea_show_amount_per_amount)
        // 10 for 1 liter
        fillAmountPerAmount(10, textViewAmountPerAmount)

        seekBarAmountPerAmount.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, value: Int, b: Boolean) {
                fillAmountPerAmount(value, textViewAmountPerAmount)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // this functionality is not needed, but needs to be override
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // this functionality is not needed, but needs to be override
            }
        })

        val dialogBuilder = AlertDialog.Builder(this, R.style.dialog_theme)
        dialogBuilder.setView(alertLayoutDialogAmount)
        dialogBuilder.setTitle(R.string.show_tea_dialog_amount)
        dialogBuilder.setIcon(DisplayAmountKindFactory[showTeaViewModel!!.amountKind, application].getImageResourceIdShowTea())
        dialogBuilder.setPositiveButton(R.string.show_tea_dialog_amount_ok, null)
        dialogBuilder.show()
    }

    private fun fillAmountPerAmount(value: Int, textViewAmountPerAmount: TextView) {
        val liter = value.toFloat() / 10
        val amountPerLiter = showTeaViewModel!!.amount.toFloat() * liter

        val displayAmountKindStrategy = DisplayAmountKindFactory[showTeaViewModel!!.amountKind, application]
        textViewAmountPerAmount.text = displayAmountKindStrategy.getTextCalculatorShowTea(amountPerLiter, liter)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_show_tea, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_show_tea_edit) {
            return navigateToEditTea()
        } else if (id == R.id.action_show_tea_information) {
            navigateToDetailInformation()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun navigateToEditTea(): Boolean {
        val newTeaScreen = Intent(this@ShowTea, NewTea::class.java)
        newTeaScreen.putExtra(EXTRA_TEA_ID, showTeaViewModel!!.getTeaId())
        newTeaScreen.putExtra("showTea", true)
        startActivity(newTeaScreen)
        finish()
        return true
    }

    companion object {
        private val LOG_TAG = ShowTea::class.java.simpleName
        const val EXTRA_TEA_ID = "teaId"
    }
}