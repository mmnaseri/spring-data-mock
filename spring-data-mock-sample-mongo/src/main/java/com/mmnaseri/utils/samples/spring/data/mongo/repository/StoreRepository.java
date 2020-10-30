package com.mmnaseri.utils.samples.spring.data.mongo.repository;

import com.mmnaseri.utils.samples.spring.data.mongo.model.Store;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreRepository extends MongoRepository<Store, ObjectId> {}
