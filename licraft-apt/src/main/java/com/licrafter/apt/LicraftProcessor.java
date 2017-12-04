package com.licrafter.apt;

import com.google.auto.service.AutoService;
import com.licrafter.apt.annotation.SetGet;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by shell on 2017/12/4.
 * <p>
 * Github: https://github.com/shellljx
 */
@AutoService(Processor.class)
public class LicraftProcessor extends AbstractProcessor{

    private Elements mElementUtils;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager.printMessage(Diagnostic.Kind.NOTE,"processor process");
        mElementUtils  = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mMessager.printMessage(Diagnostic.Kind.NOTE,"processor process");
        Set<? extends Element> bindViewElements = roundEnv.getElementsAnnotatedWith(SetGet.class);
        for (Element element:bindViewElements){
            PackageElement packageElement = mElementUtils.getPackageOf(element);
            String pkName = packageElement.getQualifiedName().toString();
            File file = new File(pkName+"hahaha.yml");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        mMessager.printMessage(Diagnostic.Kind.NOTE,"processor process");

        return super.getSupportedAnnotationTypes();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        mMessager.printMessage(Diagnostic.Kind.NOTE,"processor process");

        return super.getSupportedSourceVersion();
    }
}
