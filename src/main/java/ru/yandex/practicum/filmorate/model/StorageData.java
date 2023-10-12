package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageData {

    @EqualsAndHashCode.Exclude
    protected Long id;
}
