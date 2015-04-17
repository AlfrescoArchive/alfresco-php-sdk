## Alfresco Media Wiki Installation Instructions For PHP Intregration ##


---

### Prerequisites ###

---


  * The Alfresco PHP library.  See [here](AlfrescoPHPLibraryInstallationInstructions.md) for installation instructions.
  * A MediaWiki standard install on the same web server as the PHP library.


---

### Installation ###

---


  * Copy ExternalStoreAlfresco.php and AlfrescoConfig.php from the `<ALFRESCO-PHP-LIBRARY_HOME>/Integration/MediaWiki` directory into the MediaWiki extensions directory.
  * Modify the values in AlfrescoConfig.php appropriately:
    * Set `$alfURL` to the webservice end-point URL of your Alfresco repository.
    * Set `$alfWikiStore` to the reference of the store that you want your content to be placed in.
    * Set `$alfWikiSpace` to the path of the space where you want your content to be placed. (<i><b>Hint:</b>  in the current implementation this space must already exist in the Alfresco repository</i>)
    * `$alfUser` and `$alfPassword` specifiy the connection details you want to use when connecting to the repository.
  * Add the following code to the LocalSettings.php found in the root of the MediaWiki.

```

 # ----
 # Alfresco Extension Configuration
 # ----
 
 # include the alfresco extensions classes
 require_once("extensions/ExternalStoreAlfresco.php");
 
 # Configure in some external stores
 $wgDefaultExternalStore = array("alfresco://localhost:8080/alfresco/api");
 $wgExternalStores = array("alfresco");

```

  * Edit the end-point URL found in the above config to ensure the correct repository is referenced.

### Testing Installation ###

  * Open your MediaWiki installation in the browser.
  * Add a new page to the main page.
  * Edit your new page to contain some content and save.
  * Open the Alfresco web client and navigate to the space specified in the config as your wiki space.
  * The newly created wiki document will be present in the wiki space.