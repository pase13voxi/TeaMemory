package coolpharaoh.tee.speicher.tea.timer.views.showtea;

class TimeHelper {
    String time;
    int minutes;
    int seconds;

    static TimeHelper getMinutesAndSeconds(String timetext) {
        TimeHelper time = new TimeHelper();
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
