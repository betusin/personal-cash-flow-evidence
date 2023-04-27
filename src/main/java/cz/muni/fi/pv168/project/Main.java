package cz.muni.fi.pv168.project;

import com.formdev.flatlaf.FlatDarculaLaf;
import cz.muni.fi.pv168.project.dao.CategoryDao;
import cz.muni.fi.pv168.project.dao.TransactionDao;
import cz.muni.fi.pv168.project.ui.MainWindow;
import org.hibernate.Session;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The entry point of the application.
 */
public class Main {

    private Main() {
        throw new AssertionError("This class is not intended for instantiation.");
    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("data");
        var em = emf.createEntityManager();
        var sm = em.unwrap(Session.class);
        var transactionDao = new TransactionDao(sm);
        var categoryDao = new CategoryDao(sm);

        EventQueue.invokeLater(() -> {
            var window = new MainWindow(transactionDao, categoryDao);
            window.getFrame().addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    sm.close();
                    em.close();
                    super.windowClosing(e);
                }
            });
            window.show();
        });
    }
}
