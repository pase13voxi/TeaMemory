package coolpharaoh.tee.speicher.tea.timer.views.export_import

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.setFixedSystem
import coolpharaoh.tee.speicher.tea.timer.core.system.SystemUtility
import coolpharaoh.tee.speicher.tea.timer.views.export_import.JsonIOAdapter.setMockedTransformer
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIOAdapter
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIOAdapterFactory.setMockedDataIO
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.DatabaseJsonTransformer
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowAlertDialog

@RunWith(RobolectricTestRunner::class)
class ExportImportTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    lateinit var databaseJsonTransformer: DatabaseJsonTransformer
    @RelaxedMockK
    lateinit var dataIOAdapter: DataIOAdapter
    @MockK
    lateinit var systemUtility: SystemUtility

    @Before
    fun setUp() {
        setMockedDataIO(dataIOAdapter)
        setMockedTransformer(databaseJsonTransformer)
        mockAndroidSdkVersion()
    }

    private fun mockAndroidSdkVersion() {
        setFixedSystem(systemUtility)
        every { systemUtility.sdkVersion } returns Build.VERSION_CODES.R
    }

    @After
    fun tearDown() {
        setMockedTransformer(null)
    }

    @Test
    fun exportTeasAndExpectIntentActionOpenDocumentTree() {
        val exportImportActivityScenario = ActivityScenario.launch(ExportImport::class.java)
        exportImportActivityScenario.onActivity { exportImport: ExportImport ->
            val buttonExport = exportImport.findViewById<Button>(R.id.button_export_import_export)
            buttonExport.performClick()

            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity
            assertThat(actual.action).isEqualTo(Intent.ACTION_OPEN_DOCUMENT_TREE)
        }
    }

    @Test
    fun exportTeasAndExpectDialogFileLocation() {
        every { databaseJsonTransformer.databaseToJson() } returns "json"
        every { dataIOAdapter.write(any()) } returns true

        val exportImportActivityScenario = ActivityScenario.launch(ExportImport::class.java)
        exportImportActivityScenario.onActivity { exportImport: ExportImport ->
            val buttonExport = exportImport.findViewById<Button>(R.id.button_export_import_export)
            buttonExport.performClick()

            mockReturnActionActivityResult(exportImport, Intent.ACTION_OPEN_DOCUMENT_TREE)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            assertThat(shadowAlertDialog.title).isEqualTo(exportImport.getString(R.string.export_import_location_dialog_header))
            assertThat(shadowAlertDialog.message).isEqualTo(exportImport.getString(R.string.export_import_location_dialog_description))
        }
    }

    @Test
    fun exportTeasFailedAndExpectDialogExportFailed() {
        every { databaseJsonTransformer.databaseToJson() } returns "json"
        every { dataIOAdapter.write(any()) } returns false

        val exportImportActivityScenario = ActivityScenario.launch(ExportImport::class.java)
        exportImportActivityScenario.onActivity { exportImport: ExportImport ->
            val buttonExport = exportImport.findViewById<Button>(R.id.button_export_import_export)
            buttonExport.performClick()

            mockReturnActionActivityResult(exportImport, Intent.ACTION_OPEN_DOCUMENT_TREE)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            assertThat(shadowAlertDialog.title).isEqualTo(exportImport.getString(R.string.export_import_export_failed_dialog_header))
            assertThat(shadowAlertDialog.message).isEqualTo(exportImport.getString(R.string.export_import_export_failed_dialog_description))
        }
    }

    @Test
    fun importTeasExpectImportDialog() {
        val exportImportActivityScenario = ActivityScenario.launch(ExportImport::class.java)
        exportImportActivityScenario.onActivity { exportImport: ExportImport ->
            val buttonImport = exportImport.findViewById<Button>(R.id.button_export_import_import)
            buttonImport.performClick()

            val alertDialog = ShadowAlertDialog.getLatestAlertDialog()
            val shadowAlertDialog = Shadows.shadowOf(alertDialog)
            assertThat(shadowAlertDialog.title).isEqualTo(exportImport.getString(R.string.export_import_import_dialog_header))
        }
    }

    @Test
    fun importTeasDeleteExistingAndExpectDialogImportComplete() {
        every { databaseJsonTransformer.jsonToDatabase(any(), eq(false)) } returns true

        val exportImportActivityScenario = ActivityScenario.launch(ExportImport::class.java)
        exportImportActivityScenario.onActivity { exportImport: ExportImport ->
            val buttonImport = exportImport.findViewById<Button>(R.id.button_export_import_import)
            buttonImport.performClick()

            val alertDialog = ShadowAlertDialog.getLatestAlertDialog()
            alertDialog.findViewById<View>(R.id.button_export_import_import_delete).performClick()

            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity
            assertThat(actual.action).isEqualTo(Intent.ACTION_CHOOSER)

            mockReturnActionActivityResult(exportImport, Intent.ACTION_CHOOSER)

            val shadowAlertDialogImportComplete = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            assertThat(shadowAlertDialogImportComplete.title).isEqualTo(exportImport.getString(R.string.export_import_import_complete_dialog_header))
            assertThat(shadowAlertDialogImportComplete.message).isEqualTo(exportImport.getString(R.string.export_import_import_complete_delete_dialog_description))
        }
    }

    @Test
    fun importTeasKeepExistingAndExpectDialogImportComplete() {
        every { databaseJsonTransformer.jsonToDatabase(any(), eq(true)) } returns true

        val exportImportActivityScenario = ActivityScenario.launch(ExportImport::class.java)
        exportImportActivityScenario.onActivity { exportImport: ExportImport ->
            val buttonImport = exportImport.findViewById<Button>(R.id.button_export_import_import)
            buttonImport.performClick()

            val alertDialogImport = ShadowAlertDialog.getLatestAlertDialog()
            alertDialogImport.findViewById<View>(R.id.button_export_import_import_keep).performClick()

            val intentChooser = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity
            assertThat(intentChooser.action).isEqualTo(Intent.ACTION_CHOOSER)

            mockReturnActionActivityResult(exportImport, Intent.ACTION_CHOOSER)

            val shadowAlertDialogImportComplete = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            assertThat(shadowAlertDialogImportComplete.title).isEqualTo(exportImport.getString(R.string.export_import_import_complete_dialog_header))
            assertThat(shadowAlertDialogImportComplete.message).isEqualTo(exportImport.getString(R.string.export_import_import_complete_keep_dialog_description))
        }
    }

    @Test
    fun importTeasAndExpectDialogImportFailed() {
        every { databaseJsonTransformer.jsonToDatabase(any(), any()) } returns false

        val exportImportActivityScenario = ActivityScenario.launch(ExportImport::class.java)
        exportImportActivityScenario.onActivity { exportImport: ExportImport ->
            val buttonImport = exportImport.findViewById<Button>(R.id.button_export_import_import)
            buttonImport.performClick()

            val alertDialogImport = ShadowAlertDialog.getLatestAlertDialog()
            alertDialogImport.findViewById<View>(R.id.button_export_import_import_keep).performClick()

            val intentChooser = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity
            assertThat(intentChooser.action).isEqualTo(Intent.ACTION_CHOOSER)

            mockReturnActionActivityResult(exportImport, Intent.ACTION_CHOOSER)

            val shadowAlertDialogImportComplete = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            assertThat(shadowAlertDialogImportComplete.title).isEqualTo(exportImport.getString(R.string.export_import_import_failed_dialog_header))
            assertThat(shadowAlertDialogImportComplete.message).isEqualTo(exportImport.getString(R.string.export_import_import_failed_dialog_description))
        }
    }

    @Test
    fun startActivityWithAndroidVersionOlderQAndExpectNoWarning() {
        every { systemUtility.sdkVersion } returns Build.VERSION_CODES.P

        val exportImportActivityScenario = ActivityScenario.launch(ExportImport::class.java)
        exportImportActivityScenario.onActivity { exportImport: ExportImport ->
            val textViewWarning = exportImport.findViewById<TextView>(R.id.text_view_export_import_warning)
            assertThat(textViewWarning.visibility).isEqualTo(View.GONE)

            val textViewWarningText = exportImport.findViewById<TextView>(R.id.text_view_export_import_warning_text)
            assertThat(textViewWarningText.visibility).isEqualTo(View.GONE)
        }
    }

    private fun mockReturnActionActivityResult(exportImport: ExportImport, intent: String) {
        val intentResult = Intent()
        intentResult.data = Uri.EMPTY
        val shadowActivity = Shadows.shadowOf(exportImport)
        shadowActivity.receiveResult(Intent(intent), Activity.RESULT_OK, intentResult)
    }
}