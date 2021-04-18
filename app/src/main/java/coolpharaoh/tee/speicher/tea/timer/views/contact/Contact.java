package coolpharaoh.tee.speicher.tea.timer.views.contact;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.pm.PackageInfoCompat;

import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class Contact extends AppCompatActivity {
    private static final String LOG_TAG = Contact.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        final Button buttonEmail = findViewById(R.id.buttonSendEmail);
        buttonEmail.setOnClickListener(v -> writeEmail());
    }

    private void defineToolbarAsActionbar() {
        final Toolbar toolbar = findViewById(R.id.tool_bar);
        final TextView mToolbarCustomTitle = findViewById(R.id.tool_bar_title);
        mToolbarCustomTitle.setText(R.string.contact_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void writeEmail() {
        try {
            final PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            final String versionName = packageInfo.versionName;
            final long longVersionCode = PackageInfoCompat.getLongVersionCode(packageInfo);
            final int versionCode = (int) longVersionCode;

            final Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", getResources().getString(R.string.contact_email_address), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.contact_email_subject));
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Release: " + versionCode
                    + "\nApp: " + versionName + "\n\n");
            startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.contact_email_chooser)));
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getApplication(), R.string.contact_email_open_failed, Toast.LENGTH_LONG).show();
            Log.e(LOG_TAG, "The package manager was not found while sending an email.");
        }
    }
}
