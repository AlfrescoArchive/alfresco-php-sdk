# Introduction #

Installing the PHP AMP will allow users to create and execute PHP templates and scripts along side the already provided JavaScript and FreeMarker scripts and templates.

The Alfresco PHP API used is identical to that provided in the PHP library, so common scripts can be used in either environment.

With additional configuration once the AMP is installed, it is possible to allow the application server to host PHP applications directly. PHP application hosted in this manner will also have access to the Alfresco PHP API. See [here](EnablingAlfrescoPHPServer.md) for more details.

# Installing the PHP AMP #

  * Use the MMT to install the PHP AMP into the selected WAR file
  * Restart the Alfresco repository with the updated WAR

# Checking the PHP AMP Installation #

  * Navigate into the data dictionary and then within the presentation templates space. You should see example .php templates present.
  * Navigate into the data dictionary and then within the scripts space. You should see example .php scripts present.

![http://wiki.alfresco-php-sdk.googlecode.com/git/images/Scripts.jpg](http://wiki.alfresco-php-sdk.googlecode.com/git/images/Scripts.jpg)

  * Go into the guest space and view the details of the Alfresco-Tutorial.pdf.
  * Apply the custom template 'doc\_info.php'

![http://wiki.alfresco-php-sdk.googlecode.com/git/images/Applytemplate.jpg](http://wiki.alfresco-php-sdk.googlecode.com/git/images/Applytemplate.jpg)

  * The template should execute successfully and the following details displayed.

![http://wiki.alfresco-php-sdk.googlecode.com/git/images/Appliedtemplate.jpg](http://wiki.alfresco-php-sdk.googlecode.com/git/images/Appliedtemplate.jpg)