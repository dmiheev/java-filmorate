package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
public class AbstractEntity {

    @EqualsAndHashCode.Exclude
    protected int id;
}
