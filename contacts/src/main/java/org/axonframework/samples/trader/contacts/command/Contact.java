/*
 * Copyright (c) 2010-2011. Axon Framework
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

package org.axonframework.samples.trader.contacts.command;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.samples.trader.contacts.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>The Aggregate root component of the sample application. This component handles all contact as well as address
 * domain events.</p>
 *
 * @author Allard Buijze
 */
class Contact extends AbstractAnnotatedAggregateRoot {
	
	private final static Logger logger = LoggerFactory.getLogger(Contact.class);
	
	private AggregateIdentifier identifier;

    public Contact(AggregateIdentifier identifier, String name) {
    	super(identifier);
    	this.identifier = identifier;
        apply(new ContactCreatedEvent(identifier, name));
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public Contact(AggregateIdentifier identifier) {
        super(identifier);
    }

    /**
     * Change the name of the contact
     *
     * @param name String containing the new name
     */
    public void changeName(String name) {
        apply(new ContactNameChangedEvent(getIdentifier(), name));
    }

    public void delete() {
        apply(new ContactDeletedEvent(getIdentifier()));
    }

    @EventHandler
    protected void handleContactCreatedEvent(ContactCreatedEvent event) {
    	logger.debug("Contact received a ContactCreatedEvent");
    }

    @EventHandler
    protected void handleContactNameChangedEvent(ContactNameChangedEvent event) {
    	logger.debug("Contact received a ContactNameChangedEvent");
    }

    public AggregateIdentifier getIdentifier() {
        return this.identifier;
    }
}
