package ru.sevavto.stock.crm.util.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateValidatorConstraint implements ConstraintValidator<DateValidator, String> {
    private String pattern;

    @Override
    public void initialize(DateValidator constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDate localDate = LocalDate.from(DateTimeFormatter.ofPattern(pattern).parse(date));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
