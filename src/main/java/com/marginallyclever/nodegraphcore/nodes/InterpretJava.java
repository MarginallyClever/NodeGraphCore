package com.marginallyclever.nodegraphcore.nodes;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.Packet;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interpret a Java source file.  The source file must contain a single public class called <code>InterpretJava</code>
 * with a single method called <code>execute</code> that returns a {@link String}.
 * <pre>
 * public class InterpretJava {
 *     public String execute() {
 *         return "Hello, World!";
 *     }
 * }</pre>
 */
public class InterpretJava extends Node {
    private final Input<String> value = new Input<>("code",String.class,"");
    private final Output<String> result = new Output<>("result",String.class,"");
    private boolean done=false;

    /**
     * Constructor for subclasses to call.
     */
    public InterpretJava() {
        super("InterpretJava");
        addVariable(value);
        addVariable(result);
    }

    @Override
    public void update() {
        if(done) return;

        try {
            Class<?> dynamicClass = compileAndLoad(value.getValue(), "InterpretJava");
            // Instantiate and execute the code
            Object instance = dynamicClass.getDeclaredConstructor().newInstance();
            Method method = dynamicClass.getDeclaredMethod("execute");
            result.send(new Packet<>((String) method.invoke(instance)));
        }
        catch (Exception e) {
            result.send(new Packet<>(e.getMessage()));
        }

        done=true;
    }

    @Override
    public void reset() {
        super.reset();
        done=false;
    }

    public static Class<?> compileAndLoad(String code, String className) throws Exception {
        // Get the Java Compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("Cannot find Java Compiler. Make sure you're running with a JDK.");
        }

        // Create an in-memory JavaFileObject
        JavaFileObject javaFile = new InMemoryJavaFileObject(className, code);

        // Compile the code
        JavaFileManager fileManager = new InMemoryClassFileManager(compiler.getStandardFileManager(null, null, null));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, List.of(javaFile));
        if (!task.call()) {
            throw new RuntimeException("Compilation failed.");
        }

        // Load the compiled class
        return fileManager.getClassLoader(null).loadClass(className);
    }

    // In-memory representation of a source file
    static class InMemoryJavaFileObject extends SimpleJavaFileObject {
        private final String sourceCode;

        public InMemoryJavaFileObject(String className, String sourceCode) {
            super(java.net.URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.sourceCode = sourceCode;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return sourceCode;
        }
    }

    // Custom FileManager to handle in-memory class files
    static class InMemoryClassFileManager extends ForwardingJavaFileManager<JavaFileManager> {
        private final Map<String, InMemoryClassFile> classFiles = new HashMap<>();

        public InMemoryClassFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
            InMemoryClassFile classFile = new InMemoryClassFile(className);
            classFiles.put(className, classFile);
            return classFile;
        }

        @Override
        public ClassLoader getClassLoader(Location location) {
            return new ClassLoader(getClass().getClassLoader()) {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    InMemoryClassFile classFile = classFiles.get(name);
                    if (classFile == null) {
                        throw new ClassNotFoundException(name);
                    }
                    byte[] bytes = classFile.getBytes();
                    return defineClass(name, bytes, 0, bytes.length);
                }
            };
        }
    }

    // In-memory representation of a compiled class file
    static class InMemoryClassFile extends SimpleJavaFileObject {
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        public InMemoryClassFile(String className) {
            super(java.net.URI.create("string:///" + className.replace('.', '/') + Kind.CLASS.extension), Kind.CLASS);
        }

        @Override
        public OutputStream openOutputStream() {
            return outputStream;
        }

        public byte[] getBytes() {
            return outputStream.toByteArray();
        }
    }
}
