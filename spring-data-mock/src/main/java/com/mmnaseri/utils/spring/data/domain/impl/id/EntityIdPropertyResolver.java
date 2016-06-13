package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.NoIdPropertyException;
import com.mmnaseri.utils.spring.data.error.PrimitiveIdTypeException;
import com.mmnaseri.utils.spring.data.query.PropertyDescriptor;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import com.mmnaseri.utils.spring.data.tools.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

/**
 * <p>This class will use all the magic implemented in the other ID property resolvers to find out the ID property
 * for an entity.</p>
 *
 * <p>The order in which conditions are considered is:</p>
 *
 * <ol>
 *     <li>{@link AnnotatedGetterIdPropertyResolver Annotated getter}</li>
 *     <li>{@link AnnotatedFieldIdPropertyResolver Annotated field}</li>
 *     <li>{@link NamedGetterIdPropertyResolver Getter for ID property using name}</li>
 *     <li>{@link NamedFieldIdPropertyResolver Field having proper name}</li>
 * </ol>
 *
 * <p>After all the above are considered, if nothing is found, a {@link NoIdPropertyException NoIdPropertyException}
 * is thrown to show that the promised ID property was not found on the entity class.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/23/15)
 */
public class EntityIdPropertyResolver implements IdPropertyResolver {

    private static final Log log = LogFactory.getLog(EntityIdPropertyResolver.class);
    private final AnnotatedGetterIdPropertyResolver annotatedGetterIdPropertyResolver = new AnnotatedGetterIdPropertyResolver();
    private final AnnotatedFieldIdPropertyResolver annotatedFieldIdPropertyResolver = new AnnotatedFieldIdPropertyResolver();
    private final NamedGetterIdPropertyResolver namedGetterIdPropertyResolver = new NamedGetterIdPropertyResolver();
    private final NamedFieldIdPropertyResolver namedFieldIdPropertyResolver = new NamedFieldIdPropertyResolver();

    @Override
    public String resolve(Class<?> entityType, Class<? extends Serializable> idType) {
        log.info("Trying to resolve the ID property for entity " + entityType + " using the annotated getter method");
        String idProperty = annotatedGetterIdPropertyResolver.resolve(entityType, idType);
        if (idProperty == null) {
            log.info("Trying to resolve the ID property for entity " + entityType + " using the annotated ID field");
            idProperty = annotatedFieldIdPropertyResolver.resolve(entityType, idType);
        }
        if (idProperty == null) {
            log.info("Trying to resolve the ID property for entity " + entityType + " using the getter method");
            idProperty = namedGetterIdPropertyResolver.resolve(entityType, idType);
        }
        if (idProperty == null) {
            log.info("Trying to resolve the ID property for entity " + entityType + " using the field");
            idProperty = namedFieldIdPropertyResolver.resolve(entityType, idType);
        }
        if (idProperty == null) {
            log.error("No ID property was found for entity " + entityType);
            throw new NoIdPropertyException(entityType);
        }
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(entityType, StringUtils.capitalize(idProperty));
        if (descriptor.getType().isPrimitive()) {
            throw new PrimitiveIdTypeException(entityType, idProperty);
        }
        return idProperty;
    }

}
