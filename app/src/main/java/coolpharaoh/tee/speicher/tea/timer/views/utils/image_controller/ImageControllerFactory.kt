package coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller

import android.content.Context

object ImageControllerFactory {

    private var mockedImageController: ImageController? = null

    @JvmStatic
    fun getImageController(context: Context): ImageController {
        return mockedImageController ?: ContentResolverImageController(context)
    }

    @JvmStatic
    fun setMockedImageController(mockedImageController: ImageController) {
        ImageControllerFactory.mockedImageController = mockedImageController
    }
}