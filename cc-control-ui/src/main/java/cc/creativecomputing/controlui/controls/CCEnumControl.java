package cc.creativecomputing.controlui.controls;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import cc.creativecomputing.control.handles.CCEnumPropertyHandle;
import cc.creativecomputing.control.handles.CCPropertyEditListener;
import cc.creativecomputing.control.handles.CCPropertyHandle;
import cc.creativecomputing.control.handles.CCPropertyListener;
import cc.creativecomputing.controlui.CCControlComponent;

public class CCEnumControl extends CCValueControl<Enum<?>, CCEnumPropertyHandle>{
	
	private JComboBox<Enum<?>> _myEnums;
	
	private Enum<?> _myValue;
	
	private boolean _myTriggerEvent = true;
	private boolean _myIsInEdit = false;
	static final Dimension SMALL_BUTTON_SIZE = new Dimension(100,15);

	public CCEnumControl(CCEnumPropertyHandle theHandle, CCControlComponent theControlComponent){
		super(theHandle, theControlComponent);
 
		theHandle.events().add(new CCPropertyListener<Enum<?>>() {
			
			@Override
			public void onChange(Enum<?> theValue) {
				_myValue = theValue;
				_myTriggerEvent = false;
				 _myEnums.setSelectedItem(_myHandle.value());
				 _myTriggerEvent = true;
			}
		});
		theHandle.editEvents().add(new CCPropertyEditListener() {
			
			@Override
			public void endEdit(CCPropertyHandle<?> theProperty) {
				_myIsInEdit = false;
			}
			
			@Override
			public void beginEdit(CCPropertyHandle<?> theProperty) {
				_myIsInEdit = true;
			}
		});

        _myValue = theHandle.value();
        
        _myEnums = new JComboBox<Enum<?>>();
        CCUIStyler.styleCombo(_myEnums);
        for(Enum<?> myEnum:_myValue.getDeclaringClass().getEnumConstants()){
        	_myEnums.addItem(myEnum);
        }
        _myEnums.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent theE) {
				if(!_myTriggerEvent)return;
				if(_myEnums.getSelectedItem() == null)return;
				_myHandle.value((Enum<?>)_myEnums.getSelectedItem(), !_myIsInEdit);
				
			}
		});
        _myEnums.setSelectedItem(_myHandle.value());
	}
	
	@Override
	public Enum<?> value() {
		return _myValue;
	}
	
	
	
	@Override
	public void addToComponent(JPanel thePanel, int theY, int theDepth) {
		thePanel.add(_myLabel, constraints(0,theY, GridBagConstraints.LINE_END, 	15,  5, 1, 5));
		thePanel.add(_myEnums, constraints(1,theY, GridBagConstraints.LINE_START,	15, 15, 1, 5));
	}
}