package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.NodeFactory;
import com.marginallyclever.nodegraphcore.Node;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Swing UI allowing a user to create a new {@link Node}.  The choice of {@link Node} is retrieved from the
 * {@link NodeFactory#getNames()} registration list.
 * @author Dan Royer
 * @since 2022-02-11
 */
public class AddNodePanel extends JPanel {
    /**
     * The search field
     */
    private static final JTextField search = new JTextField();

    /**
     * The database of names in the list model.
     */
    private List<String> names = List.of(NodeFactory.getNames());

    /**
     * list model controls the contents of the list.  This is needed to add/remove as the search field is changed.
     */
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    /**
     * the list of names from the {@link NodeFactory}.
     */
    private final JList<String> myList = new JList<>(listModel);

    /**
     * The button that initiates the add action.
     */
    private final JButton confirmButton = new JButton("Add");

    private final JToggleButton caseSensitive = new JToggleButton("Aa");

    /**
     * Constructor for subclasses to call.
     */
    public AddNodePanel() {
        super(new BorderLayout());

        myList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane listScroller = new JScrollPane(myList);
        listScroller.setPreferredSize(new Dimension(250, 250));

        this.add(listScroller, BorderLayout.CENTER);
        this.add(confirmButton, BorderLayout.SOUTH);

        addSearchFeature();
        searchFor(search.getText());
    }

    private void addSearchFeature() {
        JPanel searchBlock = new JPanel(new BorderLayout());
        searchBlock.add(new JLabel(" \uD83D\uDD0D "),BorderLayout.WEST);
        searchBlock.add(search,BorderLayout.CENTER);
        searchBlock.add(caseSensitive,BorderLayout.EAST);

        this.add(searchBlock,BorderLayout.NORTH);

        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchFor(search.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchFor(search.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchFor(search.getText());
            }
        });

        caseSensitive.addActionListener((e)->searchFor(search.getText()));
    }

    /**
     * Filter the default list model based on the search query.
     * From <a href='https://stackoverflow.com/questions/15824733/option-to-ignore-case-with-contains-method'>Stack Overflow</a>
     */
    private void searchFor(String query) {
        if(!caseSensitive.isSelected()) {
            query = query.toLowerCase();
        }

        listModel.clear();

        for (String s : names) {
            String t = caseSensitive.isSelected() ? s : s.toLowerCase();
            if(t.contains(query)) {
                listModel.addElement(s);
            }
        }
    }

    /**
     * Runs the panel as a dialog.
     * @param frame the parent frame.
     * @return the node created, or null.
     */
    public static Node runAsDialog(Frame frame) {
        JDialog dialog = new JDialog(frame,"Add Node", Dialog.ModalityType.DOCUMENT_MODAL);

        final AtomicReference<Node> result = new AtomicReference<>();

        final AddNodePanel panel = new AddNodePanel();
        panel.confirmButton.addActionListener((e)->{
            try {
                if(panel.myList.getSelectedIndex()!=-1) {
                    result.set(NodeFactory.createNode(panel.myList.getSelectedValue()));
                }
            } catch(IllegalArgumentException e1) {
                e1.printStackTrace();
            }
            dialog.dispose();
        });

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

        return result.get();
    }

    /**
     * main entry point.  Good for independent test.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        NodeFactory.loadRegistries();

        JFrame frame = new JFrame(AddNodePanel.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
		frame.add(new AddNodePanel());
		frame.pack();
		frame.setVisible(true);
    }
}
