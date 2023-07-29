package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions.SuggestionsFactory.getSuggestions
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind.DisplayAmountKindFactory
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit.DisplayTemperatureUnitFactory
import java.util.Objects

// This class has 9 Parent because of AppCompatActivity
class NewTea : AppCompatActivity(), Printer {

    private var variety: Variety? = null
    private var buttonColorShape: ButtonColorShape? = null

    private var newTeaViewModel: NewTeaViewModel? = null
    private var inputValidator: InputValidator? = null
    private var teaId: Long = 0
    private var showTea = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_tea)
        hideKeyboardAtFirst()
        defineToolbarAsActionbar()
        enableAndShowBackButton()

        defineColorPicker()

        defineInfusionBar()

        defineInputPicker()

        initializeNewOrEditTea()

        newTeaViewModel!!.dataChanges()!!.observe(this) { onDataChanged() }

        //showTea wird übergeben, falls die Navigation von showTea erfolgt
        showTea = this.intent.getBooleanExtra("showTea", false)
        inputValidator = InputValidator(application, this)
    }

    private fun hideKeyboardAtFirst() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun defineToolbarAsActionbar() {
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        val mToolbarCustomTitle = findViewById<TextView>(R.id.tool_bar_title)
        mToolbarCustomTitle.setText(R.string.new_tea_heading)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.title = null
    }

    private fun enableAndShowBackButton() {
        Objects.requireNonNull(supportActionBar)?.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun defineColorPicker() {
        val buttonColor = findViewById<Button>(R.id.button_new_tea_color)
        buttonColorShape = ButtonColorShape(buttonColor.background, application)
        buttonColor.setOnClickListener { createColorPicker() }
    }

    private fun createColorPicker() {
        ColorPickerDialogBuilder
            .with(this)
            .setTitle(R.string.new_tea_color_dialog_title)
            .initialColor(buttonColorShape!!.color)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .lightnessSliderOnly()
            .density(12)
            .setPositiveButton(R.string.new_tea_color_dialog_positive) { _, selectedColor, _ -> newTeaViewModel!!.color = selectedColor }
            .setNegativeButton(R.string.new_tea_color_dialog_negative, null)
            .build()
            .show()
    }

    private fun defineInfusionBar() {
        val buttonPreviousInfusion = findViewById<ImageButton>(R.id.button_new_tea_previous_infusion)
        buttonPreviousInfusion.setOnClickListener { newTeaViewModel!!.previousInfusion() }

        val buttonNextInfusion = findViewById<ImageButton>(R.id.button_new_tea_next_infusion)
        buttonNextInfusion.setOnClickListener { newTeaViewModel!!.nextInfusion() }

        val buttonDeleteInfusion = findViewById<ImageButton>(R.id.button_new_tea_delete_infusion)
        buttonDeleteInfusion.setOnClickListener { newTeaViewModel!!.deleteInfusion() }

        val buttonAddInfusion = findViewById<ImageButton>(R.id.button_new_tea_add_infusion)
        buttonAddInfusion.setOnClickListener { newTeaViewModel!!.addInfusion() }
    }

    private fun defineInputPicker() {
        val editTextVarietyDialog = findViewById<EditText>(R.id.edit_text_new_tea_variety)
        editTextVarietyDialog.setOnClickListener { dialogVarietyPicker() }

        val editTextAmountDialog = findViewById<EditText>(R.id.edit_text_new_tea_amount)
        editTextAmountDialog.setOnClickListener { dialogAmountPicker() }

        val editTextTemperatureDialog = findViewById<EditText>(R.id.edit_text_new_tea_temperature)
        editTextTemperatureDialog.setOnClickListener { dialogTemperaturePicker() }

        val editTextCoolDownTimeDialog = findViewById<EditText>(R.id.edit_text_new_tea_cool_down_time)
        editTextCoolDownTimeDialog.setOnClickListener { dialogCoolDownTimePicker() }

        val editTextTimeDialog = findViewById<EditText>(R.id.edit_text_new_tea_time)
        editTextTimeDialog.setOnClickListener { dialogTimePicker() }
    }

    private fun dialogVarietyPicker() {
        VarietyPickerDialog(newTeaViewModel!!)
            .show(supportFragmentManager, VarietyPickerDialog.TAG)
    }

    private fun dialogAmountPicker() {
        val hints = getSuggestions(variety!!.ordinal, application)
        AmountPickerDialog(hints, newTeaViewModel!!)
            .show(supportFragmentManager, AmountPickerDialog.TAG)
    }

    private fun dialogTemperaturePicker() {
        val hints = getSuggestions(variety!!.ordinal, application)
        TemperaturePickerDialog(hints, newTeaViewModel!!)
            .show(supportFragmentManager, TemperaturePickerDialog.TAG)
    }

    private fun dialogCoolDownTimePicker() {
        CoolDownTimePickerDialog(newTeaViewModel!!)
            .show(supportFragmentManager, CoolDownTimePickerDialog.TAG)
    }

    private fun dialogTimePicker() {
        val hints = getSuggestions(variety!!.ordinal, application)
        TimePickerDialog(hints, newTeaViewModel!!)
            .show(supportFragmentManager, TimePickerDialog.TAG)
    }

    private fun initializeNewOrEditTea() {
        teaId = this.intent.getLongExtra("teaId", 0)
        if (teaId == 0L) {
            initializeNewTea()
        } else {
            initializeEditTea()
        }
    }

    private fun initializeNewTea() {
        newTeaViewModel = NewTeaViewModel(application)
    }

    private fun initializeEditTea() {
        newTeaViewModel = NewTeaViewModel(teaId, application)

        val editTextName = findViewById<EditText>(R.id.edit_text_new_tea_name)
        editTextName.setText(newTeaViewModel!!.name)
    }

    private fun onDataChanged() {
        bindVarietyToInputField()
        bindColorToInputField()
        bindAmountToInputField()
        bindTemperatureToInputField()
        bindCoolDownTimeToInputField()
        showCoolDownTimeInput()
        bindTimeToInputField()
        refreshInfusionBar()
    }

    private fun bindVarietyToInputField() {
        variety = newTeaViewModel!!.variety

        val varietyText = newTeaViewModel!!.varietyAsText
        val editTextVariety = findViewById<EditText>(R.id.edit_text_new_tea_variety)

        editTextVariety.setText(varietyText)
    }

    private fun bindColorToInputField() {
        buttonColorShape!!.color = newTeaViewModel!!.color
    }

    private fun bindAmountToInputField() {
        val editTextAmount = findViewById<EditText>(R.id.edit_text_new_tea_amount)

        val amountKind = newTeaViewModel!!.amountKind
        val displayAmountKindStrategy = DisplayAmountKindFactory[amountKind, application]

        val amount = newTeaViewModel!!.amount
        editTextAmount.setText(displayAmountKindStrategy.getTextNewTea(amount))
    }

    private fun bindTemperatureToInputField() {
        val temperature = newTeaViewModel!!.getInfusionTemperature()
        val temperatureUnit = newTeaViewModel!!.getTemperatureUnit()
        val displayTemperatureUnitStrategy = DisplayTemperatureUnitFactory[temperatureUnit, application]

        val editTextTemperature = findViewById<EditText>(R.id.edit_text_new_tea_temperature)

        editTextTemperature.setText(displayTemperatureUnitStrategy.getTextNewTea(temperature))
    }

    private fun bindCoolDownTimeToInputField() {
        val coolDownTime = newTeaViewModel!!.getInfusionCoolDownTime()
        val editTextCoolDownTime = findViewById<EditText>(R.id.edit_text_new_tea_cool_down_time)
        if (coolDownTime == null) {
            editTextCoolDownTime.setText(R.string.new_tea_edit_text_cool_down_time_empty_text)
        } else {
            editTextCoolDownTime.setText(getString(R.string.new_tea_edit_text_cool_down_time_text, coolDownTime))
        }
    }

    private fun showCoolDownTimeInput() {
        val temperature = newTeaViewModel!!.getInfusionTemperature()
        val temperatureUnit = newTeaViewModel!!.getTemperatureUnit()
        val layoutCoolDownTime = findViewById<LinearLayout>(R.id.layout_new_tea_cool_down_time)

        if (temperature == -500 || temperature == 100 && TemperatureUnit.CELSIUS == temperatureUnit || temperature == 212 && TemperatureUnit.FAHRENHEIT == temperatureUnit) {
            layoutCoolDownTime.visibility = View.GONE
            newTeaViewModel!!.resetInfusionCoolDownTime()
        } else {
            layoutCoolDownTime.visibility = View.VISIBLE
        }
    }

    private fun bindTimeToInputField() {
        val time = newTeaViewModel!!.getInfusionTime()
        val editTextTime = findViewById<EditText>(R.id.edit_text_new_tea_time)
        if (time == null) {
            editTextTime.setText(R.string.new_tea_edit_text_time_empty_text)
        } else {
            editTextTime.setText(getString(R.string.new_tea_edit_text_time_text, time))
        }
    }

    private fun refreshInfusionBar() {
        val textViewInfusion = findViewById<TextView>(R.id.text_view_new_tea_count_infusion)
        textViewInfusion.text = resources.getString(R.string.new_tea_count_infusion, newTeaViewModel!!.getInfusionIndex() + 1)

        showDeleteInfusion()
        showAddInfusion()
        showPreviousInfusion()
        showNextInfusion()
    }

    private fun showDeleteInfusion() {
        if (newTeaViewModel!!.getInfusionSize() > 1) {
            findViewById<View>(R.id.button_new_tea_delete_infusion).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.button_new_tea_delete_infusion).visibility = View.GONE
        }
    }

    private fun showAddInfusion() {
        if (newTeaViewModel!!.getInfusionIndex() + 1 == newTeaViewModel!!.getInfusionSize() && newTeaViewModel!!.getInfusionSize() < 20) {
            findViewById<View>(R.id.button_new_tea_add_infusion).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.button_new_tea_add_infusion).visibility = View.GONE
        }
    }

    private fun showPreviousInfusion() {
        val enabled = newTeaViewModel!!.getInfusionIndex() != 0
        findViewById<View>(R.id.button_new_tea_previous_infusion).isEnabled = enabled
    }

    private fun showNextInfusion() {
        val enabled = newTeaViewModel!!.getInfusionIndex() + 1 != newTeaViewModel!!.getInfusionSize()
        findViewById<View>(R.id.button_new_tea_next_infusion).isEnabled = enabled
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_new_tea, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_new_tea_done) {
            addTea()
        } else if (id == android.R.id.home) {
            if (showTea) {
                navigateToShowTeaActivity()
                return true
            } else {
                NavUtils.navigateUpFromSameTask(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addTea() {
        val editTextName = findViewById<EditText>(R.id.edit_text_new_tea_name)

        val nameInput = editTextName.text.toString()

        if (inputIsValid(nameInput)) {
            createOrEditTea(nameInput)
            navigateToMainOrShowTea()
        }
    }

    private fun inputIsValid(nameInput: String): Boolean {
        return (inputValidator!!.nameIsNotEmpty(nameInput)
                && inputValidator!!.nameIsValid(nameInput))
    }

    private fun createOrEditTea(name: String) {
        newTeaViewModel!!.saveTea(name)
    }

    private fun navigateToMainOrShowTea() {
        if (!showTea) {
            finish()
        } else {
            navigateToShowTeaActivity()
        }
    }

    private fun navigateToShowTeaActivity() {
        val showTeaScreen = Intent(this@NewTea, ShowTea::class.java)
        showTeaScreen.putExtra("teaId", newTeaViewModel!!.teaId)
        startActivity(showTeaScreen)
        finish()
    }

    override fun print(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}