package me.zhaotb.jvm.cl;


import me.zhaotb.HelloClassLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhaotangbo
 * @since 2020/11/20
 */
public class FileClassLoader extends ClassLoader {

    private String libPath;

    public FileClassLoader(String libPath) {
        this.libPath = libPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(new File(libPath, name.replace(".", "\\") + ".class"))){

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                outputStream.write(buffer, 0, read);
            }
            byte[] data = outputStream.toByteArray();
            return super.defineClass(name, data, 0, data.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("", e);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        FileClassLoader loader = new FileClassLoader("F:\\tmp\\lib");
        Class<?> aClass = loader.loadClass("me.zhaotb.HelloClassLoader");
        Object instance = aClass.newInstance();
        Method say = aClass.getDeclaredMethod("say");
        say.invoke(instance);

        HelloClassLoader helloClassLoader = new HelloClassLoader();
        helloClassLoader.say();
        ClassLoader classLoader = helloClassLoader.getClass().getClassLoader();
        System.out.println(classLoader);
        System.out.println(loader);
        System.out.println("end");
    }
}
