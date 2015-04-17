## Using PHP Scripts and Templates ##

Once the Alfresco PHP Integration AMP has been installed PHP scripts and templates can be used through out the Repository.

PHP scripts or templates placed in the usual location in the Data Dictionary space hierarchy will be available for selection in the web client along with the Freemarker and Java script templates and scripts.

When a script or template is executed, the repository determines which execute engine to use by looking at the file exetension. It is therefore important to name your PHP templates and scripts with the .php file extension.

PHP scripts and templates make use of the Alfresco PHP API to access the Alfresco repository.

## Accessing the Repository and Session objects within a PHP Script/Template ##

When a PHP script or template is executed a Repository object and a Session object are created and made available to the script or template.

The Repository object will refer to the Alfresco repository that the script/template is executing within and the Session will be based on the current users authentication details.

_**Hint:** This means the script or template will be executed with the current users permissions._

In order to gain access to the Repository object use the global variable `$_ALF_REPOSITORY`, to get the Session object use `$_ALF_SESSION`. When executing a script/template you can be guaranteed both these values will be populated and valid for the execution of the script/template.

The follow example shows access to the Repository and Session objects from a template/script:
```
 <?php
 
    // Output the repository location
    echo "Query executed on this repository - ".$_ALF_REPOSITORY->connectionURL."<br><br>";
 	
    // Get the session
    $session = $_ALF_SESSION;
     
    // Query for all nodes in the spaces store containing the word 'Alfresco'
    $nodes = $session->query(new SpacesStore(), "TEXT:\"Alfresco\"");
 
    // Output the node names
    foreach ($nodes as $node)
    {
        echo $node->cm_name."<br>";
    }
  
 ?>
```