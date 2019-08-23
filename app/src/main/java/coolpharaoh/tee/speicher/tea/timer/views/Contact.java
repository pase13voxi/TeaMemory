package coolpharaoh.tee.speicher.tea.timer.views;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import coolpharaoh.tee.speicher.tea.timer.R;

public class Contact extends AppCompatActivity {

    private TextView mToolbarCustomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = findViewById(R.id.tool_bar);
        mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.contact_heading);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button buttonEmail = findViewById(R.id.buttonSendEmail);
        buttonEmail.setOnClickListener(v -> {
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String versionname = pInfo.versionName;
                int versioncode = pInfo.versionCode;

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getResources().getString(R.string.contact_email_address), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.contact_email_subject));
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Release: " + versioncode
                        +"\nApp: " + versionname + "\n\n");
                startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.contact_email_chooser)));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
