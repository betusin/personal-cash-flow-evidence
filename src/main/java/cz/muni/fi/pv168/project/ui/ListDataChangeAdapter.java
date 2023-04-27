package cz.muni.fi.pv168.project.ui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ListDataChangeAdapter implements ListDataListener {
    private final ChangeListener changeListener;

    public ListDataChangeAdapter(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        changeListener.stateChanged(new ChangeEvent(e.getSource()));
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        changeListener.stateChanged(new ChangeEvent(e.getSource()));
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        changeListener.stateChanged(new ChangeEvent(e.getSource()));
    }
}
