package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.StorageData;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public abstract class AbstractService<T extends StorageData> {
    Storage<T> storage;
    private long counter = 0L;

    public T create(T data) {
        validate(data);
        data.setId(++counter);
        storage.create(data);
        return data;
    }

    protected abstract void validate(T data);

    public T get(long id) {
        T data = storage.get(id);
        if (data == null) {
            throw new DataNotFoundException("Data not found");
        }
        return data;
    }

    public T update(T data) {
        storage.get(data.getId());
        data.setId(data.getId());
        validate(data);
        storage.update(data);
        return data;
    }

    public void delete(long id) {
        storage.delete(id);
    }

    public List<T> getAll() {
        return storage.getAll();
    }
}
