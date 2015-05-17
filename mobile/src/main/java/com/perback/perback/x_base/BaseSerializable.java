package com.perback.perback.x_base;

import java.io.Serializable;

/**
 * Base class for holder types.
 * : Common key access interface.
 */
public class BaseSerializable implements Serializable
{
	private static final long serialVersionUID = 3835097483173645957L;

	/**
	 * Returns a consistent, unique key for this type of objects.
	 * Useful for ensuring key consistency when putting and retrieving from various bundles (Intents, Dao, SparseArrays, etc.).
	 * @return String key for this type.
	 */
	public String getKey()
	{
		return getClass().getCanonicalName();
	}
}