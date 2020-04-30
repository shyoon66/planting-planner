package com.yoonbae.planting.planner.validator;

public interface Validator {
    <T> String validate(T object);
}
