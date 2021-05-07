package coolpharaoh.tee.speicher.tea.timer.views.export_import;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.Exporter;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.Importer;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.DatabaseJsonTransformer;

public class JsonIOAdapter {

    private JsonIOAdapter() {
    }

    private static DatabaseJsonTransformer databaseJsonTransformer;

    public static void init(final Application application, final Printer printer) {
        initDatabaseJsonTransformer(application, printer);
    }

    private static void initDatabaseJsonTransformer(final Application application, final Printer printer) {
        if (databaseJsonTransformer == null) {
            databaseJsonTransformer = new DatabaseJsonTransformer(application, printer);
        }
    }

    public static boolean write(final Exporter exporter) {
        return databaseJsonTransformer.databaseToJson(exporter);
    }

    public static boolean read(final Importer importer, final boolean keepStoredTeas) {
        return databaseJsonTransformer.jsonToDatabase(importer, keepStoredTeas);
    }

    @VisibleForTesting
    public static void setMockedTransformer(DatabaseJsonTransformer mockedDatabaseJsonTransformer) {
        databaseJsonTransformer = mockedDatabaseJsonTransformer;
    }
}
