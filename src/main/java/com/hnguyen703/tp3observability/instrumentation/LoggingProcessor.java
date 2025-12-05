package com.hnguyen703.tp3observability.instrumentation;

import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.processing.AbstractProcessor;

public class LoggingProcessor extends AbstractProcessor<CtClass<?>> {

    @Override
    public boolean isToBeProcessed(CtClass<?> candidate) {
        return candidate.getSimpleName().equals("ProductService");
    }

    @Override
    public void process(CtClass<?> ctClass) {
        // 1. Add Logger Field
        getFactory().Field().create(
                ctClass,
                java.util.Set.of(spoon.reflect.declaration.ModifierKind.PRIVATE, spoon.reflect.declaration.ModifierKind.STATIC, spoon.reflect.declaration.ModifierKind.FINAL),
                getFactory().createCtTypeReference(org.slf4j.Logger.class),
                "logger",
                getFactory().createCodeSnippetExpression("org.slf4j.LoggerFactory.getLogger(" + ctClass.getSimpleName() + ".class)")
        );

        // 2. Iterate methods and inject dynamic logs
        for (CtMethod<?> method : ctClass.getMethods()) {
            String methodName = method.getSimpleName();
            String operationType = determineOperationType(methodName);

            // We use "+ userId +" because we know the variable exists in the method scope now
            String logStatement = String.format(
                    "logger.info(\"{\\\"timestamp\\\": \\\"\" + java.time.Instant.now() + \"\\\", " +
                            "\\\"user\\\": \\\"\" + userId + \"\\\", " +
                            "\\\"type\\\": \\\"%s\\\", " +
                            "\\\"method\\\": \\\"%s\\\"}\");",
                    operationType,
                    methodName
            );

            CtCodeSnippetStatement snippet = getFactory().createCodeSnippetStatement(logStatement);
            method.getBody().insertBegin(snippet);
        }
    }

    private String determineOperationType(String name) {
        if (name.startsWith("get") || name.startsWith("fetch")) return "READ";
        if (name.startsWith("add") || name.startsWith("delete") || name.startsWith("update")) return "WRITE";
        return "OTHER";
    }
}