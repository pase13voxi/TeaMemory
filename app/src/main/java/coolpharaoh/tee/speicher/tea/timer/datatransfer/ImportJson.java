package coolpharaoh.tee.speicher.tea.timer.datatransfer;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.datatransfer.pojo.TeaPOJO;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.ExportImportViewModel;

public class ImportJson {
    public static final int READ_REQUEST_CODE = 8777;

    private Uri mFileUri;
    private String mJson;
    private List<TeaPOJO> mTeaList;

    public ImportJson(Uri fileUri) {
        mFileUri = fileUri;
    }

    public void read(Context context, ExportImportViewModel exportImportViewModel, boolean keepStoredTeas) {
        mJson = readJsonFile(context);
        mTeaList = createTeaListFromJson();
        POJOToDatabase pojoToDatabase = new POJOToDatabase(exportImportViewModel);
        pojoToDatabase.fillDatabaseWithTeaList(mTeaList, keepStoredTeas);
    }

    private String readJsonFile(Context context){
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream =
                     context.getContentResolver().openInputStream(mFileUri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
         return stringBuilder.toString();
    }

    private List<TeaPOJO> createTeaListFromJson(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

        Type listType = new TypeToken<ArrayList<TeaPOJO>>(){}.getType();

        return gson.fromJson(mJson, listType);
    }
}
