package cc.creativecomputing.demo;

import cc.creativecomputing.app.modules.CCAnimator;
import cc.creativecomputing.core.CCProperty;
import cc.creativecomputing.gl.app.CCAbstractGLContext.CCPixelScale;
import cc.creativecomputing.graphics.CCDrawMode;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.graphics.app.CCGL2Adapter;
import cc.creativecomputing.graphics.app.CCGL2Application;
import cc.creativecomputing.graphics.export.CCScreenCaptureController;
import cc.creativecomputing.graphics.font.CCFontIO;
import cc.creativecomputing.math.signal.CCWorleyNoise;

public class CCScreenCaptureDemo extends CCGL2Adapter{
	
	@CCProperty(name = "worley noise")
	private CCWorleyNoise _myNoise = new CCWorleyNoise();
	@CCProperty(name = "height", min = 0, max = 500)
	private double _cHeight = 100;
	@CCProperty(name = "z", min = 0, max = 10)
	private double _cZ = 0;
	
	@CCProperty(name = "screen capure controller")
	private CCScreenCaptureController _myController;
	
	@Override
	public void start(CCAnimator theAnimator) {
		_myController = new CCScreenCaptureController(this, theAnimator);
	}
	
	@Override
	public void init(CCGraphics g) {
		
//		for(CCRealtimeGraph myGraph:_myRealTimeGraph.instances()){
//			CCLog.info(myGraph);
//			if(myGraph == null)return;
//			myGraph.draw(g);
//		}
		g.textFont(CCFontIO.createTextureMapFont("arial", 12));
	}
	
	
	@Override
	public void update(CCAnimator theAnimator) {

	}
	
	@Override
	public void display(CCGraphics g) {
		g.clear();
		g.color(255);
		for(int y = -100; y < 100;y+=20){
			g.beginShape(CCDrawMode.LINE_STRIP);
			for(int x = -g.width()/2; x <= g.width()/2; x++){
				double ny = _myNoise.value(x * 0.05, y * 0.05, _cZ) * _cHeight;
				g.vertex(x, y + ny);
			}
			g.endShape();
		}
	}
	
	public static void main(String[] args) {
		
		
		CCScreenCaptureDemo demo = new CCScreenCaptureDemo();
		
		
		CCGL2Application myAppManager = new CCGL2Application(demo);
		myAppManager.glcontext().size(1000, 500);
		myAppManager.glcontext().pixelScale = CCPixelScale.AUTOMAX;
		myAppManager.animator().framerate = 30;
		myAppManager.animator().animationMode = CCAnimator.CCAnimationMode.FRAMERATE_PRECISE;
		myAppManager.start();
	}
}
