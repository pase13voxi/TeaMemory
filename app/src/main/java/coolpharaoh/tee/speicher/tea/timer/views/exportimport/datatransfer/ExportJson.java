package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.TeaPOJO;

public class ExportJson {

    private final Context context;
    private final List<TeaPOJO> teaList;
    private final String json;

    public ExportJson(Context context) {
        this.context = context;
        DataTransferViewModel dataTransferViewModel = new DataTransferViewModel(TeaMemoryDatabase.getDatabaseInstance(context));

        DatabaseToPOJO databaseToPojo = new DatabaseToPOJO(dataTransferViewModel.getTeaList(),
                dataTransferViewModel.getInfusionList(), dataTransferViewModel.getCounterList(),
                dataTransferViewModel.getNoteList());

        teaList = databaseToPojo.createTeaList();
        json = createJsonFromTeaList();
    }

    public boolean write() {
        //Create Folder
        File folder = new File(Environment.getExternalStorageDirectory().toString() + context.getString(R.string.exportimport_export_folder_name));
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                Toast.makeText(context, context.getString(R.string.exportimport_export_create_folder_failed), Toast.LENGTH_LONG).show();
                return false;
            }
        }

        //Save the path as a string value
        String storageDirectory = folder.toString();


        File file = new File(storageDirectory, context.getString(R.string.exportimport_export_file_name));
        try (Writer output = new BufferedWriter(new FileWriter(file))) {
            output.write(json);
        } catch (IOException e) {
            Toast.makeText(context, R.string.exportimport_save_failed, Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(context, context.getString(R.string.exportimport_saved), Toast.LENGTH_LONG).show();


        return true;
    }

    private String createJsonFromTeaList() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

        return gson.toJson(teaList);
    }
}
