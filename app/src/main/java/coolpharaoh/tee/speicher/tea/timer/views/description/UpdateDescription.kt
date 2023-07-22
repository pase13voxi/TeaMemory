package coolpharaoh.tee.speicher.tea.timer.views.description

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import coolpharaoh.tee.speicher.tea.timer.R

// This class has 9 Parent because of AppCompatActivity
class UpdateDescription : AppCompatActivity() {
    private var dotLayout: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        val slideDescription = resources.getStringArray(R.array.description_update_slide_description)
        val slideAdapter = SlideAdapter(application, slideImages, slideDescription)
        val slideViewPager = findViewById<ViewPager>(R.id.slide_view_description_pager)
        slideViewPager.adapter = slideAdapter
        dotLayout = findViewById(R.id.layout_description_dots)
        addDotsIndicator(0, slideImages.size)
        slideViewPager.addOnPageChangeListener(viewListener)
        val buttonClose = findViewById<ImageButton>(R.id.button_description_close)
        buttonClose.setOnClickListener { view: View? -> finish() }
    }

    private fun addDotsIndicator(position: Int, size: Int) {
        val dots = arrayOfNulls<TextView>(size)
        dotLayout!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.setText(R.string.description_dots)
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(ContextCompat.getColor(this, R.color.background_green))
            dotLayout!!.addView(dots[i])
        }
        dots[position]!!.setTextColor(ContextCompat.getColor(this, R.color.background_green_dark))
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
                R.drawable.description_update_random_choice_1,
                R.drawable.description_update_random_choice_2
        )
    }
}