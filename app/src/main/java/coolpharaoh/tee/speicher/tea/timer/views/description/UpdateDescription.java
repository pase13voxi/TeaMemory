package coolpharaoh.tee.speicher.tea.timer.views.description;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.utils.CustomResources;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class UpdateDescription extends AppCompatActivity {
    private static final int[] slideImages = {
            R.drawable.description_update_new_tea,
            R.drawable.description_update_counter
    };

    private LinearLayout dotLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);


        final String[] slideDescription = getResources().getStringArray(R.array.description_update_slide_description);
        final SlideAdapter slideAdapter = new SlideAdapter(getApplication(), slideImages, slideDescription);
        final ViewPager slideViewPager = findViewById(R.id.slideViewPager);
        slideViewPager.setAdapter(slideAdapter);

        dotLayout = findViewById(R.id.dotsLayout);
        addDotsIndicator(0, slideImages.length);
        slideViewPager.addOnPageChangeListener(viewListener);

        final ImageButton buttonClose = findViewById(R.id.buttonDescriptionClose);
        buttonClose.setOnClickListener(view -> finish());
    }

    private void addDotsIndicator(final int position, final int size) {
        final TextView[] dots = new TextView[size];
        dotLayout.removeAllViews();

        final CustomResources customResources = new CustomResources(getApplication());
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(R.string.description_dots);
            dots[i].setTextSize(35);
            dots[i].setTextColor(customResources.getColor(R.color.colorPrimary));

            dotLayout.addView(dots[i]);
        }

        dots[position].setTextColor(customResources.getColor(R.color.colorPrimaryDark));
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
            // this functionality is not needed, but needs to be override
        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i, slideImages.length);
        }

        @Override
        public void onPageScrollStateChanged(int i) {
            // this functionality is not needed, but needs to be override
        }
    };
}