package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io

interface DataIOAdapter {
    fun write(json: String): Boolean

    fun read(): String
}