package com.gilles_m.rp_professions.loader;

import com.gilles_m.rp_professions.Identifiable;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.modelmapper.MappingException;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * An iterable designed for one-by-one object loading and mapping.
 *
 * @param <T> the final object's type
 * @param <O> the object's type from which the final object is mapped/built
 */
abstract class LoaderIterable<T extends Identifiable, O> implements Iterable<T> {

    private static final String ERROR_STRING = "Error loading %s";

	@Nullable
	protected File file;

	protected Map<String, O> objectMap;

	private List<String> keys;

	private int i = -1;

	protected LoaderIterable() { }

	/**
	 * (re)Initialize this iterable.
	 *
	 * @param file the file from which the data are loaded
	 *
	 */
	public void init(@Nullable File file) throws IOException {
		this.file = file;
		this.objectMap = initMap();
		this.keys = objectMap.keySet().stream().toList();
		this.i = 0;
	}

	protected abstract Map<String, O> initMap() throws IOException;

	protected abstract T buildObject(String id, O object);

	@NotNull
	@Override
	public Iterator<T> iterator() {
		if(i == -1) {
			throw new IllegalStateException("The iterable must be initialized");
		}

		return new LoaderIterator();
	}

	class LoaderIterator implements Iterator<T> {

		@Override
		public boolean hasNext() {
			return i < keys.size();
		}

		@Override
		public T next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
            final String key = keys.get(i);

            try {
                return buildObject(key, objectMap.get(key));
            } catch (IllegalArgumentException exception) {
                Formatter.error(String.format(ERROR_STRING, key));
                Formatter.error(exception.getMessage());
                Throwable cause = exception.getCause();

                while(cause != null) {
                    Formatter.error(String.format("Cause: %s", cause.getMessage()));
                    cause = cause.getCause();
                }
            } catch (MappingException exception) {
                Formatter.error(String.format(ERROR_STRING, key));
                //Do not display error message when dealing with MappingException as they contain no valuable info for the user
                Throwable cause = exception.getCause().getCause();

                //Only show the cause
                while(cause != null) {
                    Formatter.error(String.format("Cause: %s", cause.getMessage()));
                    cause = cause.getCause();
                }
            } finally {
                i++;
            }

            return null;
		}

	}

	abstract static class DoubleMapIterable<T extends Identifiable, A, B> extends LoaderIterable<T, A> {

		protected Map<String, B> objectMapB;

		protected DoubleMapIterable() {
			super();
		}

		@Override
		public void init(@Nullable File file) throws IOException {
			super.init(file);

			this.objectMapB = initMapB();
		}

		@Override
		protected final Map<String, A> initMap() throws IOException {
			return initMapA();
		}

		@Override
		protected final T buildObject(String id, A objectA) {
			return buildObject(id, objectA, objectMapB.get(id));
		}

		protected abstract Map<String, A> initMapA() throws IOException;

		protected abstract Map<String, B> initMapB() throws IOException;

		protected abstract T buildObject(String id, A objectA, B objectB);

	}

}
