package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.app.Application
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety

internal class ButtonColorShape(drawable: Drawable, application: Application) {

    private val application: Application
    private val gradientDrawable: GradientDrawable

    init {
        gradientDrawable = drawable as GradientDrawable
        this.application = application

        setDefaultColor()
    }

    private fun setDefaultColor() {
        val varietyColor = ContextCompat.getColor(application, Variety.BLACK_TEA.color)
        color = varietyColor
    }

    var color = 0
        set(color) {
            field = color
            gradientDrawable.setColor(color)
        }
}