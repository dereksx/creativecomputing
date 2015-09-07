package cc.creativecomputing.control.timeline;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import cc.creativecomputing.control.handles.CCPropertyHandle;
import cc.creativecomputing.io.data.CCDataObject;

public class Track extends AbstractTrack{

	private TrackData _myTrackData;
	
    private double _myMinValue = 0;
    private double _myMaxValue = 1;
    
    private boolean _myAccumulateData = false;
	
	private Map<String, String> _myExtras;

	
	
	public Track(CCPropertyHandle<?> theProperty){
		super(theProperty);
		_myTrackData = new TrackData(this);
	}
	
	public void extras(Map<String, String> theExtras) {
		_myExtras = theExtras;
	}
	
	public Map<String, String> extras() {
		return _myExtras;
	}
	
	public void addExtra(String theKey, String theValue) {
		if(_myExtras == null)_myExtras = new HashMap<String, String>();
		_myExtras.put(theKey, theValue);
	}
	
	public void accumulateData(final boolean theAccumulateData) {
		_myAccumulateData = theAccumulateData;
		
		if(_myAccumulateData && !(_myTrackData instanceof AccumulatedTrackData)) {
			_myTrackData = new AccumulatedTrackData(this);
		}
	}
	
	public boolean accumulateData() {
		return _myAccumulateData;
	}
	
    public void valueRange(double theMinValue, double theMaxValue) {
        _myMinValue = theMinValue;
        _myMaxValue = theMaxValue;
    }
    
    public double minValue() {
    	return _myMinValue;
    }
    
    public double maxValue() {
    	return _myMaxValue;
    }
	
	public TrackData trackData() {
		return _myTrackData;
	}
	
	public void trackData(TrackData theTrackData) {
		_myTrackData = theTrackData;
		_myDirtyFlag = false;
	}
	
	public boolean isDirty() {
		return _myDirtyFlag || _myTrackData.isDirty();
	}
	
	public void setDirty(boolean theFlag) {
		_myDirtyFlag = theFlag;
		_myTrackData.setDirty(theFlag);
	}
	
	public static final String TRACK_ELEMENT = "Track";
	private static final String TRACK_EXTRAS = "Extras";
	public static final String PATH_ATTRIBUTE = "path";
	private static final String MUTE_ATTRIBUTE = "mute";
	private static final String ACCUMULATE_ATTRIBUTE = "accumulate";
	
	protected static final String TYPE_ATTRIBUTE = "type";
	private static final String MIN_ATTRIBUTE = "min";
	private static final String MAX_ATTRIBUTE = "max";
	
	public CCDataObject data(double theStart, double theEnd) {
		CCDataObject myTrackData = new CCDataObject();
		myTrackData.put(PATH_ATTRIBUTE, _myProperty.path().toString());
		myTrackData.put(MUTE_ATTRIBUTE, mute());
		myTrackData.put(ACCUMULATE_ATTRIBUTE, accumulateData());
			
		myTrackData.put(TYPE_ATTRIBUTE, _myProperty.type().getName());
		myTrackData.put(MIN_ATTRIBUTE, minValue());
		myTrackData.put(MAX_ATTRIBUTE, maxValue());
			
		myTrackData.put(TrackData.TRACKDATA_ELEMENT, trackData().data(theStart, theEnd));
		
		
		
		if(_myExtras != null) {
			CCDataObject myExtraData = new CCDataObject();
			myTrackData.put(TRACK_EXTRAS, myExtraData);
			for(String myKey:_myExtras.keySet()) {
				myTrackData.put(myKey, _myExtras.get(myKey));
			}
		}
		return myTrackData;
	}
	
	public void data(CCDataObject theTrackData) {
//		setAddress(theTrackData.getString(ADDRESS_ATTRIBUTE));
		mute(theTrackData.getBoolean(MUTE_ATTRIBUTE));
		accumulateData(theTrackData.getBoolean(ACCUMULATE_ATTRIBUTE, false));
//		trackType(TrackType.valueOf(theTrackData.getString(TYPE_ATTRIBUTE)));
		valueRange(theTrackData.getDouble(MIN_ATTRIBUTE), theTrackData.getDouble(MAX_ATTRIBUTE));
		
		CCDataObject myTrackData = theTrackData.getObject(TrackData.TRACKDATA_ELEMENT);
//		TrackData myTrackData;
//		if(accumulateData()) {
//			myTrackData = new AccumulatedTrackData(this);
//		}else {
//			myTrackData = new TrackData(this);
//		}
//		myTrackData.fromXML(myTrackDataXML);
//		trackData(myTrackData);
		_myTrackData.clear();
		_myTrackData.data(myTrackData);
		
		CCDataObject myExtrasData = theTrackData.getObject(TRACK_EXTRAS);
		if(myExtrasData == null)return;
		_myExtras = new HashMap<String, String>();
		for(String myKey:myExtrasData.keySet()) {
			_myExtras.put(myKey, myExtrasData.getString(myKey));
		}
	}

	public Path path() {
		return _myProperty.path();
	}
}