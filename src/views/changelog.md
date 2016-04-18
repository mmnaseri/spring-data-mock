Change Log
==========

This page tries to list all the changes that are incorporated
in a given release.

Where applicable, an issue number and a link to the appropriate
issue is provided as well.


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