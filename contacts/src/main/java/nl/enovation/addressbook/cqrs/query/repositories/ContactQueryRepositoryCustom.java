package nl.enovation.addressbook.cqrs.query.repositories;

import java.util.List;

import nl.enovation.addressbook.cqrs.query.ContactEntry;

public interface ContactQueryRepositoryCustom {
    List<ContactEntry> searchForNames(String searchValue);
}
