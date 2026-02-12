package training.account.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ValidateUtils {

    private final Validator validator;

    public <T> Map<String, List<String>> validate(T request) {
        return validator.validate(request).stream()
                .collect(Collectors.groupingBy(o -> o.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())));
    }

}
