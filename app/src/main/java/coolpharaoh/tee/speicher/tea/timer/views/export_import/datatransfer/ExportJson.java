package coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer;

import android.app.Application;
import android.net.Uri;

import androidx.documentfile.provider.DocumentFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer.pojo.TeaPOJO;

public class ExportJson {
    public static final int WRITE_REQUEST_CODE = 8778;

    private final Application application;
    private final Printer printer;

    public ExportJson(Application application, Printer printer) {
        this.application = application;
        this.printer = printer;
    }

    public boolean write(final Uri folderPath) {
        final String json = generateJson();
        return writeFile(json, folderPath);
    }

    private String generateJson() {
        final DataTransferViewModel dataTransferViewModel = new DataTransferViewModel(application);

        final DatabaseToPOJO databaseToPojo = new DatabaseToPOJO(dataTransferViewModel.getTeaList(),
                dataTransferViewModel.getInfusionList(), dataTransferViewModel.getCounterList(),
                dataTransferViewModel.getNoteList());

        final List<TeaPOJO> teaList = databaseToPojo.createTeaList();
        return createJsonFromTeaList(teaList);
    }

    private boolean writeFile(final String json, final Uri folderPath) {
        final DocumentFile pickedFolder = DocumentFile.fromTreeUri(application, folderPath);
        if (pickedFolder == null) {
            printer.print(application.getString(R.string.export_import_save_failed));
            return false;
        }
        final DocumentFile file = pickedFolder.createFile("application/json", "tealist.json");
        if (file == null) {
            printer.print(application.getString(R.string.export_import_save_failed));
            return false;
        }

        try (final OutputStream out = application.getContentResolver().openOutputStream(file.getUri())) {
            out.write(json.getBytes());
        } catch (IOException e) {
            printer.print(application.getString(R.string.export_import_save_failed));
            return false;
        }
        printer.print(application.getString(R.string.export_import_saved));

        return true;
    }

    private String createJsonFromTeaList(final List<TeaPOJO> teaList) {
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

        return gson.toJson(teaList);
    }
}
