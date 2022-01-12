package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io;

public interface DataIOAdapter {
    boolean write(String json);

    String read();
}
