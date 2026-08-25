package org.dromara.fai.wrapper;

import com.dtflys.forest.Forest;
import org.dromara.fai.exception.FaiDuplicateToolNameException;
import org.dromara.fai.tools.FaiTool;
import org.dromara.fai.tools.FaiToolProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FaiObject implements FaiToolProvider {

    private final String name;
    
    private final Object object;
    
    private final FaiType type;

    private final Map<String, FaiTool> allTools = new ConcurrentHashMap<>();

    public static FaiObject of(String name, Object object) {
        return new FaiObject(name, object);
    }

    public static FaiObject of(String name, Class<?> clazz) {
        return new FaiObject(name, clazz);
    }


    public static FaiObject of(Object object) {
        return new FaiObject(object);
    }

    public static FaiObject of(Class<?> clazz) {
        return new FaiObject(clazz);
    }

    private FaiObject(Object object) {
        this(nameOfClassInstance(object.getClass()), object);
    }

    private FaiObject(Class<?> clazz) {
        this(nameOfClassInstance(clazz), clazz);
    }

    private FaiObject(String name, Object object) {
        this.name = name;
        this.object = object;
        this.type = FaiType.of(object.getClass());
        initTools();
    }

    private FaiObject(String name, Class<?> clazz) {
        this.name = name;
        this.object = Forest.config().getForestObject(clazz);
        this.type = FaiType.of(clazz);
        initTools();
    }

    private static String nameOfClassInstance(Class<?> clazz) {
        String name = clazz.getSimpleName();
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    private void initTools() {
        for (final FaiMethod dynamicMethod : type.dynamicMethods()) {
            final String name = dynamicMethod.name();
            if (dynamicMethod.isTool()) {
                if (allTools.containsKey(name)) {
                    throw new FaiDuplicateToolNameException(name);
                }
                allTools.put(name, FaiTool.tool(object, dynamicMethod));
            }
        }
    }

    public String name() {
        return name;
    }

    public Object javaObject() {
        return object;
    }
    
    public FaiType type() {
        return type;
    }
    
    public Class<?> javaClass() {
        return type.javaClass();
    }


    @Override
    public List<FaiTool> tools() {
        return new ArrayList<>(allTools.values());
    }

    public FaiTool tool(String name) {
        return allTools.get(name);
    }

    public FaiTool tool(FaiMethod method) {
        return FaiTool.tool(object, method);
    }




}
