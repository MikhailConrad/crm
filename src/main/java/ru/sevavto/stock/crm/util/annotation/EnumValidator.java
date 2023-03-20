package ru.sevavto.stock.crm.util.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidatorConstraint.class)
@NotNull
public @interface EnumValidator {
    Class<? extends Enum<?>> enumClass();
    String message() default "Неверный статус заказа";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
