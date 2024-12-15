package org.sberuniversity.proxy;

import java.io.Serializable;
import java.util.Arrays;


public class ArgsKey implements Serializable {
    private Object[] args;
    private static final long serialVersionUID = 362498820763181465L;

    public ArgsKey(Object[] args) {
        this.args = args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArgsKey argsKey = (ArgsKey) o;
        return Arrays.equals(args, argsKey.args);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(args);
    }
}
