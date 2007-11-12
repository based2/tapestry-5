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

package org.apache.tapestry.ioc.internal.services;

import org.apache.tapestry.ioc.AnnotationProvider;
import org.apache.tapestry.ioc.ObjectLocator;
import org.apache.tapestry.ioc.ObjectProvider;
import org.apache.tapestry.ioc.annotations.Symbol;
import org.apache.tapestry.ioc.services.SymbolSource;
import org.apache.tapestry.ioc.services.TypeCoercer;
import org.apache.tapestry.ioc.test.IOCTestCase;
import org.testng.annotations.Test;

public class SymbolObjectProviderTest extends IOCTestCase
{
    @Test
    public void no_annotation()
    {
        SymbolSource source = mockSymbolSource();
        TypeCoercer coercer = mockTypeCoercer();
        AnnotationProvider annotationProvider = mockAnnotationProvider();
        ObjectLocator locator = mockObjectLocator();

        train_getAnnotation(annotationProvider, Symbol.class, null);

        replay();

        ObjectProvider provider = new SymbolObjectProvider(source, coercer);

        assertNull(provider.provide(Long.class, annotationProvider, locator));

        verify();
    }

    @Test
    public void annotation_present()
    {
        SymbolSource source = mockSymbolSource();
        TypeCoercer coercer = mockTypeCoercer();
        AnnotationProvider annotationProvider = mockAnnotationProvider();
        ObjectLocator locator = mockObjectLocator();
        Symbol annotation = newMock(Symbol.class);
        String symbolName = "example-symbol";
        String symbolValue = "symbol-value";
        Long coercedValue = 123l;

        train_getAnnotation(annotationProvider, Symbol.class, annotation);

        expect(annotation.value()).andReturn(symbolName);

        train_valueForSymbol(source, symbolName, symbolValue);

        train_coerce(coercer, symbolValue, Long.class, coercedValue);

        replay();

        ObjectProvider provider = new SymbolObjectProvider(source, coercer);

        assertSame(provider.provide(Long.class, annotationProvider, locator), coercedValue);

        verify();
    }

    protected final void train_valueForSymbol(SymbolSource source, String symbolName,
                                              String symbolValue)
    {
        expect(source.valueForSymbol(symbolName)).andReturn(symbolValue);
    }
}
