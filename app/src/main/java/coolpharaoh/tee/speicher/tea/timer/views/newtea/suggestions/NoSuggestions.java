package coolpharaoh.tee.speicher.tea.timer.views.newtea.suggestions;

class NoSuggestions implements Suggestions {

    @Override
    public int[] getAmountTsSuggestions() {
        return new int[]{};
    }

    @Override
    public int[] getAmountGrSuggestions() {
        return new int[]{};
    }

    @Override
    public int[] getTemperatureCelsiusSuggestions() {
        return new int[]{};
    }

    @Override
    public int[] getTemperatureFahrenheitSuggestions() {
        return new int[]{};
    }

    @Override
    public String[] getTimeSuggestions() {
        return new String[]{};
    }
}
