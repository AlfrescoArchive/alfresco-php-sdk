In the tutorial we will build a simple form that updates the title and description properties of a content node.

## Step 1 ##

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
	
      // Use a serach to get the content node we are updating
      $nodes = $session->query($spacesStore, 
                               "PATH:\"app:company_home/app:guest_home/cm:Alfresco-Tutorial.pdf\"");
      $contentNode = $nodes[0]; 

   ?>
<html>

   <head>
	<title>Basic Tutorial Four - Updating Properties</title>
   </head>

   <body>
      <big>Basic Tutorial Four - Updating Properties</big><br><br>
    
   </body>

</html>
```


## Step 2 ##

Add form for editing Title and Description:

```
<html>

   <head>
	<title>Basic Tutorial Four - Updating Properties</title>
   </head>

   <body>
      <big>Basic Tutorial Four - Updating Properties</big><br><br>

      	<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="post">
      		<label for="title">Title:</label><br />
      		<input type="text" id="title" name="title" size="60" 
                     value="<?php echo $contentNode->properties["{http://www.alfresco.org/model/content/1.0}title"]; ?>" /><br /><br />
      		
      		<label for="description">Description:</label><br />
      		<input type="text" id="description" name="description" size="60" 
                     value="<?php echo $contentNode->properties["{http://www.alfresco.org/model/content/1.0}description"]; ?>" /><br /><br />
      		
      		<input type="submit" id="submit" value="Save »" />
      	</form>
   </body>

</html>
```

## Step 3 ##

Add source for saving Title and Description:

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
	
      // Use a serach to get the content node we are updating
      $nodes = $session->query($spacesStore, 
                               "PATH:\"app:company_home/app:guest_home/cm:Alfresco-Tutorial.pdf\"");
      $contentNode = $nodes[0];

      // Check if form was submit
      if( !empty($_POST['title']) || !empty($_POST['description']) ) {
            // Get current properties
            $properties = $contentNode->properties;

            // Get from $_POST the title and description values
            $title = trim( $_POST['title'] );
            $description = trim( $_POST['description'] );

            // if title is not empty then we set new title value in properties of node
            if( !empty($title) ) {
                  $properties["{http://www.alfresco.org/model/content/1.0}title"] = $title;
            }

            // if description is not empty then we set new description value in properties of node
            if( !empty($description) ) {
                  $properties["{http://www.alfresco.org/model/content/1.0}description"] = $description;
            }

            // save properties
            $contentNode->setProperties($properties);
            $session->save();
      }
   ?>
```