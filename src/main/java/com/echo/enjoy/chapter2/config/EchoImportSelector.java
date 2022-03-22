package com.echo.enjoy.chapter2.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class EchoImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{"com.echo.enjoy.chapter2.pojo.Fish","com.echo.enjoy.chapter2.pojo.Cow"};
    }
}
