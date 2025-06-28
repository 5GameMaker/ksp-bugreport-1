# Bug

If Kotlin must rename a method, KSP's experimental `getJvmName` will not return
a correct method name for suspend functions.

This works perfectly fine for regular functions, even if there are suspend funs
in the class.

## Running

```bash
./gradlew build && java -jar build/libs/KtBug1.jar
```
