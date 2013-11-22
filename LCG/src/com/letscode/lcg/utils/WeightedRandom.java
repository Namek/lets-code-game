package com.letscode.lcg.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

/**
 * Stores a collection of objects along with a float associated with each that is the object's weight for randomizing.
 * Weights can be any value, and the random output is chosen based on the item weight's size relative to all other item
 * weights in the collection.
 * 
 * @author trey miller
 */
public class WeightedRandom<T> {

	private final FloatArray weights = new FloatArray();
	private final Array<T> objects = new Array<T>();
	private float totalPossible;

	/** adds the item if it doesn't exist in the collection (checked by identity), otherwise just sets the weight. */
	public void setItem(float weight, T object) {
		boolean existed = false;
		for (int i = 0; i < weights.size; i++) {
			if (objects.get(i) == object) {
				weights.set(i, Math.max(weight, 0f));
				existed = true;
				break;
			}
		}
		if (!existed) {
			weights.add(weight);
			objects.add(object);
		}
		setTotalPossible();
	}

	/** simply removes the item, by identity. */
	public T removeItem(T object) {
		T result = null;
		for (int i = 0; i < weights.size; i++) {
			if (objects.get(i) == object)
				result = objects.removeIndex(i);
		}
		setTotalPossible();
		return result;
	}

	private void setTotalPossible() {
		totalPossible = 0f;
		for (int i = 0; i < weights.size; i++) {
			totalPossible += weights.get(i);
		}
	}

	public void clear() {
		weights.clear();
		objects.clear();
		totalPossible = 0f;
	}

	/** remove if you want the object to be removed from the collection */
	public T random(boolean remove) {
		if (weights.size < 1)
			return null;
		T result = null;
		float r = MathUtils.random(totalPossible);
		float accum = 0;
		for (int i = 0; i < weights.size; i++)
			if (r <= (accum += weights.get(i))) {
				result = objects.get(i);
				break;
			}

		if (remove)
			removeItem(result);
		return result;
	}

	/** returns the object's weight, or 0f if the object is not found */
	public float getWeight(T object) {
		for (int i = 0; i < weights.size; i++)
			if (objects.get(i) == object)
				return weights.get(i);
		return 0f;
	}

	/** sets totalPossible to 1f and adjusts all weights accordingly. never called automatically. */
	public void normalize() {
		for (int i = 0; i < weights.size; i++)
			weights.set(i, weights.get(i) / totalPossible);
		totalPossible = 1f;
	}
}
