package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
public class User {
    private Long id;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "\\S*$")
    private String login;
    @Setter(AccessLevel.NONE)
    private String name;
    @PastOrPresent
    private LocalDate birthday;

    public void setName(String name) {
        if ((name == null) || (name.isEmpty()) || (name.isBlank())) {
            this.name = login;
        } else {
            this.name = name;
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);
        return values;
    }
}
