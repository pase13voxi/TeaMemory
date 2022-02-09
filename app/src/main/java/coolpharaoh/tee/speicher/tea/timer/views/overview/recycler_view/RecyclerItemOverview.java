package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RecyclerItemOverview {
    final String category;
    final Long teaId;
    final String teaName;
    final String variety;
    final Integer color;
    final boolean favorite;

    public String getImageText() {
        final ArrayList<String> split = new ArrayList<>();
        Collections.addAll(split, teaName.split(" "));
        split.removeAll(Arrays.asList("", null));

        String imageText = "";
        if (split.size() == 1) {
            imageText = String.valueOf(split.get(0).charAt(0));
        } else if (split.size() > 1) {
            final String firstCharFirstWord = String.valueOf(split.get(0).charAt(0));
            final String firstCharLastWord = String.valueOf(split.get(split.size() - 1).charAt(0));
            imageText = firstCharFirstWord + firstCharLastWord;
        }
        imageText = imageText.toUpperCase(Locale.ROOT);
        return imageText;
    }
}
