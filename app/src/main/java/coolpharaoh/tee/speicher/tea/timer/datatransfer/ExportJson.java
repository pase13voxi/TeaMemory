package coolpharaoh.tee.speicher.tea.timer.datatransfer;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.datatransfer.pojo.TeaPOJO;
import coolpharaoh.tee.speicher.tea.timer.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.entities.Tea;

public class ExportJson {
    private List<TeaPOJO> mTeaList;
    private String mJson;

    public ExportJson(List<Tea> teas, List<Infusion> infusions, List<Counter> counters, List<Note> notes) {
        DatabaseToPOJO databaseToPojo = new DatabaseToPOJO(teas, infusions, counters, notes);
        mTeaList = databaseToPojo.createTeaList();
        mJson = createJsonFromTeaList();
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
            File file = new File(storageDirectory, "data.json");
            Writer output = new BufferedWriter(new FileWriter(file));
            output.write(mJson);
            output.close();
            Toast.makeText(context, "Composition saved", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String createJsonFromTeaList() {
        return new Gson().toJson(mTeaList);
    }
}
