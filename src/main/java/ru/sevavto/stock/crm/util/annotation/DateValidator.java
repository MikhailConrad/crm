package ru.sevavto.stock.crm.util.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidatorConstraint.class)
public @interface DateValidator {
    String pattern();
    String message() default "Неверно задана дата. Формат: дд.мм.гггг";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
