package coolpharaoh.tee.speicher.tea.timer.views.information

import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.note.Note
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.sdkVersion
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageControllerFactory.getImageController
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerItem
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Objects

// This class has 9 Parent because of AppCompatActivity
class Information : AppCompatActivity(), DetailRecyclerViewAdapter.OnClickListener {

    private lateinit var informationViewModel: InformationViewModel
    private lateinit var imageController: ImageController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        defineToolbarAsActionbar()
        enableAndShowBackButton()

        imageController = getImageController(this)

        val teaId = this.intent.getLongExtra(TEA_ID_EXTRA, 0)
        informationViewModel = InformationViewModel(teaId, application)

        fillInformationView()
        defineButtons()
    }

    private fun fillInformationView() {
        fillTexViewTeaName()
        fillTexViewTeaVariety()
        fillImage()
        fillRatingBar()
        showDetailsList()
        fillLastUsed()
        fillCounter()
        fillNotes()
    }

    private fun defineToolbarAsActionbar() {
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
    }

    private fun enableAndShowBackButton() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun fillTexViewTeaName() {
        val texViewTeaName = findViewById<TextView>(R.id.text_view_information_tea_name)
        texViewTeaName.setTextColor(ContextCompat.getColor(this, R.color.text_black))
        texViewTeaName.text = informationViewModel.teaName
    }

    private fun fillTexViewTeaVariety() {
        val texViewTeaVariety = findViewById<TextView>(R.id.text_view_information_variety)
        texViewTeaVariety.text = informationViewModel.varietyAsText
    }

    private fun fillImage() {
        if (sdkVersion >= VERSION_CODES.Q) {
            val uri = imageController.getImageUriByTeaId(informationViewModel.teaId)
            uri?.let { showImage(it) }
        }
    }

    private fun showImage(uri: Uri) {
        val imageViewImage = findViewById<ImageView>(R.id.image_view_information_image)
        imageViewImage.setImageURI(null)
        imageViewImage.setImageURI(uri)
        imageViewImage.tag = uri.toString()

        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

        val texViewTeaName = findViewById<TextView>(R.id.text_view_information_tea_name)
        texViewTeaName.setTextColor(ContextCompat.getColor(this, R.color.text_white))
    }

    private fun fillRatingBar() {
        val ratingBar = findViewById<RatingBar>(R.id.rating_bar_information)
        ratingBar.rating = informationViewModel.teaRating.toFloat()
    }

    private fun showDetailsList() {
        val recyclerViewDetails = findViewById<RecyclerView>(R.id.recycler_view_information_details)
        recyclerViewDetails.addItemDecoration(DividerItemDecoration(recyclerViewDetails.context,
            DividerItemDecoration.VERTICAL))

        informationViewModel.getDetails().observe(this) { details: List<Note> ->
            val detailsList: MutableList<RecyclerItem> = ArrayList()
            for ((_, _, _, header, description) in details) {
                detailsList.add(RecyclerItem(header!!, description!!))
            }
            val adapter = DetailRecyclerViewAdapter(detailsList, this)
            recyclerViewDetails.adapter = adapter
            recyclerViewDetails.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun fillLastUsed() {
        val textViewLastUsed = findViewById<TextView>(R.id.text_view_information_last_used)
        val formatter = SimpleDateFormat(DATE_FORMAT)
        val date = formatter.format(informationViewModel.date!!)
        textViewLastUsed.text = getString(R.string.information_counter_last_used, date)
    }

    private fun fillCounter() {
        val textViewWeek = findViewById<TextView>(R.id.text_view_information_counter_week)
        val textViewMonth = findViewById<TextView>(R.id.text_view_information_counter_month)
        val textViewYear = findViewById<TextView>(R.id.text_view_information_counter_year)
        val textViewOverall = findViewById<TextView>(R.id.text_view_information_counter_overall)

        val (_, _, week, month, year, overall) = informationViewModel.counter
        textViewWeek.text = week.toString()
        textViewMonth.text = month.toString()
        textViewYear.text = year.toString()
        textViewOverall.text = overall.toString()
    }

    private fun fillNotes() {
        val editTextNotes = findViewById<EditText>(R.id.edit_text_information_notes)
        val (_, _, _, _, description) = informationViewModel.notes
        editTextNotes.setText(description)
    }

    private fun defineButtons() {
        val ratingBar = findViewById<RatingBar>(R.id.rating_bar_information)
        ratingBar.onRatingBarChangeListener = OnRatingBarChangeListener { ratingBar1: RatingBar?, rating: Float, b: Boolean -> updateTeaRating(rating) }

        val buttonAddDetail = findViewById<ImageButton>(R.id.button_information_add_detail)
        buttonAddDetail.setOnClickListener { v: View? -> addDetail() }

        val buttonCamera = findViewById<FloatingActionButton>(R.id.button_information_camera)
        if (sdkVersion >= VERSION_CODES.Q) {
            buttonCamera.setOnClickListener { v: View? -> makeImage() }
        } else {
            buttonCamera.visibility = View.GONE
        }
    }

    private fun updateTeaRating(rating: Float) {
        informationViewModel.updateTeaRating(rating.toInt())
    }

    private fun addDetail() {
        val parent = findViewById<ViewGroup>(R.id.information_parent)

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_edit_information, parent, false)

        val editTextHeading = dialogLayout.findViewById<EditText>(R.id.edit_text_information_dialog_add_edit_header)
        val editTextDescription = dialogLayout.findViewById<EditText>(R.id.edit_text_information_dialog_add_edit_description)

        AlertDialog.Builder(this, R.style.dialog_theme)
            .setTitle(R.string.information_add_detail_dialog_heading)
            .setView(dialogLayout)
            .setNegativeButton(R.string.information_edit_detail_dialog_negative, null)
            .setPositiveButton(R.string.information_edit_detail_dialog_positive) { dialogInterface: DialogInterface?, i: Int ->
                storeDetail(editTextHeading, editTextDescription) }
            .show()
    }

    private fun storeDetail(editTextHeading: EditText, editTextDescription: EditText) {
        val heading = editTextHeading.text.toString()
        val description = editTextDescription.text.toString()
        if (heading.trim { it <= ' ' }.isNotEmpty() && description.trim { it <= ' ' }.isNotEmpty()) {
            informationViewModel.addDetail(editTextHeading.text.toString(),
                editTextDescription.text.toString())
        }
    }

    private val takePictureActivityResultLauncher =
        registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                fillImage()
            } else {
                Log.e(LOG_TAG, "Photo could not be taken.")
                Toast.makeText(this, "Photo could not be taken.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun makeImage() {
        if (sdkVersion >= VERSION_CODES.Q) {
            try {
                val takePictureIntent = imageController.getSaveOrUpdateImageIntent(informationViewModel.teaId)
                takePictureActivityResultLauncher.launch(takePictureIntent)
            } catch (exception: IOException) {
                Log.e(LOG_TAG, "Something went wrong while open photo application. Error message: " + exception.message)
                Toast.makeText(this, "Something went wrong while open photo application.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRecyclerItemClick(buttonOptions: Button, position: Int) {
        val popup = PopupMenu(application, buttonOptions)
        popup.inflate(R.menu.menu_information_details)
        popup.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.action_information_details_edit) {
                editDetail(position)
                return@setOnMenuItemClickListener true
            } else if (item.itemId == R.id.action_information_details_delete) {
                informationViewModel.deleteDetail(position)
                return@setOnMenuItemClickListener true
            }
            false
        }
        popup.show()
    }

    private fun editDetail(position: Int) {
        val parent = findViewById<ViewGroup>(R.id.information_parent)

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_edit_information, parent, false)

        val (_, _, _, header, description) = informationViewModel.getDetail(position)

        val editTextHeading = dialogLayout.findViewById<EditText>(R.id.edit_text_information_dialog_add_edit_header)
        editTextHeading.setText(header)
        val editTextDescription = dialogLayout.findViewById<EditText>(R.id.edit_text_information_dialog_add_edit_description)
        editTextDescription.setText(description)

        AlertDialog.Builder(this, R.style.dialog_theme)
            .setTitle(R.string.information_edit_detail_dialog_heading)
            .setView(dialogLayout)
            .setNegativeButton(R.string.information_edit_detail_dialog_negative, null)
            .setPositiveButton(R.string.information_edit_detail_dialog_positive) { dialogInterface: DialogInterface?, i: Int ->
                informationViewModel.updateDetail(position, editTextHeading.text.toString(),
                    editTextDescription.text.toString()) }
            .show()
    }

    override fun onPause() {
        super.onPause()

        val editTextNotes = findViewById<EditText>(R.id.edit_text_information_notes)
        informationViewModel.updateNotes(editTextNotes.text.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_information, menu)

        fillInStock(menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun fillInStock(menu: Menu) {
        val item = menu.findItem(R.id.action_information_in_stock)
        if (informationViewModel.isInStock) {
            item.setIcon(R.drawable.home_white)
        } else {
            item.setIcon(R.drawable.home_white_empty)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_information_in_stock) {
            if (informationViewModel.isInStock) {
                val homeIconEmpty = ContextCompat.getDrawable(application, R.drawable.home_white_empty)
                item.icon = homeIconEmpty
                informationViewModel.updateTeaInStock(false)
            } else {
                val homeIcon = ContextCompat.getDrawable(application, R.drawable.home_white)
                item.icon = homeIcon
                informationViewModel.updateTeaInStock(true)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val LOG_TAG = Information::class.java.simpleName
        private const val DATE_FORMAT = "dd MMMM yyyy"
        private const val TEA_ID_EXTRA = "teaId"
    }
}