package coolpharaoh.tee.speicher.tea.timer.datatransfer;

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
import coolpharaoh.tee.speicher.tea.timer.datatransfer.pojo.TeaPOJO;
import coolpharaoh.tee.speicher.tea.timer.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.entities.Tea;

public class ExportJson {
    private List<TeaPOJO> teaList;
    private String json;

    public ExportJson(List<Tea> teas, List<Infusion> infusions, List<Counter> counters, List<Note> notes) {
        DatabaseToPOJO databaseToPojo = new DatabaseToPOJO(teas, infusions, counters, notes);
        teaList = databaseToPojo.createTeaList();
        json = createJsonFromTeaList();
    }

    public void write(Context context) {
        //Create Folder
        File folder = new File(Environment.getExternalStorageDirectory().toString()+"/TeaMemory");
        if(!folder.exists()) {
            folder.mkdirs();
        }

        //Save the path as a string value
        String storageDirectory = folder.toString();

        try {
            File file = new File(storageDirectory, "tealist.json");
            Writer output = new BufferedWriter(new FileWriter(file));
            output.write(json);
            output.close();
            Toast.makeText(context, context.getString(R.string.exportimport_saved), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String createJsonFromTeaList() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

        return gson.toJson(teaList);
    }
}
