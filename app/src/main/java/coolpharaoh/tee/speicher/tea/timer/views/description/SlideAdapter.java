package coolpharaoh.tee.speicher.tea.timer.views.description;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;

public class SlideAdapter extends PagerAdapter {
    Application application;
    LayoutInflater layoutInflater;

    public SlideAdapter(Application application) {
        this.application = application;
    }

    public static int[] slideImages = {
            R.drawable.infusion_black,
            R.drawable.background,
            R.drawable.add
    };

    @Override
    public int getCount() {
        return slideImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(layoutInflater).inflate(R.layout.slide_layout, container, false);
        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);

        slideImageView.setImageResource(slideImages[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
