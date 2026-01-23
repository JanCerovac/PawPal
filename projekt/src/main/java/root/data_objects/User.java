package root.data_objects;

import jakarta.validation.constraints.Null;

public record User(String username, String role, @Null String email) {
}
