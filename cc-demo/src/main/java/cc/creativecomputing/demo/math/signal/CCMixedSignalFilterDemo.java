package cc.creativecomputing.demo.math.signal;

import cc.creativecomputing.app.modules.CCAnimator;
import cc.creativecomputing.control.code.CCCompileObject;
import cc.creativecomputing.control.code.CCRealtimeCompile;
import cc.creativecomputing.core.CCProperty;
import cc.creativecomputing.gl.app.CCAbstractGLContext.CCPixelScale;
import cc.creativecomputing.graphics.CCDrawMode;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.graphics.app.CCGL2Adapter;
import cc.creativecomputing.graphics.app.CCGL2Application;
import cc.creativecomputing.graphics.font.CCFontIO;
import cc.creativecomputing.math.filter.CCChebFilter;
import cc.creativecomputing.math.filter.CCCompressor;
import cc.creativecomputing.math.filter.CCMotionLimiter;
import cc.creativecomputing.math.signal.CCMixSignal;

public class CCMixedSignalFilterDemo extends CCGL2Adapter{
	
	@CCProperty(name = "signal")
	private CCMixSignal _mySignal = new CCMixSignal();
	@CCProperty(name = "height", min = 0, max = 500)
	private double _cHeight = 100;
	@CCProperty(name = "height vel", min = 0, max = 500)
	private double _cHeightVel = 100;
	@CCProperty(name = "height acc", min = 0, max = 500)
	private double _cHeightAcc = 100;
	@CCProperty(name = "height jerk", min = 0, max = 500)
	private double _cHeightJerk = 100;
	
	@CCProperty(name = "filter")
	private CCChebFilter _myLowPassFilter = new CCChebFilter(2);
	
	@CCProperty(name = "limiter")
	private CCCompressor _myLimiter = new CCCompressor(2);
	
	@CCProperty(name = "motion limiter")
	private CCMotionLimiter _myMotionLimiter = new CCMotionLimiter();
	
	public static class CCRealtimeFilter implements CCCompileObject{
		public void process(double[] data){
			
		}

		@Override
		public void onRecompile() {
		}

		@Override
		public Object[] parameters() {
			return null;
		}
	}
	
	@CCProperty(name = "real time filter")
	private CCRealtimeCompile<CCRealtimeFilter> _myFilter;
	
	@Override
	public void start(CCAnimator theAnimator) {
		_myFilter = new CCRealtimeCompile<CCRealtimeFilter>(CCRealtimeFilter.class);
		_myFilter.createObject();
	}
	
	private int _myIndex = 0;
	
	@Override
	public void init(CCGraphics g) {
		
//		for(CCRealtimeGraph myGraph:_myRealTimeGraph.instances()){
//			CCLog.info(myGraph);
//			if(myGraph == null)return;
//			myGraph.draw(g);
//		}
		g.textFont(CCFontIO.createTextureMapFont("arial", 12));
		
		_myData = new double[g.width()];
		_myFilteredData = new double[g.width()];

		_myFilteredVelocity = new double[g.width()];
		_myFilteredAcceleration = new double[g.width()];
		_myFilteredJerk = new double[g.width()];
		
	}
	
	double[] _myData;
	double[] _myFilteredData;
	double[] _myFilteredVelocity;
	double[] _myFilteredAcceleration;
	double[] _myFilteredJerk;
	
	
	@Override
	public void update(CCAnimator theAnimator) {
		double mySignalValue = _mySignal.value(theAnimator.frames());
		int myIndex = _myIndex % _myData.length;
		_myData[myIndex] = mySignalValue;
		
		mySignalValue = _myLowPassFilter.process(0, mySignalValue, 0);
		mySignalValue = _myLimiter.process(0,mySignalValue, 0);
//		
//		_myLimiter.process(1,CCMath.random(), 0);
//		_myLowPassFilter.process(1, CCMath.random(), 0);
		
		mySignalValue = _myMotionLimiter.process(0, mySignalValue, theAnimator.frames() * 0.2);
		

		_myFilteredData[myIndex] = mySignalValue;
//		if(_myFilter.instance() != null)_myFilter.instance().process(_myFilteredData);

		for(int i = 0; i < _myFilteredVelocity.length;i++){
			int index0 = (i + _myIndex) % _myData.length;
			int index1 = ((i  - 1) + _myIndex  + _myData.length) % _myData.length;
			
				_myFilteredVelocity[index0] = (_myFilteredData[index0] - _myFilteredData[index1]) * 5;
				_myFilteredAcceleration[index0] = (_myFilteredVelocity[index0] - _myFilteredVelocity[index1]) * 5;
				_myFilteredJerk[index0] = (_myFilteredAcceleration[index0] - _myFilteredAcceleration[index1]) * 5;
			
		}
		
		_myIndex++;
	}
	
	
	
	private void drawGraph(CCGraphics g, double[] theData, double theOffset, double theScale){
		g.beginShape(CCDrawMode.LINE_STRIP);
		for(int x = 0; x < g.width(); x++){
			double y = (theData[(x + _myIndex) % theData.length] - theOffset) * theScale;
			g.vertex(x - g.width()/2, y);
		}
		g.endShape();
	}
	
	@Override
	public void display(CCGraphics g) {
		g.clear();
		g.color(255);

		g.color(255);
		drawGraph(g, _myData, 0.5,_cHeight);

		g.color(255,255,0);drawGraph(g, _myFilteredData, 0, _cHeight);
		g.color(255,0,0);
		drawGraph(g, _myFilteredVelocity, 0, _cHeightVel);
		g.color(0,255,0);
		drawGraph(g, _myFilteredAcceleration, 0, _cHeightAcc);
		g.color(0,0,255);
		drawGraph(g, _myFilteredJerk, 0, _cHeightJerk);
	}
	
	public static void main(String[] args) {
		
		
		CCMixedSignalFilterDemo demo = new CCMixedSignalFilterDemo();
		
		
		CCGL2Application myAppManager = new CCGL2Application(demo);
		myAppManager.glcontext().size(1000, 500);
		myAppManager.glcontext().pixelScale = CCPixelScale.AUTOMAX;
		myAppManager.animator().framerate = 30;
		myAppManager.animator().animationMode = CCAnimator.CCAnimationMode.FRAMERATE_PRECISE;
		myAppManager.start();
	}
}