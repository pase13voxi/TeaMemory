package coolpharaoh.tee.speicher.tea.timer.views.description

import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import coolpharaoh.tee.speicher.tea.timer.R

class ShowTeaDescription : AppCompatActivity() {

    private var dotLayout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        val slideDescription = resources.getStringArray(R.array.description_showtea_slide_description)
        val slideAdapter = SlideAdapter(application, slideImages, slideDescription)
        val slideViewPager = findViewById<ViewPager>(R.id.slide_view_description_pager)
        slideViewPager.adapter = slideAdapter

        dotLayout = findViewById(R.id.layout_description_dots)
        addDotsIndicator(0, slideImages.size)
        slideViewPager.addOnPageChangeListener(viewListener)

        val buttonClose = findViewById<ImageButton>(R.id.button_description_close)
        buttonClose.setOnClickListener { finish() }
    }

    fun addDotsIndicator(position: Int, size: Int) {
        val dots = arrayOfNulls<TextView>(size)
        dotLayout!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.setText(R.string.description_dots)
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(ContextCompat.getColor(application, R.color.background_green))

            dotLayout!!.addView(dots[i])
        }

        dots[position]!!.setTextColor(ContextCompat.getColor(application, R.color.background_green_dark))
    }

    var viewListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(i: Int, v: Float, i1: Int) {
            // this functionality is not needed, but needs to be override
        }

        override fun onPageSelected(i: Int) {
            addDotsIndicator(i, slideImages.size)
        }

        override fun onPageScrollStateChanged(i: Int) {
            // this functionality is not needed, but needs to be override
        }
    }

    companion object {
        private val slideImages = intArrayOf(
                R.drawable.description_showtea_temperature,
                R.drawable.description_showtea_amount,
                R.drawable.description_showtea_infusions,
                R.drawable.description_showtea_information
        )
    }
}