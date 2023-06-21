package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Directors {
    private Long id;
    @NotBlank
    private String name;

    public Map<String, Object> userToDirectors() {
        Map<String, Object> temp = new HashMap<>();
        temp.put("id", id);
        temp.put("name", name);
        return temp;
    }

}