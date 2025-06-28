import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.KspExperimental
import java.io.PrintWriter

@OptIn(KspExperimental::class)
class AnnotationProcessor(private val environment: SymbolProcessorEnvironment): SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        for (sym in resolver.getSymbolsWithAnnotation("Annotation")) {
            if (sym !is KSFunctionDeclaration) {
                environment.logger.error("'@Annotation' can only be applied to functions", sym)
                continue
            }
            val name = resolver.getJvmName(sym)!!
            val serviceFile = PrintWriter(environment.codeGenerator.createNewFile(
                dependencies  = Dependencies(true, sources = arrayOf(sym.containingFile!!)),
                packageName   = "",
                fileName      = "META-INF/test.methodname",
                extensionName = ""
            ))
            serviceFile.print(name)
            serviceFile.flush()
            serviceFile.close()
        }

        return emptyList()
    }
}

class AnnotationProcessorProvider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return AnnotationProcessor(environment)
    }
}
