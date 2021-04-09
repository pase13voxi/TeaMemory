package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer;

import android.app.Application;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.TeaPOJO;

public class ExportJson {

    private final Application application;
    private final Printer printer;

    public ExportJson(Application application, Printer printer) {
        this.application = application;
        this.printer = printer;
    }

    public boolean write() {
        String json = generateJson();
        return writeFile(json);
    }

    private String generateJson() {
        DataTransferViewModel dataTransferViewModel = new DataTransferViewModel(application);

        DatabaseToPOJO databaseToPojo = new DatabaseToPOJO(dataTransferViewModel.getTeaList(),
                dataTransferViewModel.getInfusionList(), dataTransferViewModel.getCounterList(),
                dataTransferViewModel.getNoteList());

        List<TeaPOJO> teaList = databaseToPojo.createTeaList();
        return createJsonFromTeaList(teaList);
    }

    private boolean writeFile(String json) {
        //Create Folder
        File folder = new File(Environment.getExternalStorageDirectory().toString() + application.getString(R.string.export_import_export_folder_name));
        if (!folder.exists() && !folder.mkdirs()) {
            printer.print(application.getString(R.string.export_import_export_create_folder_failed));
            return false;
        }

        //Save the path as a string value
        String storageDirectory = folder.toString();


        File file = new File(storageDirectory, application.getString(R.string.export_import_export_file_name));
        try (Writer output = new BufferedWriter(new FileWriter(file))) {
            output.write(json);
        } catch (IOException e) {
            printer.print(application.getString(R.string.export_import_save_failed));
            return false;
        }
        printer.print(application.getString(R.string.export_import_saved));


        return true;
    }

    private String createJsonFromTeaList(List<TeaPOJO> teaList) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

        return gson.toJson(teaList);
    }
}
