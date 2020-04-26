Spring Data Mock
================

[![Donae](https://img.shields.io/badge/paypal-donate-yellow.svg)](https://paypal.me/mmnaseri)
[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg)](http://opensource.org/licenses/MIT)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mmnaseri.utils/spring-data-mock/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.mmnaseri.utils/spring-data-mock)
[![Build Status](https://travis-ci.org/mmnaseri/spring-data-mock.svg?branch=master)](https://travis-ci.org/mmnaseri/spring-data-mock)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/ad9f174fa0654a2b8c925b86973f272d)](https://www.codacy.com/app/mmnaseri/spring-data-mock)
[![Coverage Status](https://coveralls.io/repos/github/mmnaseri/spring-data-mock/badge.svg?branch=master)](https://coveralls.io/github/mmnaseri/spring-data-mock?branch=master)

-----------

Spring Data Mock is a framework for mocking Spring
Data repositories.

### Download

You can download this framework via:

  * [Github](http://github.com/mmnaseri/spring-data-mock)
  * Maven Central by adding this dependency to your `pom.xml` file:

    <pre><code><dependency>
        <groupId>com.mmnaseri.utils</groupId>
        <artifactId>spring-data-mock</artifactId>
        <version>${latest-version}</version>
        <scope>test</scope>
    </dependency></code></pre>

  * By cloning the project and installing it locally:

    ```bash
    git clone https://github.com/mmnaseri/spring-data-mock.git
    cd spring-data-mock
    mvn install
    ```

    and then adding the same dependency as above, or just grabbing the
    JAR file locally from the `target` folder.


### Change Log

You can see the change log for this project at the [Changes](#/changelog) page.

### Future

The future of this project depends highly on the availability of contributors and users that drive its features.

At the moment I am maintaining this myself since this is something I actively
use (that is actually how this framework was born to begin with).

[Donating](https://paypal.me/mmnaseri) certainly doesn't hurt in my esteem of the project
and how much time I dedicate to it.

However, there is [a road map](#/roadmap) that I keep, just to stay focused when I do
any development work on this project.

### Issues and Feature Requests

If you have any issues or if there are features that you require, you are more than
welcome to  submit them via the [issues page](https://github.com/mmnaseri/spring-data-mock/issues)
at Github. I will try to keep track of them, and assign them to a planned future release.

Contribution
------------

Since this project aims to help you in the testing phase of your code, it is paramount that it is
written with the best of qualities and that it maintains the highest standard.

Contributors are more than welcome. In fact, I flag most of the issues I receive as <kbd>help wanted</kbd> and
there are really generous people out there who do take care of some issues.

If you see a piece of code that you don't like for whatever reason -- so long as that reason can be backed
by pioneers and standards -- feel free to dig in and change the code to your heart's content and create a
pull request.

### Building the Code

To make the code builds universal and canonical, I have a Docker configuration attached to this project
which installs OpenJDK 8 on Ubuntu Xenial. This is the build environment I will be using to test and release
the code.

```bash
docker build -t spring-data-mock:jdk8 .
docker run -it --rm -v $(pwd):/src spring-data-mock:jdk8
```

Then, from inside the Docker image, you can build and test the project with Maven:

```bash
cd spring-data-mock
mvn test
``` 