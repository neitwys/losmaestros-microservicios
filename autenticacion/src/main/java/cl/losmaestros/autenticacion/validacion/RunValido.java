package cl.losmaestros.autenticacion.validacion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RunValidador.class)
public @interface RunValido {

    String message() default "El RUN no es válido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
