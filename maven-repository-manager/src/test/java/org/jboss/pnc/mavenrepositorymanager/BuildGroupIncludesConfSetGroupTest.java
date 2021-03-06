/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.pnc.mavenrepositorymanager;

import org.commonjava.indy.client.core.Indy;
import org.commonjava.indy.model.core.Group;
import org.commonjava.indy.model.core.StoreKey;
import org.commonjava.indy.model.core.StoreType;
import org.jboss.pnc.mavenrepositorymanager.fixture.TestBuildExecution;
import org.jboss.pnc.spi.repositorymanager.BuildExecution;
import org.jboss.pnc.spi.repositorymanager.model.RepositorySession;
import org.jboss.pnc.test.category.ContainerTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.apache.commons.lang.StringUtils.join;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@Category(ContainerTest.class)
public class BuildGroupIncludesConfSetGroupTest extends AbstractRepositoryManagerDriverTest {

    @Test
    public void verifyGroupComposition_ProductVersion_WithConfSet() throws Exception {
        // create a dummy composed (chained) build execution and a repo session based on it
        BuildExecution execution = new TestBuildExecution("build_myproject_67890");
        Indy indy = driver.getIndy(accessToken);

        RepositorySession repositoryConfiguration = driver.createBuildRepository(execution, accessToken);
        String repoId = repositoryConfiguration.getBuildRepositoryId();

        assertThat(repoId, equalTo(execution.getBuildContentId()));

        // check that the build group includes:
        // - the build's hosted repo
        // - the build-set's repo group
        // - the product version repo group
        // - the "shared-releases" repo group
        // - the "shared-imports" repo
        // - the public group
        // ...in that order
        Group buildGroup = indy.stores().load(StoreType.group, repoId, Group.class);

        System.out.printf("Constituents:\n  %s\n", join(buildGroup.getConstituents(), "\n  "));
        assertGroupConstituents(buildGroup, new StoreKey(StoreType.hosted, execution.getBuildContentId()),
                new StoreKey(StoreType.group, MavenRepositoryConstants.UNTESTED_BUILDS_GROUP), new StoreKey(StoreType.hosted,
                        MavenRepositoryConstants.SHARED_IMPORTS_ID), new StoreKey(StoreType.group,
                        MavenRepositoryConstants.PUBLIC_GROUP_ID));
    }

}
