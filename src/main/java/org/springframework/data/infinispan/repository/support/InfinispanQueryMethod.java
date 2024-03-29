package org.springframework.data.infinispan.repository.support;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.infinispan.repository.query.InfinispanQuery;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.util.StringUtils;

public class InfinispanQueryMethod extends QueryMethod {

   private final Method method;

   public InfinispanQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {
      super(method, metadata, factory);
      this.method = method;
   }

   public boolean hasAnnotatedQuery() {
      return StringUtils.hasText(getAnnotatedQuery());
   }

   String getAnnotatedQuery() {
      InfinispanQuery query = method.getAnnotation(InfinispanQuery.class);
      String queryString = (query != null ? (String) AnnotationUtils.getValue(query) : null);
      return (StringUtils.hasText(queryString) ? queryString : null);
   }

   String getKeySpace() {
      KeySpace keySpace = getEntityInformation().getJavaType().getAnnotation(KeySpace.class);
      String queryString = (keySpace != null ? (String) AnnotationUtils.getValue(keySpace) : null);
      return (StringUtils.hasText(queryString) ? queryString : null);

   }

}
