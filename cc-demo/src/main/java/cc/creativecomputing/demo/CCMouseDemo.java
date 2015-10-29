package cc.creativecomputing.demo;

import cc.creativecomputing.app.modules.CCAnimator;
import cc.creativecomputing.app.modules.CCAnimator.CCAnimationMode;
import cc.creativecomputing.gl.app.CCAbstractGLContext.CCPixelScale;
import cc.creativecomputing.gl.app.events.CCMouseAdapter;
import cc.creativecomputing.gl.app.events.CCMouseEvent;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.graphics.app.CCGL2Adapter;
import cc.creativecomputing.graphics.app.CCGL2Application;

public class CCMouseDemo extends CCGL2Adapter{
	
	
	private float _myMouseX;
	private float _myMouseY;
	
	@Override
	public void start(CCAnimator theAnimator) {
		mouseMotionListener().add(new CCMouseAdapter() {
			@Override
			public void mouseMoved(CCMouseEvent theMouseEvent) {
				_myMouseX = theMouseEvent.x();
				_myMouseY = theMouseEvent.y();
			}
		});
	}
	
	@Override
	public void init(CCGraphics g) {
	}
	
	
	@Override
	public void update(CCAnimator theAnimator) {
		
	}
	
	
	
	@Override
	public void display(CCGraphics g) {
		g.clear();
		g.color(255,0,0);
		g.rect(-g.width()/4, -g.height()/4, g.width()/2, g.height()/2);
		g.line(_myMouseX -g.width()/2, -g.height()/2, _myMouseX - g.width()/2, g.height()/2);
		g.line(-g.width()/2, g.height()/2 - _myMouseY, g.width()/2, g.height()/2 - _myMouseY) ;
		
	}
	
	public static void main(String[] args) {
		
		
		CCMouseDemo demo = new CCMouseDemo();
		
		
		CCGL2Application myAppManager = new CCGL2Application(demo);
		myAppManager.glcontext().size(1000, 500);
		myAppManager.animator().framerate = 30;
		myAppManager.animator().animationMode = CCAnimationMode.FRAMERATE_PRECISE;
		myAppManager.start();
	}
}