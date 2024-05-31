package com.bookkeepersmc.spi;

import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class ModFileScanData {
    private final Set<AnnotationData> annotations = new LinkedHashSet<>();
    private final Set<ClassData> classes = new LinkedHashSet<>();

    public Set<ClassData> getClasses() {
        return classes;
    }

    public Set<AnnotationData> getAnnotations() {
        return annotations;
    }

    public Stream<AnnotationData> getAnnotatedBy(Class<? extends Annotation> type, ElementType elementType) {
        final var anType = Type.getType(type);
        return getAnnotations().stream()
                .filter(ad -> ad.targetType == elementType && ad.annotationType.equals(anType));
    }

    public record ClassData(Type clazz, Type parent, Set<Type> interfaces) {}

    public record AnnotationData(Type annotationType, ElementType targetType, Type clazz, String memberName, Map<String, Object> annotationData) {}
}
