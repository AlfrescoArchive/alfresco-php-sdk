<i>Back to <a href='PHPAPITutorials.md'>PHP API Tutorials</a></i>

In the tutorial we will get a reference to a content node and access its properties and aspects.

<i><b>Hint:</b> See the completed tutorial script <a href='PHPTutorialThreeCompleteScript.md'>here</a></i>

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
	
      // Create a reference to the 'SpacesStore'
      $spacesStore = new SpacesStore($session);

   ?>

   <html>

   <head>
	<title>Basic Tutorial Three - Properties and Aspects</title>
   </head>

   <body>
      <big>Basic Tutorial Three - Properties and Aspects</big><br><br>

   </body>

   </html>
```

<b><u>Step 2</u></b>

Execute query to find a content node.

```
   $nodes = $session->query($spacesStore, "PATH:\"app:company_home/app:guest_home/cm:Alfresco-Tutorial.pdf\"");

   $contentNode = $nodes[0]; 
```

The given query is executed in the scope of a store.  In this case we are using a path based Lucene query to find the Alfresco tutorial PDF document.  Currently the only query lanaguge supported is Lucene, for further information of building Lucene queries go [here](https://wiki.alfresco.com/wiki/Search).

The query method returns a list of the node found.  In this instance we know we will only find one so we take the first entry in the list as the content node we will work with.

<b><u>Step 3</u></b>

Add the following to output the basic details of the node.

```
   <table border=1 cellpadding=2 cellspacing=3> 
       <tr>
    	   <td>Node Id</td>
    	   <td><?php echo $contentNode->id ?></td>
       </tr>
       <tr>
 	   <td>Node Type</td>
 	   <td><?php echo $contentNode->type ?></td>
       </tr> 	
   </table><br>
```

The node id is the UUID.  This is a unique identifier scoped by the containing store, in this case the SpacesStore.

The node type is the string form of the node's content type qualified name.  It looks like this "`{http://www.alfresco.org/model/content/1.0}content`" and had the quivalent short form, recognised by the PHP library, of "`cm_content`".

<b><u>Step 4</u></b>

Add the following to output all the names and values of the properties that are present on the node.

```
   Properties:<br> 	
   <table border=1 cellpadding=2 cellspacing=3> 	
   <?php
      // Iterate over each property name and value
      foreach ($contentNode->properties as $name=>$value)
      {
   ?>
         <tr>
            <td><?php echo $name ?></td>
            <td><?php echo $value ?></td>			
         </tr>
   <?php		
      }
   ?> 		
   </table><br>
```

We iterate over all the properties of the node outputing the name and value.  The name will be the string representation of the properties qualified name.

<u><b>Step 5</b></u>

Add the following to output the aspects applied to the node.

```
   Aspects:<br/> 	
   <table border=1 cellpadding=2 cellspacing=3> 	
   <?php
      // Iterate over the applied aspects
      foreach ($contentNode->aspects as $aspect)
      {
   ?>
         <tr>
            <td><?php echo $aspect ?></td>		
	 </tr>
   <?php
      }
   ?>
   </table>
```

The string version of the applied aspects qualified names are listed in the aspects property of a node.

<b><u>Step 6</u></b>

Navigate to the script from your web browser.  You should see the following.

![http://wiki.alfresco-php-sdk.googlecode.com/git/images/Tutorial3.jpg](http://wiki.alfresco-php-sdk.googlecode.com/git/images/Tutorial3.jpg)