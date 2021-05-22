<jump-top></jump-top>

# Contents

<table-of-contents></table-of-contents>

# Introduction

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

# Quick Start

Regardless of how you add the necessary dependency to your project, mocking a repository can be as simple as:

    builder().mock(UserRepository.class);

where `builder()` is a static method of the `RepositoryFactoryBuilder` class under package `com.mmnaseri.utils.spring.data.dsl.factory`.

The full code for that would be:

````java

import static com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder.builder;

public class UserServiceTest {

    private UserRepository repository;
    private UserService service;

    @BeforeMethod
    public void setUp() {
        repository = builder().mock(UserRepository.class);
        repository.save(new User("test", "securePassword"));
        service = new UserService(repository);
    }

    @Test
    public void testUserLookUp() {
        User user = service.load("test");
    }

}
````

An alternate way of mocking a repository would be by using the `RepositoryMockBuilder` class under the `com.mmnaseri.utils.spring.data.dsl.mock`
package:

````java
final RepositoryFactoryConfiguration configuration = ... ;
final UserRepository repository = new RepositoryMockBuilder()
    .useConfiguration(configuration)
    .mock(UserRepository.class);
````

# Mocking a Repository

To mock a repository you must somehow use the underlying `RepositoryFactory`. There is currently a single implementation of
this interface available, `com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactory`. The factory has a `getInstance(...)`
method that let's you customize the mocking for each instance of the repository you create. This means that you can mock a
single repository multiple times, each time with a different set of behaviors:

````java
final UserRepository repository = factory.getInstance(keyGenerator,
            UserRepository.class,
            Implementation1.class,
            Implementation2.class);
````

There are three components to this method call:

  1. The **key generator** (which can be `null`) will determine the strategy for generating keys for the ID field for
  an entity when it is inserted into the underlying data store.
  2. The **repository interface** which is the interface you want to instantiate and have proxied for you.
  3. The **custom implementations** (which are optional) that can provide custom ways of handling repository method calls.
  
A minimal way of instantiating a repository would be:

````java
final UserRepository repository = factory.getInstance(null, UserRepository.class);
````

which falls back on the default way of generating keys, and relies solely on default implementations for handling method calls.

## Repository Factory Configuration

The default repository factory implementation takes in a configuration object which will let you customize multiple
aspects of the mocking mechanism.

The configuration object allows you the following customizations:

  * Change the way *repository metadata* is resolved from a given repository class object. The following metadata is
  required to work with a repository and to be able to efficiently mock its intended behavior: 1) the entity type,
  2) the type of the identifier 3) the property corresponding to the identifier.

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

  * Decide how repository methods that are strictly non-data (e.g. `.equals(...)`) are to be handled

  * And what the default key generation scheme should be in case no key generator is specified for the given repository

Since the configuration object is complex and can be a hassle to create, you can use the much easier to use DSL via the
`com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder` class. We will go over that shortly.

## Using the DSL to Mock a Repository

Once you have a configuration object at hand, you can use the DSL bundled with this framework to easily mock
your repositories and avoid going through the RepositoryFactory class.

You can mock a repository this way:

````java
final RepositoryMockBuilder builder = new RepositoryMockBuilder();
final UserRepository repository = builder.useConfiguration(configuration)
    .generateKeysUsing(UUIDKeyGenerator.class)
    .usingImplementation(SampleImpl1.class)
        .and(SampleImpl2.class)
        .and(SampleImpl3.class)
    .mock(UserRepository.class);
````

You have to note that this builder is *stateless*, meaning that each of the methods in this builder will not modify an
internal state, but rather return an object which reflects all the configurations up to this point.

To better understand this concept, let's look at the following example:

````java
final RepositoryMockBuilder base = new RepositoryMockBuilder();
final RepositoryMockBuilder builder1 = base.useConfiguration(configuration)
    .generateKeysUsing(UUIDKeyGenerator.class)
    .usingImplementation(SampleImpl1.class);
final RepositoryMockBuilder builder2 = base.useConfiguration(configuration)
    .generateKeysUsing(NoOpKeyGenerator.class)
    .usingImplementation(SampleImpl2.class);
````

This is by design, and is to allow developers and testers the freedom of reusing their configurations.

## Using the DSL to Create a Configuration

Whereas mocking a repository is a relatively painless process and might not require the use of a dedicated DSL, creating
a configuration is another story altogether. In recognition of this fact, I have created a DSL for this very purpose, so
that you can craft configurations using a *stateful* builder via this DSL:

````java
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
    .withDefaultKeyGenerator(...) //set the default key generator
    .enableAuditing(...) //enable support for Spring Data's auditing and pass in a custom auditor aware instance
    .withListener(...) //register some event listener
        .and(...).and(...) //register additional event listeners
````

At this stage, you can either call to the `configure()` method on the builder object to get a configuration object, or
you can skip this step and continue from the key generation step of the mock builder (marked with three stars in the previous
listing). Alternatively, you can call `build()` to get an instance of the repository factory, which you can use to
instantiate mock repositories.

All of the steps above are optional. All values have defaults and you can skip setting them and still expect everything
to just work out of the box.

Here is a complete list of the terms you can use in the DSL to configure the repository factory:

Term | Explanation | Default Behavior
-----|-------------|--------------------
`resolveMetadataUsing` | tells the configurer to use the provided metadata resolver | `~.domain.impl.DefaultRepositoryMetadataResolver` will be used
`withOperators` | tells the configurer to use the provided operator context | `~.domain.impl.DefaultOperatorContext` will be used
`registerOperator` | registers an operator with the operator context | only the operators in the `DefaultOperatorContext` will be used
`withDataFunctions` | tells the configurer to use the provided data function registry | `~.query.impl.DefaultDataFunctionRegistry` will be used
`registerFunction` | registers a function with the default context | only default functions will be registered
`withDataStores` | tells the configurer to use the provided data store registry | `~.store.impl.DefaultDataStoreRegistry` will be used
`registerDataStore` | registers a data store | no data store will be registered by default
`withAdapters` | tells the configurer to use the provided result adapter context | `~.proxy.impl.DefaultResultAdapterContext` will be used
`adaptResultsUsing` | registers an adapter | no additional adapters will be used
`withMappings` | tells the configurer to use the provided type mapping context | `~.proxy.impl.DefaultTypeMappingContext` will be used
`honoringImplementation` | registers a type with the type mapping context | only the defaults provided will be used
`withOperationHandlers` | tells the configurer to use the provided non-data operation handler context | the default context will be used
`withOperationHandler` | registers an operation handler with the context | the default handlers will be used
`withDefaultKeyGenerator` | registers a default, fallback key generator for the repository factory | no fallback will be used
`withListeners` | tells the configurer to use the provided event listener context | `~.store.impl.DefaultDataStoreEventListenerContext` will be used
`withListener` | adds the provided event listener | no listener will be added
`enableAuditing` | enables Spring Data auditing. You can also provide an `AuditorAware`. If you don't provide anything, a default will be used which assumes user to be a string and it will have a value of `User` | auditing will not be enabled
`build` | builds and returns a repository factory | N/A
`configure` | builds and returns a repository factory configuration | N/A


To answer the question of what all of these configurable steps mean, we need to go to the next section.

# The Mechanics

In this section, we will detail the framework and go over how each part of it can be configured.

## Metadata Resolver

The metadata resolver is an entity that is capable of looking at a repository interface and figuring out detail about
the repository as well as the persistent entity it is supporting. This is what the metadata resolver will find out:

  * the type of the entity for which the repository has been created

  * the (actual or encapsulated) property of the persistent entity which holds the identifier.

  * the type of the identifier associated with the entity

The default metadata resolver is the `com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver`
class, which will first see if the repository is annotated with `@org.springframework.data.repository.RepositoryDefinition`
and if not tries to extract its metadata from the interface should it extend `org.springframework.data.repository.Repository`.

If none of these conditions are met, it will throw an exception.

<div class="alert alert-warning">
<h4 id="primitive-ids">Primitve IDs</h4>
<p> At this point, we do not support primitive identifier types. This means that if your ID type
 is `long`, you will have to change it to `java.lang.Long` to be able to use this framework.</p>
</div>

## The Operators

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


## Data Functions

Data functions determine what should be done with a particular selection of entities before a result is returned. For instance,
the `count` data function just returns the collection size for a subset of data, thus allowing you to start your query method with
`count` and expect it to return the size of the selection.

By extending the data functions, you are also extending the DSL for query methods by allowing various new data function names to be
used in the beginning of a query method's name.

Available data functions:

 Function  | Description
-----------|-----------------------------------
`count`    | Returns the number of items that were selected
`delete`   | Deletes all the selected items

## Data Stores

This framework has an abstraction hiding away the details of where and how entities are stored and are looked up. The default
behavior is, of course, to keep everything in memory. It might, however, be necessary to delegate this to some external service
or entity, such as an im-memory data store, a distributed cache, or an actual database.

All you have to do is implement the `com.mmnaseri.utils.spring.data.store.DataStore` interface and point it to the right direction.

Currently available implementations of the data store abstraction are:

  * `com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore` which, as the name implies, holds everything in memory
  * `com.mmnaseri.utils.spring.data.store.impl.EventPublishingDataStore` which acts as a wrapper around another, delegate
  data store to add event publishing capabilities.

A data store *has to* support these operations:

  * `hasKey`: to be able to tell whether or not it contains a given entity
  * `save`: to store an object at a given key
  * `delete`: to delete an item by its key
  * `retrieve`: to return an object from a key
  * `keys`: to return all the keys that it has stored
  * `retrieveAll`: to return all the entities that it has stored
  * `truncate`: to delete all items at once
  * `getEntityType`: to tell us what type of object it is storing

As you can see, this is a good abstraction of a key-value store that is centered around a particular object type.

This abstraction let's us create a separate data store for each object type, and bind them to their individual
repositories.

Additionally, if you want to support more sophisticated operations and enable batching and operation queueing, your
repository can implement `com.mmnaseri.utils.spring.data.store.QueueingDataStore` which introduces the following
three operations:

  * `flush`: to flush the queue and commit all issued operations
  * `startBatch`: to start a batch of operations
  * `end`: to end the batch that was indicated

Certain repository methods can take advantage of these additional functionality if provided.

### Caveats / Limitations

Be careful when changing entities after saving or loading from the store.
The standard implementation is an in-memory key-value store.
This means that the entity in the store also changes if the entity should be changed after saving / loading because it is the same object reference.

There are only a few sensible cases where this (in a test) could become a problem.

An example: If a controller loads the entity from the store and modifies it before the response (e.g. an `CredentialsContainer.eraseCredentials()` is called on a user entity).

You are warned.

## Result Adapters

Many times the actual implementation methods for an operation return very generic results, such as a list or a set, whereas the
required return type for the repository interface method might be something else. Suppose for instance, that the implementation
method returns a List, while the interface method returns just one instance.

In such cases, it is necessary to adapt the results to the output format, and that is exactly what the result adapters are for.

They have a priority order which dictates in what order they will be executed:

Adapter                                  | Converts from      | Converts to        | Priority | Description
-----------------------------------------|--------------------|--------------------|----------|--------------------------------------------------
`VoidResultAdapter`                      | `void`             | `*`                | `-inf`   |
`SameTypeResultAdapter`                  | `*`                | `*`                | `-500`   | Used when the source and target are of the same type
`NumberIterableResultAdapter`            | `Number`           | `Iterable`         | `-425`   |
`SimpleIterableResultAdapter`            | `*`                | `Iterable`         | `-400`   | When we want to convert an object to its iterable
`NullSimpleResultAdapter`                | `null`             | `Object`           | `-400`   | Used when target type is a single object
`NullToIteratorResultAdapter`            | `null`             | `Iterator`         | `-350`   |
`IteratorIterableResultAdapter`          | `Iterator`         | `Iterable`         | `-350`   |
`NullToCollectionResultAdapter`          | `null`             | `Collection`       | `-300`   |
`CollectionIterableResultAdapter`        | `Collection`       | `Iterable`         | `-300`   |
`SliceIterableResultAdapter`             | `Slice`            | `Iterable`         | `-250`   |
`NullToIterableResultAdapter`            | `null`             | `Iterable`         | `-250`   |
`NullToSliceResultAdapter`               | `null`             | `Slice`            | `-200`   |
`PageIterableResultAdapter`              | `Page`             | `Iterable`         | `-200`   |
`NullToFutureResultAdapter`              | `null`             | `Future`           | `-150`   |
`GeoPageIterableResultAdapter`           | `GeoPage`          | `Iterable`         | `-150`   |
`NullToListenableFutureResultAdapter`    | `null`             | `ListenableFuture` | `-100`   |
`FutureIterableResultAdapter`            | `Future          ` | `Iterable`         | `-100`   |
`ListenableFutureIterableResultAdapter`  | `ListenableFuture` | `Iterable`         | `-50`    |

## Custom Implementations and Type Mapping Context

You can map custom implementations to repository interfaces. To this end, a type mapping context exists which will let you bind
particular repository interface super types to custom implementation classes. The methods are then looked up according to their
signature.

Implementations registered with a type mapping context are made available to all repository factory instances and are thus shared.

When proxying a repository method this is the order with which a method is bound to an implementation:

  1. We first look for custom implementations supplied directly to the factory while requesting a mock instance
  2. We look at globally available implementations supplied through the configuration object
  3. We try to interpret the method name as a query.

## Spring Data Extensions

Spring Data extensions are modules provided by the Spring Data team or third parties that provide additional support for more
data sources.

These extensions can receive support from this mocking framework if you register a corresponding implementation in
the type mapping context.

We have implemented the following data extensions.

### Support for CrudRepository

All methods introduced in `org.springframework.data.repository.CrudRepository` are implemented in a class called
`com.mmnaseri.utils.spring.data.repository.DefaultCrudRepository`. This class is registered with the type mapping
for all repositories by default.

### Support for PagingAndSortingRepository

All methods introduced in `org.springframework.data.repository.PagingAndSortingRepository` are implemented in a class called
`com.mmnaseri.utils.spring.data.repository.DefaultPagingAndSortingRepository`. This class is registered with the type mapping
for all repositories by default.

### Support for "Query by Example"

Query by example is a concept introduced in Spring Data 1.12 and as such might not be shipped with your version of Spring
Data by default.

That is why we first check to see if interface `org.springframework.data.repository.query.QueryByExampleExecutor` is found
in the classpath. If so, then we register supporting type `com.mmnaseri.utils.spring.data.repository.DefaultQueryByExampleExecutor`
which includes methods that are capable of taking care of all methods introduced in this interface.

### Support for QueryDSL

There is an extension for [QueryDSL](http://www.querydsl.com) for Spring Data that will let you use a fluent API to
interact with the data store, while hiding away the complexities of talking to the database itself.

If the `org.springframework.data.querydsl.QueryDslPredicateExecutor` interface is found in the classpath and, additionally,
we can also find the CGLib library in the classpath (which is required for QueryDSL to function), we assume
QueryDSL support to be enabled. We then add the `com.mmnaseri.utils.spring.data.repository.DefaultQueryDslPredicateExecutor`
implementation to the type mapping context.

#### Note on additional dependencies

`DefaultQueryDslPredicateExecutor` uses the `com.querydsl:querydsl-collections` to query the underlying data store mock
the same way as the QueryDSL framework itself. As such, if you want to be able to mock repositories that use QueryDSL
operations, you will have to add a dependency to `querydsl-collections`:

<pre>
<code>
    <dependency>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-collections</artifactId>
        <version>${latest-version}</version>
        <scope>test</scope>
    </dependency>
</code>
</pre>

### Support for JPA

If we can find `org.springframework.data.jpa.repository.JpaRepository` in the classpath, we assume JPA support to be added.
As such, we will register `com.mmnaseri.utils.spring.data.repository.DefaultJpaRepository` which will take care of all the
methods in the `JpaRepository` interface that are not already handled by other extensions.

### Gemfire Support

If `org.springframework.data.gemfire.repository.GemfireRepository` is found in the classpath, we will assume that you want
to be able to mock Gemfire repositories as well. That is why we register `com.mmnaseri.utils.spring.data.repository.DefaultGemfireRepository`
to be able to take care of Gemfire-specific methods that aren't found on regular repositories.

## Non-Data-Operation Handlers

Non-data-operation handlers, as the name suggests, are operation handlers that support invocation of methods that are not
data-specific. Examples include `Object.equals(...)` and `Object.hashCode(...)`.

These are registered with `com.mmnaseri.utils.spring.data.proxy.impl.NonDataOperationInvocationHandler` which has a `register(...)`
method for the purpose.

You can implement your own handlers, which will be investigated and invoked in the order in which they were registered,
but these come with the framework:

  * `com.mmnaseri.utils.spring.data.proxy.impl.regular.EqualsNonDataOperationHandler`: for handling `Object.equals(Object)`
  * `com.mmnaseri.utils.spring.data.proxy.impl.regular.HashCodeNonDataOperationHandler` for handling `Object.hashCode()`
  * `com.mmnaseri.utils.spring.data.proxy.impl.regular.ToStringNonDataOperationHandler` for handling `Object.toString()`

### Disclaimer

The credit for fixing this goes to @Kaidjin who went all ninja on this and helped resolve #12 in all speed.

## Event Listeners

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

### Auditing

Out-of-the-box auditing is supported through this event mechanism for the usual `CreatedBy`, `CreatedDate`, `LastModifiedBy`,
and `LastModifiedDate` audit annotations provided by Spring Data Commons. To support user-related auditing (created by and last
modified by) you will need to supply an `AuditorAware` or accept the default one, which will always return a String object with
value, `"User"`.

By default, auditing is disabled. This is to follow in the footprints of Spring. Since Spring Data asks you to explicitly enable
auditing, this framework, too, pushes for the same requirement.

#### Note on additional dependencies

When you enable auditing, Spring Data will use the `joda-time` library to achieve precision timestamping. As such, when you
enable auditing, you will have to add a dependency on `joda-time:joda-time`.

### A Note on Distinct Selections

When using the `distinct` modifier for selection, this framework will use the internal `hashCode()` of the object being read
from the data store abstraction to figure out distinct values.

This is because as opposed to most data stores, we do not have any notion of row-by-row or object-to-object equality relations
and at the moment we cannot figure out if two objects should be considered as equal, unless they have a `hashCode`.

Having a `hashCode` is good programming practice anyway, so I don't feel too bad about not having implemented this for the moment.

### Referenced Objects

In this framework referenced objects are not going to be separated out. We have not implemented a full-fledged ORM and that is not
the plan -- at least for the moment.

When you save an object such as this:

````java
public class EntityA {

    @Reference
    private EntityB referencedObject;

}
````

we are not going to automatically create a data store for the `EntityB` class and store the values there. Though it doesn't seem to
be to complicated to implement, at the moment it is not how we do things.

## Key Generation

When storing data in the data store, we require a key. That is because the underlying data store abstraction is essentially a key-value
store.

This means that you will either have to provide keys for every entity you save, or that there must be some sort of key generation
mechanism in place to handle that for you. Well, luckily, there is one. Here is a list of the various key generators that come
with this framework, and you can easily add your own by implementing `com.mmnaseri.utils.spring.data.domain.KeyGenerator`.

  * `com.mmnaseri.utils.spring.data.domain.KeyGenerator`: when you genuinely do not wish to generate keys automatically. This key generator
  will basically generate `null` keys.
  * `com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator`: generates UUID values and returns them as Strings.
  * `com.mmnaseri.utils.spring.data.domain.impl.key.UUIDObjectTypeKeyGenerator`: generates UUID values and return the *java.util.UUID* objects.
  * `com.mmnaseri.utils.spring.data.domain.impl.key.SequentialIntegerKeyGenerator`: generates `int` values starting from `1`
  * `com.mmnaseri.utils.spring.data.domain.impl.key.SequentialLongKeyGenerator`: generates `long` values starting from `1`
  * `com.mmnaseri.utils.spring.data.domain.impl.key.ConfigurableSequentialIntegerKeyGenerator`: generates `int` values that start
  from a custom `initialValue` and increase/decrease by a custom `step`
  * `com.mmnaseri.utils.spring.data.domain.impl.key.ConfigurableSequentialLongKeyGenerator`: generates `long` values that start
  from a custom `initialValue` and increase/decrease by a custom `step`
  * `com.mmnaseri.utils.spring.data.domain.impl.key.RandomIntegerKeyGenerator`: generates random, unique `int` values
  * `com.mmnaseri.utils.spring.data.domain.impl.key.RandomLongKeyGenerator`: generates random, unique `long` values

If you are using the DSL to configure the repository factory and do not mention which of the above to use, and you don't
explicitly say that you do not want key generation to be turned off, one of the above will be chosen for you automatically
based on the type of the key.

### Key Generation Strategy

By default, only missing IDs are generated.
This is an advantage in tests because the entity ID is always fixed.
However, this does not correspond to the normal JPA (or at least with Hibernate) behavior for fields that are annotated with @GeneratedValue.

The desired behavior can be changed using the KeyGenerationStrategy.

#### Only generate missing IDs (default behavior)

```java
final UserRepository repository = new RepositoryMockBuilder()
    .generateKeysUsing(UUIDKeyGenerator.class, KeyGenerationStrategy.ONLY_NULL)
    .mock(UserRepository.class);
```

#### Generate IDs for all "unmanaged" entities (more JPA compliant)

```java
final UserRepository repository = new RepositoryMockBuilder()
    .generateKeysUsing(UUIDKeyGenerator.class, KeyGenerationStrategy.ALL_UNMANAGED)
    .mock(UserRepository.class);
```

#### Change behavior for multiple repositories

````java
final RepositoryFactoryConfiguration configuration = new DefaultRepositoryFactoryConfiguration();
configuration.setDefaultKeyGenerator(new UUIDKeyGenerator());
configuration.setsetDefaultKeyGenerationStrategy(KeyGenerationStrategy.ALL_UNMANAGED);

final RepositoryMockBuilder base = new RepositoryMockBuilder();
final RepositoryMockBuilder builder1 = base.useConfiguration(configuration)
    .useConfiguration(configuration)
    .usingImplementation(SampleImpl1.class);
final RepositoryMockBuilder builder2 = base.useConfiguration(configuration)
    .useConfiguration(configuration)
    .usingImplementation(SampleImpl2.class);
````

#### Illustration of the differences

`KeyGenerationStrategy.ONLY_NULL`

```java
final UserRepository repository = new RepositoryMockBuilder()
    .generateKeysUsing(RandomIntegerKeyGenerator.class, KeyGenerationStrategy.ONLY_NULL)
    .mock(UserRepository.class);

User user1 = new User();
user1.setId(123);
user1 = repository.save(user1);
assert(user1.id == 123); // id is preserved

final int id1 = user1.getId();
user1.setName("Charlie Brown");
user1 = repository.save(user1);
assert(user1.id == id1); // id is preserved


User user2 = new User();
user2.setId(123);
user2 = repository.save(user2);
assert(user2.id == 123); // id is preserved

// At this point the object with the ID 123 in the store was replaced with user2!

final int id2 = user2.getId();
user1.setName("Linus van Pelt");
user2 = repository.save(user2);
assert(user2.id == id2); // id is preserved
```

`KeyGenerationStrategy.ALL_UNMANAGED`

```java
final UserRepository repository = new RepositoryMockBuilder()
    .generateKeysUsing(RandomIntegerKeyGenerator.class, KeyGenerationStrategy.ALL_UNMANAGED)
    .mock(UserRepository.class);

User user1 = new User();
user1.setId(123);
user1 = repository.save(user1);
assert(user1.id != 123); // new id is generated, previous id was overwritten

final int id1 = user1.getId();
user1.setName("Charlie Brown");
user1 = repository.save(user1);
assert(user1.id == id1); // id is preserved


User user2 = new User();
user2.setId(123);
user2 = repository.save(user2);
assert(user2.id != 123); // new id is generated, previous id was overwritten

// At this point there are two items in the store. Neither of them has the ID 123!

final int id2 = user2.getId();
user1.setName("Linus van Pelt");
user2 = repository.save(user2);
assert(user2.id == id2); // id is preserved
```
