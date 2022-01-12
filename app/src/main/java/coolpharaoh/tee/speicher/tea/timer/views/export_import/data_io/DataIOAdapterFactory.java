package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io;

import android.app.Application;
import android.net.Uri;

import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;

public class DataIOAdapterFactory {

    private DataIOAdapterFactory() {
    }

    private static DataIOAdapter mockedDataIOAdapter;

    public static DataIOAdapter getDataIO(final Application application, final Printer printer, final Uri uri) {
        if (mockedDataIOAdapter != null) {
            return mockedDataIOAdapter;
        } else {
            return new FileSystemIOAdapter(application, printer, uri);
        }
    }

    public static void setMockedDataIO(final DataIOAdapter mockedDataIOAdapter) {
        DataIOAdapterFactory.mockedDataIOAdapter = mockedDataIOAdapter;
    }
}
