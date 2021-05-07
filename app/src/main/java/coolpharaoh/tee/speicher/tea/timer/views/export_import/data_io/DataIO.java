package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io;

import android.net.Uri;

public interface DataIO {
    boolean write(String json, Uri folderUri);

    String read(Uri fileUri);
}
