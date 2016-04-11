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

### Using the DSL to Mock a Repository

Once you have a configuration object at hand, you can use the DSL bundled with this framework to easily mock
your repositories and avoid going through the RepositoryFactory class.

You can mock a repository this way:

    final RepositoryMockBuilder builder = new RepositoryMockBuilder();
    final UserRepository repository = builder.useConfiguration(configuration)
        .generateKeysUsing(UUIDKeyGenerator.class) //***
        .usingImplementation(SampleImpl1.class)
            .and(SampleImpl2.class)
            .and(SampleImpl3.class)
        .mock(UserRepository.class);

You have to note that this builder is *stateless*, meaning that each of the methods in this builder will not modify an
internal state, but rather return an object which reflects all the configurations up to this point.

This is by design, and is to allow developers and testers the freedom of reusing their configurations.

### Using the DSL to Create a Configuration

Whereas mocking a repository is a relatively painless process and might not require the use of a dedicated DSL, creating
a configuration is another story altogether. In recognition of this fact, I have created a DSL for this very purpose, so
that you can craft configurations using a *stateful* builder via this DSL:

    final RepositoryFactoryBuilder builder = RepositoryFactoryBuilder.builder()
        .resolveMetadataUsing(...)
        .registerOperator(...) //register some operator
            .and(...).and(...) //register other operators
        .registerFunction(...) //register data function
            .and(...).and(...) //register additional data functions
        .registerDataStore(...) //register a data store to be used in the configured repositories
            .and(...).and(...) //register additional data stores
        .adaptResultsUsing(...) //register a result adapter
            .and(...).and(...) //register additional result adapters
        .honoringImplementation(...) //add some custom implementation on a global scope
            .and(...).and(...) //register additional implementations
        .withOperationHandler(...) //register other operation handlers
            .and(...).and(...) //add more operation handlers
        .enableAuditing(...) //enable support for Spring Data's auditing and pass in a custom auditor aware instance
        .withListener(...) //register some event listener
            .and(...).and(...) //register additional event listeners

At this stage, you can either call to the `configure()` method on the builder object to get a configuration object, or
you can skip this step and continue from the key generation step of the mock builder (marked with three stars in the previous
listing).

All of the steps above are optional. All values have defaults and you can skip setting them and still expect everything
to just work out of the box.

To answer the question of what all of these configurable steps mean, we need to go to the next section.

### The Mechanics

In this section, we will detail the framework and go over how each part of it can be configured.

#### Metadata Resolver

The metadata resolver is an entity that is capable of looking at a repository interface and figuring out detail about
the repository as well as the persistent entity it is supporting. This is what the metadata resolver will find out:

  * the type of the entity for which the repository has been created
  * the (either actual or encapsulated) property of the persistent entity which holds the identifier
  * the type of the identifier associated with the entity

The default metadata resolver is the `com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver`
class, which will first see if the repository is annotated with `@org.springframework.data.repository.RepositoryDefinition`
and if not tries to extract its metadata from the interface should it extend `org.springframework.data.repository.Repository`.

If none of these conditions are met, it will throw an exception.

#### The Operators

The operators are what drive how the query methods are parsed. This is the general recipe:

At each juncture, we look for the operator whose tokens matches the longest suffix, and assume the rest to be a
property path, so that for instance, if we are parsing "ParentParentAgeGreaterThanEqual", we will match it with the
operator "GreaterThanEqual", and consider the "ParentParentAge" to be a property path (which might be `parent.parent.age`).

Each operator has a `Matcher`, which will help identify whether or not based on a given criteria an entity instance matches
the query.

By extending the operators, you can practically extend the query method DSL. This might not be practical, as we might not want
to support things that Spring Data doesn't support yet, but it allows for a better maintainability and easier extensibility should
Spring Data actually expand beyond what it is today.

Below is a list of the default operators that ship with this framework:

 Operator                  | Suffixes
---------------------------|---------------------------------------------------
AFTER                      | `After`, `IsAfter`
BEFORE                     | `Before`, `IsBefore`
CONTAINING                 | `Containing`, `IsContaining`, `Contains`
BETWEEN                    | `Between`, `IsBetween`
NOT_BETWEEN                | `NotBetween`, `IsNotBetween`
ENDING_WITH                | `EndingWith`, `IsEndingWith`, `EndsWith`
FALSE                      | `False`, `IsFalse`
GREATER_THAN               | `GreaterThan`, `IsGreaterThan`
GREATER_THAN_EQUALS        | `GreaterThanEqual`, `IsGreaterThanEqual`
IN                         | `In`, `IsIn`
IS                         | `Is`, `EqualTo`, `IsEqualTo`, `Equals`
NOT_NULL                   | `NotNull`, `IsNotNull`
NULL                       | `Null`, `IsNull`
LESS_THAN                  | `LessThan`, `IsLessThan`
LESS_THAN_EQUAL            | `LessThanEqual`, `IsLessThanEqual`
LIKE                       | `Like`, `IsLike`
NEAR                       | `Near`, `IsNear`
NOT                        | `IsNot`, `Not`, `IsNotEqualTo`, `DoesNotEqual`
NOT_IN                     | `NotIn`, `IsNotIn`
NOT_LIKE                   | `NotLike`, `IsNotLike`
REGEX                      | `Regex`, `MatchesRegex`, `Matches`
STARTING_WITH              | `StartingWith`, `IsStartingWith`, `StartsWith`
TRUE                       | `True`, `IsTrue`

If no suffix is present to determine the operator by, it is assumed that the `IS` operator was intended.


#### Data Functions

Data functions determine what should be done with a particular selection of entities before a result is returned. For instance,
the `count` data function just returns the collection size for a subset of data, thus allowing you to start your query method with
`count` and expect it to return the size of the selection. Currently, the only other selected function is `delete`.

By extending the data functions, you are also extending the DSL for query methods by allowing various new data function names to be
used in the beginning of a query method's name.

#### Data Stores

This framework has an abstraction hiding away the details of where and how entities are stored and are looked up. The default
behavior is, of course, to keep everything in memory. It might, however, be necessary to delegate this to some external service
or entity, such as an im-memory data store, a distributed cache, or an actual database.

All you have to do is implement the `com.mmnaseri.utils.spring.data.store.DataStore` interface and point it to the right direction.

#### Result Adapters

Many times the actual implementation methods for an operation return very generic results, such as a list or a set, whereas the
required return type for the repository interface method might be something else. Suppose for instance, that the implementation
method returns a List, while the interface method returns just one instance.

In such cases, it is necessary to adapt the results to the output format, and that is exactly what the result adapters are for.

They have a priority order which dictates in what order they will be executed.

#### Custom Implementations and Type Mapping Context

You can map custom implementations to repository interfaces. To this end, a type mapping context exists which will let you bind
particular repository interface super types to custom implementation classes. The methods are then looked up according to their
signature.

Implementations registered with a type mapping context are made available to all repository factory instances and are thus shared.

When proxying a repository method this is the order with which a method is bound to an implementation:

  1. We first look for custom implementations supplied directly to the factory while requesting a mock instance
  2. We look at globally available implementations supplied through the configuration object
  3. We try to interpret the method name as a query.

By default, the following type mappings are in place:

  * All repositories can include any of the methods defined in `com.mmnaseri.utils.spring.data.commons.DefaultCrudRepository`
  which implements the methods introduced in `org.springframework.data.repository.CrudRepository`.
  * All repositories can include any of the methods defined in `com.mmnaseri.utils.spring.data.commons.DefaultPagingAndSortingRepository`
  which adds paging and sorting capabilities to the repositories as per `org.springframework.data.repository.PagingAndSortingRepository`.
  * If `org.springframework.data.jpa.repository.JpaRepository` is found in the classpath, all repositories will be able to have
  methods from `com.mmnaseri.utils.spring.data.commons.DefaultJpaRepository` that are not already present in one of the above in
  their repertoire.
  * If `org.springframework.data.gemfire.repository.GemfireRepository` is present in the classpath, all repositories will be able to
  include methods from this interface that are not present in the above in their list of methods and rest assured that an implementation
  will be provided, courtesy of `com.mmnaseri.utils.spring.data.commons.DefaultGemfireRepository`.

#### Non-Data-Operation Handlers

Non-data-operation handlers, as the name suggests, are operation handlers that support invocation of methods that are not
data-specific. Examples include `Object.equals(...)` and `Object.hashCode(...)`.

These are registered with `com.mmnaseri.utils.spring.data.proxy.impl.NonDataOperationInvocationHandler` which has a `register(...)`
method for the purpose.

You can implement your own handlers, which will be investigated and invoked in the order in which they were registered,
but these come with the framework:

  * `com.mmnaseri.utils.spring.data.proxy.impl.regular.EqualsNonDataOperationHandler`: for handling `Object.equals(Object)`
  * `com.mmnaseri.utils.spring.data.proxy.impl.regular.HashCodeNonDataOperationHandler` for handling `Object.hashCode()`
  * `com.mmnaseri.utils.spring.data.proxy.impl.regular.ToStringNonDataOperationHandler` for handling `Object.toString()`

##### Disclaimer

The credit for fixing this goes to @Kaidjin who went all ninja on this and helped resolve #12 in all speed.

#### Event Listeners

You can register event listeners for each of the following events:

  * `BeforeInsertDataStoreEvent`
  * `AfterInsertDataStoreEvent`
  * `BeforeUpdateDataStoreEvent`
  * `AfterUpdateDataStoreEvent`
  * `BeforeDeleteDataStoreEvent`
  * `AfterDeleteDataStoreEvent`

The event handlers can then modify, take note of, or otherwise interact with the entities for which the event was raised.

If you use the `enableAuditing()` feature above, an event listener (`com.mmnaseri.utils.spring.data.store.impl.AuditDataEventListener`)
will be registered with the configuration which will enable auditing features and will set relevant properties in the appropriate
juncture by listening closely to the events listed above.

##### Auditing

Out-of-the-box auditing is supported through this event mechanism for the usual `CreatedBy`, `CreatedDate`, `LastModifiedBy`,
and `LastModifiedDate` audit annotations provided by Spring Data Commons. To support user-related auditing (created by and last
modified by) you will need to supply an `AuditorAware` or accept the default one, which will always return a String object with
value, `"User"`.

By default, auditing is disabled. This is to follow in the footprints of Spring. Since Spring Data asks you to explicitly enable
auditing, this framework, too, pushes for the same requirement.

##### A Note on Distinct Selections

When using the `distinct` modifier for selection, this framework will use the internal `hashCode()` of the object being read
from the data store abstraction to figure out distinct values.

This is because as opposed to most data stores, we do not have any notion of row-by-row or object-to-object equality relations
and at the moment we cannot figure out if two objects should be considered as equal, unless they have a `hashCode`.

Having a `hashCode` is good programming practice anyway, so I don't feel too bad about not having implemented this for the moment.

##### Referenced Objects

In this framework referenced objects are not going to be separated out. We have not implemented a full-fledged ORM and that is not
the plan -- at least for the moment.

Then you save an object such as this:

    public class EntityA {

        @Reference
        private EntityB referencedObject;

    }

we are not going to automatically create a data store for the `EntityB` class and store the values there. Though it doesn't seem to
be to complicated to implement, at the moment it is not how we do things.