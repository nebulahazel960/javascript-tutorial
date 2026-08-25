package org.dromara.fai.utils;

import org.dromara.fai.exception.FaiRuntimeException;
import org.dromara.fai.utils.functions.FaiFunction;
import org.dromara.fai.wrapper.FaiMethod;
import org.dromara.fai.wrapper.FaiType;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LambdaUtils {

    private final static Map<FaiFunction, SerializedLambda> LAMBDA_CACHE = new ConcurrentHashMap<>();

    private final static Map<FaiFunction, FaiType> LAMBDA_TYPE_CACHE = new ConcurrentHashMap<>();

    private final static Map<FaiFunction, FaiMethod> LAMBDA_METHOD_CACHE = new ConcurrentHashMap<>();

    public static SerializedLambda getLambda(FaiFunction function) {
        return LAMBDA_CACHE.computeIfAbsent(function, k -> {
            try {
                final Method method = function.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(true);
                return (SerializedLambda) method.invoke(function);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new FaiRuntimeException(e);
            }
        });
    }

    public static String getMethodName(FaiFunction function) {
        final SerializedLambda lambda = getLambda(function);
        if (lambda == null) {
            return null;
        }
        return lambda.getImplMethodName();
    }

    public static Object getInvoker(FaiFunction function) {
        final SerializedLambda lambda = getLambda(function);
        if (lambda == null) {
            return null;
        }
        if (lambda.getCapturedArgCount() == 0) {
            return null;
        }
        return lambda.getCapturedArg(0);
    }

    public static FaiMethod getMethod(FaiFunction function) {
        return LAMBDA_METHOD_CACHE.computeIfAbsent(function, k -> {
            final SerializedLambda lambda = getLambda(function);
            if (lambda == null) {
                return null;
            }
            final FaiType type = getType(function);
            if (type == null) {
                return null;
            }
            final String methodName = getMethodName(function);
            if (methodName == null) {
                return null;
            }
            final int paramsNumber = function.numberOfParameters();
            if (lambda.getCapturedArgCount() == 0) {
                return type.staticMethod(methodName, paramsNumber);
            }
            return type.dynamicToolMethod(methodName, paramsNumber);
        });
    }


    public static FaiType getType(FaiFunction function) {
        return LAMBDA_TYPE_CACHE.computeIfAbsent(function, k -> {
            final SerializedLambda lambda = getLambda(function);
            if (lambda == null) {
                return null;
            }
            final String implClass = lambda.getImplClass();
            final String className = implClass.replace('/', '.');
            try {
                final Class<?> clazz = Class.forName(className);
                return FaiType.of(clazz);
            } catch (ClassNotFoundException e) {
                throw new FaiRuntimeException(e);
            }
        });
    }
}
