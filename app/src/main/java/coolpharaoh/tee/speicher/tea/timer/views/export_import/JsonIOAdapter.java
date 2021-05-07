package coolpharaoh.tee.speicher.tea.timer.views.export_import;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.VisibleForTesting;

import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.DatabaseJsonTransformer;

public class JsonIOAdapter {

    private JsonIOAdapter() {
    }

    private static DatabaseJsonTransformer databaseJsonTransformer;
    private static DataIO dataIO;

    public static void init(final Application application, final Printer printer, final DataIO dataIO) {
        initDatabaseJsonTransformer(application, printer);
        initDataIO(dataIO);
    }

    private static void initDatabaseJsonTransformer(final Application application, final Printer printer) {
        if (databaseJsonTransformer == null) {
            databaseJsonTransformer = new DatabaseJsonTransformer(application, printer);
        }
    }

    private static void initDataIO(DataIO dataIO) {
        if (JsonIOAdapter.dataIO == null) {
            JsonIOAdapter.dataIO = dataIO;
        }
    }

    public static boolean write(final Uri folderPath) {
        final String json = databaseJsonTransformer.databaseToJson();
        return dataIO.write(json, folderPath);
    }

    public static boolean read(final Uri filePath, final boolean keepStoredTeas) {
        final String json = dataIO.read(filePath);
        return databaseJsonTransformer.jsonToDatabase(json, keepStoredTeas);
    }

    @VisibleForTesting
    public static void setMockedTransformer(final DatabaseJsonTransformer mockedDatabaseJsonTransformer,
                                            final DataIO mockedDataIO) {
        databaseJsonTransformer = mockedDatabaseJsonTransformer;
        dataIO = mockedDataIO;
    }
}
