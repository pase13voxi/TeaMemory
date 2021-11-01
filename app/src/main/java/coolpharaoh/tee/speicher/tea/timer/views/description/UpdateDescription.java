package coolpharaoh.tee.speicher.tea.timer.views.description;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import coolpharaoh.tee.speicher.tea.timer.R;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class UpdateDescription extends AppCompatActivity {
    private static final int[] slideImages = {
            R.drawable.description_update_in_stock,
            R.drawable.description_update_option_in_stock,
            R.drawable.description_update_sorting,
            R.drawable.description_update_option_sorting
    };

    private LinearLayout dotLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);


        final String[] slideDescription = getResources().getStringArray(R.array.description_update_slide_description);
        final SlideAdapter slideAdapter = new SlideAdapter(getApplication(), slideImages, slideDescription);
        final ViewPager slideViewPager = findViewById(R.id.slide_view_description_pager);
        slideViewPager.setAdapter(slideAdapter);

        dotLayout = findViewById(R.id.layout_description_dots);
        addDotsIndicator(0, slideImages.length);
        slideViewPager.addOnPageChangeListener(viewListener);

        final ImageButton buttonClose = findViewById(R.id.button_description_close);
        buttonClose.setOnClickListener(view -> finish());
    }

    private void addDotsIndicator(final int position, final int size) {
        final TextView[] dots = new TextView[size];
        dotLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(R.string.description_dots);
            dots[i].setTextSize(35);
            dots[i].setTextColor(ContextCompat.getColor(this, R.color.background_green));

            dotLayout.addView(dots[i]);
        }

        dots[position].setTextColor(ContextCompat.getColor(this, R.color.background_green_dark));
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(final int i, final float v, final int i1) {
            // this functionality is not needed, but needs to be override
        }

        @Override
        public void onPageSelected(final int i) {
            addDotsIndicator(i, slideImages.length);
        }

        @Override
        public void onPageScrollStateChanged(final int i) {
            // this functionality is not needed, but needs to be override
        }
    };
}