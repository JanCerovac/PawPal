package root.data_objects;

public record Dog(
        Long id,
        String name,
        String breed,
        String age,
        String energyLevel,
        String allowedTreats,
        String healthcare,
        String personality
) {}
