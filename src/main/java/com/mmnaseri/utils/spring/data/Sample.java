package com.mmnaseri.utils.spring.data;

import com.mmnaseri.utils.spring.data.proxy.RepositoryFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder.builder;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class Sample {

    public static class Person {

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public interface PersonRepository extends JpaRepository<Person, String> {

    }

    public static void main(String[] args) {
        final RepositoryFactory factory = builder().resolveMetadataUsing(null)
                .registerOperator(null).and(null)
                .registerFunction("", null).and("", null)
                .registerDataStore(null).and(null)
                .adaptResultsUsing(null).and(null)
                .honoringImplementation(null, null).and(null, null)
                .withOperationHandler(null).and(null)
                .enableAuditing()
                .withListener(null).and(null)
                .build();
    }

}
