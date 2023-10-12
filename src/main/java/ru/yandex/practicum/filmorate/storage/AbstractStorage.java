package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.DataAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.StorageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractStorage<T extends StorageData> implements Storage<T> {
    private final Map<Long, T> storage = new HashMap<>();

    @Override
    public void create(T data) {
        validate(data);
        if (storage.containsKey(data.getId())) {
            throw new DataAlreadyExistException("Duplicate id: " + data.getId());
        }
        storage.put(data.getId(), data);
    }

    protected void validate(T data) {
        if (data.getId() == null) {
            throw new DataNotFoundException("Null id");
        }
    }

    @Override
    public void update(T data) {
        validate(data);
        if (!storage.containsKey(data.getId())) {
            throw new DataNotFoundException("id=" + data.getId());
        }
        storage.put(data.getId(), data);
    }

    @Override
    public T get(long id) {
        final T data = storage.get(id);
        if (data == null) {
            throw new DataNotFoundException("id=" + id);
        }
        return data;
    }

    @Override
    public void delete(long id) {
        storage.remove(id);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }
}
