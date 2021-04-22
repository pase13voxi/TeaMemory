package coolpharaoh.tee.speicher.tea.timer.views.show_tea;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.overview.Overview;

class NotExistingTeaDialog {
    Activity activity;

    NotExistingTeaDialog(Activity activity) {
        this.activity = activity;
    }

    AlertDialog show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.show_tea_dialog_tea_missing_header);
        builder.setMessage(R.string.show_tea_dialog_tea_missing_description);
        builder.setPositiveButton(R.string.show_tea_dialog_tea_missing_to_overview, (dialog, which) -> navigateToOverview());
        builder.setCancelable(false);
        return builder.show();
    }

    private void navigateToOverview() {
        Intent overview = new Intent(activity, Overview.class);
        activity.startActivity(overview);
    }

}
