package cl.losmaestros.autenticacion.validacion;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RunValidador implements ConstraintValidator<RunValido, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        String normalizado = normalizar(value);
        if (normalizado == null) {
            return false;
        }
        int guion = normalizado.indexOf('-');
        if (guion < 0) {
            return false;
        }
        String cuerpo = normalizado.substring(0, guion);
        String dv = normalizado.substring(guion + 1);
        if (cuerpo.length() < 7 || cuerpo.length() > 8) {
            return false;
        }
        for (int i = 0; i < cuerpo.length(); i++) {
            if (!Character.isDigit(cuerpo.charAt(i))) {
                return false;
            }
        }
        if (cuerpo.chars().allMatch(c -> c == '0')) {
            return false;
        }
        return dv.equals(calcularDv(cuerpo));
    }

    public static String normalizar(String run) {
        if (run == null) {
            return null;
        }
        String limpio = run.trim().replace(".", "").replace("-", "").toUpperCase();
        if (limpio.length() < 2) {
            return null;
        }
        String cuerpo = limpio.substring(0, limpio.length() - 1);
        char dv = limpio.charAt(limpio.length() - 1);
        return cuerpo + "-" + dv;
    }

    private static String calcularDv(String cuerpo) {
        int suma = 0;
        int factor = 2;
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(cuerpo.charAt(i)) * factor;
            factor = factor == 7 ? 2 : factor + 1;
        }
        int resto = 11 - (suma % 11);
        if (resto == 11) {
            return "0";
        }
        if (resto == 10) {
            return "K";
        }
        return String.valueOf(resto);
    }
}
