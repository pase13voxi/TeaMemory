package coolpharaoh.tee.speicher.tea.timer.views.description;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;

public class SlideAdapter extends PagerAdapter {
    final Application application;
    final int[] slideImages;
    final String[] slideDescription;

    public SlideAdapter(Application application, int[] slideImages, String[] slideDescription) {
        this.application = application;
        this.slideImages = slideImages;
        this.slideDescription = slideDescription;
    }

    @Override
    public int getCount() {
        return slideImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(layoutInflater).inflate(R.layout.slide_layout, container, false);

        TextView slideTextViewDescription = view.findViewById(R.id.textViewDescription);
        slideTextViewDescription.setText(slideDescription[position]);

        ImageView slideImageView = view.findViewById(R.id.slide_image);
        slideImageView.setImageResource(slideImages[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
