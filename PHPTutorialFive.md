In the tutorial we will create a new content node.

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

      $nodes = $session->query($spacesStore, "PATH:\"app:company_home/app:guest_home\"");
      $contentNode = $nodes[0]; 	
```

If we want to create a html file, would be as follows:
```
      $upload = $contentNode->createChild('cm_content', 'cm_contains', $contentName);
      $contentData = new ContentData($newNode, 'cm:content')		

      $contentData->mimetype = 'text/html';
      $upload->cm_name = 'my_page.html';
      $upload->cm_description = 'Page Description';
      $contentData->encoding = 'UTF-8';
      $contentData->content = '<html><head><tile>My Page</title></head><body>Content Page</body></html>';
      $upload->cm_content = $contentData;
      $session->save();

   ?>

</html>

   <head>
	<title>Basic Tutorial Five - Create a new content node</title>
   </head>

   <body>
      <big>Basic Tutorial Five - Create a new content node</big><br><br>
    
   </body>

</html> 
```

If we want to create a space, would be as follows:
```
      $newSpace = $contentNode->createChild('cm_folder', 'cm_contains', 'Name Space');
      $newSpace->cm_name = 'Name Space';
      $newSpace->cm_description = 'Description of Space';
      $session->save();

?>

</html>

   <head>
	<title>Basic Tutorial Five - Create a new space</title>
   </head>

   <body>
      <big>Basic Tutorial Five - Create a new space</big><br><br>
    
   </body>

</html> 
```

If we want to create content node from a file uploaded to the web server, would be as follows:
```
      $file_name = $_FILES['file']['name'];
      $file_tmp = $_FILES['file']['tmp_name'];
      $file_type = $_FILES['file']['type'];
      $file_size = $_FILES['file']['size'];
  
      $upload = $node->createChild('cm_content', 'cm_contains', $file_name);
      $contentData = new ContentData($upload, "cm:content");
		
      $contentData->mimetype = $file_type;
      $upload->cm_name = $file_name;
      $upload->cm_description = 'File Description';
      $contentData->size = $file_size;
      $contentData->encoding = 'UTF-8';
      $contentData->writeContentFromFile($file_tmp);
      $upload->cm_content = $contentData;
      $session->save();

</html>

   <head>
	<title>Basic Tutorial Five - Create a new content from a file uploaded to the web server</title>
   </head>

   <body>
      <big>Basic Tutorial Five - Create a new content from a file uploaded to the web server</big><br><br>
    
   </body>

</html> 
```