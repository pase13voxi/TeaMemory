package coolpharaoh.tee.speicher.tea.timer.views.overview

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.sdkVersion
import coolpharaoh.tee.speicher.tea.timer.core.tea.ColorConversation.discoverForegroundColor
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertStoredVarietyToText
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.RecyclerItemOverview
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController

class RandomChoiceDialog(private val overviewViewModel: OverviewViewModel, private val imageController: ImageController) :
    DialogFragment() {

    private var dialogView: View? = null
    private var randomChoiceItem: RecyclerItemOverview? = null

    override fun onCreateDialog(savedInstancesState: Bundle?): Dialog {
        val activity: Activity = requireActivity()
        val parent = activity.findViewById<ViewGroup>(R.id.overview_parent)
        val inflater = activity.layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_overview_random_choice, parent, false)

        refreshRandomChoice()

        val buttonRefreshRandomChoice = dialogView!!.findViewById<ImageButton>(R.id.button_random_choice_dialog_refresh)
        buttonRefreshRandomChoice.setOnClickListener { refreshRandomChoice() }

        val builder = AlertDialog.Builder(activity, R.style.dialog_theme)
            .setTitle(R.string.overview_dialog_random_choice_title)
            .setView(dialogView)
            .setNegativeButton(R.string.overview_dialog_random_choice_negative, null)

        if (randomChoiceItem != null) {
            builder.setPositiveButton(R.string.overview_dialog_random_choice_positive) { dialogInterface: DialogInterface?, i: Int -> navigateToRandomTea() }
        }

        return builder.create()
    }

    private fun refreshRandomChoice() {
        val randomTea = overviewViewModel.randomTeaInStock
        if (randomTea != null) {
            randomChoiceItem = generateRecyclerViewItem(randomTea)

            fillRandomChoice()
        } else {
            noRandomChoiceAvailable()
        }
    }

    private fun generateRecyclerViewItem(tea: Tea): RecyclerItemOverview {
        val variety = convertStoredVarietyToText(tea.variety, requireActivity().application)
        return RecyclerItemOverview(null, tea.id, tea.name, variety, tea.color, tea.inStock
        )
    }

    private fun fillRandomChoice() {
        val textViewTeaName = dialogView!!.findViewById<TextView>(R.id.text_view_random_choice_dialog_tea_name)
        textViewTeaName.text = randomChoiceItem!!.teaName

        val textViewTeaVariety = dialogView!!.findViewById<TextView>(R.id.text_view_random_choice_dialog_variety)
        textViewTeaVariety.text = randomChoiceItem!!.variety

        updateImage()
    }

    private fun updateImage() {
        val imageViewImage = dialogView!!.findViewById<ImageView>(R.id.image_view_random_tea_choice_image)
        Glide.with(dialogView!!.context).clear(imageViewImage)
        imageViewImage.tag = null

        val textViewImageText = dialogView!!.findViewById<TextView>(R.id.text_view_random_choice_dialog_image)

        var imageUri: Uri? = null
        if (sdkVersion >= VERSION_CODES.Q) {
            imageUri = imageController.getImageUriByTeaId(randomChoiceItem!!.teaId!!)
        }

        if (imageUri != null) {
            fillImage(imageViewImage, textViewImageText, imageUri)
        } else {
            fillImageText(imageViewImage, textViewImageText)
        }
    }

    private fun fillImage(imageViewImage: ImageView, textViewImageText: TextView, imageUri: Uri) {
        Glide.with(dialogView!!.context)
            .load(imageUri)
            .override(100, 100)
            .centerCrop()
            .into(imageViewImage)
        imageViewImage.tag = imageUri.toString()
        textViewImageText.visibility = View.INVISIBLE
    }

    private fun fillImageText(imageViewImage: ImageView, textViewImageText: TextView) {
        imageViewImage.setBackgroundColor(randomChoiceItem!!.color!!)
        textViewImageText.visibility = View.VISIBLE
        textViewImageText.text = randomChoiceItem!!.imageText
        textViewImageText.setTextColor(discoverForegroundColor(randomChoiceItem!!.color!!))
    }

    private fun noRandomChoiceAvailable() {
        val textViewNoTea = dialogView!!.findViewById<TextView>(R.id.text_view_random_choice_no_tea)
        textViewNoTea.visibility = View.VISIBLE

        val layoutTeaAvailable = dialogView!!.findViewById<RelativeLayout>(R.id.layout_random_choice_tea_available)
        layoutTeaAvailable.visibility = View.GONE

        val textViewHint = dialogView!!.findViewById<TextView>(R.id.text_view_random_choice_hint)
        textViewHint.visibility = View.GONE
    }

    private fun navigateToRandomTea() {
        val showTeaScreen = Intent(dialogView!!.context, ShowTea::class.java)
        showTeaScreen.putExtra("teaId", randomChoiceItem!!.teaId)
        startActivity(showTeaScreen)
    }

    companion object {
        const val TAG = "RandomChoiceDialog"
    }
}