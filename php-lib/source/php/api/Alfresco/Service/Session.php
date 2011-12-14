<?php
/*
 * Copyright (C) 2005-2011 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */

require_once('Store.php');
require_once('Node.php');
require_once('WebService/WebServiceFactory.php');

class Session extends BaseObject {
	public $authenticationService;
	public $repositoryService;
	public $contentService;

	private $_repository;
	private $_ticket;
	/** @var Store[] */
	private $_stores;
	/** @var NamespaceMap */
	private $_namespaceMap;

	private $nodeCache;
	private $idCount = 0;

	/**
	 * Constructor
	 *
	 * @param $repository
	 * @param $ticket the currently authenticated users ticket
	 */
	public function __construct($repository, $ticket) {
		$this->nodeCache = array();

		$this->_repository = $repository;
		$this->_ticket = $ticket;

		$this->repositoryService = WebServiceFactory::getRepositoryService($this->_repository->connectionUrl, $this->_ticket);
		$this->contentService = WebServiceFactory::getContentService($this->_repository->connectionUrl, $this->_ticket);
	}

	/**
	 * Creates a new store in the current repository
	 *
	 * @param $address the address of the new store
	 * @param $scheme the scheme of the new store, default value of 'workspace'
	 * @return Store the new store
	 */
	public function createStore($address, $scheme = "workspace") {
		// Create the store
		$result = $this->repositoryService->createStore(array(
				'scheme' => $scheme,
				'address' => $address)
		);
		$store = new Store($this, $result->createStoreReturn->address, $result->createStoreReturn->scheme);

		// Add to the cached list if its been populated
		if (isset($this->_stores)) {
			$this->_stores[] = $store;
		}

		// Return the newly created store
		return $store;
	}

	/**
	 * Gets the store
	 *
	 * @param $address the address of the store
	 * @param $scheme the scheme of the store.  The default it 'workspace'
	 * @return Store the store
	 */
	public function getStore($address, $scheme = "workspace") {
		return new Store($this, $address, $scheme);
	}

	/**
	 * Gets the store from it string representation (eg: workspace://SpacesStore)
	 *
	 * @param string $value the stores string representation
	 * @return Store the store
	 */
	public function getStoreFromString($value) {
		list($scheme, $address) = explode('://', $value);
		return new Store($this, $address, $scheme);
	}

	/**
	 * Gets a Node
	 *
	 * @param Store $store
	 * @param $id
	 * @return Node
	 */
	public function getNode(Store $store, $id) {
		$node = $this->getNodeImpl($store, $id);
		if ($node === NULL) {
			$node = new Node($this, $store, $id);
			$this->addNode($node);
		}
		return $node;
	}

	public function getNodeFromString($value) {
		// TODO
		throw new Exception('getNode($value) not yet implemented', 1322830975);
	}

	/**
	 * Adds a new node to the session.
	 *
	 * @param $node
	 */
	public function addNode($node) {
		$this->nodeCache[$node->__toString()] = $node;
	}

	private function getNodeImpl(Store $store, $id) {
		$result = NULL;
		$nodeRef = $store->scheme . '://' . $store->address . '/' . $id;
		if (array_key_exists($nodeRef, $this->nodeCache)) {
			$result = $this->nodeCache[$nodeRef];
		}
		return $result;
	}

	/**
	 * Commits all unsaved changes to the repository
	 *
	 * @param boolean $debug
	 * @return void
	 */
	public function save($debug = FALSE) {
		// Build the update statements from the node cache
		$statements = array();
		foreach ($this->nodeCache as $node) {
			$node->onBeforeSave($statements);
		}

		if ($debug) {
			var_dump($statements);
			echo ('<br /><br />');
		}

		if (count($statements) > 0) {
			// Make the web service call
			$result = $this->repositoryService->update(array("statements" => $statements));

			// Update the state of the updated nodes
			foreach ($this->nodeCache as $node) {
				$node->onAfterSave($this->getIdMap($result));
			}
		}
	}

	/**
	 * Clears the current session by emptying the node cache.
	 *
	 * WARNING:  all unsaved changes will be lost when clearing the session.
	 */
	public function clear() {
		// Clear the node cache
		$this->nodeCache = array();
	}

	private function getIdMap($result) {
		$return = array();
		$statements = $result->updateReturn;
		if (is_array($statements)) {
			foreach ($statements as $statement) {
				if ($statement->statement == "create") {
					$id = $statement->sourceId;
					$uuid = $statement->destination->uuid;
					$return[$id] = $uuid;
				}
			}
		} else {
			if ($statements->statement == "create") {
				$id = $statements->sourceId;
				$uuid = $statements->destination->uuid;
				$return[$id] = $uuid;
			}
		}
		return $return;
	}

	public function query(Store $store, $query, $language = 'lucene') {
		// TODO need to support paged queries
		$result = $this->repositoryService->query(array(
			"store" => $store->__toArray(),
			"query" => array(
				"language" => $language,
				"statement" => $query
			),
			"includeMetaData" => false
		));

		// TODO for now do nothing with the score and the returned data
		$resultSet = $result->queryReturn->resultSet;
		return $this->resultSetToNodes($this, $store, $resultSet);
	}

	public function getTicket() {
		return $this->_ticket;
	}

	public function getRepository() {
		return $this->_repository;
	}

	/**
	 * Gets the namespace map.
	 *
	 * @return NamespaceMap
	 */
	public function getNamespaceMap() {
		if ($this->_namespaceMap === NULL) {
			$this->_namespaceMap = new NamespaceMap();
		}
		return $this->_namespaceMap;
	}

	/**
	 * Gets the stores.
	 *
	 * @return Store[]
	 */
	public function getStores() {
		if (!isset($this->_stores)) {
			$this->_stores = array();
			$results = $this->repositoryService->getStores();

			foreach ($results->getStoresReturn as $result) {
				$this->_stores[] = new Store($this, $result->address, $result->scheme);
			}
		}

		return $this->_stores;
	}

	/** Want these methods to be package scope some how! **/

	public function nextSessionId() {
		$sessionId = 'session' . $this->_ticket . $this->idCount;
		$this->idCount++;
		return $sessionId;
	}
}

?>