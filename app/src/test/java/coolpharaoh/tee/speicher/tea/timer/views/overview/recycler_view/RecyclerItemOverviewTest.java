package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RecyclerItemOverviewTest {
    @Test
    void getImageTextOneWord() {
        final String imageText = getImageText("Sencha");
        assertThat(imageText).isEqualTo("S");
    }

    @Test
    void getImageTextFirstCharSpace() {
        final String imageText = getImageText(" Sencha");
        assertThat(imageText).isEqualTo("S");
    }

    @Test
    void getImageTextTwoWords() {
        final String imageText = getImageText("Earl Grey");
        assertThat(imageText).isEqualTo("EG");
    }

    @Test
    void getImageTextThreeWords() {
        final String imageText = getImageText("Pai Mu Tan");
        assertThat(imageText).isEqualTo("PT");
    }

    private String getImageText(final String teaName) {
        final RecyclerItemOverview itemOverview = new RecyclerItemOverview(null, null, teaName, null, null, false);
        return itemOverview.getImageText();
    }
}