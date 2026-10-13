package br.com.fiap.feedback.infraestructure.annotation.validarenum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidarEnumConstraintValidator implements ConstraintValidator<ValidarEnum, CharSequence> {
    private Set<String> allowedValues;
    private String nomeCampo;

    @Override
    public void initialize(ValidarEnum constraintAnnotation) {
        this.nomeCampo = constraintAnnotation.nomeCampo();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        allowedValues = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {

        if (value == null || value.toString().isBlank()) {
            return true;
        }

//        if (!allowedValues.contains(value.toString())) {
//            context.disableDefaultConstraintViolation();
//
//            context.buildConstraintViolationWithTemplate(
//                            context.getDefaultConstraintMessageTemplate())
//                    .addPropertyNode(nomeCampo)
//                    .addConstraintViolation();
//            return false;
//        }

        return allowedValues.contains(value.toString());
    }
}
