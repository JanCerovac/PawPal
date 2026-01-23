package root.data_objects;

public record NotificationConfirm(
        String ownerUsername,
        String type,
        String date,
        String duration,
        String address
) {

}
