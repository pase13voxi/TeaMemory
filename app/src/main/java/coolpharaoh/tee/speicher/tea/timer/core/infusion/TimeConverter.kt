package coolpharaoh.tee.speicher.tea.timer.core.infusion;

import java.text.DecimalFormat;

import lombok.Getter;

@Getter
public class TimeConverter {
    private String time;
    private int minutes;
    private int seconds;


    public TimeConverter(final String time) {
        this.time = time;
        extractMinutesAndSeconds();
    }

    public TimeConverter(final int minutes, final int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
        extractText();
    }

    private void extractText() {
        final DecimalFormat formatter = new DecimalFormat("00");
        final String formattedMinutes = formatter.format(minutes);
        final String formattedSeconds = formatter.format(seconds);
        time = formattedMinutes + ":" + formattedSeconds;
    }

    private void extractMinutesAndSeconds() {
        if (time == null) {
            minutes = 0;
            seconds = 0;
        } else {
            final String[] split = time.split(":");
            minutes = Integer.parseInt(split[0]);
            if (split.length > 1) seconds = Integer.parseInt(split[1]);
            else seconds = 0;
        }
    }
}
