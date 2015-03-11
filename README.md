# spring-data-mock
Mock facility for Spring Data repositories.

Using this tool you can mock your repositories. This is specially useful when you want to test services relying on repositories in an isolated manner.

Let's say that I have a repository called "PersonRepository" which has the following method:

    List<Person> findTop5PeopleByNameAndAddressLike(String name, String address, Pageable pageable);

and I have a service method which, as part of its logic, leverages this method (among other database methods) to access data. If I wanted to test this service method, I would like to be able to inject the repository into that service as a dependency, but without it actually connecting to a database.

What we usually do is create a whole different set of database environments for our testing and then start up a whole instance of the application context capable of creating the repositories and injecting them and then getting the instance of the single bean the test is supposed to evaluate.

## The Alternative

This is the alternative offered by this framework:

    @BeforeClass
    public void setup() {
      this.personRepository = RepositoryMock.forRepository(PersonRepository.class, "id", KeyGeneration.STRING_RANDOM).mock();
      this.service = new PeopleServiceImpl(personRepository);
    }
    
    @After
    public void testCleanup() {
      this.personRepository.deleteAll();
    }

This way, you can directly instantiate the service class without having to load the entire application context or create complicated, hard-to-maintain profiles, and then treat the repository like any other data repository. When you are done, simply delete the data inside the repository.

## Initial Data

You can, of course start by inserting data the usual way:

    final Person p = new Person();
    p.setName("Milad");
    this.personRepository.save(p);

Or you can pass in the initial data when mocking:

    RepositoryMock.forRepository(PersonRepository.class, "id", KeyGeneration.STRING_RANDOM)
        .withData(p, q, r)
        .mock();

Note that the initial data passed this way will have to have its ID property set, whereas the insertion works like a normal DB would and sets the ID value for you.

## License

This project is released under the MIT license.
