package coolpharaoh.tee.speicher.tea.timer.core.tea;

public class ColorConversation {

    private ColorConversation() {
    }

    public static int discoverForegroundColor(final int color) {
        final int red = (color >> 16) & 0x000000FF;
        final int green = (color >> 8) & 0x000000FF;
        final int blue = (color) & 0x000000FF;
        final double lum = ((0.299 * red) + ((0.587 * green) + (0.114 * blue)));
        return lum > 186 ? 0xFF000000 : 0xFFFFFFFF;

    }
}
