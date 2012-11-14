/*
 * Copyright (C) 2005-2010 Alfresco Software Limited.
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
package org.alfresco.module.phpIntegration;

import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.jscript.ClasspathScriptLocation;
import org.alfresco.repo.transaction.RetryingTransactionHelper;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.ScriptLocation;
import org.alfresco.service.cmr.repository.ScriptService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.repository.TemplateService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.ApplicationContextHelper;
import org.alfresco.util.RetryingTransactionHelperTestCase;
import org.springframework.context.ApplicationContext;

/**
 * @author Roy Wetherall
 */
public class PHPTest extends RetryingTransactionHelperTestCase
{
    private static final String CLASSPATH_ROOT = "alfresco/module/phpIntegration/test/";
    
    private static final String TEST_SCRIPT = "<?php return \"SCRIPT_RESULT\" ?>";
    private static final String TEST_TEMPLATE = "<?php echo \"TEMPLATE_RESULT\" ?>"; 
    
    private ApplicationContext applicationContext;
    
    private NodeService nodeService;
    private ContentService contentService;
    @SuppressWarnings("unused")
    private TemplateService templateService;
    private ScriptService scriptService;
    private PHPProcessor phpProcessor;    
    
    private StoreRef storeRef;
    private NodeRef rootNode;
    private NodeRef templateNodeRef;
    private NodeRef scriptNodeRef;

    @Override
    protected void setUp() throws Exception
    {
        // Get the application context
        applicationContext = ApplicationContextHelper.getApplicationContext();

        // Get references to the required beans
        nodeService = (NodeService)applicationContext.getBean("NodeService");
        contentService = (ContentService)applicationContext.getBean("ContentService");
        templateService = (TemplateService)applicationContext.getBean("TemplateService");
        scriptService = (ScriptService)applicationContext.getBean("scriptService");
        phpProcessor = (PHPProcessor)applicationContext.getBean("phpProcessor");

        doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        // Create nodes used in the tests
		        storeRef = nodeService.createStore(StoreRef.PROTOCOL_WORKSPACE, "phpTest_" + System.currentTimeMillis());
		        rootNode = nodeService.getRootNode(storeRef);
		        
		        Map<QName, Serializable> props = new HashMap<QName, Serializable>(1);
		        props.put(ContentModel.PROP_NAME, "testTemplate.php");
		        templateNodeRef = nodeService.createNode(
		                rootNode, 
		                ContentModel.ASSOC_CHILDREN, 
		                ContentModel.ASSOC_CHILDREN, 
		                ContentModel.TYPE_CONTENT, 
		                props).getChildRef();
		        ContentWriter contentWriter = contentService.getWriter(templateNodeRef, ContentModel.PROP_CONTENT, true);
		        contentWriter.setEncoding("UTF-8");
		        contentWriter.setMimetype(MimetypeMap.MIMETYPE_TEXT_PLAIN);
		        InputStream is = this.getClass().getClassLoader().getResourceAsStream(CLASSPATH_ROOT + "testTemplate.php");
		        contentWriter.putContent(is);
		        
		        Map<QName, Serializable> props2 = new HashMap<QName, Serializable>(1);
		        props2.put(ContentModel.PROP_NAME, "testScript.php");
		        scriptNodeRef = nodeService.createNode(
		                rootNode, 
		                ContentModel.ASSOC_CHILDREN, 
		                ContentModel.ASSOC_CHILDREN, 
		                ContentModel.TYPE_CONTENT, 
		                props2).getChildRef();
		        ContentWriter contentWriter2 = contentService.getWriter(scriptNodeRef, ContentModel.PROP_CONTENT, true);
		        contentWriter2.setEncoding("UTF-8");
		        contentWriter2.setMimetype(MimetypeMap.MIMETYPE_TEXT_PLAIN);
		        InputStream is2 = this.getClass().getClassLoader().getResourceAsStream(CLASSPATH_ROOT + "testScript.php");
		        contentWriter2.putContent(is2);
		        
		        return null;
            }
        }, "admin");
    }
    
    /** ========= Test template processor implementation ========= */
    
    public void testTemplateFromNodeRef()
    {
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        StringWriter out = new StringWriter();
		        Map<String, Object> model = new HashMap<String, Object>();
		        phpProcessor.process(templateNodeRef.toString(), model, out);
		        
		        assertEquals("TEMPLATE_RESULT", out.toString());
		        
		        return null;
            }
        }, "admin");
    }
    
    public void testTemplateFromClasspath()
    {
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        StringWriter out = new StringWriter();
		        Map<String, Object> model = new HashMap<String, Object>();
		        phpProcessor.process(CLASSPATH_ROOT + "testTemplate.php", model, out);
        
		        assertEquals("TEMPLATE_RESULT", out.toString());    
		        
		        return null;
            }
        }, "admin");    
    }
    
    public void testTemplateFromString()
    {
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        StringWriter out = new StringWriter();
		        Map<String, Object> model = new HashMap<String, Object>();
		        phpProcessor.processString(TEST_TEMPLATE, model, out);
		        
		        assertEquals("TEMPLATE_RESULT", out.toString());
		        
		        return null;
		    }
		}, "admin");
    }
    
    /** ========= Test script processor implementation ========= */
    
    public void testScriptExecutionFromScriptLocation()
    {
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        Map<String, Object> model = new HashMap<String, Object>(1);
		        ScriptLocation scriptLocation = new ClasspathScriptLocation(CLASSPATH_ROOT + "testScript.php");        
		        Object result = phpProcessor.execute(scriptLocation, model);
		        
		        assertNotNull(result);
		        assertEquals("SCRIPT_RESULT", result.toString());
		        
		        return null;
		    }
		}, "admin");
    }
    
    public void testScriptExecutionFromNodeRef()
    {
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        Map<String, Object> model = new HashMap<String, Object>(1);
		        Object result = phpProcessor.execute(scriptNodeRef, ContentModel.PROP_CONTENT, model);
		        
		        assertNotNull(result);
		        assertEquals("SCRIPT_RESULT", result.toString());
		        
		        return null;
		    }
		}, "admin");
        
    }
    
    public void testScriptExecutionFromClasspath()
    {
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        Map<String, Object> model = new HashMap<String, Object>(1);
		        Object result = phpProcessor.execute(CLASSPATH_ROOT + "testScript.php", model);
		        
		        assertNotNull(result);
		        assertEquals("SCRIPT_RESULT", result.toString());
		        
		        return null;
		    }
		}, "admin");
    }
    
    public void testScriptExecutionFromString()
    {
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        Map<String, Object> model = new HashMap<String, Object>(1);
		        Object result = phpProcessor.executeString(TEST_SCRIPT, model);
		        
		        assertNotNull(result);
		        assertEquals("SCRIPT_RESULT", result.toString());
		        
		        return null;
		    }
		}, "admin");
    }    
    
    /** ========= Test execution from template and script services ========= */
    

    /** ========= Execute PHP test scripts ========= */
    
    public void testUnitTestMethods()
    {
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        Map<String, Object> model = new HashMap<String, Object>();        
		        scriptService.executeScript(CLASSPATH_ROOT + "testUnitTestMethods.php", model);
		        
		        return null;
		    }
		}, "admin");
    }
    
    public void testGlobalVariables()
    {
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        assertNotNull(templateNodeRef);
		        
		        Map<String, Object> model = new HashMap<String, Object>(6);
		        model.put("testStore", storeRef);
		        model.put("testString", "testString");
		        model.put("testNumber", 1);
		        model.put("nodeId", templateNodeRef.getId());
		        model.put("storeId", storeRef.getIdentifier());
		        model.put("testNode", templateNodeRef);
		        // TODO test dates and other common types
		        
		        StringWriter out = new StringWriter();        
		        phpProcessor.process(CLASSPATH_ROOT + "testModelAndGlobals.php", model, out);
		        
		        System.out.println("testGlobalVariables output:");
		        System.out.println(out.toString());
		        
		        return null;
		    }
		}, "admin");
    }
    
    public void testNamespaceMap()
    {
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        Map<String, Object> model = new HashMap<String, Object>(6);   
		        StringWriter out = new StringWriter();        
		        phpProcessor.process(CLASSPATH_ROOT + "testNamespaceMap.php", model, out);
		        
		        System.out.println("testNamespaceMap output:");
		        System.out.println(out.toString());
		        
		        return null;
		    }
		}, "admin");
    }
    
    public void testNode()
    {
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        // Create a folder
		        Map<QName, Serializable> props3 = new HashMap<QName, Serializable>(1);
		        props3.put(ContentModel.PROP_NAME, "testFolder");
		        NodeRef testFolder = nodeService.createNode(
		                rootNode, 
		                ContentModel.ASSOC_CHILDREN, 
		                ContentModel.ASSOC_CHILDREN, 
		                ContentModel.TYPE_FOLDER, 
		                props3).getChildRef();
		        
		        // Create the node
		        Map<QName, Serializable> props = new HashMap<QName, Serializable>(2);
		        props.put(ContentModel.PROP_NAME, "testNode.txt");
		        props.put(ContentModel.PROP_AUTHOR, "Roy Wetherall");
		        NodeRef nodeRef = nodeService.createNode(
		                testFolder, 
		                ContentModel.ASSOC_CONTAINS, 
		                QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, "testNode.txt"), 
		                ContentModel.TYPE_CONTENT, 
		                props).getChildRef(); 
		        
		        // Add some test content
		        ContentWriter contentWriter = contentService.getWriter(nodeRef, ContentModel.PROP_CONTENT, true);
		        contentWriter.setEncoding("UTF-8");
		        contentWriter.setMimetype(MimetypeMap.MIMETYPE_TEXT_PLAIN);
		        contentWriter.putContent("test content");
		        
		        // Add a couple of aspects
		        nodeService.addAspect(nodeRef, ContentModel.ASPECT_VERSIONABLE, null);
		        nodeService.addAspect(nodeRef, ContentModel.ASPECT_CLASSIFIABLE, null);     
		        
		        // Create a second test node
		        Map<QName, Serializable> props2 = new HashMap<QName, Serializable>(2);
		        props2.put(ContentModel.PROP_NAME, "testNode2.txt");
		        props2.put(ContentModel.PROP_AUTHOR, "Roy Wetherall");
		        NodeRef nodeRef2 = nodeService.createNode(
		                testFolder, 
		                ContentModel.ASSOC_CONTAINS, 
		                QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, "testNode2.txt"), 
		                ContentModel.TYPE_CONTENT, 
		                props2).getChildRef(); 
		        
		        // Add association from one node to the other
		        nodeService.addAspect(nodeRef, ContentModel.ASPECT_REFERENCEABLE, null);
		        nodeService.createAssociation(nodeRef, nodeRef2, ContentModel.ASSOC_REFERENCES);
		        
		        // Create the model
		        Map<String, Object> model = new HashMap<String, Object>(1);
		        model.put("testFolder", testFolder);
		        model.put("testNode", nodeRef);
		        
		        System.out.println("Executing testNode.php ... ");
		        
		        // Process the test script
		        StringWriter out = new StringWriter();        
		        phpProcessor.process(CLASSPATH_ROOT + "testNode.php", model, out);
		        System.out.println("testNode output:");
		        System.out.println(out.toString());
		        
		        System.out.println("Executing testNodeSave.php ... ");
		        
		        StringWriter out2 = new StringWriter();        
		        phpProcessor.process(CLASSPATH_ROOT + "testNodeSave.php", model, out2);
		        System.out.println("testNodeSave output:");
		        System.out.println(out2.toString());
		        
		        // Check the details of the node after the save 
		        System.out.println("Checking properties of node " + nodeRef.getId());
		        assertEquals("changed.txt", nodeService.getProperty(nodeRef, ContentModel.PROP_NAME));
		        assertEquals("Mr Trouble", nodeService.getProperty(nodeRef, ContentModel.PROP_AUTHOR));
		        
		        System.out.println("Executing testCopyMove.php ... ");
		        
		        // Set the titled property value
		        nodeService.setProperty(nodeRef, ContentModel.PROP_TITLE, "My Title");
		        
		        StringWriter out3 = new StringWriter();        
		        phpProcessor.process(CLASSPATH_ROOT + "testCopyMove.php", model, out3);
		        System.out.println("testCopyMove output:");
		        System.out.println(out3.toString());
		        
		        System.out.println("Executing testContentProperties.php ... ");
		        
		        StringWriter out4 = new StringWriter();        
		        phpProcessor.process(CLASSPATH_ROOT + "testContentProperties.php", model, out4);
		        System.out.println("testContentProperties output:");
		        System.out.println(out4.toString());
		        
		//        System.out.println("Tempory ... ");        
		//        Map<String, Object> model2 = new HashMap<String, Object>(1);
		//        model2.put("document", nodeRef);        
		//        StringWriter out5 = new StringWriter();        
		//        this.phpProcessor.process("alfresco/module/phpIntegration/script/append_copyright.php", model2, out5);
		//        System.out.println("result ...");
		//        System.out.println(out5.toString());
		//        ContentReader contentReader = this.contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);
		//        System.out.println("appended content ... " + contentReader.getContentString());
		        
		        return null;
		    }
		}, "admin");
        
    }
    
    public void testFileFolder()
    {
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
		        // Create a folder
		        Map<QName, Serializable> props3 = new HashMap<QName, Serializable>(1);
		        props3.put(ContentModel.PROP_NAME, "testFolder");
		        NodeRef testFolder = nodeService.createNode(
		                rootNode, 
		                ContentModel.ASSOC_CHILDREN, 
		                ContentModel.ASSOC_CHILDREN, 
		                ContentModel.TYPE_FOLDER, 
		                props3).getChildRef();
		        
		        // Create the node
		        Map<QName, Serializable> props = new HashMap<QName, Serializable>(2);
		        props.put(ContentModel.PROP_NAME, "testNode.txt");
		        props.put(ContentModel.PROP_AUTHOR, "Roy Wetherall");
		        NodeRef nodeRef = nodeService.createNode(
		                testFolder, 
		                ContentModel.ASSOC_CONTAINS, 
		                QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, "testNode.txt"), 
		                ContentModel.TYPE_CONTENT, 
		                props).getChildRef(); 
		        
		        // Add some test content
		        ContentWriter contentWriter = contentService.getWriter(nodeRef, ContentModel.PROP_CONTENT, true);
		        contentWriter.setEncoding("UTF-8");
		        contentWriter.setMimetype(MimetypeMap.MIMETYPE_TEXT_PLAIN);
		        contentWriter.putContent("test content");
		                
		        Map<String, Object> model = new HashMap<String, Object>(2);   
		        model.put("file", nodeRef);
		        model.put("folder", testFolder);
		        StringWriter out = new StringWriter();        
		        phpProcessor.process(CLASSPATH_ROOT + "testFileFolder.php", model, out);
		        
		        System.out.println("testFileFolder output:");
		        System.out.println(out.toString());
		        
		        return null;
		    }
		}, "admin");
    }

    /**
     * @see org.alfresco.util.RetryingTransactionHelperTestCase#getRetryingTransactionHelper()
     */
	@Override
	public RetryingTransactionHelper getRetryingTransactionHelper() 
	{
		return (RetryingTransactionHelper)applicationContext.getBean("retryingTransactionHelper");
	}
}
