package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.VisibleForTesting;

import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;

public class JsonIOAdapter {

    private JsonIOAdapter(){}

    private static ExportJson exportJson;
    private static ImportJson importJson;

    public static void init(Application application, Printer printer){
        initExport(application, printer);
        initImport(application, printer);
    }

    private static void initExport(Application application, Printer printer) {
        if(exportJson == null) {
            exportJson = new ExportJson(application, printer);
        }
    }

    private static void initImport(Application application, Printer printer) {
        if(importJson == null) {
            importJson = new ImportJson(application, printer);
        }
    }

    public static boolean write(){
        return exportJson.write();
    }

    public static boolean read(Uri fileUri, boolean keepStoredTeas) {
        return importJson.read(fileUri, keepStoredTeas);
    }

    @VisibleForTesting
    public static void setMockedExportImport(ExportJson mockedExportJson, ImportJson mockedImportJson){
        exportJson = mockedExportJson;
        importJson = mockedImportJson;
    }
}
