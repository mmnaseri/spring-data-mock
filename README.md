
# Spring Data Mock

[![Donae](https://img.shields.io/badge/paypal-donate-yellow.svg)](https://paypal.me/mmnaseri)
[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg)](http://opensource.org/licenses/MIT)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mmnaseri.utils/spring-data-mock/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.mmnaseri.utils/spring-data-mock)
[![Dependency Status](https://www.versioneye.com/user/projects/5709ee7dfcd19a00415b101a/badge.svg?style=flat)](https://www.versioneye.com/user/projects/5709ee7dfcd19a00415b101a)
[![Open Issues](https://badge.waffle.io/mmnaseri/spring-data-mock.svg?label=ready&title=issues)](http://waffle.io/mmnaseri/spring-data-mock)
[![Build Status](https://travis-ci.org/mmnaseri/spring-data-mock.svg?branch=master)](https://travis-ci.org/mmnaseri/spring-data-mock)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/ad9f174fa0654a2b8c925b86973f272d)](https://www.codacy.com/app/mmnaseri/spring-data-mock)
[![Coverage Status](https://coveralls.io/repos/github/mmnaseri/spring-data-mock/badge.svg?branch=master)](https://coveralls.io/github/mmnaseri/spring-data-mock?branch=master)

-----------


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

## Downloading

You can either clone this project and start using it:

    $ git clone https://github.com/mmnaseri/spring-data-mock.git

or you can add a maven dependency since it is now available in Maven central:

    <dependency>
        <groupId>com.mmnaseri.utils</groupId>
        <artifactId>spring-data-mock</artifactId>
        <version>${latest-version}</version>
    </dependency>

## Quick Start

Regardless of how you add the necessary dependency to your project, mocking a repository can be as simple as:

    final UserRepository repository = builder().mock(UserRepository.class);
    
where `builder()` is a static method of the `RepositoryFactoryBuilder` class under package `com.mmnaseri.utils.spring.data.dsl.factory`.

An alternate way of mocking a repository would be by using the `RepositoryMockBuilder` class under the `com.mmnaseri.utils.spring.data.dsl.mock`
package:

    final RepositoryFactoryConfiguration configuration = ... ;
    final UserRepository repository = new RepositoryMockBuilder().useConfiguration(configuration).mock(UserRepository.class);
    
Documentation
-------------

For a complete documentation check out [the website](https://mmnaseri.github.io/spring-data-mock).

There you can get more information on how to download the framework, as well as how you can
incorporate it in your project to have hassle-free data store mocking capabilities added to
your shiny applications.

FAQ
-------------

  1. Why did you write this?

  > I was testing some pretty complicated services that relied on Spring Data to provide data. It was a lot of
  hassle to keep the test environment up-to-date with the test requirements as well as the real world situation.
  Also, it was pretty darn slow to run the tests, given database connection latency and all. I wanted to be able
  to isolate my services and test them regardless of the database features. To make it short, I wrote this
  framework to be able to separate integration/acceptance tests and unit tests.

  2. Why did you make this open source?

  > Because everything I use (or nearly so) in my line of work is open source. It was time I gave something back.
  Also, the people behind Spring rock. I felt like I was selling tickets to the concert of rockstars by releasing
  this.

  3. What is the main design decision behind this framework?

  > Make you do as little as possible.

  4. When should I use this?

  > You should only use this to write you *unit* tests. For anything else, you would want the whole application to
  come alive and work. Using mocks for that is a bad idea.

  5. This is going to be used at the level of code testing. Is it really well written?

  > It is. According to Cobertura, it has **100% code coverage**, and according to Codacy, it has **0 code issues**.
  It is maintained by myself the best I can. The rest is up to you.

Some Numbers and Facts
----------------------

  * This project has *1000+* individual unit tests.

  * This project has **100%** [code coverage](https://coveralls.io/github/mmnaseri/spring-data-mock)

  * This project has **95%** branch coverage rate.

  * The project issue response turn around is an average of 2 days.

  * It covers *all* the repository specifications in Spring Data Commons (except predicates -- support is planned).

  * It has more than 6k lines of code, a lot of which is unit tests.

  * Every public class or method has JavaDoc

  * There is a dedicated documentation website for this project at https://mmnaseri.github.io/spring-data-mock/

Contribution
------------

Since this project is aimed at the testing phase of your code, it is paramount that it is written with the best of
qualities and that it maintains the highest standard.

Contributors are more than welcome. In fact, I flag most of the issues I receive as `help wanted` and
there are really generous people out there who do take care of some issues.

If you see a piece of code that you don't like for whatever reason -- so long as that reason can be backed
by pioneers and standards -- feel free to dig in and change the code to your heart's content and create a
pull request.

Donation
--------

This software is written without any expectations. I developed this originally to solve a business need at
the company I was working at at the time, and then rewrote it for the purpose of open-sourcing it.

After it received some attention, I decided that I should sit down and redo it.

That is why I did a marathon development on it, and got it to a point where I could say it was safe for
public use.

It still has a lot of room for improvement and enhancements. Even though I will continue to develop and
maintain this framework, receiving donations would make it feel so much more real.

If you feel generous and want to buy me a cup of coffee, you can use my PayPal link: https://paypal.me/mmnaseri

Thank you in advance if you choose to donate! And if not, I hope you have some time to explore this framework
and give me feedback so that I can make it better.
