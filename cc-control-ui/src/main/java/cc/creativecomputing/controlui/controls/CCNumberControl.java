package cc.creativecomputing.controlui.controls;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cc.creativecomputing.control.handles.CCNumberPropertyHandle;
import cc.creativecomputing.control.handles.CCPropertyListener;
import cc.creativecomputing.controlui.CCControlComponent;
import cc.creativecomputing.core.logging.CCLog;
import cc.creativecomputing.core.util.CCFormatUtil;
import cc.creativecomputing.math.CCMath;

public class CCNumberControl extends CCValueControl<Number, CCNumberPropertyHandle<Number>>{
	
	private static final int MAX_SLIDER_VALUE = 1000;
	
	private double _myMin;
	private double _myMax;
	private double _myValue;
	
	private JTextField _myValueField;
	private JSlider _mySlider;
	
	private boolean _myTriggerEvent = true;

	public CCNumberControl(CCNumberPropertyHandle<Number> theHandle, CCControlComponent theControlComponent){
		super(theHandle, theControlComponent);
 
		
        //Create the label.
		theHandle.events().add(new CCPropertyListener<Number>() {
			
			@Override
			public void onChange(Number theValue) {
				_myTriggerEvent = false;
				_myValue = theValue.doubleValue();
				updateSlider(_myValue);
				_myValueField.setText(CCFormatUtil.nfc((float)_myValue, 2) + "");
				_myTriggerEvent = true;
			}
		});
 
        //Create the slider.
        _myMin = _myHandle.min().doubleValue();
        _myMax = _myHandle.max().doubleValue();
        _myValue = _myHandle.value().doubleValue();
        if(_myHandle.isNumberBox()){
        	_mySlider = null;
        }else{
	        _mySlider = new JSlider(JSlider.HORIZONTAL, 0, MAX_SLIDER_VALUE, 0);
	        _mySlider.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent theE) {
					if(!_myTriggerEvent)return;
					value((float)(_mySlider.getValue() / (float)MAX_SLIDER_VALUE * (_myMax - _myMin) + _myMin), true);
				}
				
			});
	 
	        //Turn on labels at major tick marks.
	 
	        _mySlider.setMajorTickSpacing(MAX_SLIDER_VALUE / 10);
	        _mySlider.setMinorTickSpacing(MAX_SLIDER_VALUE / 20);
	        
	        _mySlider.setPaintTicks(false);
	        _mySlider.setPaintLabels(false);
	        _mySlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
	        _mySlider.putClientProperty( "JComponent.sizeVariant", "mini" );
	        _mySlider.setPreferredSize(new Dimension(130,17));
        }
        
        _myValueField = new JTextField();
        CCUIStyler.styleTextField(_myValueField, 45);
        
        _myValueField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent theE) {
				try{
					value(Double.parseDouble(_myValueField.getText()), true);
				}catch(Exception e){
					e.printStackTrace();
					value((float)(_mySlider.getValue() / (float)MAX_SLIDER_VALUE * (_myMax - _myMin) + _myMin), true);
				}
			}
		});
        
        
        value(_myHandle.value().doubleValue(), true);
	}
	
	@Override
	public void addToComponent(JPanel thePanel, int theY, int theDepth) {
		thePanel.add(_myLabel, constraints(0, theY, GridBagConstraints.LINE_END,1, 5, 1, 5));
		if(_mySlider != null){
			thePanel.add(_mySlider, constraints(1, theY, GridBagConstraints.PAGE_END,10, 5, 1, 5));
			thePanel.add(_myValueField, constraints(2, theY, GridBagConstraints.LINE_START,1, 5, 1, 5));
		}else{
			thePanel.add(_myValueField, constraints(1, theY, GridBagConstraints.LINE_START,1, 5, 1, 5));
		}
	}
	
	public Number value(){
		return _myValue;
	}
	
	public void value(double theValue, boolean theOverWrite){
		_myValue = theValue;
		_myHandle.value(_myValue, theOverWrite);
	}
	
	private void updateSlider(double theValue){
		if(_mySlider == null)return;
		double myValue = CCMath.constrain((theValue - _myMin) / (_myMax - _myMin) * MAX_SLIDER_VALUE, 0, MAX_SLIDER_VALUE);
		_mySlider.setValue((int)myValue);
	}

}