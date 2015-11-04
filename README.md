# Spring Data Mock

This is a fairly flexible, versatile framework for mocking Spring Data repositories. Spring Data provides a very good
foundation for separating the concerns of managing a database and its subsequently resulting queries from those of the
business layer.

This is great for writing services. They only need to depend upon Spring Data repositories and manage their data through
this level of indirection. This, however, means that for testing purposes, you will either have to write lots of boilerplate
code for your Spring powered application, or you will have to start up a full blown application context with a backing
database.

For most test cases, this is entirely unnecessary and, moreover, creates time burdens and takes away valuable time from
productive tasks. This is why I decided to write this framework: to avoid the unnecessary effort, and to have a reliable
infrastructure replicating what Spring would do with an actual database, only in-memory. This will allow for mocking the
repository with actual data. Thus, you can test your services *without* having to start up the application context, and
with the highest level of isolation -- with actual data.

## Quick Start

Regardless of how you add the necessary dependency to your project, mocking a repository can be as simple as:

    final UserRepository repository = builder().mock(UserRepository.class);
    
where `builder()` is a static method of the `RepositoryFactoryBuilder` class under package `com.mmnaseri.utils.spring.data.dsl.factory`.

An alternate way of mocking a repository would be by using the `RepositoryMockBuilder` class under the `com.mmnaseri.utils.spring.data.dsl.mock`
package:

    final RepositoryFactoryConfiguration configuration = ... ;
    final UserRepository repository = new RepositoryMockBuilder().useConfiguration(configuration).mock(UserRepository.class);
    
## Mocking a Repository

To mock a repository you must somehow use the underlying `RepositoryFactory`. There is currently a single implementation of
this interface available, `com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactory`. The factory has a `getInstance(...)`
method that let's you customize the mocking for each instance of the repository you create. This means that you can mock a
single repository multiple times, each time with a different set of behaviors:

    final UserRepository repository = factory.getInstance(keyGenerator, UserRepository.class, Implementation1.class, Implementation2.class);

There are three components to this method call:

  1. The **key generator** (which can be `null`) will determine the strategy for generating keys for the ID field for
  an entity when it is inserted into the underlying data store.
  2. The **repository interface** which is the interface you want to instantiate and have proxied for you.
  3. The **custom implementations** (which are optional) that can provide custom ways of handling repository method calls.
  
A minimal way of instantiating a repository would be:

    final UserRepository repository = factory.getInstance(null, UserRepository.class);

which disables automatic key generation, and relies solely on default implementations for handling method calls.

### Repository Factory Configuration

The default repository factory implementation takes in a configuration object which will let you customize multiple
aspects of the mocking mechanism.

The configuration object allows you the following customizations:

  * Change the way *repository metadata* is resolved from a given repository class object. The following metadata is
  required to work with a repository and to be able to efficiently mock its intended behavior: 1) the entity type,
  2) the type of the identifier 3) the property corresponding to the identifier
  * Change the way a *query method name* is parsed and converted into a data filter object. By default, query methods
  which follow the standard declared by Spring Data are parsed and honored. Additionally, the first word is taken as
  a function and can thus be used to implement custom behavior. This is to enable extension in case of further support
  by the Spring Data team. Also, the operators (such as "Is Greater Than", "Is Equal To", "Is Between", etc.) are customizable.
  * Customize the set of available aggregate and operational *functions*.
  * Customize the underlying *data store* mapping for each entity type. By default, storage is done in-memory. But there is
  nothing preventing you from attaching the data store for a particular type of entity to another source. All you have to do
  is to implement the `com.mmnaseri.utils.spring.data.store.DataStore` interface and add it to this context. It is actually
  much easier than it sounds.
  * Decide *how results should be adapted* from an actual value returned from implementation methods to the way a
  repository method is supposed to return values. This is to allow, for instance, to implement a `findAll` method that
  returns a collection of items, and then reuse its implementation for a repository method named `findAll` that is supposed
  to return a set.
  * Set *default implementations* for particular repository types and subtypes.
  * Add and customize *event listeners* to data store actions. These can listen to events before and after inserting, updating,
  and deleting a particular entity.

Since the configuration object is complex and can be a hassle to create, you can use the much easier to use DSL via the
`com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder` class. We will go over that shortly.

### 