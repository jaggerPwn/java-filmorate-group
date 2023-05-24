package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Friendship {
    @NonNull
    private Long userId;
    @NonNull
    private Long friendId;
    @NonNull
    private boolean isConfirmed;
}
