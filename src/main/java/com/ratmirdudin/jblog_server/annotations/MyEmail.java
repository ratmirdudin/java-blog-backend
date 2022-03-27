package com.ratmirdudin.jblog_server.annotations;

import com.ratmirdudin.jblog_server.validations.MyEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = MyEmailValidator.class)
public @interface MyEmail {
    String message() default "Email should be name@domain.example formatted";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
