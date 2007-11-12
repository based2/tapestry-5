// Copyright 2007 The Apache Software Foundation
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

package org.apache.tapestry.internal.services;

import org.apache.tapestry.MarkupWriter;
import org.apache.tapestry.internal.InternalComponentResourcesCommon;
import org.apache.tapestry.model.MutableComponentModel;
import org.apache.tapestry.runtime.RenderCommand;
import org.apache.tapestry.runtime.RenderQueue;
import org.apache.tapestry.services.ClassTransformation;
import org.apache.tapestry.services.ComponentClassTransformWorker;
import org.apache.tapestry.services.TransformMethodSignature;

import java.lang.reflect.Modifier;

/**
 * Ensures that all components implement {@link RenderCommand} by delegating to
 * {@link InternalComponentResourcesCommon#queueRender(org.apache.tapestry.runtime.RenderQueue)}.
 */
public class RenderCommandWorker implements ComponentClassTransformWorker
{
    private final TransformMethodSignature RENDER_SIGNATURE = new TransformMethodSignature(Modifier.PUBLIC, "void",
                                                                                           "render", new String[]
            {MarkupWriter.class.getName(), RenderQueue.class.getName()}, null);

    public void transform(ClassTransformation transformation, MutableComponentModel model)
    {
        // Subclasses don't need to bother, they'll inherit from super-classes.

        if (model.getParentModel() != null)
            return;

        transformation.addImplementedInterface(RenderCommand.class);

        String body = String.format("%s.queueRender($2);", transformation.getResourcesFieldName());

        transformation.addMethod(RENDER_SIGNATURE, body);
    }
}
