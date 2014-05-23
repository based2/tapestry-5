// Copyright 2007, 2009, 2011 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.ioc.internal.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.PreventServiceDecoration;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.MasterObjectProvider;
import org.apache.tapestry5.plastic.PlasticUtils;

import java.util.List;

@PreventServiceDecoration
public class MasterObjectProviderImpl implements MasterObjectProvider
{
    private final List<ObjectProvider> configuration;

    private final OperationTracker tracker;

    public MasterObjectProviderImpl(List<ObjectProvider> configuration, OperationTracker tracker)
    {
        this.configuration = CollectionFactory.newList(configuration);
        this.tracker = tracker;

        // Add this special case to the front of the list.
        this.configuration.add(0, new StaticObjectProvider(OperationTracker.class, tracker));
    }

    @Override
    public <T> T provide(final Class<T> objectType, final AnnotationProvider annotationProvider,
                         final ObjectLocator locator,
                         final boolean required)
    {
        return tracker.invoke(String.format("Resolving object of type %s using MasterObjectProvider",
                PlasticUtils.toTypeName(objectType)), new Invokable<T>()
        {
            @Override
            public T invoke()
            {
                for (ObjectProvider provider : configuration)
                {
                    T result = provider.provide(objectType, annotationProvider, locator);

                    if (result != null) return result;
                }

                // If required, then we must obtain it the hard way, by
                // seeing if there's a single service that implements the interface.

                if (required) return locator.getService(objectType);

                return null;
            }
        });
    }
}
