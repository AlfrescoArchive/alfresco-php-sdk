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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.alfresco.repo.importer.ImporterComponent;
import org.alfresco.repo.transaction.RetryingTransactionHelper;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.view.ImporterBinding;
import org.alfresco.service.cmr.view.Location;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.ApplicationContextHelper;
import org.alfresco.util.RetryingTransactionHelperTestCase;
import org.springframework.context.ApplicationContext;

/**
 * @author Roy Wetherall
 */
public class PHPUpdateExamplesTest extends RetryingTransactionHelperTestCase
{
	private ApplicationContext applicationContext;
	
	@Override
    protected void setUp() throws Exception
    {
        // Get the application context
        applicationContext = ApplicationContextHelper.getApplicationContext();
    }
	
	@Override
	public RetryingTransactionHelper getRetryingTransactionHelper() 
	{
		return (RetryingTransactionHelper)applicationContext.getBean("retryingTransactionHelper");
	}
	
    public void testUpdateTemplatesAndScripts()
    {        
    	doTestInTransaction(new Test<Void>()
        {
            public Void run()
            {
            	importFile("alfresco/module/phpIntegration/script/script-import.xml", "/app:company_home/app:dictionary/app:scripts");
            	importFile("alfresco/module/phpIntegration/template/template-import.xml", "/app:company_home/app:dictionary/app:content_templates");
            	
            	return null;
            }
        }, "admin");
    }
    
    private void importFile(final String file, final String destination)
    {   
    	doTestInTransaction(new Test<Void>()
    	{
	        public Void run()
	        {     
		        ImporterComponent importer = (ImporterComponent)applicationContext.getBean("importerComponent");
		        
		        InputStream viewStream = getClass().getClassLoader().getResourceAsStream(file);
		        InputStreamReader inputReader = new InputStreamReader(viewStream);
		        BufferedReader reader = new BufferedReader(inputReader);
		
		        Location location = new Location(new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore"));
		        location.setPath(destination);
		        
		        importer.importView(reader, location, new TempBinding(), null);
		        
		        return null;
	        }
    	}, "admin");
    }
    
    private static class TempBinding implements ImporterBinding
    {   
        /* (non-Javadoc)
         * @see org.alfresco.service.cmr.view.ImporterBinding#getValue(java.lang.String)
         */
        public String getValue(String key)
        {
            return null;
        }

        /*
         *  (non-Javadoc)
         * @see org.alfresco.service.cmr.view.ImporterBinding#getUUIDBinding()
         */
        public UUID_BINDING getUUIDBinding()
        {
            // always use create new strategy for bootstrap import
            return UUID_BINDING.UPDATE_EXISTING;
        }

        /*
         *  (non-Javadoc)
         * @see org.alfresco.service.cmr.view.ImporterBinding#searchWithinTransaction()
         */
        public boolean allowReferenceWithinTransaction()
        {
            return true;
        }

        /*
         *  (non-Javadoc)
         * @see org.alfresco.service.cmr.view.ImporterBinding#getExcludedClasses()
         */
        public QName[] getExcludedClasses()
        {
            // Note: Do not exclude any classes, we want to import all
            return new QName[] {};
        }
    }

}
