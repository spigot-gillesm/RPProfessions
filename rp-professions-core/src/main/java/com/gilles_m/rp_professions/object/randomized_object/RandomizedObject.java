package com.gilles_m.rp_professions.object.randomized_object;

import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.*;

@Setter
public class RandomizedObject {

	private static final Random RANDOM = new SecureRandom();

	protected Type type;

	@Getter
	protected double chance;

	/**
	 * Randomly get an entity based on its relative chance.
	 *
	 * @param randomizedObjects the random objects
	 * @param <T> the type
	 * @return a randomly chosen object
	 */
	public static <T extends RandomizedObject> Optional<T> randomRelative(Collection<T> randomizedObjects) {
		final List<T> selectedEntities = new ArrayList<>();
		randomizedObjects.stream()
				.filter(entity -> entity.type == Type.RELATIVE)
				.forEach(entity -> {
					for(int i = 0; i < entity.getChance(); i++) {
						selectedEntities.add(entity);
					}
				});

		if(selectedEntities.isEmpty()) {
			return Optional.empty();
		}
		if(selectedEntities.size() == 1) {
			return Optional.of(selectedEntities.get(0));
		}

		return Optional.of(selectedEntities.get(RANDOM.nextInt(selectedEntities.size())));
	}

	/**
	 * Randomly get an amount between lower and upper bound of random objects based on their relative chance.
	 *
	 * @param randomizedObjects the random objects
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 * @param <T> the type
	 * @return a randomly chosen object
	 */
	public static <T extends RandomizedObject> Set<T> randomRelative(Collection<T> randomizedObjects, final int lowerBound,
																	 final int upperBound) {

		//Generate a random int between lower bound and upper bound
		int amount = RANDOM.nextInt(upperBound - lowerBound + 1) + lowerBound;

		if(amount < 1) {
			amount = 1;
		}
		final Set<T> selectedObjects = new HashSet<>();
		Optional<T> nextRandom = randomRelative(randomizedObjects);
		int i = 0;

		//Put the required amount of random objects in the list
		while(nextRandom.isPresent() && i < amount) {
			selectedObjects.add(nextRandom.get());
			i++;
			nextRandom = randomRelative(randomizedObjects);
		}

		return selectedObjects;
	}

	/**
	 * Randomly get entities based on its absolute chance (= probability).
	 *
	 * @param randomizedObjects the random objects
	 * @param <T> the type
	 * @return a list of randomly chosen objects
	 */
	public static <T extends RandomizedObject> List<T> randomAbsolute(Collection<T> randomizedObjects) {
		final List<T> selectedEntities = new ArrayList<>();
		randomizedObjects.stream()
				.filter(entity -> entity.type == Type.ABSOLUTE)
				.forEach(entity -> {
					if(RANDOM.nextDouble() <= entity.chance) {
						selectedEntities.add(entity);
					}
				});

		return selectedEntities;
	}

	public enum Type {

		RELATIVE,
		ABSOLUTE

	}

}
