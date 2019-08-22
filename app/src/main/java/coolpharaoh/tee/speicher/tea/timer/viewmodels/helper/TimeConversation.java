package coolpharaoh.tee.speicher.tea.timer.viewmodels.helper;

public class TimeConversation {

    public static Time getMinutesAndSeconds(String timetext) {
        Time time = new Time();
        time.time = timetext;
        if (timetext == null) {
            time.minutes = 0;
            time.seconds = 0;
        } else {
            String[] split = timetext.split(":");
            time.minutes = Integer.parseInt(split[0]);
            if (split.length > 1) time.seconds = Integer.parseInt(split[1]);
            else time.seconds = 0;
        }
        return time;
    }
}
