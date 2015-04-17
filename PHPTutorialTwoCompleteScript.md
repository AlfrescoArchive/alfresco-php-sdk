<i>Back to <a href='PHPTutorialTwo.md'>PHP Tutorial Two</a></i>

```
<?php
	/**
	 * Basic Tutorial Two - Company Home
	 * 
	 * In the tutorial we will get a reference to the company home node.
	 * 
	 * A discusion of this tutorial can be found at http://wiki.alfresco.com/wiki/PHP_Tutorial_Two
	 * 
	 * Note: any changes to this file should be uploaded to the wiki
	 */ 
  
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
	
	// Get the company home node
	$companyHome = $spacesStore->companyHome;	 
?>

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