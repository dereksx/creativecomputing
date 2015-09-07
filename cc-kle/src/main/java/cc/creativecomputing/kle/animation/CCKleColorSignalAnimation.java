package cc.creativecomputing.kle.animation;

import cc.creativecomputing.core.CCProperty;
import cc.creativecomputing.kle.elements.CCSequenceElement;
import cc.creativecomputing.math.CCColor;

public class CCKleColorSignalAnimation extends CCKleAnimation<CCColor>{
	
	@CCProperty(name = "hue")
	private CCKleSignal _myHueSignal = new CCKleSignal();
	@CCProperty(name = "saturation")
	private CCKleSignal _mySaturationSignal = new CCKleSignal();
	@CCProperty(name = "brightness")
	private CCKleSignal _myBrightnessSignal = new CCKleSignal();
	@CCProperty(name = "phase speed", min = 0, max = 10)
	private double _cSpeed = 0;
	
	private double _myGlobalPhase = 0;
	
	public void update(final double theDeltaTime){
		_myGlobalPhase += theDeltaTime * _cSpeed;
		_myHueSignal.update(theDeltaTime);
		_mySaturationSignal.update(theDeltaTime);
		_myBrightnessSignal.update(theDeltaTime);
	}
	
	public CCColor animate(CCSequenceElement theElement){
		CCColor myResult = CCColor.createFromHSB(
			_myHueSignal.value(theElement, _myGlobalPhase), 
			_mySaturationSignal.value(theElement, _myGlobalPhase), 
			_myBrightnessSignal.value(theElement, _myGlobalPhase)
		);
		
		myResult.r *= _cBlend;
		myResult.g *= _cBlend;
		myResult.b *= _cBlend;
		
		return myResult;
	}
}