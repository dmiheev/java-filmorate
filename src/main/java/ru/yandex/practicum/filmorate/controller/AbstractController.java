package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractController<T extends AbstractEntity> {

    protected final Map<Integer, T> items = new HashMap<>();
    private int maxId = 0;

    public Collection<T> getAll() {
        return new ArrayList<>(items.values());
    }

    public T create(T item) {
        item.setId(++maxId);

        validate(item);
        items.put(item.getId(), item);

        return item;
    }

    public T update(T item) {
        validate(item);
        items.put(item.getId(), item);

        return item;
    }

    protected void validate(T item) {
        if (items.containsValue(item)) {
            throw new ValidationException(item.getClass().getName() + " уже создан");
        }
    }
}
