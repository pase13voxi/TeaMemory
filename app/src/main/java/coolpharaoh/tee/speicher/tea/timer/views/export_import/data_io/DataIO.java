package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io;

public interface DataIO {
    boolean write(String json);

    String read();
}
