package org.springframework.data.infinispan.repository.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.data.infinispan.repository.query.InfinispanQueryCreator;
import org.springframework.data.keyvalue.repository.config.QueryCreatorType;

/**
 * Annotation to activate Infinispan repositories. If no base package is configured through either {@link #value()},
 * {@link #basePackages()} or {@link #basePackageClasses()} it will trigger scanning of the package of annotated class.
 *
 * @author Katia Aresti
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(InfinispanRepositoriesRegistrar.class)
//@QueryCreatorType(InfinispanQueryCreator.class)
public @interface EnableInfinispanRepositories {

}
