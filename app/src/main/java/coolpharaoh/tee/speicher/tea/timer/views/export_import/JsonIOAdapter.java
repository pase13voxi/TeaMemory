package coolpharaoh.tee.speicher.tea.timer.views.export_import;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.DatabaseJsonTransformer;

public class JsonIOAdapter {

    private JsonIOAdapter() {
    }

    private static DatabaseJsonTransformer databaseJsonTransformer;

    public static void init(final Application application, final Printer printer) {
        if (databaseJsonTransformer == null) {
            databaseJsonTransformer = new DatabaseJsonTransformer(application, printer);
        }
    }

    public static boolean write(final DataIO dataIO) {
        final String json = databaseJsonTransformer.databaseToJson();
        return dataIO.write(json);
    }

    public static boolean read(final DataIO dataIO, final boolean keepStoredTeas) {
        final String json = dataIO.read();
        return databaseJsonTransformer.jsonToDatabase(json, keepStoredTeas);
    }

    @VisibleForTesting
    public static void setMockedTransformer(final DatabaseJsonTransformer mockedDatabaseJsonTransformer) {
        databaseJsonTransformer = mockedDatabaseJsonTransformer;
    }
}
