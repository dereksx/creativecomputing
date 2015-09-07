package cc.creativecomputing.gl.enums;

import com.jogamp.opengl.GL4;

public enum GLCapability {
	PRIMITIVE_RESTART(GL4.GL_PRIMITIVE_RESTART),
	DEPTH_TEST(GL4.GL_DEPTH_TEST),
	STENCIL_TEST(GL4.GL_STENCIL_TEST),
	SCISSOR_TEST(GL4.GL_SCISSOR_TEST),
	CULL_FACE(GL4.GL_CULL_FACE),
	RASTERIZER_DISCARD(GL4.GL_RASTERIZER_DISCARD),
	POLYGON_OFFSET_POINT(GL4.GL_POLYGON_OFFSET_POINT),
	POLYGON_OFFSET_LINE(GL4.GL_POLYGON_OFFSET_LINE),
	POLYGON_OFFSET_FILL(GL4.GL_POLYGON_OFFSET_FILL),
	BLEND(GL4.GL_BLEND),
	COLOR_LOGIC_OP(GL4.GL_COLOR_LOGIC_OP),
	DITHER(GL4.GL_DITHER),
	MULTISAMPLE(GL4.GL_MULTISAMPLE),
	SAMPLE_SHADING(GL4.GL_SAMPLE_SHADING),
	LINE_SMOOTH(GL4.GL_LINE_SMOOTH),
	POLYGON_SMOOTH(GL4.GL_POLYGON_SMOOTH),
	PROGRAM_POINT_SIZE(GL4.GL_PROGRAM_POINT_SIZE),
	TEXTURE_CUBE_MAP_SEAMLESS(GL4.GL_TEXTURE_CUBE_MAP_SEAMLESS),
	SAMPLE_ALPHA_TO_COVERAGE(GL4.GL_SAMPLE_ALPHA_TO_COVERAGE),
	SAMPLE_ALPHA_TO_ONE(GL4.GL_SAMPLE_ALPHA_TO_ONE),
	SAMPLE_COVERAGE(GL4.GL_SAMPLE_COVERAGE),
	FRAMEBUFFER_SRGB(GL4.GL_FRAMEBUFFER_SRGB);
	
	private int _myGLID;
	
	private GLCapability(int theGLID){
		_myGLID = theGLID;
	}
	
	public int glID(){
		return _myGLID;
	}
	
	public static GLCapability fromGLID(int theGLID){
		switch(theGLID){
		case GL4.GL_PRIMITIVE_RESTART:return PRIMITIVE_RESTART;
		case GL4.GL_DEPTH_TEST:return DEPTH_TEST;
		case GL4.GL_STENCIL_TEST:return STENCIL_TEST;
		case GL4.GL_SCISSOR_TEST:return SCISSOR_TEST;
		case GL4.GL_CULL_FACE:return CULL_FACE;
		case GL4.GL_RASTERIZER_DISCARD:return RASTERIZER_DISCARD;
		case GL4.GL_POLYGON_OFFSET_POINT:return POLYGON_OFFSET_POINT;
		case GL4.GL_POLYGON_OFFSET_LINE:return POLYGON_OFFSET_LINE;
		case GL4.GL_POLYGON_OFFSET_FILL:return POLYGON_OFFSET_FILL;
		case GL4.GL_BLEND:return BLEND;
		case GL4.GL_COLOR_LOGIC_OP:return COLOR_LOGIC_OP;
		case GL4.GL_DITHER:return DITHER;
		case GL4.GL_MULTISAMPLE:return MULTISAMPLE;
		case GL4.GL_SAMPLE_SHADING:return SAMPLE_SHADING;
		case GL4.GL_LINE_SMOOTH:return LINE_SMOOTH;
		case GL4.GL_POLYGON_SMOOTH:return POLYGON_SMOOTH;
		case GL4.GL_PROGRAM_POINT_SIZE:return PROGRAM_POINT_SIZE;
		case GL4.GL_TEXTURE_CUBE_MAP_SEAMLESS:return TEXTURE_CUBE_MAP_SEAMLESS;
		case GL4.GL_SAMPLE_ALPHA_TO_COVERAGE:return SAMPLE_ALPHA_TO_COVERAGE;
		case GL4.GL_SAMPLE_ALPHA_TO_ONE:return SAMPLE_ALPHA_TO_ONE;
		case GL4.GL_SAMPLE_COVERAGE:return SAMPLE_COVERAGE;
		case GL4.GL_FRAMEBUFFER_SRGB:return FRAMEBUFFER_SRGB;
		}
		return null;
	}
}
