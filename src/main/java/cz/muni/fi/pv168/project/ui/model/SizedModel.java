package cz.muni.fi.pv168.project.ui.model;

public interface SizedModel {

    boolean isEmpty();

    void addSizeChangedListener(OnSizeChangedListener listener);

    interface OnSizeChangedListener {
        void onSizeChanged();
    }
}
