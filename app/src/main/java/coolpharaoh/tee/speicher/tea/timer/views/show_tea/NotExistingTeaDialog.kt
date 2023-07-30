package coolpharaoh.tee.speicher.tea.timer.views.show_tea

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.views.overview.Overview

internal class NotExistingTeaDialog(var activity: Activity) {

    fun show(): AlertDialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.show_tea_dialog_tea_missing_header)
        builder.setMessage(R.string.show_tea_dialog_tea_missing_description)
        builder.setPositiveButton(R.string.show_tea_dialog_tea_missing_to_overview) { _, _ -> navigateToOverview() }
        builder.setCancelable(false)
        return builder.show()
    }

    private fun navigateToOverview() {
        val overview = Intent(activity, Overview::class.java)
        activity.startActivity(overview)
    }
}