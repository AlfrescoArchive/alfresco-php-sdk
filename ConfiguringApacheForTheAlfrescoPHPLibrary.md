<i>Back to <a href='AlfrescoPHPLibraryInstallationInstructions.md'>Alfresco PHP Library Installation Instructions</a></i>

## Configuring Apache For The Alfresco PHP Library ##

The following instrcutions show how Apache can be configured with a virtual directory that points your Alfresco PHP library installation.

  * Open your `httpd.conf` file
  * Add the following and replace `<install-directory>` with the alfresco-php-library folder location:

```
 Alias /alfresco "<install-directory>"

 <Directory "<install-directory>">
    Options Indexes MultiViews
    AllowOverride None
    Order allow,deny
    Allow from all
 </Directory>
```

  * Save and close the modified httpd.conf file
  * Re-start Apache for the changes to take effect
  * Goto `http://localhost/alfresco`, you should see a directory listing of the contents of the install directory. (<i><b>Hint:</b> if the contents of the install directory are not listed ensure that directory listing is allowed in your Apache configuration</i>)