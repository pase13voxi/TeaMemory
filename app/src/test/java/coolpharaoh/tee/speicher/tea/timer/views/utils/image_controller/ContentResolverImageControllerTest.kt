package coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller;

import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
public class ContentResolverImageControllerTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    Context context;
    @Mock
    ContentResolver contentResolver;
    @Mock
    Cursor cursor;

    public static final long TEA_ID = 2L;

    @Before
    public void setUp() throws Exception {
        when(context.getContentResolver()).thenReturn(contentResolver);
    }

    @Test
    public void getImageUriByTeaId() {
        mockImageAvailable();

        final ContentResolverImageController contentResolverImageController = new ContentResolverImageController(context);
        final Uri uri = contentResolverImageController.getImageUriByTeaId(TEA_ID);

        assertThat(uri).isEqualTo(Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/2"));
    }

    @Test
    public void getImageUriByTeaIdImageNotAvailable() {
        mockImageNotAvailable();

        final ContentResolverImageController contentResolverImageController = new ContentResolverImageController(context);
        final Uri uri = contentResolverImageController.getImageUriByTeaId(TEA_ID);

        assertThat(uri).isNull();
    }

    @Test
    public void getSaveImageIntent() throws Exception {
        mockImageNotAvailable();
        final Uri uri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/13");
        mockContentResolverInsert(uri);

        final ContentResolverImageController contentResolverImageController = new ContentResolverImageController(context);
        final Intent saveIntent = contentResolverImageController.getSaveOrUpdateImageIntent(TEA_ID);

        assertThat(saveIntent.getAction()).isEqualTo(MediaStore.ACTION_IMAGE_CAPTURE);
        assertThat(saveIntent.getExtras().get(MediaStore.EXTRA_OUTPUT)).isEqualTo(uri);
    }

    @Test
    public void getSaveImageIntentCouldNotCreateUri() {
        mockImageNotAvailable();

        final ContentResolverImageController contentResolverImageController = new ContentResolverImageController(context);
        assertThatExceptionOfType(IOException.class)
                .isThrownBy(() -> contentResolverImageController.getSaveOrUpdateImageIntent(TEA_ID))
                .withMessage("Failed to create new MediaStore record.");
    }

    @Test
    public void getUpdateImageIntent() throws Exception {
        mockImageAvailable();

        final ContentResolverImageController contentResolverImageController = new ContentResolverImageController(context);
        final Intent updateIntent = contentResolverImageController.getSaveOrUpdateImageIntent(TEA_ID);

        assertThat(updateIntent.getAction()).isEqualTo(MediaStore.ACTION_IMAGE_CAPTURE);
        assertThat(updateIntent.getExtras().get(MediaStore.EXTRA_OUTPUT)).hasToString(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/2");
    }

    @Test
    public void removeImageByTeaId() {
        mockImageAvailable();

        final ContentResolverImageController contentResolverImageController = new ContentResolverImageController(context);
        contentResolverImageController.removeImageByTeaId(TEA_ID);

        final Uri uri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/2");
        verify(contentResolver).delete(uri, null, null);
    }

    @Test
    public void removeImageByTeaIdImageNotAvailable() {
        mockImageNotAvailable();

        final ContentResolverImageController contentResolverImageController = new ContentResolverImageController(context);
        contentResolverImageController.removeImageByTeaId(TEA_ID);

        verify(contentResolver, never()).delete(any(), eq(null), eq(null));
    }

    @Test
    public void getLastModified() {
        final Uri uri = Uri.parse("Uri");
        final String modificationDate = "1234";
        when(contentResolver.query(uri, null, null, null, null)).thenReturn(cursor);
        when(cursor.getColumnIndex(DATE_MODIFIED)).thenReturn(2);
        when(cursor.getString(2)).thenReturn(modificationDate);

        final ContentResolverImageController contentResolverImageController = new ContentResolverImageController(context);
        final String lastModified = contentResolverImageController.getLastModified(uri);

        assertThat(lastModified).isEqualTo(modificationDate);
    }

    @Test
    public void getLastModifiedReturnEmpty() {
        final ContentResolverImageController contentResolverImageController = new ContentResolverImageController(context);
        final String lastModified = contentResolverImageController.getLastModified(Uri.parse("Uri"));

        assertThat(lastModified).isEmpty();
    }

    private void mockImageAvailable() {
        mockContentResolverQuery();

        when(cursor.getCount()).thenReturn(1);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.getColumnIndexOrThrow(BaseColumns._ID)).thenReturn(2);
        when(cursor.getLong(2)).thenReturn(2L);
    }

    private void mockImageNotAvailable() {
        mockContentResolverQuery();

        when(cursor.getCount()).thenReturn(0);
    }

    private void mockContentResolverQuery() {
        final String[] projection = {
                BaseColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.RELATIVE_PATH,
                MediaStore.MediaColumns.DATE_MODIFIED
        };

        final String selection = MediaStore.MediaColumns.RELATIVE_PATH + "='" +
                Environment.DIRECTORY_PICTURES + File.separator + "TeaMemory" + File.separator +
                "' AND " + MediaStore.MediaColumns.DISPLAY_NAME + "='" + TEA_ID + ".jpg'";

        when(contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null)).thenReturn(cursor);
    }

    private void mockContentResolverInsert(final Uri uri) {
        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, TEA_ID);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TeaMemory");

        when(contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)).thenReturn(uri);
    }
}
