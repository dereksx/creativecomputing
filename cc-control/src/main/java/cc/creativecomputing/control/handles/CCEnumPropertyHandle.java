package cc.creativecomputing.control.handles;

import cc.creativecomputing.core.CCProperty;
import cc.creativecomputing.core.util.CCReflectionUtil.CCMember;
import cc.creativecomputing.io.data.CCDataObject;
import cc.creativecomputing.math.CCMath;

public class CCEnumPropertyHandle extends CCPropertyHandle<Enum<?>>{
	
	private final int _myNumberOfConstants;

	protected CCEnumPropertyHandle(CCObjectPropertyHandle theParent, CCMember<CCProperty> theMember) {
		super(theParent, theMember);
		_myNumberOfConstants = value().getDeclaringClass().getEnumConstants().length;
	}
	
	@Override
	public double formatNormalizedValue(double theValue) {
		theValue = CCMath.round(theValue * _myNumberOfConstants);
		theValue /= _myNumberOfConstants;
		return theValue;
	}

	@Override
	public Enum<?> convertNormalizedValue(double theValue) {
		return value().getDeclaringClass().getEnumConstants()[(int)(theValue * _myNumberOfConstants)];
	}
	@Override
	public void fromNormalizedValue(double theValue, boolean theOverWrite) {
		return;
	}
	
	@Override
	public double normalizedValue() {
		return 0;
	}

	@Override
	public String valueString() {
		return value().name();
	}
	
	@Override
	public CCDataObject data() {
		CCDataObject myResult = super.data();
		return myResult;
	}
	
	@Override
	public void data(CCDataObject theData) {
		String myName = theData.getString("value");
		for(Enum<?> myEnumConstant :value().getDeclaringClass().getEnumConstants()){
			if(myEnumConstant.name().equals(myName)){
				value(myEnumConstant, true);
				return;
			}
		}
		
	}
	
	

}