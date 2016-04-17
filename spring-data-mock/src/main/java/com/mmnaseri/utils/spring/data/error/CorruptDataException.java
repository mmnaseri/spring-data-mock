package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public class CorruptDataException extends DataStoreException {

    public CorruptDataException(Class<?> entityType, Object data, String corruption) {
        super(entityType, entityType + ": Data <" + data + "> is unacceptable. " + corruption);
    }

}
