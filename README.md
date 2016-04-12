# Spring Data Mock

[![Donae](https://img.shields.io/badge/paypal-donate-yellow.svg)](https://paypal.me/mmnaseri)
[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg)](http://opensource.org/licenses/MIT)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mmnaseri.utils/spring-data-mock/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.mmnaseri.utils/spring-data-mock)
[![Dependency Status](https://www.versioneye.com/user/projects/5709ee7dfcd19a00415b101a/badge.svg?style=flat)](https://www.versioneye.com/user/projects/5709ee7dfcd19a00415b101a)
[![Open Issues](https://badge.waffle.io/mmnaseri/spring-data-mock.svg?label=ready&title=issues)](http://waffle.io/mmnaseri/spring-data-mock)
[![Build Status](https://travis-ci.org/mmnaseri/spring-data-mock.svg?branch=master)](https://travis-ci.org/mmnaseri/spring-data-mock)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/b3c6793832b247cda253041f9a12fe2e)](https://www.codacy.com/app/mmnaseri/spring-data-mock)
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

Contribution
------------

This is a project that is quite large. Last I checked, according to [OpenHub](https://www.openhub.net)
this project had more than 6500 lines of code and was worth more than
[$85k](https://www.openhub.net/p/spring-data-mock/estimated_cost).

This should give you some idea of how big this project is to be maintained as a side project. Since the
project is aimed at the testing phase of your code, it is paramount that it is written with the best of
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

If you feel generous and want to donate, you can use my PayPal link: https://paypal.me/mmnaseri

Thank you in advance if you choose to donate! And if not, I hope you have some time to explore this framework
and give me feedback so that I can make it better.
