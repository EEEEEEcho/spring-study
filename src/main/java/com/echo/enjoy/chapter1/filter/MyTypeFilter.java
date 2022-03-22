package com.echo.enjoy.chapter1.filter;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class MyTypeFilter implements TypeFilter {
    /**
     *
     * @param metadataReader 读取到当前正在扫描的类信息
     * @param metadataReaderFactory 从该工厂可以获取到其他任何类信息
     * @return 是否过滤
     * @throws IOException
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //获取当前扫描到的注解的元数据
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获取当前扫描到的类的元数据
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //获取当前类的资源(其实就是类的路径)
        Resource resource = metadataReader.getResource();
        //获取当前扫描到的类的名字
        String className = classMetadata.getClassName();
        System.out.println("当前扫描的类是: " + className);

        if (className.contains("echo")){
            return true;
        }
        return false;
    }
}
