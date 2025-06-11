package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.Identifiable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class ObjectManager<T extends Identifiable> {

	protected final List<T> registeredObjects = new ArrayList<>();

	protected ObjectManager() { }

	public void register(@NotNull T object) {
		registeredObjects.add(object);
	}

	public Optional<T> get(@NotNull String id) {
		return registeredObjects.stream().filter(o -> o.getId().equals(id)).findFirst();
	}

	public List<T> getAll() {
		return Collections.unmodifiableList(registeredObjects);
	}

	public void clear() {
		registeredObjects.clear();
	}

	public int size() {
		return registeredObjects.size();
	}

}
