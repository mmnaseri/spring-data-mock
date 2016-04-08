package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.NoIdPropertyException;
import org.springframework.data.annotation.Id;
import org.testng.annotations.Test;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class EntityIdPropertyResolverTest {

    public static class FirstEntity {

        private String id;
        @Id
        private String annotatedId;
        private String unannotatedId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAnnotatedId() {
            return annotatedId;
        }

        public void setAnnotatedId(String annotatedId) {
            this.annotatedId = annotatedId;
        }

        @Id
        public String getUnannotatedId() {
            return unannotatedId;
        }

        public void setUnannotatedId(String unannotatedId) {
            this.unannotatedId = unannotatedId;
        }

    }

    public static class SecondEntity {

        private String id;
        @Id
        private String annotatedId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAnnotatedId() {
            return annotatedId;
        }

        public void setAnnotatedId(String annotatedId) {
            this.annotatedId = annotatedId;
        }

    }

    public static class ThirdEntity {

        private String _id;

        public String getId() {
            return _id;
        }

        public void setId(String id) {
            this._id = id;
        }

    }

    public static class FourthEntity {

        private String id;

        public String getIdentifier() {
            return id;
        }

        public void setIdentifier(String id) {
            this.id = id;
        }

    }

    public static class FifthEntity {

        private String identifier;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

    }

    @Test
    public void testThatAnnotatedGetterHasPrecedence() throws Exception {
        final IdPropertyResolver resolver = new EntityIdPropertyResolver();
        final String resolved = resolver.resolve(FirstEntity.class, Serializable.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("unannotatedId"));
    }

    @Test
    public void testThatAnnotatedPropertyIsSecond() throws Exception {
        final IdPropertyResolver resolver = new EntityIdPropertyResolver();
        final String resolved = resolver.resolve(SecondEntity.class, Serializable.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("annotatedId"));
    }

    @Test
    public void testThatNamedGetterIsThird() throws Exception {
        final IdPropertyResolver resolver = new EntityIdPropertyResolver();
        final String resolved = resolver.resolve(ThirdEntity.class, Serializable.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("id"));
    }

    @Test
    public void testThatNamedFieldIsFourth() throws Exception {
        final IdPropertyResolver resolver = new EntityIdPropertyResolver();
        final String resolved = resolver.resolve(FourthEntity.class, Serializable.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("id"));
    }

    @Test(expectedExceptions = NoIdPropertyException.class)
    public void testThatNoOtherValueIsHonored() throws Exception {
        final IdPropertyResolver resolver = new EntityIdPropertyResolver();
        resolver.resolve(FifthEntity.class, Serializable.class);
    }

}