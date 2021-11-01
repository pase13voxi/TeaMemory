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

    public SlideAdapter(final Application application, final int[] slideImages, final String[] slideDescription) {
        this.application = application;
        this.slideImages = slideImages;
        this.slideDescription = slideDescription;
    }

    @Override
    public int getCount() {
        return slideImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull final View view, @NonNull final Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        final LayoutInflater layoutInflater = (LayoutInflater) application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = Objects.requireNonNull(layoutInflater).inflate(R.layout.slide_layout, container, false);

        final TextView slideTextViewDescription = view.findViewById(R.id.text_view_description);
        slideTextViewDescription.setText(slideDescription[position]);

        final ImageView slideImageView = view.findViewById(R.id.image_view_description_slide_image);
        slideImageView.setImageResource(slideImages[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull final ViewGroup container, final int position, @NonNull final Object object) {
        container.removeView((RelativeLayout) object);
    }
}
