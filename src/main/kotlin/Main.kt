annotation class Annotation

class TestClass {
    @Annotation
    private suspend fun testMethod(arg: UInt?) {}
    private fun testMethod(arg: String) {}
}

fun main() {
    val k = TestClass()
    val methodName = k.javaClass.classLoader.getResourceAsStream("META-INF/test.methodname").readBytes().toString(charset("UTF-8"))
    println("Expected to find: ${methodName}")
    val method = k::class.java.declaredMethods.find { it.name == methodName }
    if (method == null) {
        println("No method found!")
        val otherName = k::class.java.declaredMethods.find {
            it.name.startsWith("testMethod")
            && it.parameters.any { it.type.canonicalName == "kotlin.UInt" }
        }!!.name
        println("Possible actual method: $otherName")
    } else {
        println("Found method: ${method.name}")
    }
}
