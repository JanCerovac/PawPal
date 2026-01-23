package root.data_objects;

public record Reservation(
        String eventId,
        String walkerUsername,
        String address,
        String notes,
        String type,
        String date,
        String duration
) {}
