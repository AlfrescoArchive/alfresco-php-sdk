# Installing the MediaWiki AMP #

The MediaWiki AMP installs the Alfresco MediaWiki integration into the Alfresco WAR.

Before installing make sure that the [PHP integration AMP](PHPTemplatingandScriptingAMPInstallationInstructions.md) is already installed and the [Alfresco PHP server](EnablingAlfrescoPHPServer.md) has been enabled.

The Alfresco MediaWiki AMP can only be installed on Alfresco installations that use MySQL. Other databases are not supported. when you install the AMP, the relevant MediaWiki tables are created in the Alfresco database. It is recommended you back up your Alfresco data before installing the MediaWiki AMP.

To install MediaWiki:

  1. Download the MediaWiki AMP.
  1. Use the MMT to install the MediaWiki AMP into the selected WAR file.
  1. Restart the Alfresco repository with the updated WAR.

# Getting Started #

  1. Access the Wiki by browsing to http://localhost:8080/alfresco/php/wiki/index.php. The Login page displays. <br />http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediawiki-login.JPG
  1. Use the default administration login (admin/admin) to login to display the standard MediaWiki interface. As the main page has nt been defined yet, this will be empty. <br />http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediawiki-mainpageempty.JPG
  1. Click the Edit tab to enter some details for the main page. <br />http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediawiki-editmainpage.JPG
  1. Enter the details of the revision change and click _Save_. The main page displays as you would expect. <br />http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediaiwiki-savedmainpage.JPG

# The MediaWiki Space #

A MediaWiki Space exists inside Alfresco Explorer.

To access this space:

  1. Open the Explorer client at http://localhost:8080/alfresco and log in as the admnistrator (admin, admin. The "Wiki" space displays in the root of Company Home. <br />http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediawiki-wikispace.JPG
  1. Open the Wiki space. You will see a template displaying information about the Wiki with some useful links and the content of the Wiki. Each time a page is saved via MediaWiki, the corresponding content is stored in the Wiki space.
  1. Click the Detail Summary of the main.mwv file to see that the contents of that document corresponds to the text entered when you created the main page via MediaWiki. <br /> http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediawiki-docdetails.JPG
  1. Click the details of the main.mwv file and inspect the version history of the document. The edit made via the MediaWiki interface has been recorded in documents version history. <br /> http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediawiki-versionhistory.JPG

The interchange of content between MediaWiki and Alfresco is currently only one way. This means that if you edit the content within Alfresco, these changes will not be reflected when viewing the page in MediaWiki.

# Configuring MediaWiki #

The Alfresco Media Integration AMP allows simple configuration of the Wiki from the corresponding Wiki Space. The custom view in the Wiki space shows the current configuration values and provides a convenient link to the Wiki main page. When entering MediaWiki from this link you are automatically signed into MediaWiki with your current authentication details.

http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediawiki-template.JPG

If you are a Wiki admin user, click on the edit configuration link. You can change the name and logo of the Wiki here.

http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediawiki-changeprops.JPG

Once saved these changes will take effect the next time you enter MediaWiki. Note that these configuration options are cached in the PHP session, so you will need to close your browser and refresh to see the changes in the MediaWiki interface if you have it open when making the configuration changes.

http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediawiki-updatedname.JPG

There are numerous other configuration options that are not yet covered, but the integration framework allows other configuration options to be modeled and added easily.

# Wiki Users #

By default, all users of the repository have access to the MediaWiki installation. For a user to become an administrator of the Wiki, they must be added to the associated Wiki administrators user group. Once a member of this group, the user will be able to change the wiki configuration. Normal wiki users cannot preform this task and will not have access to the configuration link from the template.

http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediawiki-groups.JPG

The Wiki users groups can be used to add a user as a "specific" user of this Wiki. In future releases, the Wiki will be able to be marked as public or private. When private, the users of the Wiki will be restricted to those who are members of the Wiki user group. Much like how site user access works.

# Share Integration #

When the MediaWiki Integration AMP is installed on a Labs 3c repository or higher it automatically integrates with the Share client. It does this by creating a MediaWiki page for every site that is created. This occurs automatically without any need for user intervention.

http://wiki.alfresco-php-sdk.googlecode.com/git/images/Share-createsite.JPG

http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediawiki-sitepage.JPG

The page that is created is based on a PHP template found in the data dictionary. You can edit it to provide the desired site page template.

http://wiki.alfresco-php-sdk.googlecode.com/git/images/Mediawiki-sitepagetemplate.JPG

The site Wiki page is named using the title of the Site and can be easily accessed from Share using the link that will be present on the Site details dashlet.

http://wiki.alfresco-php-sdk.googlecode.com/git/images/Share-mediawikilink.JPG