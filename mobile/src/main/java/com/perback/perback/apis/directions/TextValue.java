package com.perback.perback.apis.directions;

import com.perback.perback.x_base.BaseSerializable;

public class TextValue extends BaseSerializable {

    protected String text;
    protected long value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
