package coolpharaoh.tee.speicher.tea.timer.views.description;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import coolpharaoh.tee.speicher.tea.timer.R;

public class ShowTeaDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        final String slideDescription = getString(R.string.description_showtea_slide_description);
        final ViewPager slideViewPager = findViewById(R.id.slideViewPager);
        final SlideAdapter slideAdapter = new SlideAdapter(getApplication(), new int[]{R.drawable.description_showtea}, new String[]{slideDescription});
        slideViewPager.setAdapter(slideAdapter);

        final ImageButton buttonClose = findViewById(R.id.buttonDescriptionClose);
        buttonClose.setOnClickListener(view -> finish());
    }
}
