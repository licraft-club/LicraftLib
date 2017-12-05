package com.licrafter.lib.apt;

import com.licrafter.lib.apt.annotation.SetGet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by shell on 2017/12/5.
 * <p>
 * Github: https://github.com/shellljx
 */
public class LicraftProcessor extends AbstractProcessor {
    private Elements mElementUtils;
    private Messager mMessager;

    public LicraftProcessor() {
    }

    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.mMessager.printMessage(Diagnostic.Kind.NOTE, "processor process");
        this.mElementUtils = processingEnv.getElementUtils();
        this.mMessager = processingEnv.getMessager();
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        this.mMessager.printMessage(Diagnostic.Kind.NOTE, "processor process");
        Set bindViewElements = roundEnv.getElementsAnnotatedWith(SetGet.class);
        Iterator var4 = bindViewElements.iterator();

        while(var4.hasNext()) {
            Element element = (Element)var4.next();
            PackageElement packageElement = this.mElementUtils.getPackageOf(element);
            String pkName = packageElement.getQualifiedName().toString();
            File file = new File(pkName + "hahaha.yml");

            try {
                file.createNewFile();
            } catch (IOException var10) {
                var10.printStackTrace();
            }
        }

        return false;
    }

    public Set<String> getSupportedAnnotationTypes() {
        this.mMessager.printMessage(Diagnostic.Kind.NOTE, "processor process");
        return super.getSupportedAnnotationTypes();
    }

    public SourceVersion getSupportedSourceVersion() {
        this.mMessager.printMessage(Diagnostic.Kind.NOTE, "processor process");
        return super.getSupportedSourceVersion();
    }
}
