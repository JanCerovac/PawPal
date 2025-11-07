package root.data_objects;

public record SetupRequest(
        String role,
        String username,
        String petName,
        String address,
        Integer experienceYears,
        String availability
) {}
