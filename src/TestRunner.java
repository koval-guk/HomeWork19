import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestRunner {
    public static void start(Class<?> c) {
        List<Method> testList = new ArrayList<>();
        Method before = null;
        Method after = null;
        try {
            for (Method method : c.getMethods()) {
                if (method.isAnnotationPresent(BeforeSuite.class)) {
                    if (before != null)
                        throw new NonSingleAnnotationException("Annotation @BeforeSuite used 2 or more times");
                    before = method;
                } else if (method.isAnnotationPresent(AfterSuite.class)) {
                    if (after != null)
                        throw new NonSingleAnnotationException("Annotation @AfterSuite used 2 or more times");
                    after = method;
                } else if (method.isAnnotationPresent(Test.class)) {
                    testList.add(method);
                }
            }
        } catch (NonSingleAnnotationException e) {
            //throw new RuntimeException(e.message);
            System.out.println(e.message);
            return;
        }
        testList.sort(Comparator.comparing(o -> o.getAnnotation(Test.class).priority()));
        try {
            if (before != null) before.invoke(c);
            for (Method method : testList) {
                method.invoke(c);
            }
            if (after != null) after.invoke(c);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
