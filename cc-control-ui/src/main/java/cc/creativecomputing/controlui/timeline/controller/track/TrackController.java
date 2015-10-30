/*  
 * Copyright (c) 2009  Christian Riekoff <info@texone.org>  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 2 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
 */
package cc.creativecomputing.controlui.timeline.controller.track;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import cc.creativecomputing.control.timeline.Selection;
import cc.creativecomputing.control.timeline.Track;
import cc.creativecomputing.control.timeline.TrackData;
import cc.creativecomputing.control.timeline.point.ControlPoint;
import cc.creativecomputing.control.timeline.point.TimedEventPoint;
import cc.creativecomputing.controlui.timeline.controller.TimelineTool;
import cc.creativecomputing.controlui.timeline.controller.ToolController;
import cc.creativecomputing.controlui.timeline.controller.TrackContext;
import cc.creativecomputing.controlui.timeline.controller.Zoomable;
import cc.creativecomputing.controlui.timeline.view.TimedContentView;
import cc.creativecomputing.math.CCMath;


/**
 * @author christianriekoff
 *
 */
public abstract class TrackController extends TrackDataController implements Zoomable, TimedContentView{
	
	protected Track _myTrack;
	
	private Selection _mySelection;
	
	protected ToolController _myToolController;
	protected GroupTrackController _myParent;
	
	public TrackController(
		TrackContext theTrackContext, 
		ToolController theToolController, 
		Track theTrack, 
		GroupTrackController theParent
	) {
		super(theTrackContext, theTrack.property());
		_myTrack = theTrack;
        _myToolController = theToolController;
		_myParent = theParent;
	}
	
	public boolean isParentOpen(){
		return _myParent == null || _myParent.isOpen();
	}
	
	public Track track() {
		return _myTrack;
	}
	
	public ToolController tool() {
		return _myToolController;
	}
	
	public void trackData(Track theTrack) {
		_myTrack = theTrack;
		if(_myTrackView == null)return;
		_myTrackView.mute(theTrack.mute());
		_myTrackView.render();
	}
	
	public void mute(boolean theIsMuted) {
		_myTrack.mute(theIsMuted);
		if(_myTrackView != null)_myTrackView.mute(theIsMuted);
	}
	
	public void muteGroup(boolean theIsMuted){
		_myParent.groupTrack().mute(theIsMuted);//.address(), theIsMuted);
	}
	
	public void viewValue(String theValue) {
		if(_myTrackView != null)_myTrackView.value(theValue);
	}
	
	public void writeValue(double theTime){
		ControlPoint myControlPoint = new ControlPoint(theTime, _myProperty.normalizedValue());
        myControlPoint = createPointImpl(myControlPoint);
        	
        trackData().add(myControlPoint);
        _myHasAdd = true;
        _myTrackView.render();
	}

	public void color(Color theColor) {
		// TODO fix color track
//		_myTimelineController.colorTrack(theColor, _myTrack.address());
	}
	
	@Override
	public TrackData trackData() {
		return _myTrack.trackData();
	}
	
	public void time(double theTime){
		if(_myTrackView != null)_myTrackView.update();
		
		_myTrack.property().fromNormalizedValue(value(theTime), false);
		double myValue = _myTrack.property().formatNormalizedValue(value(theTime));
		viewValue(_myTrack.property().valueString());
		if(trackData().size() == 0 && _myTrackView != null)_myTrackView.render();

			
		if(track().mute()) return;
    	if(trackData().size() == 0)return;
    	
    	timeImplementation(theTime, myValue);
	}
	
	public abstract void timeImplementation(double theTime, double theValue);
	
	public double maxTime(){
		ControlPoint myLastPoint = trackData().getLastPoint();
		if(myLastPoint == null)return 0;
		
		if(myLastPoint instanceof TimedEventPoint) {
			return ((TimedEventPoint)myLastPoint).endTime();
		}
		
		return myLastPoint.time();
		
	}
	
	/**
	 * Returns the value of the track at the given time
	 * @param theTime time where to get the value
	 * @return value at the given time
	 */
	public double value(double theTime) {
		if(trackData().size() == 0){
			if(_myTrack.property() == null)return 0;
			return _myTrack.property().normalizedValue();
		}
    	return trackData().value(theTime);
    }
    
//	/**
//	 * Similar to the {@link #value(double)} method but scales the output
//	 * within the min max range of the track
//	 * @param theTime time where to get the value
//	 * @return scaled value at the given time
//	 */
//    public double scaledValue() {
//    	return CCMath.blend(_myTrack.minValue(), _myTrack.maxValue(), value(theTime));
//    }
    
    public void reset() {
    	trackData().clear();
    	if(_myTrackView != null)_myTrackView.render();
    }

    public Point2D curveToViewSpace(ControlPoint thePoint) {
        Point2D myResult = new Point2D.Double();
        int myX = timeToViewX(thePoint.time());
        int myY = (int) ((1 - thePoint.value()) * (_myTrackView.height() - 5) + 2.5); // reverse y axis
        myResult.setLocation(myX, myY);
        return myResult;
    }
    
    
    public Point2D curveToViewSpace(ControlPoint thePoint, double theTimeOffset) {
        Point2D myResult = new Point2D.Double();
        int myX = timeToViewX(thePoint.time() + theTimeOffset);
        int myY = (int) ((1 - thePoint.value()) * (_myTrackView.height() - 5) + 2.5); // reverse y axis
        myResult.setLocation(myX, myY);
        return myResult;
    }
    
    public ControlPoint viewToCurveSpace(Point2D thePoint, boolean theGetPos) {
        ControlPoint myResult = new ControlPoint();
        
        myResult.time(viewXToTime((int) thePoint.getX(), theGetPos));
        double myValue = CCMath.constrain(1 - (thePoint.getY() - 2.5) / (_myTrackView.height() - 5),0,1);
        myValue = _myTrack.property() != null ? _myTrack.property().formatNormalizedValue(myValue) : myValue;
        myResult.value(myValue);
        return myResult;
    }
    
    public void clearSelection() {
        _mySelection = null;
        if(_myTrackView != null) _myTrackView.render();
    }
    
    public void selection(Selection theSelection) {
    	_mySelection = theSelection;
    	if(_myTrackView != null)_myTrackView.render();
    }
    
    public Selection selection() {
    	return _mySelection;
    }
    
    public void mousePressed(MouseEvent e) {
			
		if (_myToolController.toolMode() == TimelineTool.MOVE) {
			_myToolController.selectionController().assignTrackData(this);
			_myToolController.selectionController().mousePressed(e);
		   	return;
		} else {
			super.mousePressed(e, _myToolController);
		}
	}


	public void mouseReleased(MouseEvent e) {
		if (_myToolController.toolMode() == TimelineTool.MOVE) {
			_myToolController.selectionController().mouseReleased(e);
			return;
		} else {
			super.mouseReleased(e, _myToolController);
		}
	}
	
	public void mouseMoved(MouseEvent e){
		if (_myToolController.toolMode() == TimelineTool.MOVE) {
			_myToolController.selectionController().assignTrackData(this);
			_myToolController.selectionController().mouseMoved(e);
			return;
		}else{
			super.mouseMoved(e, _myToolController);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (_myToolController.toolMode() == TimelineTool.MOVE) {
			_myToolController.selectionController().assignTrackData(this);
			_myToolController.selectionController().mouseDragged(e);
			return;
		}else {
			super.mouseDragged(e, _myToolController);
		}
	}

}
