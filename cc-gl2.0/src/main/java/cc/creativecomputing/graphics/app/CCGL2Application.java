package cc.creativecomputing.graphics.app;

import java.nio.file.Path;

import cc.creativecomputing.app.modules.CCAnimator;
import cc.creativecomputing.app.modules.CCAnimatorListener;
import cc.creativecomputing.app.modules.CCAnimator.CCAnimationMode;
import cc.creativecomputing.control.CCPropertyMap;
import cc.creativecomputing.controlui.CCControlApp;
import cc.creativecomputing.controlui.CCTimelineSynch;
import cc.creativecomputing.core.CCProperty;
import cc.creativecomputing.core.logging.CCLog;
import cc.creativecomputing.gl.app.CCGLAdapter;
import cc.creativecomputing.graphics.CCGraphics;
import cc.creativecomputing.io.CCNIOUtil;

public class CCGL2Application {
	
	@CCProperty(name = "animator")
	private CCAnimator _myAnimator;
	@CCProperty(name = "gl context")
	private CCGL2Context _myGLContext;
	@CCProperty(name = "synch")
	private CCTimelineSynch _mySynch;
	
	@CCProperty(name = "app")
	private CCGLAdapter<CCGraphics, CCGL2Context> _myAdapter;
	
	public String presetPath = null;
	
	private boolean _myIsInitialized = false;
	
	private boolean _myUseUI;

	public CCGL2Application(CCGLAdapter<CCGraphics, CCGL2Context> theGLAdapter, boolean useUI) {
		_myUseUI = useUI;
		_myAdapter = theGLAdapter;
		_myAnimator = new CCAnimator();
		_myAnimator.framerate = 60;
		_myAnimator.animationMode = CCAnimationMode.FRAMERATE_PRECISE;
		
		_mySynch = new CCTimelineSynch(_myAnimator);
		
		_myAnimator.listener().add(new CCAnimatorListener() {
			
			@Override
			public void update(CCAnimator theAnimator) {
				if(_myIsInitialized)theGLAdapter.update(theAnimator);
			}
			
			@Override
			public void stop(CCAnimator theAnimator) {
				theGLAdapter.start(theAnimator);
			}
			
			@Override
			public void start(CCAnimator theAnimator) {
				theGLAdapter.start(theAnimator);
			}
		});
		
		_myGLContext = new CCGL2Context(_myAnimator);
		theGLAdapter.glContext(_myGLContext);
		if(presetPath == null){
			presetPath = "settings/" + _myAdapter.getClass().getName() + "/";
		}
		CCGLAdapter<CCGraphics, CCGL2Context> myGLAdapter = new CCGLAdapter<CCGraphics, CCGL2Context>() {
			
			
			@Override
			public void init(CCGraphics theG) {
				_myAdapter.init(theG, _myAnimator);
				if(_myUseUI){
					_myControlApp.setData(CCGL2Application.this, presetPath);
					theGLAdapter.controlApp(_myControlApp);
					_myControlApp.update(0);
					_myAdapter.setupControls(_myControlApp);
				}else{
					CCPropertyMap myProps = new CCPropertyMap();
					CCLog.info(presetPath);
					myProps.setData(CCGL2Application.this, presetPath);
					
					Path myPresetsPath = myProps.rootHandle().presetPath();
					CCNIOUtil.createDirectories(myPresetsPath);
					for(Path myPath:CCNIOUtil.list(myPresetsPath, "json")){
						CCLog.info(myPath.getFileName().toString());
						myProps.rootHandle().preset(CCNIOUtil.fileName(myPath.getFileName().toString()));
						break;
					}
					myProps.rootHandle().update(0);
				}
				_mySynch.animator().start();
				
				_myIsInitialized = true;
			}
		};
		
		
		if(_myUseUI)myGLAdapter.controlApp(new CCControlApp(myGLAdapter, _mySynch, theGLAdapter.getClass()));

		_myGLContext.listener().add(myGLAdapter);
		_myGLContext.listener().add(
			new CCGLAdapter<CCGraphics, CCGL2Context>() {
				@Override
				public void display(CCGraphics theG) {
					theG.beginDraw();
				}
			}
		);
		_myGLContext.listener().add(theGLAdapter);
		_myGLContext.listener().add(
			new CCGLAdapter<CCGraphics, CCGL2Context>() {
				@Override

				public void display(CCGraphics theG) {
					theG.endDraw();
				}
			}
		);
		_myGLContext.listener().add(
			new CCGLAdapter<CCGraphics, CCGL2Context>() {
				@Override
				public void dispose(CCGraphics theG) {
					_myAnimator.stop();
				}
			}
		);
	}
	
	public CCGL2Application(CCGLAdapter<CCGraphics, CCGL2Context> theGLAdapter){
		this(theGLAdapter, true);
	}
	
	public CCGL2Application(){
		this(null, true);
	}
	
	public CCAnimator animator(){
		return _myAnimator;
	}
	
	public CCGL2Context glcontext(){
		return _myGLContext;
	}
	
	
	public void start(){
		_myGLContext.start();
		_myAnimator.start();
		
		
	}
}