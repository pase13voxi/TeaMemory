package coolpharaoh.tee.speicher.tea.timer.views.description

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import coolpharaoh.tee.speicher.tea.timer.R
import java.util.Objects

class SlideAdapter(val application: Application, val slideImages: IntArray, val slideDescription: Array<String>) : PagerAdapter() {
    override fun getCount(): Int {
        return slideImages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = application.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = Objects.requireNonNull(layoutInflater).inflate(R.layout.slide_layout, container, false)
        val slideTextViewDescription = view.findViewById<TextView>(R.id.text_view_description)
        slideTextViewDescription.text = slideDescription[position]
        val slideImageView = view.findViewById<ImageView>(R.id.image_view_description_slide_image)
        slideImageView.setImageResource(slideImages[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}