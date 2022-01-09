package coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller;

import android.content.Context;

public class ImageControllerFactory {

    private ImageControllerFactory() {
    }

    private static ImageController mockedImageController;

    public static ImageController getImageController(final Context context) {
        if (mockedImageController != null) {
            return mockedImageController;
        } else {
            return new ContentResolverImageController(context);
        }
    }

    public static void setMockedImageController(final ImageController mockedImageController) {
        ImageControllerFactory.mockedImageController = mockedImageController;
    }
}
