package coolpharaoh.tee.speicher.tea.timer.views.export_import

import android.app.Application
import androidx.annotation.VisibleForTesting
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIOAdapter
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.DatabaseJsonTransformer

object JsonIOAdapter {
    private var databaseJsonTransformer: DatabaseJsonTransformer? = null

    @JvmStatic
    fun init(application: Application, printer: Printer) {
        if (databaseJsonTransformer == null) {
            databaseJsonTransformer = DatabaseJsonTransformer(application, printer)
        }
    }

    @JvmStatic
    fun write(dataIOAdapter: DataIOAdapter): Boolean {
        val json = databaseJsonTransformer!!.databaseToJson()
        return dataIOAdapter.write(json)
    }

    @JvmStatic
    fun read(dataIOAdapter: DataIOAdapter, keepStoredTeas: Boolean): Boolean {
        val json = dataIOAdapter.read()
        return databaseJsonTransformer!!.jsonToDatabase(json, keepStoredTeas)
    }

    @JvmStatic
    @VisibleForTesting
    fun setMockedTransformer(mockedDatabaseJsonTransformer: DatabaseJsonTransformer?) {
        databaseJsonTransformer = mockedDatabaseJsonTransformer
    }
}