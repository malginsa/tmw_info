import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OnlineUsersSupplier {

    private static final Logger LOG = LogManager.getLogger();

    private static final String ONLINE_USERS_URL = "https://server.themanaworld.org/";
    private static final Pattern DATE_TIME = Pattern.compile(
            "\\((\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})\\)");
    private static final DateTimeFormatter TMW_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public OnlineUsersSnapshot getSnapshotNow() throws IOException {
        Elements body = Jsoup.connect(ONLINE_USERS_URL)
                .get()
                .getElementsByTag("body");

        String h3Text = body.select("h3").toString();
        OnlineUsersSnapshot snapshot = new OnlineUsersSnapshot();
        snapshot.setDateTime(extractDateTimeOrSetNow(h3Text));

        snapshot.setUsers( body
                .select("td")
                .stream()
                .map(Element::text)
                .collect(Collectors.toList()));

        return snapshot.removeBots();
    }

    private LocalDateTime extractDateTimeOrSetNow(String h3Text) {
        Matcher matcher = DATE_TIME.matcher(h3Text);
        LocalDateTime dateTime;
        if (matcher.find() && matcher.groupCount() == 1) {
            String dateTimeAsString = matcher.group(1);
            try {
                dateTime = LocalDateTime.parse(dateTimeAsString, TMW_DATE_TIME_FORMAT);
            } catch (DateTimeParseException e) {
                LOG.error("Can't extract dateTime from " + ONLINE_USERS_URL +
                        "\nIt's initislized by current one.");
                dateTime = LocalDateTime.now();
            }
        } else {
            dateTime = LocalDateTime.now();
        }
        return dateTime;
    }
}
