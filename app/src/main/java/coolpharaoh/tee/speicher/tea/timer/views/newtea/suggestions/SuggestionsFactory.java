package coolpharaoh.tee.speicher.tea.timer.views.newtea.suggestions;

import android.app.Application;

public class SuggestionsFactory {

    private SuggestionsFactory() {
    }

    public static Suggestions getSuggestions(final int variety, final Application application) {
        switch (variety) {
            case 0:
                return new BlackTeaSuggestions(application);
            case 1:
                return new GreenTeaSuggestions(application);
            case 2:
                return new YellowTeaSuggestions(application);
            case 3:
                return new WhiteTeaSuggestions(application);
            case 4:
                return new OolongTeaSuggestions(application);
            case 5:
                return new PuerhTeaSuggestions(application);
            case 6:
                return new HerbalTeaSuggestions(application);
            case 7:
                return new FruitTeaSuggestions(application);
            case 8:
                return new RooibusTeaSuggestions(application);
            default:
                return new NoSuggestions();
        }
    }
}
