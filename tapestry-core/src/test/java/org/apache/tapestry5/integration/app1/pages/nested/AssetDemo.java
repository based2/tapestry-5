// Copyright 2006, 2007, 2010 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.integration.app1.pages.nested;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavascriptSupport;
import org.apache.tapestry5.services.javascript.StylesheetOptions;

public class AssetDemo
{
    @Inject
    @Path("context:images/tapestry_banner.gif")
    private Asset icon;

    @Inject
    @Path("tapestry-button.png")
    private Asset button;

    @Inject
    @Path("context:css/ie-only.css")
    private Asset ieOnly;

    @Environmental
    private JavascriptSupport javascriptSupport;

    void afterRender()
    {
        javascriptSupport.importStylesheet(ieOnly, new StylesheetOptions(null, "IE"));
    }

    public Asset getIcon()
    {
        return icon;
    }

    public Asset getButton()
    {
        return button;
    }
}
