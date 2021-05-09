package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io;

import android.app.Application;
import android.net.Uri;

import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;

public class DataIOFactory {

    private DataIOFactory() {
    }

    private static DataIO mockedDataIO;

    public static DataIO getDataIO(final Application application, final Printer printer, final Uri uri) {
        if (mockedDataIO != null) {
            return mockedDataIO;
        } else {
            return new FileSystemIO(application, printer, uri);
        }
    }

    public static void setMockedDataIO(final DataIO mockedDataIO) {
        DataIOFactory.mockedDataIO = mockedDataIO;
    }
}
