Change Log
==========

This page tries to list all the changes that are incorporated
in a given release.

Where applicable, an issue number and a link to the appropriate
issue is provided as well.


[v1.1.3](https://github.com/mmnaseri/spring-data-mock/releases/tag/v1.1.3)
------------------------------------------------

##### Enhancements

  * [#92](https://github.com/mmnaseri/spring-data-mock/issues/92) Have the `pom` files follow the conventions

  * [#93](https://github.com/mmnaseri/spring-data-mock/issues/93) Allow for specifying a fallback key generator on a per-application basis

  * [#95](https://github.com/mmnaseri/spring-data-mock/issues/95) Upgrade `maven-javadoc-plugin` to `2.10.4`

  * [#96](https://github.com/mmnaseri/spring-data-mock/issues/96) Optimize imports and usages

  * [#97](https://github.com/mmnaseri/spring-data-mock/issues/97) Add `.configure()` to `RepositoryFactoryBuilder` DSL

  * [#104](https://github.com/mmnaseri/spring-data-mock/issues/104) Create more flexible key generators

  * [#105](https://github.com/mmnaseri/spring-data-mock/issues/105) Add samples outlining configuration scope

  * [#109](https://github.com/mmnaseri/spring-data-mock/issues/109) Add support for using the DSL to configure on top of an existing configuration


[v1.1.2](https://github.com/mmnaseri/spring-data-mock/releases/tag/v1.1.2)
------------------------------------------------

##### Enhancements

  * [#79](https://github.com/mmnaseri/spring-data-mock/issues/79) Restructure the project
  and add samples


[v1.1.1](https://github.com/mmnaseri/spring-data-mock/releases/tag/v1.1.1)
------------------------------------------------

##### Enhancements

  * [#68](https://github.com/mmnaseri/spring-data-mock/issues/68):
   Fix up the dependencies left from hastily adding support for QueryDSL

  * [#72](https://github.com/mmnaseri/spring-data-mock/issues/72):
   Fix Codacey issues prior to v1.1.1

##### Fixes

  * [#69](https://github.com/mmnaseri/spring-data-mock/issues/69):
   Fix the dependency on CGLIB

[v1.1](https://github.com/mmnaseri/spring-data-mock/releases/tag/v1.1)
------------------------------------------------

##### Enhancements

  * [#3](https://github.com/mmnaseri/spring-data-mock/issues/3):
  Add support for `QueryDslPredicateExecutor`

  * [#50](https://github.com/mmnaseri/spring-data-mock/issues/50):
  Update Maven dependencies

  * [#54](https://github.com/mmnaseri/spring-data-mock/issues/54):
  Indicating that the library depends on `commons-logging`

  * [#56](https://github.com/mmnaseri/spring-data-mock/issues/56):
  Add support for querying entities by example

##### Fixes

  * [#55](https://github.com/mmnaseri/spring-data-mock/issues/55):
  Fix the issue where JPA annotations aren't honored

[v1.0.3](https://github.com/mmnaseri/spring-data-mock/releases/tag/v1.0.3)
------------------------------------------------

##### Enhancements

  * [#29](https://github.com/mmnaseri/spring-data-mock/issues/29):
  Add logging to operations

  * [#25](https://github.com/mmnaseri/spring-data-mock/issues/25):
  Restructure the project to allow for a more sophisticated build reactor
  that would let us extend the project by adding optional artifacts that
  are used alongside the original artifact.

[v1.0.2](https://github.com/mmnaseri/spring-data-mock/releases/tag/v1.0.2)
------------------------------------------------

##### Enhancements

  * [#17](https://github.com/mmnaseri/spring-data-mock/issues/17):
  Add more JavaDoc to classes so that published sources and documentation
  at Maven Central make more sense and are more helpful.

  * [#18](https://github.com/mmnaseri/spring-data-mock/issues/18):
  Increase test coverage so that future development can be done with more
  certainty and users feel better about the source code they use.

  * [#27](https://github.com/mmnaseri/spring-data-mock/issues/27):
  Increase code quality metrics

  * [#28](https://github.com/mmnaseri/spring-data-mock/issues/28):
   Update project dependencies



[v1.0.1](https://github.com/mmnaseri/spring-data-mock/releases/tag/v1.0.1)
------------------------------------------------

##### Fixes

  * [#12](https://github.com/mmnaseri/spring-data-mock/issues/12):
  Add support for providing mock behavior for methods that are not
  strictly data-oriented on a repository (such as `Object.equals(...)`)

##### Enhancements

  * [#14](https://github.com/mmnaseri/spring-data-mock/issues/14):
  Add proper exception handling to the project

[v1.0](https://github.com/mmnaseri/spring-data-mock/releases/tag/v1.0)
------------------------------------------------

The original release with a complete rewrite of the initial platform.

This added many new features to the original code base, such as:

 * A more fluid DSL for defining mock factories and mocks
 * An abstraction for data stores that allows for more sophisticated
 mocking (such as binding mocks to an actual data store).
 * Use of listeners and auditing capabilities
 * Extensible operation handlers that would facilitate further
 enhancements
 * An extensible parser that supports newer operators
 * Better separation from the Spring Data codebase.