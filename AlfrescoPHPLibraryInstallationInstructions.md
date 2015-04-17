## Alfresco PHP Library Installation Instructions ##


---

### Download ###

---


The Alfresco PHP library is available via:

  * the [download](http://code.google.com/p/alfresco-php-sdk/downloads/list) area.
  * the [git project archive](http://code.google.com/p/alfresco-php-sdk/source/checkout).


---

### Prerequisites ###

---


  * Alfresco 2.0 (or higher) repository
  * PHP 5.x
  * Apache 2.0 configured with PHP (or similar)

_**Hint:**  This web site has some helpful tips on installing Apache 2.0 on WinXP [php.net](http://us2.php.net/manual/en/install.windows.apache2.php)_

_**Hint:** You might want to try [Easy PHP](http://www.easyphp.org/) if installing on Windows environment_


---

### Instructions ###

---


  * Unpack the contents of the Alfresco PHP library into a convenient directory, retaining the directory structure.
  * Ensure that the following SOAP extensions are installed/enabled by editing your php.ini to include the following.  (<i><b>Hint:</b> this may differ slightly on a non-Windows installation</i>)

```
 extension=php_mysql.dll
 extension=php_sockets.dll
 extension=php_soap.dll
```

  * On linux with php 5.x you do not need these entries in php.ini. Just type php -m and check for mysql, sockets and soap.

  * Edit your php.ini file and add the install directory to your include path.  For example:

```
 include_path="<the existing include path>:<your install directory>"
```

<i><b>Hint:</b>  have a look here for a brief introduction on how to set the <a href='http://www.modwest.com/help/kb.phtml?qid=98&cat=5'>PHP include path.</a></i>

_**Hint:**  this is a good resource on how to set-up [which php.ini file is loaded](http://www.php.net/manual/en/configuration.file.php)_

  * Configure your web server to include /alfresco as virtual directory.  This directory should map to the directory into which you unpacked the PHP library.  ([Configuring Apache For The Alfresco PHP Library](ConfiguringApacheForTheAlfrescoPHPLibrary.md))
  * Start your Alfresco repository
  * Run the sample scripts to ensure correct installation


---

### Running The Sample Scripts ###

---


#### Configuring The Samples ####

  * Edit the `<ALFRESCO-PHP-LIBRARY_HOME>/Examples/config.php` file
  * Modify the repository URL and connection credentials as required
  * Save and close config.php
  * The default configuration is as follows:

```
 $repositoryUrl = "http://localhost:8080/alfresco/api";
 $userName = "admin";
 $password = "admin";
```

<i><b>Hint:</b>  The repository URL points to the Axis end-point location for the repository that you wish the samples to connect to.</i>

#### Query Executer ####

  * Open http://myserver/alfresco/Examples/QueryExecuter/queryExecuter.php
  * The following web page should be shown:

http://wiki.alfresco-php-sdk.googlecode.com/git/images/QueryExecuter.JPG

  * Select the store you want to query from the list in the drop-down control, enter a valid Lucene query and click  the 'Submit Query' button.
  * Results from the query should be displayed.

http://wiki.alfresco-php-sdk.googlecode.com/git/images/QueryResults.JPG

<i><b>Hint:</b>  A good Lucene query to try is TEXT:"Alfresco", note that double-quotes are used to denote a string.</i>

#### Document Browser ####

  * Open http://myserver/alfresco/Examples/SimpleBrowse/index.php
  * The following should be displayed:

http://wiki.alfresco-php-sdk.googlecode.com/git/images/SimpleBrowse.JPG

  * Clicking on a space will navigate into that space
  * Clicking on a content item will show the content in the browser
  * The breadcrumb control at the top of the page can be used to navigate back up the space hierarchy.


---

### Running The Unit Tests ###

---


#### Why Unit Tests? ####

The PHP library is shipped with a number of unit tests that test the core features of the PHP library.  They do not necessarily provide good code examples, but do provide a way of easily checking the "health" of the PHP library and its connection to the repository.

Under normal circumstances it is not nessesary to run the unit tests unless you are experiencing some difficulties and wish to check that the PHP library is functioning as expected.

Unit tests are used throughout the product and the PHP library is no exception.

#### Installing PHPUnit ####

Before you can run any of the unit tests you need to install the PEAR package PHPUnit.

_**Hint:** Go [here](http://pear.php.net/manual/en/installation.php) for instructions on how to install PEAR if it isn't included in your PHP distribution._

_**Hint:** Go [here](http://pear.phpunit.de/) for details on the JUnit PEAR packages.  Only JUnit is currently needed._

#### Configuring The Unit Tests ####

  * Open the file 'my-install-dir/Tests/alfresco/BaseTest.php'
  * Look for the following code:

```
   const USERNAME = "admin";
   const PASSWORD = "admin";
   const URL = "http://localhost:8080/alfresco/api";
```

  * Modify the username, password and URL as required.
  * Save and close the file.

#### Running The Unit Test ####

  * Browse to the URL http://myserver/alfresco/Tests
  * The following page should be shown:

http://wiki.alfresco-php-sdk.googlecode.com/git/images/Unittests.JPG

  * Enter the tests you want to execute in the edit box provided.  Enter a new test location on seperate line.  In order to execute all the unit test enter the following into the edit box:

```
 alfresco/ContentTest
 alfresco/NodeTest
 alfresco/SessionTest
 alfresco/StoreTest
 alfresco/VersionTest
 alfresco/RepositoryTest
 alfresco/NamespaceMapTest
```

<i><b>Hint:</b>  Click on the test history link to get a list of recently run tests.</i>
  * Click on the 'Run Tests' button to execute tests and wait for result.
  * Once the tests have executed the following view will be shown:

http://wiki.alfresco-php-sdk.googlecode.com/git/images/Unittestsresult.JPG

  * Any test failures will be displayed on this page and can be used to diagnose any problems.