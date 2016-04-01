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

package org.jboss.pnc.coordinator.test;

import org.jboss.pnc.coordinator.builder.BuildCoordinator;
import org.jboss.pnc.coordinator.builder.BuildSchedulerFactory;
import org.jboss.pnc.coordinator.builder.datastore.DatastoreAdapter;
import org.jboss.pnc.coordinator.builder.filtering.BuildTaskFilter;
import org.jboss.pnc.coordinator.builder.filtering.HasSuccessfulBuildRecordFilter;
import org.jboss.pnc.mock.datastore.DatastoreMock;
import org.jboss.pnc.spi.events.BuildCoordinationStatusChangedEvent;
import org.jboss.pnc.spi.events.BuildSetStatusChangedEvent;
import org.jboss.pnc.test.cdi.TestInstance;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class BuildCoordinatorFactory {

    @Inject
    Event<BuildCoordinationStatusChangedEvent> buildStatusChangedEventNotifier;

    @Inject
    Event<BuildSetStatusChangedEvent> buildSetStatusChangedEventNotifier;

    @Inject
    BuildSchedulerFactory buildSchedulerFactory;

    public BuildCoordinator createBuildCoordinator(DatastoreMock datastore) {
        DatastoreAdapter datastoreAdapter = new DatastoreAdapter(datastore);
        Instance<BuildTaskFilter> taskFilters;
        taskFilters = new TestInstance<>(new HasSuccessfulBuildRecordFilter(datastoreAdapter));

        return new BuildCoordinator(datastoreAdapter, buildStatusChangedEventNotifier, buildSetStatusChangedEventNotifier, buildSchedulerFactory, taskFilters);
    }
}