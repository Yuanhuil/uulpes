package com.njpes.www.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * reflect class
 */
public class ReflectUtils {

    /**
     * 得到指定类型的指定位置的泛型实参
     *
     * @param clazz
     * @param index
     * @param <T>
     * @return
     */
    public static <T> Class<T> findParameterizedType(Class<?> clazz, int index) {
        Type parameterizedType = clazz.getGenericSuperclass();
        // CGLUB subclass target object(泛型在父类上)
        if (!(parameterizedType instanceof ParameterizedType)) {
            parameterizedType = clazz.getSuperclass().getGenericSuperclass();
        }
        if (!(parameterizedType instanceof ParameterizedType)) {
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) parameterizedType).getActualTypeArguments();
        if (actualTypeArguments == null || actualTypeArguments.length == 0) {
            return null;
        }
        return (Class<T>) actualTypeArguments[0];
    }

    /**
     * 将父类所有的属性COPY到子类中。 类定义中child一定要extends father；
     * 而且child和father一定为严格javabean写法， 必须是有set和get方法
     * 
     * @author 赵忠诚
     */
    public static void fatherToChild(Object father, Object child) {
        if (!(child.getClass().getSuperclass() == father.getClass())) {
            System.err.println("child不是father的子类");
        }
        Class fatherClass = father.getClass();
        Method ff[] = fatherClass.getMethods();
        Method cc[] = child.getClass().getMethods();

        for (int i = 0; i < ff.length; i++) {
            Method f = ff[i];// 取出每一个属性，如deleteDate
            if (!f.getName().startsWith("get"))
                continue;
            try {
                Object obj = f.invoke(father);// 取出属性值
                if (obj == null)
                    continue;
                for (int j = 0; j < cc.length; j++) {
                    Method child_m = cc[j];
                    if (child_m.getName().equals(f.getName().replace("get", "set"))) {
                        child_m.invoke(child, obj);
                        break;
                    }

                }
                // Method child_m =
                // child.getClass().getMethod(f.getName().replace("get",
                // "set"));

            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 首字母大写，in:deleteDate，out:DeleteDate
     * 
     * @param in
     *            输入字符串
     * @return out 输出字符串
     * @author 赵忠诚
     */
    private static String upperHeadChar(String in) {
        String head = in.substring(0, 1);
        String out = head.toUpperCase() + in.substring(1, in.length());
        return out;
    }
}
