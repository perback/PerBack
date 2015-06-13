package com.perback.perback.apis.ean;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class EANDeserializer<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException
    {
        // Get the "content" element from the parsed JSON
        String[] typeSplitted = ((Class) type).getName().split("\\.");
        String typeStr = typeSplitted[typeSplitted.length-1];
        JsonElement content = je.getAsJsonObject().get(typeStr);

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(content, type);

    }
}
