# Cucumber-JVM (Pre-V4) Selenium WedDriver Test Suite

[Selenium](http://www.seleniumhq.org/) is an automated test framework, which can be programmed to interact with a 
web site in the way that site users would.

Tests can be run using specific browser instances. This test runs using a single web browser (Google Chrome).  
This means that Chrome (v59 up) must be installed on any target server.

## Running the suite

#### Profiles and Arguments

##### Arguments

The following arguments, if passed through the command line will override the environment specific config in the 
embedded files. 

- `browserstack.url` is the connection URL for Browserstack.
- `grid.url` is the connection URL for Selenium Grid.
- `local.id` is only used for specifying an identifier for Browserstack.
- `build` is the build number of the solution.

##### Local Profile

This profile is selected by default, though can be specified by running `mvn clean verify -Plocal`.

No other arguments are required.

The default capability is to run Chrome on your local machine (platform independent), although firefox is also an available option (requiring a value change in the TestBase).

##### Selenium Grid Profile

Can be specified by running `mvn clean verify -Pgrid`.

###### Required Arguments

Running via the `./runseleniumgrid.sh` file, the following arguments need to be specified;

- `grid.url`
- `base.url`
- `buildnumber`

The above can also be specified using the `-DpropertyName=propertyValue` notation appended to the maven command, however the following are optional additions to running using maven.

- `parallel.tests` is the number of tests to run concurrently

##### Browserstack Profile

Can be specified by running `mvn clean verify -Pbstack`.

###### Required Arguments

Running via the `./runbrowserstack.sh` file, the following arguments need to be specified in order;

- `browserstack.url`
- `browser`
- `build`
- `local.id`

The above can also be specified using the `-DpropertyName=propertyValue` notation appended to the maven command, however the following are optional additions to running using maven.

- `parallel.tests` is the number of tests to run concurrently

#### Test Reports

Reports are generated using the [maven cucumber reporting](https://github.com/damianszczepanik/maven-cucumber-reporting) plugin provided the maven profile has been set.

The output directory (for grid/bstack profiles only, specified within the pom.xml) is `target/cucumber-html-reports/**`

Multiple JSON files are also created for use with [cucumber reports plugin](https://wiki.jenkins.io/display/JENKINS/Cucumber+Reports+Plugin)

The JSON files are in the `target/cucumber-parallel/` directory.

#### CI execution

##### Run the tests against Selenium Grid

Tests should be run against Selenium Grid through Jenkins/VSTS using `runseleniumgrid.sh`.

##### Run the tests against BrowserStack

Tests should be run against BrowserStack through Jenkins/VSTS using `runbrowserstack_<browser>.sh`.