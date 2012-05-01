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

package org.axonframework.samples.trader.contacts.command;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.UUIDAggregateIdentifier;
import org.axonframework.repository.Repository;
import org.axonframework.unitofwork.UnitOfWork;
import org.axonframework.unitofwork.UnitOfWorkListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.axonframework.samples.trader.query.contacts.repositories.ContactQueryRepository;
import org.axonframework.samples.trader.contacts.api.CreateContactCommand;
import org.axonframework.samples.trader.contacts.api.RemoveContactCommand;
import org.axonframework.samples.trader.contacts.api.ChangeContactNameCommand;
import org.axonframework.samples.trader.contacts.api.Address;
import org.axonframework.samples.trader.contacts.api.RegisterAddressCommand;
import org.axonframework.samples.trader.contacts.api.RemoveAddressCommand;
import org.axonframework.samples.trader.query.contacts.ContactEntry;

/**
 * <p>Command handler that can be used to create and update Contacts. It can also be used to register and remove
 * addresses.</p>
 * <p>The provided repository is used to store the changes.</p>
 *
 * @author Allard Buijze
 */
@Component
public class ContactCommandHandler {

    private final static Logger logger = LoggerFactory.getLogger(ContactCommandHandler.class);
    
    private Repository<Contact> repository;
//    private ContactNameRepository contactNameRepository;
    private ContactQueryRepository contactQueryRepository;

    private void cancelClaimedContactName(AggregateIdentifier contactIdentifier, UnitOfWork unitOfWork) {
        final ContactEntry contactEntry = contactQueryRepository.findOne(contactIdentifier.asString());
        unitOfWork.registerListener(new UnitOfWorkListenerAdapter() {
            @Override
            public void afterCommit() {
                logger.debug("About to cancel the name {}", contactEntry.getName());
//                contactNameRepository.cancelContactName(contactEntry.getName());
            }
        });
    }

    /**
     * Sets the contact name repository used to maintain unique contact names
     *
     * @param contactNameRepository the contact name repository
     */
//    @Autowired
//    public void setContactNameRepository(ContactNameRepository contactNameRepository) {
//        this.contactNameRepository = contactNameRepository;
//    }

    /**
     * Changes the provided data for the contact found based on the provided identifier
     * <p/>
     * An {@code AggregateNotFoundException} is thrown if the identifier does not represent a valid contact.
     *
     * @param command    ChangeContactNameCommand that contains the identifier and the data to be updated
     * @param unitOfWork Unit of work for the current running thread
     */
    @CommandHandler
    public void handle(final ChangeContactNameCommand command, UnitOfWork unitOfWork) {
        Assert.notNull(command.getContactId(), "ContactIdentifier may not be null");
        Assert.notNull(command.getContactNewName(), "Name may not be null");
//        if (contactNameRepository.claimContactName(command.getContactNewName())) {
            registerUnitOfWorkListenerToCancelClaimingName(command.getContactNewName(), unitOfWork);
            Contact contact = repository.load(command.getContactId());
            contact.changeName(command.getContactNewName());
//
//            cancelClaimedContactName(command.getContactId(), unitOfWork);
//        } else {
//            throw new ContactNameAlreadyTakenException(command.getContactNewName());
//        }
    }

    /**
     * Creates a new contact based on the provided data. The provided user name is tested for uniqueness before
     * continuing.
     * <p/>
     * BEWARE
     * The mechanism to guarantee uniqueness is not a best practice for axon. This is a pretty expensive operation
     * especially when the number of users increases. It is better to make the client responsible for guaranteeing
     * unique contact names and make an explicit process to overcome the very rare situation where a duplicate contact
     * name is entered.
     *
     * @param command    CreateContactCommand object that contains the needed data to create a new contact
     * @param unitOfWork Unit of work for the current running thread
     */
    @CommandHandler
    public void handleCreateContact(final CreateContactCommand command, UnitOfWork unitOfWork) {
        logger.debug("Received a command for a new contact with name : {}", command.getNewContactName());
        Assert.notNull(command.getNewContactName(), "Name may not be null");

//        if (contactNameRepository.claimContactName(command.getNewContactName())) {
//            registerUnitOfWorkListenerToCancelClaimingName(command.getNewContactName(), unitOfWork);
            Contact contact = new Contact(new UUIDAggregateIdentifier(), command.getNewContactName());
            repository.add(contact);
//        } else {
//            throw new ContactNameAlreadyTakenException(command.getNewContactName());
//        }
    }

    /**
     * Registers an address for the contact with the provided identifier. If the contact already has an address with the
     * provided type, this address will be updated. An {@code AggregateNotFoundException} is thrown if the provided
     * identifier does not exist.
     *
     * @param command RegisterAddressCommand that contains all required data
     */
    @CommandHandler
    public void handle(RegisterAddressCommand command) {
        Assert.notNull(command.getContactId(), "ContactIdentifier may not be null");
        Assert.notNull(command.getAddressType(), "AddressType may not be null");
        Address address = new Address(command.getStreetAndNumber(), command.getZipCode(), command.getCity());
        Contact contact = repository.load(command.getContactId());
        contact.registerAddress(command.getAddressType(), address);
    }

    /**
     * Removes the address with the specified type from the contact with the provided identifier. If the identifier
     * does not exist, an {@code AggregateNotFoundException} is thrown. If the contact does not have an address with
     * specified type nothing happens.
     *
     * @param command RemoveAddressCommand that contains all required data to remove an address from a contact
     */
    @CommandHandler
    public void handle(RemoveAddressCommand command) {
        Assert.notNull(command.getContactId(), "ContactIdentifier may not be null");
        Assert.notNull(command.getAddressType(), "AddressType may not be null");
        Contact contact = repository.load(command.getContactId());
        contact.removeAddress(command.getAddressType());
    }

    /**
     * Removes the contact belonging to the contactId as provided by the command.
     *
     * @param command    RemoveContactCommand containing the identifier of the contact to be removed
     * @param unitOfWork Unit of work for the current running thread
     */
    @CommandHandler
    public void handle(RemoveContactCommand command, UnitOfWork unitOfWork) {
        Assert.notNull(command.getContactId(), "ContactIdentifier may not be null");
        Contact contact = repository.load(command.getContactId());
        contact.delete();

        cancelClaimedContactName(command.getContactId(), unitOfWork);
    }

    @SuppressWarnings("unused")
	private void registerUnitOfWorkListenerToCancelClaimingName(final String name, UnitOfWork unitOfWork) {
        unitOfWork.registerListener(new UnitOfWorkListenerAdapter() {
            @Override
            public void onRollback(Throwable failureCause) {
//                contactNameRepository.cancelContactName(name);
            }
        });
    }

    /**
     * Sets the query repository for users
     *
     * @param contactRepository for the query database
     */
    @Autowired
    public void setContactQueryRepository(ContactQueryRepository contactRepository) {
        this.contactQueryRepository = contactRepository;
    }

    /**
     * Sets the contact domain event repository.
     *
     * @param repository the contact repository
     */
    @Autowired
    @Qualifier("contactRepository")
    public void setRepository(Repository<Contact> repository) {
        this.repository = repository;
    }
}
