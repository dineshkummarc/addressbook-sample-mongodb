/*
 * Copyright (c) 2010-2012. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.enovation.addressbook.cqrs.infra.mongo;

import org.springframework.data.mongodb.MongoDbFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * CloudFoundry implementation of a MongoTemplate. We obtain the connection through the acquired factory.
 * 
 * @author Jettro Coenradie
 */
public class CFMongoTemplate extends org.springframework.data.mongodb.core.MongoTemplate implements org.axonframework.eventstore.mongo.MongoTemplate {

    private static final String DEFAULT_DOMAINEVENTS_COLLECTION = "domainevents";

    private static final String DEFAULT_SNAPSHOTEVENTS_COLLECTION = "snapshotevents";

    // private MongoDbFactory mongoDbFactory;

    public CFMongoTemplate(MongoDbFactory mongoDbFactory) {
        super(mongoDbFactory);
        // this.mongoDbFactory = mongoDbFactory;
    }

    @Override
    public DB database() {
        return /* mongoDbFactory */super.getDb();
    }

    @Override
    public DBCollection domainEventCollection() {
        return database().getCollection(DEFAULT_DOMAINEVENTS_COLLECTION);
    }

    @Override
    public DBCollection snapshotEventCollection() {
        return database().getCollection(DEFAULT_SNAPSHOTEVENTS_COLLECTION);
    }
}
