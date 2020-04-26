package com.mmnaseri.utils.spring.data.domain.impl.key;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import org.bson.types.ObjectId;

public class BsonObjectIdKeyGenerator implements KeyGenerator<ObjectId> {

    @Override
    public ObjectId generate() {
        return ObjectId.get();
    }

}
