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

package org.axonframework.samples.trader.query.contacts;

import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.query.contacts.repositories.ContactQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.axonframework.samples.trader.contacts.api.ContactCreatedEvent;

/**
 * @author Jettro Coenradie
 */
@Component
public class ContactListener {

    private ContactQueryRepository contactRepository;

    @EventHandler
    public void handleContactCreated(ContactCreatedEvent event) {
        ContactEntry contactEntry = new ContactEntry();
        contactEntry.setIdentifier((UUIDAggregateIdentifier) event.getContactId());
        contactEntry.setName(event.getName());

        contactRepository.save(contactEntry);
    }

    @Autowired
    public void setContactRepository(ContactQueryRepository contactRepository) {
        this.contactRepository = contactRepository;
    }
}
