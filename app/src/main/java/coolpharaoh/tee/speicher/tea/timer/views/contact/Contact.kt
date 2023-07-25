package coolpharaoh.tee.speicher.tea.timer.views.contact

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.pm.PackageInfoCompat
import coolpharaoh.tee.speicher.tea.timer.R
import java.util.Objects

class Contact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        defineToolbarAsActionbar()
        enableAndShowBackButton()

        val buttonEmail = findViewById<Button>(R.id.button_contact_send_email)
        buttonEmail.setOnClickListener { writeEmail() }
    }

    private fun defineToolbarAsActionbar() {
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        val mToolbarCustomTitle = findViewById<TextView>(R.id.tool_bar_title)
        mToolbarCustomTitle.setText(R.string.contact_heading)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.title = null
    }

    private fun enableAndShowBackButton() {
        Objects.requireNonNull(supportActionBar)?.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun writeEmail() {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionName = packageInfo.versionName
            val longVersionCode = PackageInfoCompat.getLongVersionCode(packageInfo)
            val versionCode = longVersionCode.toInt()

            val emailIntent = Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", resources.getString(R.string.contact_email_address), null))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.contact_email_subject))
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Release: " + versionCode
                    + "\nApp: " + versionName + "\n\n")
            startActivity(Intent.createChooser(emailIntent, resources.getString(R.string.contact_email_chooser)))
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(application, R.string.contact_email_open_failed, Toast.LENGTH_LONG).show()
            Log.e(LOG_TAG, "The package manager was not found while sending an email.")
        }
    }

    companion object {
        private val LOG_TAG = Contact::class.java.simpleName
    }
}