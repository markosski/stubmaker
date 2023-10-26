package stubmaker.annotation;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import org.junit.Test;

import java.net.MalformedURLException;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class ImplementFakeProcessorTest {
    @Test
    public void EmptyClassCompiles() throws MalformedURLException {
//        Compilation compilation =
//                javac()
//                        .withProcessors(new ImplementFakeProcessor())
//                        .compile(JavaFileObjects.forResource("UserRepo.java"));
//        assertThat(compilation).succeeded();
//        assertThat(compilation)
//                .generatedSourceFile("UserRepoFake");
    }
}
