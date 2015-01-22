/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. Distributed under license by Red Hat, Inc.
 * All rights reserved. This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Red Hat, Inc.
 ******************************************************************************/
package com.openshift3.internal.client.capability.resources;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.openshift3.client.IClient;
import com.openshift3.client.ResourceKind;
import com.openshift3.client.model.IPod;
import com.openshift3.client.model.IReplicationController;
import com.openshift3.internal.client.capability.resources.AnnotationDeploymentTraceability;

@RunWith(MockitoJUnitRunner.class)
public class AnnotationDeploymentTraceabilityTest {

	private AnnotationDeploymentTraceability capability;

	@Mock private IReplicationController deployment;
	@Mock private IPod resource;
	@Mock private IClient client;
	
	@Before
	public void setUp(){
		capability = new AnnotationDeploymentTraceability(resource, client);
		
		when(resource.getNamespace()).thenReturn("mynamespace");
		
		when(client.get(eq(ResourceKind.ReplicationController), eq("foobar"), eq("mynamespace")))
			.thenReturn(deployment);
	}
	
	@Test
	public void supportedWhenAnnotationsHaveADeploymentKey(){
		when(resource.isAnnotatedWith(eq("deployment"))).thenReturn(true);
		when(resource.getAnnotation("deployment")).thenReturn("foobar");

		assertEquals("Exp. to get the deployment", deployment, capability.getDeployment());
		
		verify(client).get(eq(ResourceKind.ReplicationController), eq("foobar"), eq("mynamespace"));
	}

	@Test
	public void unsupportedWhenAnnotationsDoNotHaveADeploymentKey(){
		assertNull("Exp. to get the deployment", capability.getDeployment());
	}
	
}
