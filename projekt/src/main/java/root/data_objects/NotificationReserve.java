package root.data_objects;

public record NotificationReserve(
        String ownerUsername,
        String eventId,
        String type,
        String date,
        String duration,
        String address,
        String notes
) {

}
