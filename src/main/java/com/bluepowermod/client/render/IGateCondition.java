package com.bluepowermod.client.render;

import java.util.Map;

@FunctionalInterface
public interface IGateCondition {
    IGateCondition ALWAYS_TRUE = m -> true;
    boolean test(Map<String, Object> map);
}
