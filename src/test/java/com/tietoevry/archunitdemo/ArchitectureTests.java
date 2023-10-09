package com.tietoevry.archunitdemo;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.*;
import static com.tngtech.archunit.lang.conditions.ArchConditions.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;

@AnalyzeClasses(packages = "com.tietoevry")
public class ArchitectureTests {

    // ArchRules can just be declared as static fields and will be evaluated


    // class with annotation x have name y
    @ArchTest
    public static final ArchRule serviceNameAndAnnotationMatch = classes()
            .that().areAnnotatedWith(Service.class)
            .or().haveSimpleNameEndingWith("Service")
            .should().beAnnotatedWith(Service.class)
            .andShould().haveSimpleNameEndingWith("Service")
            .because("service should be easy to find");

    // classes with annotation x reside in package y
    @ArchTest
    public static final ArchRule restcontrollersResideInRestPackage = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().resideInAPackage("..rest..")
            .because("rest-controllers should be easy to find");

    // dependencies between packages
    @ArchTest
    public static final ArchRule reposDontDependOnServices = classes()
            .that().resideInAPackage("..repository..")
            .should(not(dependOnClassesThat(resideInAPackage("..service.."))));

    // defining and checking architecture layers
    @ArchTest
    public static final ArchRule adheresToLayeredArchitecture = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Repository").definedBy("..repository..")
            .layer("Service").definedBy("..service..")
            .layer("Rest").definedBy("..rest..")
            .whereLayer("Rest").mayNotBeAccessedByAnyLayer()
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service", "Rest") //just an example, don't shoot
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Rest");

    // onion architecture check
    @ArchTest
    public static final ArchRule rule = onionArchitecture()
            .domainModels("com.tietoevry.archunitdemo.domain..")
            .domainServices("com.tietoevry.archunitdemo.repository..")
            .applicationServices("com.tietoevry.archunitdemo.service..")
            .adapter("Web", "com.tietoevry.archunitdemo.web..");



    @ArchTest
    public void predefined_predicates_and_conditions(JavaClasses importedClasses) {
        DescribedPredicate<JavaClass> haveAnythingDoToWithService =
                simpleNameContaining("Service").or(resideInAPackage("..service.."));

        ArchCondition<JavaClass> beImmutable =
                haveOnlyFinalFields().and(beAnnotatedWith(Service.class));

        ArchRule rule = classes()
                .that(haveAnythingDoToWithService)
                .should(beImmutable);

        rule.check(importedClasses);
    }
}
