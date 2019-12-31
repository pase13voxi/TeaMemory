package coolpharaoh.tee.speicher.tea.timer.models.datatransfer;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.models.datatransfer.pojo.TeaPOJO;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

public class ExportJson {
    private final List<TeaPOJO> teaList;
    private final String json;

    public ExportJson(List<Tea> teas, List<Infusion> infusions, List<Counter> counters, List<Note> notes) {
        DatabaseToPOJO databaseToPojo = new DatabaseToPOJO(teas, infusions, counters, notes);
        teaList = databaseToPojo.createTeaList();
        json = createJsonFromTeaList();
    }

    public boolean write(Context context) {
        //Create Folder
        File folder = new File(Environment.getExternalStorageDirectory().toString()+ context.getString(R.string.exportimport_export_folder_name));
        if(!folder.exists()) {
            if(!folder.mkdirs()){
                Toast.makeText(context, context.getString(R.string.exportimport_export_create_folder_failed), Toast.LENGTH_LONG).show();
                return false;
            }
        }

        //Save the path as a string value
        String storageDirectory = folder.toString();

        try {
            File file = new File(storageDirectory, context.getString(R.string.exportimport_export_file_name));
            Writer output = new BufferedWriter(new FileWriter(file));
            output.write(json);
            output.close();
            Toast.makeText(context, context.getString(R.string.exportimport_saved), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private String createJsonFromTeaList() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

        return gson.toJson(teaList);
    }
}
