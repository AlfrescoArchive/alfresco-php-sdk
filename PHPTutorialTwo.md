<i>Back to <a href='PHPAPITutorials.md'>PHP API Tutorials</a></i>

In this tutorial we will get a reference to the company home node.  The company home node resides in the SpacesStore.  Spaces and documents that exist beneth the company home node are shown in the web client UI.

<i><b>Hint:</b> See the completed tutorial script <a href='PHPTutorialTwoCompleteScript.md'>here</a></i>

<b><u>Step 1</u></b>

Create a new PHP source file with the following skeleton.

```
   <?php

	// Include the required Alfresco PHP API objects  
	require_once "Alfresco/Service/Repository.php";
	require_once "Alfresco/Service/Session.php";
	require_once "Alfresco/Service/SpacesStore.php";

	// Specify the connection details
	$repositoryUrl = "http://localhost:8080/alfresco/api";
	$userName = "admin";
	$password = "admin"; 
	
	// Authenticate the user and create a session
	$repository = new Repository($repositoryUrl);
	$ticket = $repository->authenticate($userName, $password);
	$session = $repository->createSession($ticket); 
   ?>
```

<b><u>Step 2</u></b>

Create a reference to the Spaces store.

```
  $spacesStore = new SpacesStore($session); 
```

This is easily achieved using the SpacesStore helper class as shown above.  This extends the more general Store object which can be used to represent any store.

<i><b>Hint:</b>  The Spaces store is the repository store used by the user interface</i>

<b><u>Step 3</u></b>

Get the company home node.

```
   $companyHome = $spacesStore->companyHome;
```

The company home node is well known and as such the SpacesStore object has a helper property that allows direct access to it.

In most situations you will be manipulating spaces and files that reside beneath the company home node.

<i><b>Hint:</b>  All spaces and files under the company home are shown in the UI</i>

<b><u>Step 4</u></b>

Add some HTML to give some confirmation that the script has executed successfully.

```
   <html>

   <head>
	<title>Basic Tutorial Two - Company Home</title>
   </head>

   <body>
      <big>Basic Tutorial Two - Company Home</big>
      <p>
         Found the "<?php echo $companyHome->cm_name ?>" node with node 
         reference <?php echo $companyHome->__toString() ?>
      </p>
   </body>

   </html>
```

<b><u>Step 5</u></b>

Navigate to the script from your web browser.