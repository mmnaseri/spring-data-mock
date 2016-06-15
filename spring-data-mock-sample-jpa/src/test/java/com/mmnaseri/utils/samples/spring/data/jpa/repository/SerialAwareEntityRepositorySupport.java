package com.mmnaseri.utils.samples.spring.data.jpa.repository;

import com.mmnaseri.utils.samples.spring.data.jpa.model.SerialAwareEntity;
import com.mmnaseri.utils.spring.data.domain.RepositoryAware;
import com.mmnaseri.utils.spring.data.error.DataOperationExecutionException;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/14/16, 11:45 PM)
 */
public class SerialAwareEntityRepositorySupport implements SerialAwareEntityRepository, RepositoryAware<JpaRepository> {

    private JpaRepository repository;

    @Override
    public SerialAwareEntity lookupBySerial(String serial) {
        final SerialAwareEntity probe = new SerialAwareEntity();
        probe.setSerial(serial);
        final Example<?> example = Example.of(probe);
        final List found = repository.findAll(example);
        if (found.isEmpty()) {
            return null;
        }
        if (found.size() > 1) {
            throw new DataOperationExecutionException("Expected only one instance to be found", null);
        }
        return (SerialAwareEntity) found.get(0);
    }

    @Override
    public void setRepository(JpaRepository repository) {
        this.repository = repository;
    }

}
