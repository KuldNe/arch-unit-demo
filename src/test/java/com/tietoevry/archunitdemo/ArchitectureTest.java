package com.tietoevry.archunitdemo;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.conditions.ArchConditions.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;


@AnalyzeClasses(packages = "com.tietoevry")
public class ArchitectureTest {

    // ArchRules can just be declared as static fields and will be evaluated
    @ArchTest
    public static final ArchRule RULE = classes()
            .that().areAnnotatedWith(Service.class)
            .or().haveSimpleNameEndingWith("Service")
            .should().beAnnotatedWith(Service.class)
            .andShould().haveSimpleNameEndingWith("Service")
            .because("controller should be easy to find");

    @ArchTest
    public static final ArchRule REPO_SERVICE_DEPENDANCY = classes()
            .that().resideInAPackage("..repository..")
            .should(not(dependOnClassesThat(resideInAPackage("..service.."))));

//    @ArchTest
//    public static void rule3(JavaClasses classes) {
//        // The runner also understands static methods with a single JavaClasses argument
//        // reusing the cached classes
//    }

}
