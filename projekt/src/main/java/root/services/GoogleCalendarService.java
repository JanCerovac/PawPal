package root.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service za upravljanje Google Calendar API-em
 */
@Service
public class GoogleCalendarService {

    private static final List<String> SCOPES = List.of("https://www.googleapis.com/auth/calendar");

    private final Calendar calendar;
    private final String calendarId;
    private final ZoneId zoneId;

    public GoogleCalendarService(
            @Value("${gcal.calendar-id}") String calendarId,
            @Value("${gcal.service-account-key-path}") String keyPath,
            @Value("${app.timezone}") String timezone
    ) throws Exception {
        this.calendarId = calendarId;
        this.zoneId = ZoneId.of(timezone);

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(keyPath))
                .createScoped(SCOPES);

        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        this.calendar = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer
        ).setApplicationName("PawPal").build();
    }

    public List<Event> readAllWalks() throws Exception {
        Events events = calendar.events().list(calendarId)
                .setSingleEvents(true)
                .setOrderBy("startTime")
                .setMaxResults(250)
                .execute();

        return events.getItems();
    }

    public void markReserved(
            String eventId,
            String ownerName,
            String address,
            String notes
    ) throws Exception {
        Event event = calendar.events().get(calendarId, eventId).execute();

        String oldSummary = event.getSummary() == null ? "PawPal Walk" : event.getSummary();
        if (!oldSummary.toUpperCase().contains("RESERVED")) {
            event.setSummary(oldSummary + " (RESERVED)");
        }

        String oldDesc = event.getDescription() == null ? "" : event.getDescription();
        String reservedBlock = "\n\n--- REZERVACIJA ---\n" +
                        "Vlasnik: " + ownerName + "\n" +
                        "Polazišna adresa: " + address + "\n" +
                        (notes == null || notes.isBlank() ? "" : ("Napomene: " + notes + "\n"));

        event.setDescription(oldDesc + reservedBlock);

        Event.ExtendedProperties ext = event.getExtendedProperties();
        if (ext == null) ext = new Event.ExtendedProperties();

        Map<String, String> priv = ext.getPrivate();
        if (priv == null) priv = new java.util.HashMap<>();

        priv.put("reserved", "true");
        priv.put("ownerName", ownerName);
        priv.put("address", address);
        if (notes != null) priv.put("notes", notes);

        ext.setPrivate(priv);
        event.setExtendedProperties(ext);

        calendar.events().update(calendarId, eventId, event).execute();
    }

    public void deleteEvent(String eventId) throws IOException {
        calendar.events().delete(calendarId, eventId).execute();
    }

    public void createWalkEvent(
            String name,
            String username,
            String type,
            String location,
            Integer price,
            Integer durationMinutes,
            LocalDateTime startLocal
    ) throws IOException {

        ZonedDateTime start = startLocal.atZone(zoneId);
        ZonedDateTime end = start.plusMinutes(durationMinutes);

        String description = "Walker: " + name + "\n"
                + "Walker Username: " + username + "\n"
                + "Location: " + location + "\n"
                + "Type: " + type + "\n"
                + "Price: " + price + "\n"
                + "Duration: " + durationMinutes + " minutes";

        Event event = new Event()
                .setSummary("PawPal Walk (" + type + ")")
                .setDescription(description)
                .setStart(new EventDateTime()
                        .setDateTime(new DateTime(start.toInstant().toEpochMilli()))
                        .setTimeZone(zoneId.getId()))
                .setEnd(new EventDateTime()
                        .setDateTime(new DateTime(end.toInstant().toEpochMilli()))
                        .setTimeZone(zoneId.getId()));

        calendar.events().insert(calendarId, event).execute();
    }
}