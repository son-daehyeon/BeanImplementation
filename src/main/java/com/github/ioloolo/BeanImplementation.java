package com.github.ioloolo;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.ioloolo.annotation.Bean;
import com.github.ioloolo.annotation.Inject;
import com.google.common.reflect.ClassPath;

import lombok.SneakyThrows;

public class BeanImplementation {

	private static Map<String, Object> beans;

	public static void init() {

		loadBeans();
		injectBeans();
	}

	@SneakyThrows(IOException.class)
	private static void loadBeans() {

		beans = ClassPath.from(BeanImplementation.class.getClassLoader())
				.getAllClasses()
				.stream()
				.map(ClassPath.ClassInfo::getName)
				.map(x -> {
					try {
						return Class.forName(x);
					} catch (ClassNotFoundException | NoClassDefFoundError ignored) {
					}

					return null;
				})
				.filter(Objects::nonNull)
				.filter(x -> x.getDeclaredAnnotation(Bean.class) != null)
				.map(x -> {
					try {
						return x.getConstructor().newInstance();
					} catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
						throw new RuntimeException(e);
					} catch (NoSuchMethodException e) {
						throw new RuntimeException("%s의 기본 생성자가 없습니다.".formatted(x.getName()));
					}
				})
				.collect(Collectors.toUnmodifiableMap(x -> x.getClass().getName(), x -> x));
	}

	@SneakyThrows(IOException.class)
	private static void injectBeans() {

		ClassPath.from(BeanImplementation.class.getClassLoader())
				.getAllClasses()
				.stream()
				.map(ClassPath.ClassInfo::getName)
				.map(x -> {
					try {
						return Class.forName(x);
					} catch (ClassNotFoundException | NoClassDefFoundError ignored) {
					}

					return null;
				})
				.filter(Objects::nonNull)
				.map(Class::getDeclaredFields)
				.flatMap(Arrays::stream)
				.peek(x -> x.setAccessible(true))
				.filter(x -> x.getDeclaredAnnotation(Inject.class) != null)
				.filter(x -> Modifier.isStatic(x.getModifiers()) && !Modifier.isFinal(x.getModifiers()))
				.forEach(x -> {
					if (!beans.containsKey(x.getType().getName())) {
						throw new RuntimeException("%s 타입의 Bean이 없습니다.".formatted(x.getType().getName()));
					}

					try {
						System.out.printf("[Inject] %s %s%n", x.getDeclaringClass().getName(), x.getName());
						x.set(null, beans.get(x.getType().getName()));
					} catch (IllegalAccessException ignored) {
					}
				});
	}
}
