/*
 * Copyright (c) 2013 christianr.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.html
 * 
 * Contributors:
 *     christianr - initial API and implementation
 */
package cc.creativecomputing.model.collada;

import java.util.List;

import cc.creativecomputing.io.xml.CCXMLElement;

public class CCColladaCameras extends CCColladaLibrary<CCColladaCamera>{
	
	CCColladaCameras(List<CCXMLElement> theCamerasXML) {
		for (CCXMLElement myCameraXML : theCamerasXML) {
			CCColladaCamera myCamera = new CCColladaCamera(myCameraXML);
			_myElementMap.put(myCamera.id(), myCamera);
			_myElementList.add(myCamera);
		}

	}
}