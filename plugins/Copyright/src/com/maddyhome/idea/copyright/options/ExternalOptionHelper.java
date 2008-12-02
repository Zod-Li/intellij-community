package com.maddyhome.idea.copyright.options;

/*
 * Copyright - Copyright notice updater for IDEA
 * Copyright (C) 2004-2005 Rick Maddy. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.JDOMUtil;
import org.jdom.Document;
import org.jdom.Element;

import java.io.File;
import java.util.List;

public class ExternalOptionHelper
{


    public static Options loadOptions(File file)
    {
        try
        {
            Document doc = JDOMUtil.loadDocument(file);
            Element root = doc.getRootElement();
            List list = root.getChildren("component");
            for (Object element : list)
            {
                Element component = (Element)element;
                String name = component.getAttributeValue("name");
                if (name.equals("CopyrightManager")) {
                  final Element child = component.getChild("copyright");
                  if (child != null) {
                    for (Object o : child.getChildren("option")) {
                      if (Comparing.strEqual(((Element)o).getAttributeValue("name"), "myOptions")) {
                        final Element valueElement = ((Element)o).getChild("value");
                        if (valueElement != null) {
                          Options res = new Options();
                          res.readExternal(valueElement);

                          return res;
                        }
                      }
                    }
                  }
                } else
                if (name.equals("copyright"))
                {
                    Options res = new Options();
                    res.readExternal(component);

                    return res;
                }
            }

            return null;
        }
        catch (Exception e)
        {
            logger.error(e);
            return null;
        }
    }


    private ExternalOptionHelper()
    {
    }

    private static final Logger logger = Logger.getInstance(ExternalOptionHelper.class.getName());
}
