# Using the Alfresco as a PHP Server #

If correctly configured, the Alfresco server can, not only, process PHP file within the repository as templates and scripts, as we have already seen, but can also server PHP files directly from within the Alfresco WAR.

This makes it possible to host an standard PHP application within the same WAR as Alfresco and therefore within the same application server and database. This makes backup/clustering and other administrative task much easier as there is only one application server/database to consider.

PHP application being servered in this manner also have access to the Alfresco PHP API, but since the PHP application is being hosted in the same process connection to the repository is made using the Java native API rather than the Web Service API (as it would using the remote Alfresco lib). This had obvious performance benifits.

_**NOTE:** In version 2.x of the Alfresco repository this functionality is experimental and it is not recommended that it is enabled on a production installation._

# Configuring the Alfresco as a PHP Server #

  1. Install the PHP AMP <br /> See [here](PHPTemplatingandScriptingAMPInstallationInstructions.md) for installation details.
  1. Open the relevant web.xml file for editing <br /> In order to configure the Alfresco web application to act as a PHP Server the web.xml file of the Alfresco Web Client needs to be modified.<br />  This is best done within the WAR or the expanded WAR once the PHP AMP has been successfully deplyed. Note that any changes to the web.xml made in the way will be overwriten when a new WAR is deloyed.<br /> The web.xml file can be found in the WEB-INF directory of the Alfresco WAR.
  1. Add the following configuration to the web.xml file
```
   <!-- Add at the end of the existing servlet tags -->

   <servlet>
     <servlet-name>QuercusServlet</servlet-name>
     <servlet-class>org.alfresco.module.phpIntegration.servlet.AlfrescoQuercusServlet</servlet-class>
   </servlet>

   <!-- Add at the end of the existing servlet-mapping tags -->

   <servlet-mapping>
      <servlet-name>QuercusServlet</servlet-name>
      <url-pattern>*.php</url-pattern>
   </servlet-mapping>
```
  1. Restart the application server

# Testing PHP Server from the Alfresco WAR #

Access the URL http://localhost:8080/alfresco/php/Examples/phpinfo.php. You will see the following information about the PHP installation.

http://wiki.alfresco-php-sdk.googlecode.com/git/images/Php-phpinfo.JPG