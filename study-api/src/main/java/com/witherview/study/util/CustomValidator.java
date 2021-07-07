package com.witherview.study.util;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

@Component
@RequiredArgsConstructor
public class CustomValidator implements Validator {
    private final SpringValidatorAdapter validator;

    @Override
    public boolean supports(Class<?> clazz) {
        return List.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof List) {
            Collection collection = (Collection) target;
            for (Object object : collection) {
                ValidationUtils.invokeValidator(validator, object, errors);
            }
        }
    }
}