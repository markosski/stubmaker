package fakegen.annotation;

import com.google.auto.service.AutoService;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

@SupportedAnnotationTypes("fakegen.annotation.ImplementStub")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class ImplementStubProcessor extends AbstractProcessor {
    static String SUFFIX = "Stub";
    Logger logger = LoggerFactory.getLogger(ImplementStubProcessor.class);

    static public class MethodParam {
        private final String paramName;
        private final String paramType;
        public MethodParam(String paramName, String paramType) {
            this.paramName = paramName;
            this.paramType = paramType;
        }
        public String getParamName() {
            return paramName;
        }
        public String getParamType() {
            return paramType;
        }
        @Override
        public String toString() {
            return paramType + " " + paramName;
        }
    }

    static public class MetaData {
        private final String methodName;
        private final String returnType;
        private final List<MethodParam> params;
        public MetaData(String methodName, String returnType, List<MethodParam> params) {
            this.methodName = methodName;
            this.returnType = returnType;
            this.params = params;
        }
        public String getMethodName() {
            return methodName;
        }
        public String getReturnType() {
            return returnType;
        }
        public List<MethodParam> getParams() {
            return params;
        }
        @Override
        public String toString() {
            return methodName + "; " + returnType + "; " + params;
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

            for (Element element : annotatedElements) {
                var metaData = new ArrayList<MetaData>();
                logger.info("elements: " + annotatedElements);
                TypeElement clazz = (TypeElement) element;
                var elements = clazz.getEnclosedElements();

                elements.stream()
                        .filter(x -> x.getKind() == ElementKind.METHOD)
                        .forEach(m -> {
                            var method = (ExecutableType) m.asType();
                            var methodExec = (ExecutableElement) m;

                            var methodParamNames = new ArrayList<String>();
                            for (VariableElement p : methodExec.getParameters()) {
                                methodParamNames.add(p.getSimpleName().toString());
                            }

                            var methodParamTypes = new ArrayList<String>();
                            for (TypeMirror p : method.getParameterTypes()) {
                                methodParamTypes.add(p.toString());
                            }

                            var params = new ArrayList<MethodParam>();
                            if (methodParamNames.size() == methodParamTypes.size()) {
                                var i = 0;
                                for (String name : methodParamNames) {
                                    params.add(new MethodParam(name, methodParamTypes.get(i)));
                                    i++;
                                }
                            }
                            metaData.add(new MetaData(m.getSimpleName().toString(), method.getReturnType().toString(), params));
                            System.out.println(params);
                });
                try {
                    writeBuilderFile(clazz.toString(), clazz.toString(), metaData);
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        return true;
    }

    private void writeBuilderFile(String className, String interfaceName, ArrayList<MetaData> data) throws IOException, URISyntaxException {
        logger.info("Stubmaker start for class " + className);
        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }
        String builderClassName = className.substring(lastDot + 1) + SUFFIX;
        JavaFileObject builderFile = processingEnv.getFiler()
                .createSourceFile(builderClassName);

        String inputTemplate = "templates/ClassNameStub.vm";
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init();

        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);
        context.put("className", builderClassName);
        context.put("interfaceName", interfaceName);
        context.put("methods", data);

        Writer writer = builderFile.openWriter();
        velocityEngine.mergeTemplate(inputTemplate, "UTF-8", context, writer);
        writer.flush();
        writer.close();

        logger.info("Stubmaker completed " + builderFile);
    }
}