package com.weshare.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AuthorizationTest {

    @Test
    @DisplayName("All controller methods must have some kind of @Authorize annotation")
    void authorizationAnnotationTest() {
        List<Class<? extends Annotation>> acceptedAnnotations = List.of(PreAuthorize.class, PostAuthorize.class, NoAuthorization.class);
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

        // Only scan classes that have these annotations
        Stream.of(Controller.class, RestController.class)
            .map(AnnotationTypeFilter::new)
            .forEach(provider::addIncludeFilter);

        Set<Class<?>> controllers = provider.findCandidateComponents("com.weshare.controller")
            .stream()
            .map(BeanDefinition::getBeanClassName)
            .map(name -> {
                try {
                    return Class.forName(name);
                }
                catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            })
            .collect(Collectors.toSet());

        List<String> errors = controllers.stream()
            .flatMap(controller -> Stream.of(controller.getDeclaredMethods()))
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> acceptedAnnotations.stream().noneMatch(method::isAnnotationPresent))
            .map(method -> {
                String params = Arrays.stream(method.getParameters())
                    .map(parameter -> parameter.getType().getSimpleName() + " " + parameter.getName())
                    .collect(Collectors.joining(", "));
                return "\t" +
                    method.getDeclaringClass().getSimpleName() + "." +
                    method.getName() + "(" + params + ")";
            })
            .sorted()
            .toList();

        if (!errors.isEmpty()) {
            Assertions.fail("Found " + errors.size() + " methods without @Authorize annotation:\n" +
                String.join("\n", errors));
        }
    }

}







