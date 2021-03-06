/*
 * Copyright (c) 2010. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.enovation.addressbook.cqrs.command;

import javax.validation.constraints.NotNull;

import org.axonframework.domain.AggregateIdentifier;
import org.springframework.util.Assert;

/**
 * <p>
 * Parent class for all contact related commands
 * </p>
 * 
 * @author Jettro Coenradie
 */
public abstract class AbstractContactCommand {
    @NotNull
    private AggregateIdentifier contactId;

    public AbstractContactCommand(AggregateIdentifier identifier) {
        contactId = identifier;
    }

    /**
     * Returns the identifier for the contact
     */
    public AggregateIdentifier getContactId() {
        return contactId;
    }

    /**
     * Provide the identifier for the existing contact. An error is thrown if the provided identifier is empty.
     */
    public void setContactId(AggregateIdentifier contactId) {
        Assert.notNull(contactId, "Cannot take actions on a contact with an empty id");
        this.contactId = contactId;
    }

}
