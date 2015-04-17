<i>Back to <a href='PHPAPITutorials.md'>PHP API Tutorials</a></i>

In this tutorial we will authenticate against the repository and create a new session object.

<i><b>Hint:</b> See the completed tutorial script <a href='PHPTutorialOneCompleteScript.md'>here</a></i>

<b><u>Step 1</u></b>

Create a new php source file.

<b><u>Step 2</u></b>

Include the relevant Alfreco PHP library files.  In this case we are only using the Repository and Session objects so our inclusion list reflects this.

```
   require_once "Alfresco/Service/Repository.php";
   require_once "Alfresco/Service/Session.php";
```

If installed correctly then the root of the Alfresco PHP library should be on the include path.  If this is not the case then the full path to the relevant Alfresco PHP library file will need to be provided.

<i><b>Hint:</b>  In general each code file provided in the Alfresco PHP library contains only one class that matches the source file name.</i>

<b><u>Step 3</u></b>

Define the details of the repository that you are going to connect to.

```
   $repositoryUrl = "http://localhost:8080/alfresco/api;
   $userName = "admin";
   $password = "admin";
```

The repository url is the web service end point URL for the Alfresco repository that we want to connect to.

We can authenticate against this repository with any number of user credentials, but in this example we will stick to the default 'admin' user credentials.

Remember to modify these details according to your Alfresco repository installation.

<b><u>Step 4</u></b>

Create the repository object.

```
   $repository = new Repository($repositoryUrl);
```

The repository object represents the Alfresco repository we are going to be working with.  It can be used to authenticate user credentials and as a Session factory.

<b><u>Step 5</u></b>

Authenticate the user credentials against the Alfresco repository.

```
   $ticket = $repository->authenticate($userName, $password);
```

Assuming the authentication call is successful, a repository ticket is returned.  This ticket represents an authenticated set of credentials.  It can be used to create a session which will do work as the tickets associated user, with that user's permissions and priviages.

<b><u>Step 6</u></b>

Create the session object.

```
   $session = $repository->createSession($ticket);
```

The session is created with in the context of a vaild ticket.  This provides the atuehntication details to the repository when futher communication (via webservices or HTTP) occures.

The session object will provide a start point for within which we can start to work with spaces and content files.  Later tutorials will show we can use the session object, for now we will be content that we have successfully created one.

<b><u>Step 7</u></b>

Add some HTML to give some confirmation that the script has executed successfully.

```
   <html>

   <head>
	<title>Basic Tutorial One - Authentication</title>
   </head>

   <body>
       <big>Basic Tutorial One - Authentication</big>
	   <p>
              Connected to repository <?php echo $repositoryUrl ?> 
              as user <?php echo $userName ?> 
              with ticket <?php echo $ticket ?>
           </p>
   </body>

   </html>
```

<b><u>Step 8</u></b>

Navigate to the script from your web browser.