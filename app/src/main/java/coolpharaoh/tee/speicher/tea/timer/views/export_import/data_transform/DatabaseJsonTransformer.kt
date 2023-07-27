package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform

import android.app.Application
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.TeaPOJO

class DatabaseJsonTransformer(private val application: Application, private val printer: Printer) {
    fun databaseToJson(): String {
        return generateJson()
    }

    private fun generateJson(): String {
        val dataTransformViewModel = DataTransformViewModel(application)

        val databaseToPojo = DatabaseToPOJO(dataTransformViewModel.teas,
            dataTransformViewModel.infusions, dataTransformViewModel.counters,
            dataTransformViewModel.notes)

        val teaList = databaseToPojo.createTeaList()
        return createJsonFromTeaList(teaList)
    }

    private fun createJsonFromTeaList(teaList: List<TeaPOJO>): String {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create()

        return gson.toJson(teaList)
    }

    fun jsonToDatabase(json: String, keepStoredTeas: Boolean): Boolean {
        val teaList = createTeaListFromJson(json)
        if (teaList.isEmpty()) {
            return false
        }
        val pojoToDatabase = POJOToDatabase(DataTransformViewModel(application))
        pojoToDatabase.fillDatabaseWithTeaList(teaList, keepStoredTeas)
        printer.print(application.getString(R.string.export_import_teas_imported))
        return true
    }

    private fun createTeaListFromJson(json: String): List<TeaPOJO> {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create()

        val listType = object : TypeToken<ArrayList<TeaPOJO?>?>() {}.type

        return try {
            gson.fromJson(json, listType)
        } catch (e: JsonSyntaxException) {
            printer.print(application.getString(R.string.export_import_import_parse_teas_failed))
            emptyList()
        }
    }
}