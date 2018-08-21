package com.tsm.template.util;

import com.tsm.template.dto.BaseDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class BaseDTOTest {

    protected Validator validator;

    protected void checkResource(Supplier<? extends BaseDTO> resource, final String attribute, final Object value,
                                 final String message) {
        assertResource(resource.get(), attribute, value, message);
    }

    public static Integer getRandomInt(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end + 1);
    }

    protected <T extends BaseDTO> void assertResource(final T t, final String attribute, final Object value,
                                                      final String message) {
        ReflectionTestUtils.setField(t, attribute, value);
        // Do test
        Set<ConstraintViolation<T>> result = validator.validate(t, Default.class);

        // Assertions
        assertNotNull(result);
        if (Objects.nonNull(message)) {
            assertThat(result.size(), is(1));
            assertThat(result.iterator().next().getMessageTemplate(), is(message));
        } else {
            assertThat(result.size(), is(0));
        }

    }

}
