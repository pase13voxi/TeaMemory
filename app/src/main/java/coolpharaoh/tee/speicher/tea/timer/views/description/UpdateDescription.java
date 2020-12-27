package coolpharaoh.tee.speicher.tea.timer.views.description;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import coolpharaoh.tee.speicher.tea.timer.R;

public class UpdateDescription extends AppCompatActivity {
    private static final int[] slideImages = {
            R.drawable.description_update_information,
            R.drawable.description_update_navigation,
            R.drawable.description_update_sort
    };

    private LinearLayout dotLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        final ViewPager slideViewPager = findViewById(R.id.slideViewPager);
        dotLayout = findViewById(R.id.dotsLayout);

        final String[] slideDescription = getResources().getStringArray(R.array.description_update_slide_description);
        final SlideAdapter slideAdapter = new SlideAdapter(getApplication(), slideImages, slideDescription);
        slideViewPager.setAdapter(slideAdapter);

        addDotsIndicator(0, slideImages.length);
        slideViewPager.addOnPageChangeListener(viewListener);

        final ImageButton buttonClose = findViewById(R.id.buttonDescriptionClose);
        buttonClose.setOnClickListener(view -> finish());
    }

    public void addDotsIndicator(final int position, final int size) {
        final TextView[] mDots = new TextView[size];
        dotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(R.string.description_dots);
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorPrimary));

            dotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
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