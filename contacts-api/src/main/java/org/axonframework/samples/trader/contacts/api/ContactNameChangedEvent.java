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

package org.axonframework.samples.trader.contacts.api;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;
import org.springframework.util.Assert;

/**
 * @author Allard Buijze
 */
public class ContactNameChangedEvent extends DomainEvent {

    private final AggregateIdentifier contactId;
    private final String newName;

    public ContactNameChangedEvent(AggregateIdentifier contactId, String newName) {
        this.contactId = contactId;
        this.newName = newName;
    }

    public String getNewName() {
        return newName;
    }

    public AggregateIdentifier getContactId() {
        return contactId;
    }
}
