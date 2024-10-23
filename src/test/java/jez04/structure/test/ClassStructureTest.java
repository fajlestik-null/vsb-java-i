package jez04.structure.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

class ClassStructureTest {

	private static final String drawableSimulableName = "DrawableSimulable";
	private static final String collisionableName = "Collisionable";

	Set<String> allClasses = getNameOfAllClasses();

	@Test
	void interfaceExistencePropertiesTest() {
		classExist(drawableSimulableName);
		Class<?> c = getClass(drawableSimulableName);
		isInterface(c);
		hasMethod(c, "draw", void.class, GraphicsContext.class);
		hasMethod(c, "simulate", void.class);
	}

	@Test
	void interface2ExistencePropertiesTest() {
		classExist(collisionableName);
		Class<?> c = getClass(collisionableName);
		isInterface(c);
		hasMethod(c, "getBoundingBox", Rectangle2D.class);
		hasMethod(c, "intersect", boolean.class, Rectangle2D.class);
		hasMethod(c, "hitBy", void.class, c);
	}

	@Test
	void movingClassTest() {
		classExist("LochNess");
		Class<?> c = getClass("LochNess");
		hasImplements(c, collisionableName);
		hasExtends(c, "WorldEntity");

		hasMethod(c, "draw", void.class, GraphicsContext.class);
		hasMethod(c, "drawInternal", void.class, GraphicsContext.class);
	}

	@Test
	void abstractClassTest() {
		classExist("WorldEntity");
		Class<?> c = getClass("WorldEntity");
		hasImplements(c, drawableSimulableName);

		hasMethod(c, true, false, "draw", void.class, GraphicsContext.class);
		hasMethod(c, false, true, "drawInternal", void.class, GraphicsContext.class);
	}

	@Test
	void groupClassPropertyTest() {
		classExist("lab.Scene");
		Class<?> c = getClass("lab.Scene");
		hasMethod(c, "draw");
		hasMethod(c, "simulate");
		Class<?> drawable = getClass(drawableSimulableName);
		hasProperty(c, ".*", drawable, true);
	}

	@Test
	void drawingClassExistenceTest() {
		Set<String> allClasses = getNameOfAllClasses();
		int detectedClassCount = 0;
		for (String className : allClasses) {
			try {
				Class<?> clazz = Class.forName(className);
				List<Constructor<?>> constructorList = Arrays.asList(clazz.getConstructors());
				List<Method> methodList = Arrays.asList(clazz.getMethods());
				long drawMethodCount = methodList.stream().filter(m -> m.getName().contains("draw")).count();
				long simulateMethodCount = methodList.stream().filter(m -> m.getName().contains("sim")).count();
				if (!constructorList.isEmpty() && (drawMethodCount > 0 || simulateMethodCount > 0)) {
					System.out.println("DETECT:" + className);
					detectedClassCount++;
				}
			} catch (ClassNotFoundException e) {
				System.out.println(String.format("Class '%s' cannot be loaded: %s", className, e.getMessage()));
			}
		}
		assertTrue(detectedClassCount >= 2, "");
	}

	private void isInterface(Class<?> c) {
		assertTrue(c.isInterface(), c.getName() + " have to be interface.");
	}

	private void classExist(String name) {
		assertTrue(allClasses.stream().anyMatch(c -> c.endsWith(name)), "Interface " + name + " not found");
	}

	private Class<?> getClass(String name) {
		String className = allClasses.stream().filter(c -> c.endsWith(name)).findAny().orElse(null);
		if (className == null) {
			Assertions.fail("Class " + name + " not found.");
		}
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (PrintStream ps = new PrintStream(baos, true)) {
				e.printStackTrace(ps);
			} catch (Exception e2) {
				Assertions.fail(e2.getMessage());
			}
			String stackTrace = baos.toString();
			Assertions.fail("Class " + name + " not found.\n" + stackTrace);
			return null;
		}
	}

	private void hasProperty(Class<?> classDef, String propertyNameRegexp, Class<?> type, boolean array) {
		List<Field> fields = Arrays.asList(classDef.getDeclaredFields());
		assertTrue(fields.stream().anyMatch(f -> {
			if (f.getName().matches(propertyNameRegexp)) {
				if (array) {
					return f.getType().isArray() && f.getType().getComponentType().equals(type);
				} else {
					return f.getType().equals(type);
				}
			}
			return false;
		}), "No field " + propertyNameRegexp + " of type " + type.getName() + " (is array " + array + ") in class "
				+ classDef.getName());
	}

	private void hasMethod(Class<?> interfaceDef, String methodName, Class<?> returnType) {
		List<Method> methods = Arrays.asList(interfaceDef.getMethods());
		assertTrue(methods.stream().anyMatch(m -> m.getName().contains(methodName)), "No method " + methodName);
		assertTrue(
				methods.stream().filter(m -> m.getName().contains(methodName))
						.anyMatch(m -> m.getReturnType().equals(returnType)),
				"Method " + methodName + " not return " + returnType.getName());
	}

	private void hasMethod(Class<?> interfaceDef, String methodName, Class<?> returnType, Class<?>... params) {
		List<Method> methods = Arrays.asList(interfaceDef.getMethods());
		assertTrue(methods.stream().anyMatch(m -> m.getName().contains(methodName)), "No method " + methodName);
		assertTrue(
				methods.stream().filter(m -> m.getName().contains(methodName))
						.filter(m -> m.getReturnType().equals(returnType))
						.anyMatch(m -> Arrays.asList(m.getParameterTypes()).containsAll(Arrays.asList(params))),
				"Method " + methodName + " has no all parrams:"
						+ Arrays.asList(params).stream().map(Class::getName).collect(Collectors.joining(", ")));
	}

	private void hasMethod(Class<?> interfaceDef, boolean finalTag, boolean abstractTag, String methodName,
			Class<?> returnType, Class<?>... params) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		assertTrue(methods.stream().anyMatch(m -> m.getName().contains(methodName)), "No method " + methodName);
		assertTrue(
				methods.stream().filter(m -> m.getName().contains(methodName))
						.filter(m -> m.getReturnType().equals(returnType)
								&& (Modifier.isAbstract(m.getModifiers()) == abstractTag)
								&& (Modifier.isFinal(m.getModifiers()) == finalTag))
						.anyMatch(m -> Arrays.asList(m.getParameterTypes()).containsAll(Arrays.asList(params))),
				"Method " + methodName + " has no all params:"
						+ Arrays.asList(params).stream().map(Class::getName).collect(Collectors.joining(", ")));
	}

	private void hasImplements(Class<?> clazz, String... interfaceNames) {
		List<Class<?>> interfaces = new ArrayList<>();
		Arrays.asList(interfaceNames).stream().map(name -> getClass(name)).forEach(c -> interfaces.add(c));
		assertTrue(Arrays.asList(clazz.getInterfaces()).containsAll(interfaces), "Class not implements all interfaces:"
				+ interfaces.stream().map(Class::getName).collect(Collectors.joining(", ")));
	}

	private void hasExtends(Class<?> clazz, String parentName) {
		Class<?> parent = getClass(parentName);
		assertTrue(clazz.getSuperclass().equals(parent),
				"Class " + clazz.getName() + " not extends class " + parentName);
	}

	private void hasMethod(Class<?> interfaceDef, String methodName) {
		List<Method> methods = Arrays.asList(interfaceDef.getMethods());
		assertTrue(methods.stream().anyMatch(m -> m.getName().contains(methodName)), "No method " + methodName);
	}

	private Set<String> getNameOfAllClasses() {
		List<String> initClassesName = Arrays.asList("lab.Routines", "lab.App", "lab.DrawingThread");
		for (String className : initClassesName) {
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				System.out.println(String.format("Class '%s' cannot be loaded: %s", className, e.getMessage()));
			}
		}
		Set<String> allClasses = new HashSet<>();
		for (Package p : Package.getPackages()) {
			if (p.getName().startsWith("java.") || p.getName().startsWith("com.") || p.getName().startsWith("jdk.")
					|| p.getName().startsWith("javafx.") || p.getName().startsWith("org.")
					|| p.getName().startsWith("sun.") || p.getName().startsWith("javax.")
					|| p.getName().startsWith("javassist")) {
				continue;
			}
			System.out.println(p.getName());
			Configuration conf = new ConfigurationBuilder().addScanners(Scanners.SubTypes.filterResultsBy(pc -> true))
					.forPackages(p.getName());
			Reflections reflections = new Reflections(conf);
			allClasses.addAll(reflections.getAll(Scanners.SubTypes.filterResultsBy(c -> {
				System.out.println(c);
				return true;
			})));
		}
		return allClasses;
	}
}
