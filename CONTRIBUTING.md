Contributing
------------

Contributors are more than welcome!

Since this project aims to help you in the testing phase of your code, it is paramount that it is
written with the best of qualities and that it maintains the highest standard.

Contributors are more than welcome. In fact, I flag most of the issues I receive as `help wanted` and
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

See the project website at https://mmnaseri.github.io/spring-data-mock for details, explanation, and other stuff.

Contact me for more info, if needed.
