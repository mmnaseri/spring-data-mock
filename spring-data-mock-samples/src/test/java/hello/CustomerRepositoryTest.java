package hello;
import org.junit.Test;

import com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder;

public class CustomerRepositoryTest {

    @Test
    public void testDemo() {

        final CustomerRepository repository = RepositoryFactoryBuilder.builder().mock(CustomerRepository.class);
        repository.save(new Customer());
    }
    
}