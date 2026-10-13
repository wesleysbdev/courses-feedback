package br.com.fiap.feedback.infraestructure.annotation.validarenum;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidarEnumConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidarEnum {

    String message() default "Valor informado não pertence a nenhum dos valores válidos.";

    Class<? extends Enum<?>> enumClass();

    String nomeCampo();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
