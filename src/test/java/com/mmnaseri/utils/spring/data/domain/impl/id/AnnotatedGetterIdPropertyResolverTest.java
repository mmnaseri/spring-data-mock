package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import org.springframework.data.annotation.Id;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("unused")
public class AnnotatedGetterIdPropertyResolverTest extends BaseRepeatableIdPropertyResolverTest {

    @Override
    protected IdPropertyResolver getIdPropertyResolver() {
        return new AnnotatedGetterIdPropertyResolver();
    }

    @Override
    protected Class<?> properEntity() {
        return EntityWithAnnotatedIdGetter.class;
    }

    @Override
    protected Class<?> entityWithNoProperty() {
        return EntityWithoutAnnotatedIdGetter.class;
    }

    @Override
    protected Class<?> entityWithMultipleProperties() {
        return EntityWithMultipleAnnotatedIdGetters.class;
    }

    public static class EntityWithAnnotatedIdGetter {

        private String id;

        @Id
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    public static class EntityWithoutAnnotatedIdGetter {

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    public static class EntityWithMultipleAnnotatedIdGetters {

        private String first;
        private String second;

        @Id
        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        @Id
        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }
    }

}