package coolpharaoh.tee.speicher.tea.timer.views.show_tea;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.main.Main;

class NotExistingTeaDialog {
    Activity activity;

    NotExistingTeaDialog(Activity activity) {
        this.activity = activity;
    }

    AlertDialog show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.show_tea_dialog_tea_missing_header);
        builder.setMessage(R.string.show_tea_dialog_tea_missing_description);
        builder.setPositiveButton(R.string.show_tea_dialog_tea_missing_to_main, (dialog, which) -> navigateToMain());
        builder.setCancelable(false);
        return builder.show();
    }

    private void navigateToMain() {
        Intent main = new Intent(activity, Main.class);
        activity.startActivity(main);
    }

}
