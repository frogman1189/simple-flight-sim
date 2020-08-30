package project.eventlisteners;

import java.util.ArrayList;

/**
 * KeyAutoDispatcher provides a more responsive key event than is provided by
 * the standard java keyevent. This works by adding a key to its event array for
 * it to simulate the pressing off whenever triggerEvent is called. This should
 * called either with a timer, or on some update. This simulates a key being 
 * pressed once every "frame" and allows multiple keys to be pressed down
 * @author Logan Warner zdv5950 17991274
 */
public class KeyAutoDispatcher {
    private ArrayList<AutoDispatchListener> listeners;
    private ArrayList<Integer> typesToDispatch;
    
    public KeyAutoDispatcher() {
        listeners = new ArrayList<>();
        typesToDispatch = new ArrayList<>(); // specifically in this case KeyEvent.VK_ codes
    }
    public void addEventListener(AutoDispatchListener l) {
        listeners.add(l);
    }
    public void removeEventListener(AutoDispatchListener l) {
        listeners.remove(l);
    }
    /**
     * Add an event to simulate the dispatching off when triggerEvent is called
     * @param eventType Event object type. REFACTOR to object in feature for
     * more flexibility
     */
    public void addDispatchEvent(Integer eventType) {
        typesToDispatch.add(eventType);
    }
    public void removeDispatchEvent(Integer eventType) {
        typesToDispatch.remove(eventType);
    }
    /**
     * Check if event has been added to the auto dispatcher
     * @param e Event to check whether added
     * @return 
     */
    public boolean eventAdded(Integer e) {
        return typesToDispatch.contains(e);
    }
    /**
     * Dispatch all events that have been added to dispatch listeners
     */
    public void triggerEvent() {
        for(Integer e: typesToDispatch) {
            for(AutoDispatchListener l: listeners) {
                l.EventDispatched(new DispatchEvent(e));
            }
        }
    }
    
}
