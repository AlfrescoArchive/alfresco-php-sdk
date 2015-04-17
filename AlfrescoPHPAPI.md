# General Note #

Throughout the PHP SDK, namespace prefixes should be delimited via underscores and not via normal colons. For instance, in order to create a subfolder of a folder you will use `$subfolder = $folder->createChild('cm_folder', 'cm_contains', $childName)` (notice the use of "cm\_folder" and "cm\_contains" for "cm:folder" and "cm:contains", respectively). This differs from for instance the JavaScript API.

# Core Meta-Data and Content Manipulation API #

## Repository ##

### Description ###

The Repository object represents an Alfresco Repository.

The connection details for the Alfresco Repository concerned are definied in this object and it is responsible for authentication and creation of Session objects.

### Constructors ###

`public function __construct($connectionURL=LOCAL_CONNECTION_URL);`

Constructs a repository object connecting to the repository specified in the connection URL. If no connection URL is provided the local connection URL is assumed.

  * **`$connectionURL`** - the connection URL (default is the local connection URL)

### Properties ###

`$repository->connectionURL;`

### Methods ###

`public function authenticate($user, $password);`

Authenticates the user credentials against the repository. If the user name and password are successfully authenticated a valid ticket is returned.

  * **`$user`** - the user name
  * **`$password`** - the password
  * **return `String`** - a valid ticket

`public function createSession($ticket=null);`

Creates a new session from the provided ticket. If no ticket is provided either the current ticket will be used or an exception thrown, depending on what environment the script is running in.

  * param **`$ticket`** the ticket to base the session on (default is null)

## Session ##

### Description ###

The session object contains the current authentication details and is used as a container for local modifications to nodes.

### Properties ###

`$session->ticket;`

The currently authenticated ticket for this session.

`$session->repository;`

The repository that this session relates to.

`$session->namespaceMap;`

The associated instance of the namespace map.

`$session->dataDictionary;`

The associated instance of the data dictionary.

`$session->stores;`

A read only property that returns an array of all the Store's currently available in the repository.

### Methods ###

`public function getStore($address, $scheme="workspace");`

Gets the store object for a given address and scheme. If no scheme is specified the default is taken to be 'workspace'.

If the store does not exist null is returned.

  * **`$address`** - the address of the Store
  * **`$scheme`** - scheme of the Store, the default value is 'workspace'
  * **return `Store`** - the store requested, null if it does not exist

`public function getStoreFromString($value);`

`public function getNode($store, $id);`

Gets the Node of a specified id in a particular Store or from the string representation of the node.

If the Node does not exist null is returned.

  * **`$store`** - the Store
  * **`$id`** - the id of the Node
  * **return `Node`** - the node requested, null is it does not exist

`public function getNodeFromString($value);`

`public function query($store, $statement, $language='lucene');`

Executes a given query statement on the specified store. The query language can be specified, but the default is lucene.

The result is returned as an array of matching Node's.

  * **`$store`** - the Store to execute the query upon
  * **`$statement`** - the query statement
  * **`$language`** - optional parameter specifying the query language
  * **return `Node[]`** - array of matching nodes

`public function save();`

`public function clear();`

## Store ##

### Description ###

The Store object represents an Alfresco store. A store is made of two parts, the scheme and the address. Together these form an uniquie key to a store within a repository.

A store has a single root node which is accessible via the rootNode property. Store's also provide the context for a search.

### Properties ###

`$store->scheme;`

The scheme of the store, typically this is 'workspace'.

`$store->address;`

The address of the store. The most well known store is the 'SpacesStore'.

`$store->rootNode;`

The Node at the root of the store. All other nodes in the store are children of this node.

### Methods ###

`public function __toString();`

Returns the string representation of the store which takes the StoreRef form of 'scheme://address'.

## Node ##

### Properties ###

`$node->session;`

The session associated with this node.

`$node->store;`

The Store of the node.

`$node->id;`

The id of the Node.

`$node->type;`

The type of the Node.

`$node->properties;`

Read/write property that contains a map of node property values. The key is the full QName string of the property and the value is the current value of the property.

Setting this property sets all the property value with the provided map.

`$node->aspects;`

The list of aspects applied to this Node

`$node->children;`

`$node->parents;`

`$node->primaryParent;`

`$node->associations;`

### Dynamic Properties ###

### Methods ###

`public function setPropertyValues($properties);`

Overwrites the current property values of the node with those found in the passed map.

  * **`$properties`** - a full qname string => property value map

`public function hasAspect($aspect);`

Indicates whether an aspect is present on the node.

  * **`$aspect`** - the name of the aspect
  * **return** - boolean, true is aspect is present, false otherwise

`public function addAspect($aspect, $properties=null);`

Adds an aspect to the node. Optionally values for the aspects properties can be provided.

  * **`$aspect`** - the name of the aspect
  * **`$properties`** - a map of aspect properties values. This parameter is optional.

`public function removeAspect($aspect);`

Removes an aspect from the node.

  * **`$aspect`** - the aspect name

`public function updateContent($property, $mimetype, $encoding="UTF-8", $content=null);`

Updates the value of a content property. Returns the ContentData object that was created or updated.

  * **`$property`** - the content property name
  * **`$mimetype`** - the content mimetype
  * **`$encoding`** - the content encoding
  * **`$content`** - the content, an optional parameter
  * **return** - ContentData, the content data object created or updated.

`public function createChild($type, $associationType, $associationName)`

Creates a node child node of the given type. The association type and association type are also specified. The newly created node is returned.

  * **`$type`** - the type of the node
  * **`$associationType`** - the association type
  * **`$associationName`** - the association name
  * **return** - the created Node

`public function addChild($node, $associationType, $associationName);`

Adds a node as a non-primary child, with the association details specified.

  * **`$node`** - the child node
  * **`$associationType`** - the association type
  * **`$associationName`** - the association name

`public function removeChild($childAssociation);`

Removes a child association. If it is the primary assocation of the child then error will be raised.

  * **`$childAssocation`** - the child association

`public function addAssociation($toNode, $associationType);`

Adds as association of the given type from this node to another.

  * **`$toNode`** - the destination node
  * **`$associationType`** - the association type

`public function removeAssociation($association);`

Remove an association from this node.

  * **`$association`** - the association to remove

`public function copy($destinationNode, $associationType, $associationName,
$copyChildren=true);`

Copies the node into the specified destination node with the provided association details.

NOTE: Any modification made to the node being copied must have been saved before a copy can be made. If unsaved modifications are detected an exception will be raised.

  * **`$destinationNode`** - the node into which the new copy will be created
  * **`$associationType`** - the association type
  * **`$associationName`** - the name of the association
  * **`$copyChildren`** - indicates whether the children of the node should also be copied. The default value is true.

`public function move($destinationNode, $associationType, $associationName);`

Moves the node into the specified destination node, with the assocation details provided.

NOTE: Any modifications made to the node being moved must have been saved before the move can be made. If unsaved modifications are derected an expcetion will be raised.

  * **`$destinationNode`** - the node to move the node into
  * **`$associationType`** - the association type
  * **`$associationName`** - the association name

`public function hasPermission($permission);`

Determines whether the authenticated user has the specified permission on the node. Returns true if the permission is available for the user, false otherwise.

  * **`permission`** - the permission string (eg: 'Read', 'Write', etc)
  * **return** - boolean, true if the user has the permission on the node, false otherwise

`public function isSubTypeOf($subTypeOf);`

Determines whether this node is a sub type of the type specified.

  * **`$subTypeOf`** - is this node a sub type of this type.
  * **return** - true if it is, false otherwise

## Association ##

### Description ###

An association object represents an association between two nodes.

### Constructors ###

`public function __construct($from, $to, $type);`

### Properties ###

`$association->from;`

`$association->to;`

`$association->type;`

## ChildAssociation ##

### Description ###

A child association object represents a child association relationship between two nodes.

### Constructors ###

`public function __construtor($parent, $child, $type, $name, $isPrimary=false, nthSibling=0);`

### Properties ###

`$childAssociation->parent;`

`$childAssociation->child;`

`$childAssociation->type;`

`$childAssociation->name;`

`$childAssociation->isPrimary;`

`$childAssociation->nthSibling;`

## ContentData ##

### Description ###

Object containing content meta-data and access to the actual content for a content property.

### Properties ###

`$contentData->node;`

`$contentData->property;`

`$contentData->mimetype;`

`$contentData->encoding;`

`$contentData->size;`

`$contentData->url;`

`$contentData->guestUrl;`

`$contentData->content;`

### Methods ###

`setContent($content);`

`writeContentFromFile($fileName);`

`readContentToFile($fileName);`

## Extended Content API ##

## SpacesStore ##

### Extends ###

  * **Store**

### Constructor ###

`public function __construct($session);`

### Properties ###

`$spacesStore->companyHome;`

## File ##

### Description ###

A file node. This is a node of the standard cm\_content type.

### Extends ###

  * **Node**

### Properties ###

`$file->encoding;`

The encoding of the content. This is read/write property.

`$file->mimetype;`

The mimetype of the content. This is a read/write property.

`$file->size;`

The size of the content. Read-only property.

`$file->content;`

The content of the file. Read/write property;

`$file->url;`

The download URL of the content. Has the current ticket already appended. Read-only property.

## Folder ##

### Description ###

A folder node. This node is of the standard cm\_folder type.

### Extends ###

  * **Node**

### Properties ###

`$folder->files;`

A list of the sub-files;

`$folder->folders;`

A list of the sub-folders.

### Methods ###

`public function createFile($fileName, $type="cm_content");`

Creates a file in the folder. Optionally details of the file's content can be given during creation.

  * **`$fileName`** - the name of the file
  * **`$type`** - the type of file to create (optional)
  * **result** - File, the newly created file

`public function createFolder($folderName, $type="cm_folder");`

Creates a sub-folder.

> $folderName - the name of the folder
> $type - the type of folder to create (optional)
> result - Folder, the newly created folder

## Services ##

## NamespaceMap ##

### Constructors ###

`public function __construct($session);`

### Methods ###

`public function isShortName($shortName);`

`public function getFullName($shortName);`

`public function getFullNames($shortNames);`

`public function getShortName($fullName);`

## DataDictionary ##

### Constructors ###

`public function __construct($session);`

  * **`$session`** - the session object

### Methods ###

`public function isSubTypeOf($class, $subTypeOf);`

Indicates whether a class is the sub-class of another. Returns true if it is a sub-class, false otherwise.

  * **`$class`** - the class to test
  * **`$subTypeOf`** - the class do the sub-type test on
  * **boolean** - true if class is a sub-type, false otherwise

## Implementation Status ##
|Object/Method/Property|PHP API Status|Java API Status|
|:---------------------|:-------------|:--------------|
|Repository|Complete|Complete|
| - connectionUrl|Complete|Complete|