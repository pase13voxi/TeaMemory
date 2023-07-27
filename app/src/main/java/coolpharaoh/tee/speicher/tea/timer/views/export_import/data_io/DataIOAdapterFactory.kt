package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io

import android.app.Application
import android.net.Uri
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer

object DataIOAdapterFactory {

    private var mockedDataIOAdapter: DataIOAdapter? = null

    @JvmStatic
    fun getDataIO(application: Application, printer: Printer, uri: Uri): DataIOAdapter {
        return mockedDataIOAdapter ?: FileSystemIOAdapter(application, printer, uri)
    }

    @JvmStatic
    fun setMockedDataIO(mockedDataIOAdapter: DataIOAdapter) {
        DataIOAdapterFactory.mockedDataIOAdapter = mockedDataIOAdapter
    }
}